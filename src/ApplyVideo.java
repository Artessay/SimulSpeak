import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;

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
         
        response.setHeaders("Content-Type", "video/mp4")
                // .setStatus(206)
                .send(data);
        
        System.out.println("[Angie] send video success");
    }

    private byte[] getEntireVideoData(String userId, String videoId) {
        String path = "./video.mp4";
        FileInputStream fileInputStream = null;
        byte[] data = null;
        try {
            fileInputStream = new FileInputStream(path);
            data = new byte[fileInputStream.available()];
            fileInputStream.read(data);
        } catch (Exception e) {
            System.out.println("Error: " + "File Transfer Error");
            // e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception e) {
                    System.out.println("Error: " + "File Stream Release Error");
                    // e.printStackTrace();
                }
            }
        }

        return data;
    }

    private byte[] getRandomVideoData(String userId, String videoId) {
        // get video data by (userId, videoId)
        String path = "./video.mp4";
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
