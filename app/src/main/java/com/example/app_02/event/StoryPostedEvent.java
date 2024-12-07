package com.example.app_02.event;

import com.example.app_02.model.Story;

public class StoryPostedEvent {
    private Story story;

    public StoryPostedEvent(Story story) {
        this.story = story;
    }

    public Story getStory() {
        return story;
    }
} 