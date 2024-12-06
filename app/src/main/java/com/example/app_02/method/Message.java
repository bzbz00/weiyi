package com.example.app_02.method;

public class Message {
    private String title;
    private int iconResId; // 图标资源 ID

    public Message(String title, int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResId() {
        return iconResId; // 返回图标资源 ID
    }
}
