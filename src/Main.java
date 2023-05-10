// import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Angie app = new Angie();

        app.use("/applyVideo", new ApplyVideo());

        // Logger logger = Logger.getLogger("Angie");

        app.listen(80);
    }
}