package org.simulspeak.bridge.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import org.simulspeak.bridge.domain.UserInfo;


public interface UserRepository extends JpaRepository<UserInfo, Long> {

    UserInfo findByUserId(Long userId);

    UserInfo findByUserName(String userName);

}
