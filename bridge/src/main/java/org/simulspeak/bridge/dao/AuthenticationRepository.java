package org.simulspeak.bridge.dao;

import org.simulspeak.bridge.domain.UserAuth;
import org.simulspeak.bridge.domain.UserInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface AuthenticationRepository extends JpaRepository<UserAuth, Long> {
    
    List<UserAuth> findByUserInfo(UserInfo userInfo);

    UserAuth findByUserInfoAndIdentityType(UserInfo userInfo, String identityType);

}
