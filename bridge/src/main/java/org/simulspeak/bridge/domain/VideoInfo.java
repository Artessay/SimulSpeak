package org.simulspeak.bridge.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="video_info")
public class VideoInfo {
    
    @Id
    @Column(nullable=false, name = "video_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoId;

    @Column(name = "video_name")
    private String videoName;

    @Column(name = "video_path")
    private String videoPath;

    @Column(name = "figure_path")
    private String figurePath;

    @Column(name = "audio_path")
    private String audioPath;

    // @Column(name = "comment_path")
    // private String commentPath;

    @CreationTimestamp
    @Column(name = "upload_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Timestamp uploadTime;

    @LastModifiedDate
    @Column(name = "update_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Timestamp updateTime;

    @ManyToOne
    @JoinColumn(name = "uploader_id", referencedColumnName = "user_id", nullable = false)
    private UserInfo userInfo;

    @OneToMany(mappedBy = "videoInfo", cascade = CascadeType.PERSIST)
    private List<VideoComment> videoComments;


    public VideoInfo() {
        super();
        this.videoComments = new ArrayList<>();
    }

    public VideoInfo(String videoName) {
        super();
        this.videoName = videoName;
        this.videoComments = new ArrayList<>();
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String video_path) {
        this.videoPath = video_path;
    }

    public String getFigurePath() {
        return figurePath;
    }

    public void setFigurePath(String figure_path) {
        this.figurePath = figure_path;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audio_path) {
        this.audioPath = audio_path;
    }

    // public String getCommentPath() {
    //     return commentPath;
    // }

    // public void setCommentPath(String comment_path) {
    //     this.commentPath = comment_path;
    // }

    public Timestamp getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        if (!userInfo.getVideoInfos().contains(this)) {
            userInfo.getVideoInfos().add(this);
        }
    }

    public List<VideoComment> getVideoComments() {
        return videoComments;
    }

    public void setVideoComments(List<VideoComment> videoComments) {
        this.videoComments = videoComments;
    }
    
}
