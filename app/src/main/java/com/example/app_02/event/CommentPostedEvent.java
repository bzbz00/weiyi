package com.example.app_02.event;

import com.example.app_02.model.Comment;

public class CommentPostedEvent {
    private Comment comment;

    public CommentPostedEvent(Comment comment) {
        this.comment = comment;
    }

    public Comment getComment() {
        return comment;
    }
} 