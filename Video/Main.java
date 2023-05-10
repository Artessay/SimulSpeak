// import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Angie app = new Angie();

        app.use("/applyVideo", new ApplyVideo());
        app.use("/", (request, response) -> {
            System.out.println("All");
            byte[] data = "Hello".getBytes();
            response.setHeaders("Content-Length", String.valueOf(data.length))
                    .setHeaders("Content-Type", "text/html; charset-utf-8")
                    .send(data);
            System.out.println("good");
        });

        System.out.println("[Aigen] server start");
        app.listen(80);
        System.out.println("[Aigen] server stop");
    }
}