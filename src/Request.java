import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class Request {
    private String url;
    private String params;
    private String method;
    private Map<String, String> paramMap = new HashMap<String, String>();
    private Map<String, String> headerMap = new HashMap<String, String>();

    public Request(InputStream inputStream){
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String[] requestLine =  bufferedReader.readLine().split(" ");

            if (requestLine.length == 3 && requestLine[2].equals("HTTP/1.1")) {
                this.method = requestLine[0];
                String fullUrl = requestLine[1];
                if (fullUrl.contains("?")) {
                    this.url = fullUrl.substring(0, fullUrl.indexOf("?"));
                    this.params = fullUrl.substring(fullUrl.indexOf("?") + 1);

                    // System.out.println(method + " " + url + " " + params);

                    String[] keyValues = this.params.split("&");
                    for (String keyValue : keyValues) {
                        String[] pair = keyValue.split("=");
                        if (pair.length == 2) {
                            paramMap.put(pair[0], pair[1]);
                        }
                    }
                } else {
                    this.url = fullUrl;
                }

                while (bufferedReader.ready()) {
                    String[] keyValue = bufferedReader.readLine().split(": ");
                    
                    if (keyValue.length == 2) {
                        headerMap.put(keyValue[0], keyValue[1]);
                        System.out.println(keyValue[0] + ": " + keyValue[1]);
                    }
                }
                System.out.println("");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     *         {@code null} if this map contains no mapping for the key
     * @throws ClassCastException if the key is of an inappropriate type for
     *         this map
     */
    public String getParam(String key) {
        return paramMap.get(key);
    }

    public String getParams() {
        return params;
    }

   public String getMethod() {
        return method;
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     *         {@code null} if this map contains no mapping for the key
     * @throws ClassCastException if the key is of an inappropriate type for
     *         this map
     */
    public String getHeader(String key) {
        return headerMap.get(key);
    }
}