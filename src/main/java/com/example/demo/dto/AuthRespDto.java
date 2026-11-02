package com.example.demo.dto;

public class AuthRespDto {
    private String token;
    private UserRespDto user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserRespDto getUser() {
        return user;
    }

    public void setUser(UserRespDto user) {
        this.user = user;
    }
}
