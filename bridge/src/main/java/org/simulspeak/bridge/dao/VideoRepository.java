package org.simulspeak.bridge.dao;

import org.simulspeak.bridge.domain.VideoInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.simulspeak.bridge.domain.UserInfo;



public interface VideoRepository extends JpaRepository<VideoInfo, Long> {
    
    VideoInfo findByVideoId(Long videoId);

    List<VideoInfo> findByVideoName(String videoName);

    VideoInfo findByVideoNameAndUserInfo(String videoName, UserInfo userInfo);

}
