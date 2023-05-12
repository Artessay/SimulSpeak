package org.simulspeak.bridge.component;

import org.simulspeak.bridge.configuration.BridgeConfig;
import org.simulspeak.bridge.dao.AuthenticationRepository;
import org.simulspeak.bridge.dao.UserRepository;
import org.simulspeak.bridge.domain.UserAuth;
import org.simulspeak.bridge.domain.UserInfo;
import org.simulspeak.bridge.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserImplement implements UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserImplement.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationRepository authenticationRepository;
    
    @Override
    public boolean register(String userName, String identityType, String identifier, String credential) {
        UserInfo user = userRepository.findByUserName(userName);
        if (user != null) {
            logger.debug("username {} has already existed", userName);
            return false;
        }

        user = new UserInfo(userName);
        UserAuth userAuth = new UserAuth(identityType, identifier, credential);
        userAuth.setUserInfo(user);
        
        userRepository.saveAndFlush(user);
        authenticationRepository.saveAndFlush(userAuth);

        return true;
    }

    @Override
    public Long login(String identityType, String identifier, String credential) {
        if (identityType == null || identifier == null || credential == null) {
            logger.debug("some login parameter is null");
            return BridgeConfig.ERROR_USER_ID;
        }
        
        UserAuth userAuth = authenticationRepository.findByIdentityTypeAndIdentifierAndCredential(identityType, identifier, credential);
        
        if (userAuth == null) {
            return BridgeConfig.ERROR_USER_ID;
        }

        UserInfo userInfo = userAuth.getUserInfo();
        assert(userInfo != null);

        return userInfo.getUserId();
    }

}
