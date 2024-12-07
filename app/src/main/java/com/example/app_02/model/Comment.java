package com.example.app_02.model;

public class Comment {
    private String content;
    private String authorName;
    private String time;
    private Story story;  // 关联的帖子

    public Comment(String content, String authorName, String time, Story story) {
        this.content = content;
        this.authorName = authorName;
        this.time = time;
        this.story = story;
    }

    // Getters
    public String getContent() { return content; }
    public String getAuthorName() { return authorName; }
    public String getTime() { return time; }
    public Story getStory() { return story; }
} 