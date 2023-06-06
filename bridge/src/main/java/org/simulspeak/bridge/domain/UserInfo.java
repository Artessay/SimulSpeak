package org.simulspeak.bridge.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable=false, name = "user_id")
    private Long userId;

    @Column(name = "username", unique = true, nullable=false)
    private String userName;

    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.PERSIST)
    private List<UserAuth> userAuths;

    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.PERSIST)
    private List<VideoInfo> videoInfos;

    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.PERSIST)
    private List<VideoComment> videoComments;

    public UserInfo() {
        super();
        this.userAuths = new ArrayList<>();
        this.videoInfos = new ArrayList<>();
    }

    public UserInfo(String userName) {
        super();
        this.userName = userName;
        this.userAuths = new ArrayList<>();
        this.videoInfos = new ArrayList<>();
    }

    @Override
	public String toString() {
		return String.format(
				"UserInfo [ userId=%d, userName='%s']",
				userId, userName);
	}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<UserAuth> getUserAuths() {
        return userAuths;
    }

    public void setUserAuths(List<UserAuth> userAuths) {
        this.userAuths = userAuths;
    }

    public List<VideoInfo> getVideoInfos() {
        return videoInfos;
    }

    public void setVideoInfos(List<VideoInfo> videoInfos) {
        this.videoInfos = videoInfos;
    }

    public List<VideoComment> getVideoComments() {
        return videoComments;
    }

    public void setVideoComments(List<VideoComment> videoComments) {
        this.videoComments = videoComments;
    }
}