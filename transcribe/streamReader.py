import sys
import ffmpeg
from whisper.audio import SAMPLE_RATE

DEBUG = True

def open_local_stream(stream):
    try:
        process = (
            ffmpeg.input(stream, loglevel="panic")
            .output("pipe:", format="s16le", acodec="pcm_s16le", ac=1, ar=SAMPLE_RATE)
            .run_async(pipe_stdout=True)
        )
    except ffmpeg.Error as e:
        raise RuntimeError(f"Failed to load audio: {e.stderr.decode()}") from e
    
    return process

def open_online_stream(stream, preferred_quality):
    import streamlink
    import subprocess
    import threading
    
    # Initializes an empty Streamlink session
    stream_options = streamlink.streams(stream)

    if not stream_options:
        print("No playable streams found on this URL:", stream)
        sys.exit(0)
    
    # choose audio quality
    quality_option = None
    for quality in [preferred_quality, 'audio_only', 'audio_mp4a', 'audio_opus', 'best']:
        if quality in stream_options:
            quality_option = quality
            break

    if quality_option is None:
        # Fallback
        quality_option = next(iter(stream_options.values()))
    
    # audio writer
    def writer(streamlink_proc, ffmpeg_proc):
        while (not streamlink_proc.poll()) and (not ffmpeg_proc.poll()):
            try:
                chunk = streamlink_proc.stdout.read(1024)
                ffmpeg_proc.stdin.write(chunk)
            except (BrokenPipeError, OSError):
                pass
    
    # stream link process
    cmd = ['streamlink', stream, quality_option, "-O"]
    streamlink_process = subprocess.Popen(cmd, stdout=subprocess.PIPE)

    # ffmpeg process
    try:
        ffmpeg_process = (
            ffmpeg.input("pipe:", loglevel="panic")
            .output("pipe:", format="s16le", acodec="pcm_s16le", ac=1, ar=SAMPLE_RATE)
            .run_async(pipe_stdin=True, pipe_stdout=True)
        )
    except ffmpeg.Error as e:
        raise RuntimeError(f"Failed to load audio: {e.stderr.decode()}") from e

    thread = threading.Thread(target=writer, args=(streamlink_process, ffmpeg_process))
    thread.start()
    return ffmpeg_process, streamlink_process

def open_stream(stream, direct_url, preferred_quality):
    if DEBUG: print("[whisper] Opening stream...")

    if direct_url:
        # pass the URL directly to ffmpeg
        return open_local_stream(stream), None
    else:
        # read from online stream
        return open_online_stream(stream, preferred_quality)
