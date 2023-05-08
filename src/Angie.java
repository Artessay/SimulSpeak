import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Angie {
    public void listen(int port) {
        try (// 监听 port 端口
            ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                // 接受连接
                Socket client = serverSocket.accept();
                
                // 创建新线程，发送数据
                new Thread(() -> {
                    try {
                        OutputStream clientOutStream = client.getOutputStream();
                        clientOutStream.write(
                                ("HTTP/1.1 200\n"
                                        + "Content-Type: text/html\n"
                                        + "\n"
                                        + "<h1> Hello, web Framework! </h1>").getBytes()
                        );
                        clientOutStream.flush();
                        clientOutStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}