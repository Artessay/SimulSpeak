package org.simulspeak.bridge.component;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.simulspeak.bridge.configuration.BridgeConfig;
import org.simulspeak.bridge.configuration.NetAddress;
import org.simulspeak.bridge.dao.UserRepository;
import org.simulspeak.bridge.dao.VideoRepository;
import org.simulspeak.bridge.domain.UserInfo;
import org.simulspeak.bridge.domain.VideoInfo;
import org.simulspeak.bridge.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StorageImplement implements VideoService {
    
    private Socket socket = null;

    private DataInputStream fromServer = null;

    private DataOutputStream toServer = null;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UserRepository userRepository;

    private final static Logger logger = LoggerFactory.getLogger(StorageImplement.class);

    /**
     * In order to speed up the connection time, we don't create the socket when the
     * server starts. Instead, we create the socket when the first request comes.
     */
    // public StorageImplement() {
    //     // create connection
    //     try {
    //         // Create a socket to connect to the server
    //         socket = new Socket("192.168.137.78", 9999);

    //         // create a output stream to send data to the server
    //         toServer = new DataOutputStream(socket.getOutputStream());

    //         // Create an input stream to receive data from the server
    //         fromServer = new DataInputStream(socket.getInputStream());
    //     }
    //     catch (IOException ex) {
    //         ex.printStackTrace();
    //         logger.error("[client] create socket failed");
    //     }
    // }

    private void connectStorageServer() {
        // create connection
        try {
            // Create a socket to connect to the server
            socket = new Socket("192.168.137.87", 9999);

            // create a output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error("[client] create socket failed");
        }

        logger.info("[client] create socket success");
    }

    @Override
    public Long upload(String videoName, Long userId, NetAddress address) {
        System.out.println("[storage] upload");
        System.out.println(videoName + " " + userId);
        if (videoName == null || userId == BridgeConfig.ERROR_USER_ID) {
            logger.debug("upload video parameter is null");
            return -1L;
        }
        System.out.println("[storage] upload");
        UserInfo userInfo = userRepository.findByUserId(userId);
        if (userInfo == null) {
            logger.error("user does not exist");
            return -1L;
        }
        System.out.println("[storage] upload");
        VideoInfo videoInfo;
        videoInfo = videoRepository.findByVideoNameAndUserInfo(videoName, userInfo);
        if (videoInfo != null) {
            logger.debug("video {} has already existed", videoName);
            return -1L;
        }
        System.out.println("[storage] upload connect");
        if (socket == null || toServer == null || fromServer == null) {
            connectStorageServer();
        }
        System.out.println("[storage] upload");
        try {
            toServer.writeUTF("upload");

            String ip = fromServer.readUTF();
            // String port = fromServer.readUTF();
            String port = "7777";

            logger.debug(ip + " : " + port);
            // logger.debug(ip);

            address.setIp(ip);
            address.setPort(port);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("[client] upload video failed");
            return -1L;
        }

        videoInfo = new VideoInfo(videoName);

        videoInfo.setUserInfo(userInfo);
        videoRepository.saveAndFlush(videoInfo);

        return videoInfo.getVideoId();
    }

    @Override
    public boolean uploadVideo(String videoName, Long userId, NetAddress address) {
        if (videoName == null || userId == BridgeConfig.ERROR_USER_ID) {
            logger.debug("upload video parameter is null");
            return false;
        }
        
        UserInfo userInfo = userRepository.findByUserId(userId);
        if (userInfo == null) {
            logger.error("user does not exist");
            return false;
        }

        VideoInfo videoInfo;
        videoInfo = videoRepository.findByVideoNameAndUserInfo(videoName, userInfo);
        if (videoInfo != null) {
            logger.debug("video {} has already existed", videoName);
            return false;
        }

        if (socket == null || toServer == null || fromServer == null) {
            connectStorageServer();
        }
        
        try {
            toServer.writeUTF("upload");

            String ip = fromServer.readUTF();
            // String port = fromServer.readUTF();
            String port = "7777";

            logger.debug(ip + " : " + port);
            // logger.debug(ip);

            address.setIp(ip);
            address.setPort(port);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("[client] upload video failed");
            return false;
        }

        videoInfo = new VideoInfo(videoName);

        videoInfo.setUserInfo(userInfo);
        videoRepository.saveAndFlush(videoInfo);

        return true;
    }
    
    @Override
    public boolean requestVideo(String videoName, Long userId, NetAddress address) {
        if (videoName == null || userId == BridgeConfig.ERROR_USER_ID) {
            logger.debug("request video parameter is null");
            return false;
        }

        UserInfo userInfo = userRepository.findByUserId(userId);
        if (userInfo == null) {
            logger.error("user does not exist");
            return false;
        }

        VideoInfo videoInfo;
        videoInfo = videoRepository.findByVideoNameAndUserInfo(videoName, userInfo);
        if (videoInfo == null) {
            logger.debug("video {} does not exist", videoName);
            return false;
        }

        Long videoId = videoInfo.getVideoId();

        if (socket == null || toServer == null || fromServer == null) {
            connectStorageServer();
        }

        try {
            toServer.writeUTF("apply");
            toServer.writeLong(userId);
            toServer.writeLong(videoId);

            String ip = fromServer.readUTF();
            String port = fromServer.readUTF();

            logger.debug(ip + " : " + port);

            address.setIp(ip);
            address.setPort(port);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("[client] upload video failed");
            return false;
        }

        return true;
    }

}
