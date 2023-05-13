// import java.util.logging.Logger;
import java.net.http.HttpRequest;

public class Main {
    public static void main(String[] args) {
        Angie app = new Angie();

        System.out.println("main");
        app.use("/applyVideo", new ApplyVideo());
        // app.use("/", (request, response) -> {
        //     System.out.println("All");
        //     byte[] data = "Hello".getBytes();
        //     response.setHeaders("Content-Length", String.valueOf(data.length))
        //             .setHeaders("Content-Type", "text/html; charset-utf-8")
        //             .send(data);
        //     System.out.println("good");
        // });

        // HttpServletRequest request = new HttpServletRequest();

        app.listen(80);
    }
}