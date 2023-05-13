import torch
import whisper
import numpy as np

from datetime import datetime
from ringbuffer import RingBuffer
from whisper.audio import SAMPLE_RATE

from streamReader import open_stream

DEBUG = True

import time

class Recognizer():
    def __init__(self) -> None:
        self.device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
        # self.device = torch.device("cpu")
        print(f"[whisper] Using {self.device}")
        # self.model = load_model()
        self.model = load_model('medium', torch.device("cpu"))
        # self.model = load_model('large', device=self.device)

    def input_stream(
            self,
            url,
            direct_url=True,
            preferred_quality = "audio_only",
    ):
        self.ffmpeg_process, self.streamlink_process = open_stream(url, direct_url, preferred_quality)

    def speech_recognition(
            self,
            language = None,
            interval = 5,
            history_buffer_size = 0,
            use_vad = True,
            **decode_options
    ):
        if DEBUG: print("[whisper] Recognizing...")
        print(time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())))
        model = self.model
        device = self.device
        ffmpeg_process = self.ffmpeg_process
        streamlink_process = self.streamlink_process
        
        # Voice Activity Detection
        if use_vad:
            from VoiceActivityDetection import VoiceActivityDetection
            vad = VoiceActivityDetection(device=torch.device("cpu"))

        n_bytes = interval * SAMPLE_RATE * 2  # Factor 2 comes from reading the int16 stream as bytes
        audio_buffer = RingBuffer((history_buffer_size // interval) + 1)
        previous_text = RingBuffer(history_buffer_size // interval)

        # Extract Text
        try:
            while ffmpeg_process.poll() is None:
                # Read audio from ffmpeg stream
                in_bytes = ffmpeg_process.stdout.read(n_bytes)
                if not in_bytes:
                    break

                audio = np.frombuffer(in_bytes, np.int16).flatten().astype(np.float32) / 32768.0
                if use_vad and vad.no_speech(audio):
                    print(f'{datetime.now().strftime("%H:%M:%S")}')
                    continue
                audio_buffer.append(audio)

                # Decode the audio
                clear_buffers = False
                
                result = model.transcribe(np.concatenate(audio_buffer.get_all()),
                                            prefix="".join(previous_text.get_all()),
                                            language=language,
                                            without_timestamps=True,
                                            **decode_options)
                
                decoded_language = "" if language else "(" + result.get("language") + ")"
                decoded_text = result.get("text")
                new_prefix = ""
                for segment in result["segments"]:
                    if segment["temperature"] < 0.5 and segment["no_speech_prob"] < 0.6:
                        new_prefix += segment["text"]
                    else:
                        # Clear history if the translation is unreliable, otherwise prompting on this leads to
                        # repetition and getting stuck.
                        clear_buffers = True

                previous_text.append(new_prefix)

                if clear_buffers or previous_text.has_repetition():
                    audio_buffer.clear()
                    previous_text.clear()
                    
                print(f'{datetime.now().strftime("%H:%M:%S")} {decoded_language} {decoded_text}')

            if DEBUG: print("[whisper] Stream ended")
        finally:
            ffmpeg_process.kill()
            if streamlink_process:
                streamlink_process.kill()
    
def load_model(model = "small", device = None):
    # Load Whisper Model
    if DEBUG: print("[whisper] Loading model...")
    model = whisper.load_model(model, device=device, download_root='./model')

    return model


# def recognize(
#         url,
#         language = None,
#         interval = 5,
#         history_buffer_size = 0,
#         preferred_quality = "audio_only",
#         use_vad = True,
#         direct_url=True,
#         device = torch.device('cpu')
# ):
#     device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
#     model = load_model()
#     ffmpeg_process, streamlink_process = open_stream(url, direct_url, preferred_quality)
#     speech_recognition(model, ffmpeg_process, streamlink_process, device, 
#                        language, interval, history_buffer_size, use_vad)


if __name__ == '__main__':
    # recognize('./nobel.mp4')
    recognizer = Recognizer()
    recognizer.input_stream('./audio.mp3')
    # recognizer.input_stream('./video.mp4')
    recognizer.speech_recognition()