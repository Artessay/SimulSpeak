package org.simulspeak.bridge.service;

import java.util.List;

import org.simulspeak.bridge.configuration.NetAddress;
import org.simulspeak.bridge.domain.VideoInfo;

public interface VideoService {

    public Long upload(String videoName, Long userId, NetAddress address);
    
    public boolean uploadVideo(String videoName, Long userId, NetAddress address);

    public String request(Long videoId);

    public boolean requestVideo(String videoName, Long userId, NetAddress address);

    public String recommend();
    
    public List<VideoInfo> recommendList();

    public String search(String videoName);

    public List<VideoInfo> searchList(String videoName);

    public String searchByUser(Long userId);

    public List<VideoInfo> searchListByUser(Long userId);

}
