package org.simulspeak.bridge.service;

import org.simulspeak.bridge.configuration.NetAddress;

public interface VideoService {
    
    public boolean uploadVideo(String videoName, Long userId, NetAddress address);

    public boolean requestVideo(String videoName, Long userId, NetAddress address);

}
