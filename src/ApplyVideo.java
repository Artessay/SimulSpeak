import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ApplyVideo implements Processor {
    @Override
    public void callback(Request request, Response response) {
        String userId = request.getParam("uid");
        String videoId = request.getParam("vid");
        
        byte[] data = getEntireVideoData(userId, videoId);
        if (data == null) {
            response.setStatus(404).send("Video not found");
            return;
        }
        // String data = "<h1> Hello World </h1><p> This is a video </p>";
        
        System.out.println("[Angie] start send video");
        
        response.setHeaders("Content-Length", String.valueOf(data.length))
                .setHeaders("Content-Type", "video/mp4; charset-utf-8")
                .setHeaders("Access-Control-Allow-Origin", "*")
                // .setStatus(206)
                .send(data);
        
        System.out.println("[Angie] send video success");
    }

    private String getVideoPath(String userId, String videoId) {
        return "./video.mp4";
    }

    private byte[] getEntireVideoData(String userId, String videoId) {
        byte[] data = null;
        
        try {
            String path = getVideoPath(userId, videoId);
            data = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    private byte[] getRandomVideoData(String userId, String videoId) {
        // get video data by (userId, videoId)
        String path = getVideoPath(userId, videoId);
        File imgFile = new File(path);
        if (!imgFile.exists()) {
            return null;
        }
        
        RandomAccessFile targetFile = null;
        byte[] data = null;
        try {
            targetFile = new RandomAccessFile(imgFile, "r");
            long fileLength = targetFile.length();


        } catch (Exception e) {
            System.out.println("Error: " + "File Transfer Error");
            // e.printStackTrace();
        } finally {
            if (targetFile != null) {
                try {
                    targetFile.close();
                } catch (Exception e) {
                    System.out.println("Error: " + "File Stream Release Error");
                    // e.printStackTrace();
                }
            }
        }

        return data;
    }
}
