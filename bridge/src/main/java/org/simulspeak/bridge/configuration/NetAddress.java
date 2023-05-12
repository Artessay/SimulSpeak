package org.simulspeak.bridge.configuration;

import java.util.Map;

public class NetAddress {
    private String ip;
    private String port;

    public Map<String, Object> toJson() {
        Map<String, Object> map = new java.util.HashMap<>();
        map.put("ip", ip);
        map.put("port", port);
        return map;
    }
    
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getPort() {
        return port;
    }
    public void setPort(String port) {
        this.port = port;
    }
    
}
