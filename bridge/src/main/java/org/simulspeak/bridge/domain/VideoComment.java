package org.simulspeak.bridge.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="video_comment")
public class VideoComment {
    
    @Id
    @Column(nullable=false, name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "video_id", referencedColumnName = "video_id", nullable = false)
    private VideoInfo videoInfo;

    // @OneToOne
    // @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    // private UserInfo userInfo;

    @Column(name = "comment")
    private String comment;

    public VideoComment() {
        super();
    }

    public VideoComment(String comment) {
        super();
        this.comment = comment;
    }

    // public VideoComment(VideoInfo videoInfo, UserInfo userInfo, String comment) {
    //     super();
    //     this.videoInfo = videoInfo;
    //     this.userInfo = userInfo;
    //     this.comment = comment;
    // }

    // public VideoComment(Long commentId, VideoInfo videoInfo, UserInfo userInfo, String comment) {
    //     super();
    //     this.commentId = commentId;
    //     this.videoInfo = videoInfo;
    //     this.userInfo = userInfo;
    //     this.comment = comment;
    // }

    public Long getCommentId() {
        return this.commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public VideoInfo getVideoInfo() {
        return this.videoInfo;
    }

    public void setVideoInfo(VideoInfo videoInfo) {
        this.videoInfo = videoInfo;
        if (!videoInfo.getVideoComments().contains(this)) {
            videoInfo.getVideoComments().add(this);
        }
    }

    // public UserInfo getUserInfo() {
    //     return this.userInfo;
    // }

    // public void setUserInfo(UserInfo userInfo) {
    //     this.userInfo = userInfo;
    // }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
