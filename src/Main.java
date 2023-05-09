public class Main {
    public static void main(String[] args) {
        Angie app = new Angie();

        app.use("/applyVideo", (request, response) -> {
            String userId = request.getParam("uid");
            String videoId = request.getParam("vid");

            response.setHeaders("Content-Type", "video/mp4")
                    .send("video data " + userId + " " + videoId);
        });

        app.listen(80);
    }
}