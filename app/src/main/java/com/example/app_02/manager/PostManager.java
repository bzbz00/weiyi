package com.example.app_02.manager;

import com.example.app_02.R;
import com.example.app_02.model.Story;
import java.util.ArrayList;
import java.util.List;

public class PostManager {
    private static PostManager instance;
    private List<Story> stories = new ArrayList<>();
    private boolean hasInitialStory = false;  // 添加标志位

    private PostManager() {
        // 私有构造函数
    }

    public static synchronized PostManager getInstance() {
        if (instance == null) {
            instance = new PostManager();
        }
        return instance;
    }

    public void addStory(Story story) {
        // 检查是否已存在相同的故事（根据标题和内容判断）
        for (Story existingStory : stories) {
            if (existingStory.getTitle().equals(story.getTitle()) 
                && existingStory.getContent().equals(story.getContent())) {
                return;  // 如果已存在，直接返回
            }
        }
        stories.add(0, story);  // 添加到列表开头
    }

    public List<Story> getStories() {
        return new ArrayList<>(stories);  // 返回副本，避免外部修改
    }

    public void addInitialStory() {
        if (!hasInitialStory && stories.isEmpty()) {
            Story story1 = new Story(
                1,  // 提供一个 id
                "秋冬交替，谨防水痘！请收下这份防护手册!",
                "今年秋冬交替，在这个时期，人们抵抗力容易下降，会导致病毒入侵，那么我们应该怎么保护自己呢？\n1.首先，多运动\n2.经常锻炼\n...",
                "XXX 主任医师 医生集团-北京 皮肤科",
                "2小时前",
                R.drawable.per
            );
            addStory(story1);
            hasInitialStory = true;  // 设置标志位
        }
    }
} 