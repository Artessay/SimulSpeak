package org.simulspeak.bridge.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
@RequestMapping("/admin")
public class Controller {
 
    @RequestMapping("/test")
    public Map<String,Object> test(){
        Map<String,Object> map=new HashMap<>();
        map.put("msg", "访问成功！");
        return map;
    }

    @RequestMapping("/**")
    public String NotFound() {
        return "404 Not Found";
    }
}