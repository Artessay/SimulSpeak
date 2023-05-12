package org.simulspeak.bridge.controller;

import java.util.HashMap;
import java.util.Map;

import org.simulspeak.bridge.dao.CustomerRepository;
import org.simulspeak.bridge.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
@RequestMapping("/admin")
public class Controller {

    @Autowired
    private CustomerRepository customerRepository;

    @RequestMapping("/index")
    public String index(){
        Customer result = customerRepository.findById(1L);;
        return result != null ? result.toString() : "No result";
    }
 
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