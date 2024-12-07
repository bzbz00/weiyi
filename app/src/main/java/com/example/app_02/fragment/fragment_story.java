package com.example.app_02.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.app_02.R;
import com.example.app_02.model.Story;
import com.example.app_02.event.StoryPostedEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_story#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_story extends Fragment {

    private Button btnPostStory;
    private List<Story> storyList;
    private LinearLayout storyContainer;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_story() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_story.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_story newInstance(String param1, String param2) {
        fragment_story fragment = new fragment_story();
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
        View view = inflater.inflate(R.layout.fragment_story, container, false);
        
        btnPostStory = view.findViewById(R.id.button_tell);
        btnPostStory.setOnClickListener(v -> showPostDialog());
        
        storyContainer = view.findViewById(R.id.story_container);
        
        return view;
    }

    private void showPostDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_post_story, null);
        
        EditText editTitle = dialogView.findViewById(R.id.edit_title);
        EditText editContent = dialogView.findViewById(R.id.edit_content);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnPost = dialogView.findViewById(R.id.btn_post);

        AlertDialog dialog = builder.setView(dialogView).create();

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnPost.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String content = editContent.getText().toString().trim();
            
            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(getContext(), "标题和内容不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            // 创建新故事
            Story newStory = new Story(
                (int) System.currentTimeMillis(),  // 使用当前时间戳作为 id
                title,
                content,
                "XXX 主任医师 医生集团-北京 皮肤科",
                "刚刚",
                R.drawable.per
            );

            try {
                // 通过EventBus发送新故事到全部页面
                EventBus.getDefault().post(new StoryPostedEvent(newStory));
                Log.d("StoryFragment", "故事发送成功");
                
                dialog.dismiss();
                Toast.makeText(getContext(), "发表成功", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("StoryFragment", "发送故事失败", e);
                Toast.makeText(getContext(), "发表失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
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
            Log.d("StoryFragment", "开始添加故事卡片: " + story.getTitle());
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
            Log.d("StoryFragment", "故事卡片添加成功");
        } catch (Exception e) {
            Log.e("StoryFragment", "添加故事卡片失败", e);
        }
    }
}