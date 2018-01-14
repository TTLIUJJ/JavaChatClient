package com.service;

public class UserModel {
    private String userId;
    private String nickname;
    private String ip;
    private String port;

    public String getUserId() {
        return userId;
    }

    public UserModel setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public UserModel setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public UserModel setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getPort() {
        return port;
    }

    public UserModel setPort(String port) {
        this.port = port;
        return this;
    }
}
