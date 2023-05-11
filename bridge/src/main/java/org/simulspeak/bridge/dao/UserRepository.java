package org.simulspeak.bridge.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import org.simulspeak.bridge.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String userName);

}
