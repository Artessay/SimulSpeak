package org.simulspeak.bridge.controller;

import java.util.HashMap;
import java.util.Map;

import org.simulspeak.bridge.configuration.NetAddress;
import org.simulspeak.bridge.service.CommentService;
import org.simulspeak.bridge.service.SubtitleService;
import org.simulspeak.bridge.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/video")
public class Controller {

    @Autowired
    private VideoService videoService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private SubtitleService subtitleService;

    @GetMapping("/recommend")
    public String recommend() {
        System.out.println("recommend");

        return videoService.recommend();
    }

    @GetMapping("/search")
    public String search(String videoName) {
        System.out.println("search");

        return videoService.search(videoName);
    }

    @GetMapping("/searchByUser")
    public String searchByUser(Long userId) {
        System.out.println("searchByUser");

        return videoService.searchByUser(userId);
    }

    @GetMapping("/upload")
    public String upload(String videoName, Long userId) {
        String result = "";
        System.out.println("upload");
        NetAddress netAddress = new NetAddress();
        Long videoID = videoService.upload(videoName, userId, netAddress);

        if (videoID >= 0) {
            result = netAddress.toString();
            result += "\r\n" + userId;
            result += "\r\n" + videoID;
        }

        // System.out.println(result);

        return result;
    }

    @GetMapping("/uploadVideo")
    public Map<String, String> uploadVideo(String videoName, Long userId) {
        Map<String, String> map;

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

    @GetMapping("request")
    public String request(Long videoId) {
        System.out.println("request");
        return videoService.request(videoId);
    }

    @GetMapping("/requestVideo")
    public Map<String, String> requestVideo(String videoName, Long userId) {
        Map<String, String> map;

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

    @GetMapping("/pullComment")
    public String pullComment(Long videoId) {
        System.out.println("comment: pull");

        return commentService.pull(videoId);
    }

    @GetMapping("/pushComment")
    public String pushComment(Long videoId, Long userId, String comment) {
        System.out.println("comment: push");
        
        if (commentService.push(videoId, userId, comment)) {
            return "success";
        } else {
            return "failed";
        }
    }

    @GetMapping("/SubTitle")
    public String Subtitle(Long videoId) {
        System.out.println("Subtitle");

        return "";
        // return subtitleService.subtitle(videoId);
    }
 
    @RequestMapping("/test")
    public Map<String, Object> test(){
        Map<String, Object> map=new HashMap<>();
        map.put("msg", "访问成功！");

        return map;
    }
}