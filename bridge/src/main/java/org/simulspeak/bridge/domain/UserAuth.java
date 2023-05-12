package org.simulspeak.bridge.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="user_auth")
public class UserAuth implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable=false, name = "auth_id")
    private Long authId;

    @Column(name = "identity_type")
    private String identityType;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "credential")
    private String credential;
    
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserInfo userInfo;

    public UserAuth() {
        super();
    }

    public UserAuth(String identityType, String identifier, String credential) {
        super();
        this.identityType = identityType;
        this.identifier = identifier;
        this.credential = credential;
    }

    @Override
    public String toString() {
        return String.format(
                "UserAuth [ authId=%d, identityType='%s', identifier='%s', credential='%s']",
                authId, identityType, identifier, credential);
    }

    public Long getAuthId() {
        return authId;
    }

    public void setAuthId(Long authId) {
        this.authId = authId;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }
    
    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        if (!userInfo.getUserAuths().contains(this)) {
            userInfo.getUserAuths().add(this);
        }
    }
}
