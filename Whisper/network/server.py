import socket
import threading
from config.configServer import *

class Server():
    def __init__(self) -> None:
        self.server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server.bind((HOST, PORT))
        self.server.listen(BACKLOG)
    
    def start(self):
        thread = threading.Thread(target=self.accept, args=())
        thread.setDaemon(True)
        thread.start()

    def accept(self):
        while True:
            client, address = self.server.accept()
            print('[server] connected: ', address)

            thread = threading.Thread(
                target=self.receiveMessage(),
                args=(client, address)
            )
            thread.setDaemon(True)  # Daemon
            thread.start()

    def receiveMessage(self, client:socket.socket, address):
        while True:
            try:
                while True:
                    message = client.recv(BUFFER_SIZE)
                    if not message:
                        break
                    else:
                        print(message)
            except Exception as e:
                print('[server] disconnected: ', address)
                exit(-1)



