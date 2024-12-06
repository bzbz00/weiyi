package com.example.app_02.model;

public class ToolService {
    private int iconResId;
    private String name;

    public ToolService(int iconResId, String name) {
        this.iconResId = iconResId;
        this.name = name;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getName() {
        return name;
    }
} 