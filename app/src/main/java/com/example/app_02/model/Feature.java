package com.example.app_02.model;

public class Feature {
    private int iconResId;
    private String name;

    public Feature(int iconResId, String name) {
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