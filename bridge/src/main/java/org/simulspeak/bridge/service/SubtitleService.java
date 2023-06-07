package org.simulspeak.bridge.service;

public interface SubtitleService {
    
    public String recognize(String audioFilePath);

    public String subtitle(Long videoId);

}
