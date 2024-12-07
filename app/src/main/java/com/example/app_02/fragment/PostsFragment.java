package com.example.app_02.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_02.R;
import com.example.app_02.model.Story;
import com.example.app_02.event.StoryPostedEvent;
import com.example.app_02.event.CommentPostedEvent;
import com.example.app_02.model.Comment;
import com.example.app_02.manager.PostManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class PostsFragment extends Fragment {
    private View rootView;
    private LinearLayout postsContainer;
    private PostManager postManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在这里注册 EventBus
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_posts, container, false);
        postsContainer = rootView.findViewById(R.id.posts_container);
        postManager = PostManager.getInstance();
        
        // 显示所有帖子
        displayAllPosts();
        
        return rootView;
    }

    private void displayAllPosts() {
        // 确保有初始帖子
        postManager.addInitialStory();
        
        // 清空容器
        postsContainer.removeAllViews();
        
        // 显示所有帖子（按照添加顺序显示，最新的在前面）
        List<Story> stories = postManager.getStories();
        for (Story story : stories) {
            addPostCard(story, -1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStoryPosted(StoryPostedEvent event) {
        Log.d("PostsFragment", "收到新帖子事件: " + event.getStory().getTitle());
        Story story = event.getStory();
        // 刷新整个列表
        displayAllPosts();
    }

    private void addPostCard(Story story, int position) {
        try {
            View cardView = getLayoutInflater().inflate(R.layout.item_story, postsContainer, false);
            
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

            likeButton.setOnClickListener(v -> {
                story.toggleLike();
                likeCount.setText(String.valueOf(story.getLikeCount()));
                likeButton.setImageResource(story.isLiked() ? 
                    R.drawable.like : R.drawable.heart_1);
            });

            // 设置评论按钮点击事件
            View commentButton = cardView.findViewById(R.id.btn_comment);
            TextView commentCount = cardView.findViewById(R.id.textview_comment);
            commentCount.setText(String.valueOf(story.getCommentCount()));
            
            commentButton.setOnClickListener(v -> showCommentDialog(story));

            // 设置分享按钮点击事件
            View shareButton = cardView.findViewById(R.id.btn_share);
            shareButton.setOnClickListener(v -> shareStory(story));

            if (position == -1) {
                postsContainer.addView(cardView);
            } else {
                postsContainer.addView(cardView, 0);  // 总是添加到顶部
            }
        } catch (Exception e) {
            Log.e("PostsFragment", "添加帖子卡片失败", e);
        }
    }

    private void showCommentDialog(Story story) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_comment, null);
        
        EditText editComment = dialogView.findViewById(R.id.edit_comment);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnComment = dialogView.findViewById(R.id.btn_comment);

        AlertDialog dialog = builder.setView(dialogView).create();

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnComment.setOnClickListener(v -> {
            String content = editComment.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(getContext(), "评论内容不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            Comment comment = new Comment(
                content,
                "XXX 主任医师 医生集团-北京 皮肤科",
                "刚刚",
                story
            );

            story.addComment(comment);
            EventBus.getDefault().post(new CommentPostedEvent(comment));
            
            dialog.dismiss();
            Toast.makeText(getContext(), "评论成功", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    private void shareStory(Story story) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        
        String shareText = String.format(
            "【%s】\n\n%s\n\n作者：%s\n发布时间：%s",
            story.getTitle(),
            story.getContent(),
            story.getAuthorName(),
            story.getTime()
        );
        
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        
        try {
            startActivity(Intent.createChooser(shareIntent, "分享到"));
        } catch (Exception e) {
            Toast.makeText(getContext(), "分享失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 在这里注销 EventBus
        EventBus.getDefault().unregister(this);
    }
} 