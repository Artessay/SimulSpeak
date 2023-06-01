package org.simulspeak.bridge.component.audio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AudioRecognition {

    private final static String requestUrl = "http://localhost:5000/recognize"; // 服务器的URL";

    private final static Logger logger = LoggerFactory.getLogger(AudioRecognition.class);
    
    public String recognize(String audioFilePath) {
        // 读取音频文件
        byte[] audioData;
        try {
            Path audioPath = Paths.get(audioFilePath);
            audioData = Files.readAllBytes(audioPath);
        } catch (IOException e) {
            System.out.println("无法读取音频文件");
            return "";
        }

        // 将音频数据转换为Base64编码
        String base64AudioData = Base64.getEncoder().encodeToString(audioData);

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 构建请求体
        String requestBody = "{\"audio\": \"" + base64AudioData + "\"}";

        // 构建请求实体
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // 发送请求并获取响应
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, String.class);

        // 处理响应
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String response = responseEntity.getBody();
            logger.info("recognize result: " + response);
            return response;
        } else {
            HttpStatusCode statusCode = responseEntity.getStatusCode();
            logger.error("recognize request failed, status code: " + statusCode);
            return "";
        }
    }

    public static void main(String[] args) {
        AudioRecognition audioRecognition = new AudioRecognition();
        String audioFilePath = "src/test/resources/audio/1.mp3";
        String result = audioRecognition.recognize(audioFilePath);
        System.out.println(result);
    }
}

