package com.example.app_02.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.app_02.R;
import com.example.app_02.model.Comment;
import com.example.app_02.model.Story;
import com.example.app_02.event.StoryPostedEvent;
import com.example.app_02.event.CommentPostedEvent;
import com.example.app_02.method.Message;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DiscussionFragment extends Fragment {
    private DrawerLayout drawerLayout;
    private ImageButton drawerButton;
    private View rootView;
    private LinearLayout storyContainer;
    private Fragment currentFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_discussion, container, false);
        storyContainer = rootView.findViewById(R.id.story_container);
        
        initViews();
        EventBus.getDefault().register(this);
        
        // 添加一些初始的故事
        addInitialStories();
        
        return rootView;
    }

    private void initViews() {
        setHasOptionsMenu(true);
        setupDrawer();
        setupToolbar();
        
        // 初始加载推荐Fragment(用于显示发帖内容)
        if (currentFragment == null) {
            loadFragment(new PostsFragment());  // 创建新的Fragment来显示发帖内容
        }
    }

    private void setupDrawer() {
        drawerLayout = rootView.findViewById(R.id.drawer_layout);
        drawerButton = rootView.findViewById(R.id.drawer_button);
        
        drawerButton.setOnClickListener(v -> {
            if (drawerLayout != null) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }

        toolbar.setOnMenuItemClickListener(item -> {
            Fragment selectedFragment = getFragmentForMenuItem(item.getItemId());
            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    private Fragment getFragmentForMenuItem(int itemId) {
        if (itemId == R.id.Story) {
            return new fragment_story();
        } else if (itemId == R.id.Hot) {
            return new HotFragment();
        } else if (itemId == R.id.Recommend) {
            return new PostsFragment();  // 返回发帖内容Fragment
        } else if (itemId == R.id.Concern) {
            return new ConcernFragment();  // 修正拼写错误
        }
        return null;
    }

    private void loadFragment(Fragment fragment) {
        if (getActivity() != null && fragment != null) {
            currentFragment = fragment;
            FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                .replace(R.id.fragment_container, fragment);
            
            transaction.addToBackStack(null);
            transaction.commit();
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStoryPosted(StoryPostedEvent event) {
        addStoryCard(event.getStory(), 0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommentPosted(CommentPostedEvent event) {
        Comment comment = event.getComment();
        Story story = comment.getStory();
        
        // 创建新消息并发送事件
        Message message = new Message(
            String.format("%s 评论了你的帖子: %s", comment.getAuthorName(), comment.getContent()),
            comment.getTime(),
            0,
            story.getTitle(),
            story.getId()
        );
        
        // 直接发送消息事件
        EventBus.getDefault().post(message);
    }

    private void saveMessage(Message message) {
        // 这里可以将消息保存到本地数据库或发送到服务器
        // 保存后,MessagesFragment应该可以加载这些消息
    }

    private void addStoryCard(Story story, int position) {
        View cardView = getLayoutInflater().inflate(R.layout.item_story, storyContainer, false);
        
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

        // 设置分享按钮点击事件
        View shareButton = cardView.findViewById(R.id.btn_share);
        shareButton.setOnClickListener(v -> shareStory(story));

        if (position == -1) {
            storyContainer.addView(cardView);
        } else {
            storyContainer.addView(cardView, position);
        }
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
            // 发送评论事件
            EventBus.getDefault().postSticky(new CommentPostedEvent(comment));
            
            dialog.dismiss();
            Toast.makeText(getContext(), "评论成功", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.ttop_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}