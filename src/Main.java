public class Main {
    public static void main(String[] args) {
        Angie app = new Angie();

        app.use("/applyVideo", new ApplyVideo());

        app.listen(80);
    }
}