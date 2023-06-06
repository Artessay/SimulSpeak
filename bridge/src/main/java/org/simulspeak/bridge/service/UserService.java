package org.simulspeak.bridge.service;


public interface UserService {

    public Long register(String userName, String identityType, String identifier, String credential);

    public Long login(String identityType, String identifier, String credential);

}
