package com.example.app_02.method;

public class Message {
    private String content;  // 消息内容
    private String time;  // 消息时间
    private int type;  // 消息类型,如评论、点赞等
    private String postTitle;  // 关联的帖子标题
    private int postId;  // 关联的帖子ID

    public Message(String content, String time, int type, String postTitle, int postId) {
        this.content = content;
        this.time = time;
        this.type = type;
        this.postTitle = postTitle;
        this.postId = postId;
    }

    // Getters
    public String getContent() { return content; }
    public String getTime() { return time; }
    public int getType() { return type; }
    public String getPostTitle() { return postTitle; }
    public int getPostId() { return postId; }
}
