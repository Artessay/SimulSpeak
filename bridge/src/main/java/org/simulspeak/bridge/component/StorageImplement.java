package org.simulspeak.bridge.component;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.simulspeak.bridge.configuration.BridgeConfig;
import org.simulspeak.bridge.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StorageImplement implements VideoService {
    
    private Socket socket;
    private DataInputStream fromServer;
    private DataOutputStream toServer;

    private final static Logger logger = LoggerFactory.getLogger(StorageImplement.class);

    public StorageImplement() {
        // create connection
        try {
            // Create a socket to connect to the server
            socket = new Socket("192.168.137.78", 9999);

            // create a output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());
        }
        catch (IOException ex) {
            ex.printStackTrace();
            logger.error("[client] create socket failed");
        }
    }
    
    public boolean uploadVideo(String videoName, Long userId) {
        if (videoName == null || userId == BridgeConfig.ERROR_USER_ID) {
            logger.debug("upload video parameter is null");
            return false;
        }

        if (socket == null || toServer == null || fromServer == null) {
            logger.debug("socket does not connected");
            return false;
        }
        
        try {
            toServer.writeUTF("upload");

            String ip = fromServer.readUTF();
            String port = fromServer.readUTF();

            logger.debug(ip + " : " + port);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("[client] upload video failed");
        }

        return true;
    }
    
    public void requestVideo(String videoName, Long userId) {
        Long videoId = 1L;
        try {
            toServer.writeUTF("apply");
            toServer.writeLong(userId);
            toServer.writeLong(videoId);

            String ip = fromServer.readUTF();
            String port = fromServer.readUTF();

            System.out.println(ip + " : " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
