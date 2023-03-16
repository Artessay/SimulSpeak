import requests
import urllib3


def set_proxy():
    # 增加重试连接次数
    requests.adapters.DEFAULT_RETRIES = 5
    session = requests.Session()
    # 关闭多余连接
    session.keep_alive = False
    # 取消验证证书
    session.verify = False
    # 关闭在设置了verify=False后的错误提示
    urllib3.disable_warnings()
    # 设置代理
    proxies = {
        'http': 'http://127.0.0.1:7890/',
        'https': 'http://127.0.0.1:7890/'
    }
    session.proxies = proxies