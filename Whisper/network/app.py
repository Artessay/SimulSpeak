from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/recognize', methods=['POST'])
def recognize_audio():
    if 'audio' not in request.json:
        return jsonify({'error': 'No audio data found'}), 400

    audio_data = request.json['audio']
    # 在此处进行音频识别的处理
    # ...
    # print("audio_data:")

    # 假设识别结果为recognized_text
    recognized_text = 'Hello, world!'

    return jsonify({'text': recognized_text})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)


# from flask import Flask, request
# from base64 import b64decode

# app = Flask(__name__)

# @app.route('/upload_audio', methods=['POST'])
# def upload_audio():
#     audio_data = b64decode(request.data)

#     with open('audio.wav', 'wb') as f:
#         f.write(audio_data)

#     # do audio processing here
    
#     return 'Audio received'

# if __name__ == '__main__':
#     app.run()
