from flask import Flask,render_template,request
from flask import jsonify
from flask_cors import *
# from database import *
from flask_paginate import Pagination,get_page_parameter
import json
from flask_cors import CORS
import ast

app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False
database = ''
# try:
#     # database = WenxueDatabase('127.0.0.1', 'wenxue_faye', 'liyifei123', 'wenxue_db')
#     database = WenxueDatabase('172.17.0.1', 'root', 'Zju@20200512', 'wenxue_db')
# except:
#     print('数据库启动失败')
# CORS(app, resources={r"/*": {"origins":"*"}}, send_wildcard=True)
CORS(app, supports_credentials=True)    # 跨平台访问


@app.route('/login', methods=['POST', 'GET'])
@cross_origin()
def login():
    print(request.values)
    identity_type = request.values.get('identity_type')
    identifier = request.values.get('identifier')
    credential = request.values.get('credential')
    print(identity_type)
    print(identifier)
    print(credential)

    # import user
    # user_id:int = user.verification(identity_type, identifier, credential)
    user_id = 1

    if (user_id >= 0):
        data={
            'message': 'success',
            'user_id': user_id
        }
    else:
        data={
            'message': 'failed'
        }
    return jsonify(data)


@app.route('/', methods=['POST', 'GET'])
@cross_origin()
def register():
    print(request.values)

    data={
        'message': 'success',
        'user_id': 1
    }
    
    return jsonify(data)

if __name__ =='__main__':
    app.run()
    # app.debug=True
