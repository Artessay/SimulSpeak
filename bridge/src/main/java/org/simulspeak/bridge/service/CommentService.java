package org.simulspeak.bridge.service;

public interface CommentService {
    
    public String pull(Long videoId);

    public boolean push(Long videoId, Long userId, String comment);

}
