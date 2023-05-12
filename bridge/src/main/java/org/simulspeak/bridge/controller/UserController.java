package org.simulspeak.bridge.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.simulspeak.bridge.dao.UserRepository;
import org.simulspeak.bridge.domain.UserInfo;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/getAllUser")
    @ResponseBody
    public List<UserInfo> findAll() {
        List<UserInfo> list = new ArrayList<UserInfo>();
        list = userRepository.findAll();
        return list;
    }

    @RequestMapping("/getByUserName")
    @ResponseBody
    public UserInfo getByUserName(String userName) {
        UserInfo user = userRepository.findByUserName(userName);
        return user;
    }

}
