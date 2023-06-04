package org.simulspeak.bridge.controller;

import java.util.HashMap;
import java.util.Map;

import org.simulspeak.bridge.configuration.NetAddress;
import org.simulspeak.bridge.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Controller {

    @Autowired
    private VideoService videoService;

    @GetMapping("/uploadVideo")
    public Map<String, Object> uploadVideo(String videoName, Long userId) {
        Map<String, Object> map;

        NetAddress netAddress = new NetAddress();
        if (videoService.uploadVideo(videoName, userId, netAddress)) {
            map = netAddress.toJson();
            map.put("status", "success");
        } else {
            map=new HashMap<>();
            map.put("status", "fail");
        }

        return map;
    }

    @GetMapping("/requestVideo")
    public Map<String, Object> requestVideo(String videoName, Long userId) {
        Map<String, Object> map;

        NetAddress netAddress = new NetAddress();
        if (videoService.requestVideo(videoName, userId, netAddress)) {
            map = netAddress.toJson();
            map.put("status", "success");
        } else {
            map=new HashMap<>();
            map.put("status", "fail");
        }

        return map;
    }
 
    @RequestMapping("/test")
    public Map<String, Object> test(){
        Map<String, Object> map=new HashMap<>();
        map.put("msg", "访问成功！");

        return map;
    }
}