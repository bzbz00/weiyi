package com.example.app_02.model;

public class HealthNews {
    private int imageResId;
    private String title;
    private String summary;
    private String time;

    public HealthNews(int imageResId, String title, String summary, String time) {
        this.imageResId = imageResId;
        this.title = title;
        this.summary = summary;
        this.time = time;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getTime() {
        return time;
    }
} 