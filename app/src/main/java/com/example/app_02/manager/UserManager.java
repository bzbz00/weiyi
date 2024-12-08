package com.example.app_02.manager;

public class UserManager {
    private static UserManager instance;
    private String nickname;
    private boolean isLoggedIn = false;

    private UserManager() {}

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void login(String nickname) {
        this.nickname = nickname;
        this.isLoggedIn = true;
    }

    public void logout() {
        this.nickname = null;
        this.isLoggedIn = false;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
} 