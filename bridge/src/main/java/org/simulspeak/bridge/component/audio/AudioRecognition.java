package org.simulspeak.bridge.component.audio;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
import java.util.Base64;

import org.simulspeak.bridge.component.StorageImplement;
import org.simulspeak.bridge.configuration.BridgeConfig;
import org.simulspeak.bridge.dao.VideoRepository;
import org.simulspeak.bridge.domain.VideoInfo;
import org.simulspeak.bridge.service.SubtitleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AudioRecognition implements SubtitleService {

    private final static String requestUrl = "http://localhost:5000/recognize";
    // private final static String requestUrl = "http://localhost:5000/audio/recognize";

    private final static Logger logger = LoggerFactory.getLogger(AudioRecognition.class);

    @Autowired
    private VideoRepository videoRepository;

    private Long videoId = BridgeConfig.ERROR_VIDEO_ID;

    public String subtitle(Long videoId) {
        VideoInfo videoInfo = videoRepository.findByVideoId(videoId);
        if (videoInfo == null) {
            logger.error("video not found");
            return null;
        }

        String audioPath = videoInfo.getAudioPath();
        if (audioPath == null) {
            StorageImplement storageImplement = new StorageImplement();
            audioPath = storageImplement.apply(videoInfo.getUserInfo().getUserId(), videoId, BridgeConfig.APPLY_AUDIO_ENUM);
            videoInfo.setAudioPath(audioPath);
            videoRepository.save(videoInfo);
        }

        this.videoId = videoId;
        String result = recognize(audioPath);
        System.out.println(result);
        return result;
    }

    public String recognize(String audioFilePath) {
        // 读取音频文件
        byte[] audioData;
        try {
            URL url = new URL(audioFilePath);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            
            buffer.flush();
            audioData = buffer.toByteArray();
        } catch (IOException e) {
            System.out.println("无法读取音频文件");
            return null;
        }

        // 将音频数据转换为Base64编码
        String base64AudioData = Base64.getEncoder().encodeToString(audioData);

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 构建请求体
        String requestBody = "{\"audio\": \"" + base64AudioData + "\", \"videoId\": " + videoId + "}";

        // 构建请求实体
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // 发送请求并获取响应
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, byte[].class);

        // 处理响应
        StringBuffer response = new StringBuffer();
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            try {
                byte[] responseBody = responseEntity.getBody();

                String responseText = new String(responseBody, StandardCharsets.UTF_8);

                BufferedReader reader = new BufferedReader(new StringReader(responseText));

                String line;
                int lineNum = 0;
                while ((line = reader.readLine()) != null) {
                    lineNum = (lineNum + 1) % 4;
                    logger.info("recognize result: " + line);
                    
                    if (lineNum == 1 || lineNum == 0) {
                        continue;
                    }

                    if (lineNum == 2) {
                        String[] words = line.split(" --> ");
                        String startTime = words[0];
                        String endTime = words[1];
                        // logger.info("startTime: " + startTime + ", endTime: " + endTime);
                        
                        int startTimeInt = parseTime(startTime);
                        int endTimeInt = parseTime(endTime);
                        response.append(Integer.toString(startTimeInt));
                        response.append("\r\n");
                        response.append(Integer.toString(endTimeInt));
                        response.append("\r\n");
                    } else {
                        response.append(line);
                        response.append("\r\n");
                    }
                }
            } catch (IOException e) {
                logger.error("can not get response from server");
            }
        } else {
            HttpStatusCode statusCode = responseEntity.getStatusCode();
            logger.error("recognize request failed, status code: " + statusCode);
        }

        

        return response.toString();
    }

    private int parseTime(String timeString) {
        String[] words = timeString.split(":");
        int hour = Integer.parseInt(words[0]);
        int minute = Integer.parseInt(words[1]);
        int second = Integer.parseInt(words[2].split(",")[0]);
        int millisecond = Integer.parseInt(words[2].split(",")[1]);
        int time = hour * 3600 + minute * 60 + second + millisecond / 1000;
        return time;
    }

    public static void main(String[] args) {
        AudioRecognition audioRecognition = new AudioRecognition();
        String audioFilePath = "http://192.168.137.1:80/sounds/Tyl1nf6wP8.mp3";
        String result = audioRecognition.recognize(audioFilePath);
        System.out.println(result);
    }
}

