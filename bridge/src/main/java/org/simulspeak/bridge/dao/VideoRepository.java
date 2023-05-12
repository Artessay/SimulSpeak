package org.simulspeak.bridge.dao;

import org.simulspeak.bridge.domain.VideoInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface VideoRepository extends JpaRepository<VideoInfo, Long> {
    
    VideoInfo findByVideoId(Long videoId);

    List<VideoInfo> findByVideoName(String videoName);

    
}
