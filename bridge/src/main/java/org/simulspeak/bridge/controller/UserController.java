package org.simulspeak.bridge.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.simulspeak.bridge.dao.UserRepository;
import org.simulspeak.bridge.domain.UserInfo;
import org.simulspeak.bridge.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @ResponseBody
    public boolean register(String identifier, String credential) {
        return true;
        // return userService;
    }

    @RequestMapping("/getAllUser")
    @ResponseBody
    public List<UserInfo> findAll() {
        List<UserInfo> list = new ArrayList<UserInfo>();
        list = userRepository.findAll();
        return list;
    }

    @GetMapping("/getByUserName")
    @ResponseBody
    public UserInfo getByUserName(String userName) {
        UserInfo user = userRepository.findByUserName(userName);
        return user;
    }

}
