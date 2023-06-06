package org.simulspeak.bridge.dao;

import org.simulspeak.bridge.domain.VideoComment;
import org.simulspeak.bridge.domain.VideoInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface CommentRepository extends JpaRepository<VideoComment, Long> {
    
    List<VideoComment> findByVideoInfo(VideoInfo videoInfo);
    
}
