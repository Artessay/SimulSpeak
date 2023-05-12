package org.simulspeak.bridge.domain;

import java.sql.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

// import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    // @Column(name = "video_path")
    // private String videoPath;

    @CreationTimestamp
    @Column(name = "upload_time")
    // @Column(name = "upload_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Date uploadTime;

    @LastModifiedDate
    @Column(name = "update_time")
    // @Column(name = "update_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Date updateTime;

    @ManyToOne
    @JoinColumn(name = "uploader_id", referencedColumnName = "user_id", nullable = false)
    private UserInfo userInfo;

    public VideoInfo() {
        super();
    }

    public VideoInfo(String videoName) {
        super();
        this.videoName = videoName;
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

    public Date getUploadTime() {
        return uploadTime;
    }

    // public void setUploadTime(Date uploadTime) {
    //     this.uploadTime = uploadTime;
    // }

    public Date getUpdateTime() {
        return updateTime;
    }

    // public void setUpdateTime(Date updateTime) {
    //     this.updateTime = updateTime;
    // }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        if (!userInfo.getVideoInfos().contains(this)) {
            userInfo.getVideoInfos().add(this);
        }
    }
}
