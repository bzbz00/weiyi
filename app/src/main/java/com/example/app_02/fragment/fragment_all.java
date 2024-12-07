package com.example.app_02.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.app_02.R;
import com.example.app_02.model.Story;
import com.example.app_02.event.StoryPostedEvent;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class fragment_all extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Story> storyList = new ArrayList<>();
    private LinearLayout storyContainer;

    public fragment_all() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_all.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_all newInstance(String param1, String param2) {
        fragment_all fragment = new fragment_all();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        storyContainer = view.findViewById(R.id.linearlayout_all);
        
        // 注册EventBus
        EventBus.getDefault().register(this);
        
        // 添加一些初始的故事卡片
        addInitialStories();
        
        return view;
    }

    private void addInitialStories() {
        Story story1 = new Story(
            1,  // 提供一个 id
            "秋冬交替，谨防水痘！请收下这份防护手册!",
            "今年秋冬交替，在这个时期，人们抵抗力容易下降，会导致病毒入侵，那么我们应该怎么保护自己呢？\n1.首先，多运动\n2.经常锻炼\n...",
            "XXX 主任医师 医生集团-北京 皮肤科",
            "2小时前",
            R.drawable.per
        );
        addStoryCard(story1, -1);
    }

    private void addStoryCard(Story story, int position) {
        try {
            Log.d("AllFragment", "开始添加故事卡片: " + story.getTitle());
            View cardView = getLayoutInflater().inflate(R.layout.item_story, storyContainer, false);
            
            // 设置故事卡片的内容
            ImageView avatarView = cardView.findViewById(R.id.user_avatar);
            TextView titleView = cardView.findViewById(R.id.story_title);
            TextView contentView = cardView.findViewById(R.id.story_content);
            TextView authorView = cardView.findViewById(R.id.user_name);
            ImageView likeButton = cardView.findViewById(R.id.imageview_heart);
            TextView likeCount = cardView.findViewById(R.id.textview_heart);

            avatarView.setImageResource(story.getAvatarResId());
            titleView.setText(story.getTitle());
            contentView.setText(story.getContent());
            authorView.setText(story.getAuthorName());
            likeCount.setText(String.valueOf(story.getLikeCount()));

            // 设置点赞按钮点击事件
            likeButton.setOnClickListener(v -> {
                story.toggleLike();
                likeCount.setText(String.valueOf(story.getLikeCount()));
                likeButton.setImageResource(story.isLiked() ? 
                    R.drawable.like : R.drawable.heart_1);
            });

            // 添加到容器
            if (position == -1) {
                storyContainer.addView(cardView);
            } else {
                storyContainer.addView(cardView, position);
            }
            Log.d("AllFragment", "故事卡片添加成功");
        } catch (Exception e) {
            Log.e("AllFragment", "添加故事卡片失败", e);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStoryPosted(StoryPostedEvent event) {
        // 添加新故事到顶部
        addStoryCard(event.getStory(), 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}