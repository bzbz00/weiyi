package com.example.app_02.model;

import java.util.ArrayList;
import java.util.List;

public class Story {
    private int id;
    private String title;
    private String content;
    private String authorName;
    private String time;
    private int avatarResId;
    private int likeCount;
    private boolean isLiked;
    private List<Comment> comments = new ArrayList<>();
    private int commentCount = 0;

    public Story(int id, String title, String content, String authorName, String time, int avatarResId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.time = time;
        this.avatarResId = avatarResId;
        this.likeCount = 0;
        this.isLiked = false;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthorName() { return authorName; }
    public String getTime() { return time; }
    public int getAvatarResId() { return avatarResId; }
    public int getLikeCount() { return likeCount; }
    public boolean isLiked() { return isLiked; }

    public void toggleLike() {
        isLiked = !isLiked;
        likeCount += isLiked ? 1 : -1;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        commentCount++;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public int getId() {
        return id;
    }
} 