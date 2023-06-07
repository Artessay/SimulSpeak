import os
from flask import Flask, Response, request, jsonify, send_file
from base64 import b64decode
import whisper

# import transcribe
from transcribe.recognize import Recognizer

app = Flask(__name__)

# recognizer = transcribe.Recognizer()
# recognizer = Recognizer()
audio_path = './audio.mp3'
# model = whisper.load_model("small", download_root='./model')

@app.route('/ping', methods=['GET'])
def ping():
    return jsonify({'message': 'pong'})

@app.route('/recognize', methods=['POST', 'GET'])
def recognize_audio():
    srt_path = "./video.mp4.srt"
    
    with open(srt_path, 'rb') as f:
        srt_data = f.read()
        
    return srt_data

# @app.route('/audio/recognize', methods=['POST', 'GET'])
# def recognize_audio():
#     # if 'audio' not in request.json:
#     #     return jsonify({'error': 'No audio data found'}), 400
#     # if 'videoId' not in request.json:
#     #     return jsonify({'error': 'No audio data found'}), 400

#     # base64_audio_data = request.json['audio']
#     # audio_data = b64decode(base64_audio_data)
#     # with open(audio_path, 'wb') as f:
#     #     f.write(audio_data)

#     # videoId = request.json['videoId']
#     # srt_path = "./cache/" + videoId + "/audio.mp3.srt"
#     # if (os.path.exists(srt_path) == False):
#     #     model.transcribe(audio_path, srt_path)

#     model.transcribe(audio_path)
#     srt_path = "./audio.mp3.srt"
    
#     with open(srt_path, 'rb') as f:
#         srt_data = f.read()
#     return Response(srt_data, mimetype='text/plain')

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
