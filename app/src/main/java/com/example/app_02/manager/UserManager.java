package com.example.app_02.manager;

public class UserManager {
    private static UserManager instance;
    private String nickname;
    private String username;
    private boolean isLoggedIn = false;

    private UserManager() {}

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void login(String username) {
        this.username = username;
        this.nickname = username;
        this.isLoggedIn = true;
    }

    public void logout() {
        this.nickname = null;
        this.username = null;
        this.isLoggedIn = false;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUsername() {
        return username;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
} 