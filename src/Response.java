import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class Response {
    private OutputStream outputStream;
    private HashMap<String, String> headers = new HashMap<>();
    private int status;
    private String method;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.status = 200;
        this.method = null;
    }

    public Response setHeaders(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public Response setStatus(int statusCode) {
        this.status = statusCode;
        return this;
    }

    public Response setMethod(String method) {
        this.method = method;
        return this;
    }

    public void send(String data) {
        try {
            StringBuilder dataBuilder = new StringBuilder();

            dataBuilder.append("HTTP/1.1 ").append(this.status);
            if (this.method != null) {
                dataBuilder.append(" ").append(this.method);
            }
            dataBuilder.append("\n");
            
            for (String key:
                this.headers.keySet()) {
                dataBuilder.append(key).append(": ").append(this.headers.get(key)).append("\n");
            }
            dataBuilder.append("\n").append(data);

            outputStream.write(dataBuilder.toString().getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(byte[] data) {
        try {
            StringBuilder dataBuilder = new StringBuilder();
            dataBuilder.append("HTTP/1.1 ").append(this.status).append("\n");
            for (String key:
                 this.headers.keySet()) {
                dataBuilder.append(key).append(": ").append(this.headers.get(key)).append("\n");
            }
            dataBuilder.append("\n");

            // System.out.println(dataBuilder);

            outputStream.write(dataBuilder.toString().getBytes());
            outputStream.write(data);

            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}