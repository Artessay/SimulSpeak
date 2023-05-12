package org.simulspeak.bridge.component;

import org.simulspeak.bridge.dao.AuthenticationRepository;
import org.simulspeak.bridge.dao.UserRepository;
import org.simulspeak.bridge.domain.UserAuth;
import org.simulspeak.bridge.domain.UserInfo;
import org.simulspeak.bridge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserImplement implements UserService {

    private final static Long ERROR_USER_ID = -1L;

    // private final static Logger logger = LoggerFactory.getLogger(UserImplement.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationRepository authenticationRepository;
    
    @Override
    public boolean register(String userName, String identityType, String identifier, String credential) {
        UserInfo user = userRepository.findByUserName(userName);
        if (user != null) {
            // logger.info("username {} has already existed", userName);
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
        UserAuth userAuth = authenticationRepository.findByIdentityTypeAndIdentifierAndCredential(identityType, identifier, credential);
        
        if (userAuth == null) {
            return ERROR_USER_ID;
        }

        UserInfo userInfo = userAuth.getUserInfo();
        assert(userInfo != null);

        return userInfo.getUserId();
    }

}
