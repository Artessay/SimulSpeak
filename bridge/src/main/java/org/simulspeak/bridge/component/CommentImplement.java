package org.simulspeak.bridge.component;

import org.simulspeak.bridge.dao.CommentRepository;
import org.simulspeak.bridge.dao.UserRepository;
import org.simulspeak.bridge.dao.VideoRepository;
import org.simulspeak.bridge.domain.UserInfo;
import org.simulspeak.bridge.domain.VideoComment;
import org.simulspeak.bridge.domain.VideoInfo;
import org.simulspeak.bridge.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentImplement implements CommentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public String pull(Long videoId) {
        System.out.println("[comment] pull " + videoId);
        VideoInfo videoInfo = videoRepository.findByVideoId(videoId);
        if (videoInfo == null) {
            System.out.println("[comment] video not found");
            return null;
        }

        List<VideoComment> comments = commentRepository.findByVideoInfo(videoInfo);

        StringBuffer result = new StringBuffer();
        for (VideoComment comment: comments) {
            result.append(comment.getComment());
            result.append("\r\n");

            UserInfo userInfo = comment.getUserInfo();
            String userName = userInfo.getUserName();
            result.append(userName);
            result.append("\r\n");
        }

        return result.toString();
    }

    @Override
    public boolean push(Long videoId, Long userId, String comment) {
        VideoInfo videoInfo = videoRepository.findByVideoId(videoId);
        UserInfo userInfo = userRepository.findByUserId(userId);

        if (videoInfo == null || userInfo == null) {
            System.out.println("[comment] comment error, null");
            return false;
        }

        VideoComment videoComment = new VideoComment(videoInfo, userInfo, comment);
        commentRepository.saveAndFlush(videoComment);

        return true;
    }
    
}
