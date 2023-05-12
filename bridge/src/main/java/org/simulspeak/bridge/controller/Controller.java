package org.simulspeak.bridge.controller;

import java.util.HashMap;
import java.util.Map;

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
    public boolean uploadVideo(String videoName, Long userId) {
        return videoService.uploadVideo(videoName, userId);
    }
 
    @RequestMapping("/test")
    public Map<String,Object> test(){
        Map<String,Object> map=new HashMap<>();
        map.put("msg", "访问成功！");

        return map;
    }
}