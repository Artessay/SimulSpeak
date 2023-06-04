package org.simulspeak.bridge.component.audio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
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
        // ResponseEntity<InputStream> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, InputStream.class);
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, byte[].class);

        // 处理响应
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            // try {
            //     InputStream responseStream = responseEntity.getBody();
            //     BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream, StandardCharsets.UTF_8));

            //     String line;
            //     while ((line = reader.readLine()) != null) {
            //         // 处理每一行文本结果
            //         System.out.println("recognize result: " + line);
            //     }
            // } catch (IOException e) {
            //     System.out.println("can not get response from server");
            // }
            try {
                byte[] responseBody = responseEntity.getBody();
                String responseText = new String(responseBody, StandardCharsets.UTF_8);

                BufferedReader reader = new BufferedReader(new StringReader(responseText));

                String line;
                while ((line = reader.readLine()) != null) {
                    // 处理每一行文本结果
                    logger.info("recognize result: " + line);
                }
            } catch (IOException e) {
                logger.error("can not get response from server");
            }


            String response = "";
            return response;
        } else {
            HttpStatusCode statusCode = responseEntity.getStatusCode();
            logger.error("recognize request failed, status code: " + statusCode);
            return "";
        }
    }

    // public static void main(String[] args) {
    //     AudioRecognition audioRecognition = new AudioRecognition();
    //     String audioFilePath = "src/test/resources/audio/1.mp3";
    //     String result = audioRecognition.recognize(audioFilePath);
    //     System.out.println(result);
    // }
}

