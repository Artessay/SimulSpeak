package org.simulspeak.bridge.service;

public interface VideoService {
    
    public boolean uploadVideo(String videoName, Long userId);

    public void requestVideo(String videoName, Long userId);

}
