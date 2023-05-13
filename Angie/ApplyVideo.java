import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ApplyVideo implements Processor {
    private Request request;
    private Response response;

    @Override
    public void callback(Request request, Response response) {
        this.request = request;
        this.response = response;
        
        String rangeString = request.getHeader("Range");

        if (rangeString == null) {
            sendEntireVideoData();
        } else {
            sendRandomVideoData(rangeString);
        }
    }

    private void sendEntireVideoData() {
        byte[] data = null;
        
        try {
            String path = getVideoPath();
            data = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            // e.printStackTrace();
        }

        if (data == null) {
            response.setStatus(404).send("Video not found");
            return;
        }

        System.out.println("[Angie] start send video");
            
        response.setHeaders("Content-Length", String.valueOf(data.length))
                .setHeaders("Content-Type", "video/mp4; charset-utf-8")
                .setHeaders("Access-Control-Allow-Origin", "*")
                .send(data);
        
        System.out.println("[Angie] send video success");
    }

    private void sendRandomVideoData(String rangeString) {
        // get video data
        String path = getVideoPath();
        // System.out.println("path: " + path);

        File file = new File(path);
        if (!file.exists()) {
            response.setStatus(404).send("Video not found");
            return;
        }
        
        RandomAccessFile targetFile = null;
        byte[] data = null;
        long fileLength = 0, requestStart = 0, requestEnd = 0;

        try {
            targetFile = new RandomAccessFile(file, "r");
            fileLength = targetFile.length();

            // process Range
            // System.out.println(rangeString);
            String ranges[] = rangeString.split("-");
            // example: bytes=0-100, bytes=0- 
            
            requestStart = Long.parseLong(ranges[0].substring(6));
            if (ranges.length > 1) {
                requestEnd = Long.parseLong(ranges[1]);
            } else {
                requestEnd = fileLength - 1;
            }
            // System.out.println("requestStart: " + requestStart);
            // System.out.println("requestEnd: " + requestEnd);

            long requestSize = requestEnd - requestStart + 1;
            if (requestSize < 0 || requestSize > Integer.MAX_VALUE) {
                System.out.println("[Angie] Error: " + "File Size Error " + requestStart + " - " + requestEnd);
                response.setStatus(503).send("Server Error");
                return;
            }

            targetFile.seek(requestStart);
            data = new byte[(int) requestSize];
            targetFile.read(data, 0, (int)requestSize);
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

        if (data == null) {
            response.setStatus(404).send("Video not found");
            return;
        }
        
        System.out.println("[Angie] start send video");
            
        response.setStatus(206)
                .setMethod("Partial Content")
                .setHeaders("Content-Length", String.valueOf(data.length))
                .setHeaders("Content-Type", "video/mp4; charset-utf-8")
                .setHeaders("Accept-Range", "bytes")
                .setHeaders("Content-Range", "bytes " + requestStart + "-" + requestEnd + "/" + fileLength)
                .setHeaders("Access-Control-Allow-Origin", "*")
                .send(data);
        
        System.out.println("[Angie] send video success");
    }
    
    private String getVideoPath() {
        String userId = request.getParam("uid");
        String videoId = request.getParam("vid");
        if (userId == null || videoId == null) {
            // return null;
        }

        return "./video.mp4";
    }
}
