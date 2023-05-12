package org.simulspeak.bridge.service;

import org.simulspeak.bridge.domain.UserInfo;

public interface VideoService {
    
    public void uploadVideo(String videoName, UserInfo userInfo);

    public void requestVideo(String videoName, UserInfo userInfo);
    
}
