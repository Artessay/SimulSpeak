from flask import Flask, Response, request, jsonify
from base64 import b64decode

# import transcribe
from transcribe.recognize import Recognizer

app = Flask(__name__)

# recognizer = transcribe.Recognizer()
recognizer = Recognizer()
audio_path = 'audio.mp3'

@app.route('/ping', methods=['GET'])
def ping():
    return jsonify({'message': 'pong'})

@app.route('/recognize', methods=['POST'])
def recognize_audio():
    if 'audio' not in request.json:
        return jsonify({'error': 'No audio data found'}), 400

    base64_audio_data = request.json['audio']
    audio_data = b64decode(base64_audio_data)
    with open(audio_path, 'wb') as f:
        f.write(audio_data)
    
    recognizer.input_stream(audio_path)

    return Response(recognizer.speech_recognition(), mimetype='text/plain')

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
