package com.example.app_02.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.app_02.R;
import com.example.app_02.adapter.ViewPagerAdapter;
import com.example.app_02.event.StoryPostedEvent;
import com.example.app_02.manager.PostManager;
import com.example.app_02.manager.UserManager;
import com.example.app_02.model.Story;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;

import service.FloatBallService;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fabPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupViewPager();
        setupBottomNavigation();
        setupFabPost();
        manageOverlayPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initViews() {
        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fabPost = findViewById(R.id.fab_post);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        
        // 设置预加载页面数量
        viewPager.setOffscreenPageLimit(3);
        
        // 设置滑动灵敏度
        viewPager.setUserInputEnabled(true);
        
        // 添加自定义触摸事件处理
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });

        // 设置页面转换动画
        viewPager.setPageTransformer((page, position) -> {
            float absPosition = Math.abs(position);
            page.setAlpha(1f - absPosition * 0.5f);
        });
    }

    private void setupBottomNavigation() {
        // 设置底部导航栏点击监听
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                viewPager.setCurrentItem(0, true);
                return true;
            } else if (itemId == R.id.nav_discussion) {
                viewPager.setCurrentItem(1, true);
                return true;
            } else if (itemId == R.id.nav_messages) {
                viewPager.setCurrentItem(2, true);
                return true;
            } else if (itemId == R.id.nav_profile) {
                viewPager.setCurrentItem(3, true);
                return true;
            }
            return false;
        });
    }

    private void setupFabPost() {
        fabPost.setOnClickListener(v -> {
            showPostDialog();
        });
    }

    private void showPostDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                Toast.makeText(this, "标题和内容不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            String authorName = UserManager.getInstance().getUsername();
            if (authorName == null) {
                authorName = "游客";
            }

            Story newStory = new Story(
                    (int) System.currentTimeMillis(),
                title,
                content,
                authorName,
                "刚刚",
                R.drawable.per
            );

            try {
                PostManager.getInstance().addStory(newStory);
                EventBus.getDefault().postSticky(new StoryPostedEvent(newStory));
                
                dialog.dismiss();
                Toast.makeText(this, "发表成功", Toast.LENGTH_SHORT).show();
                
                viewPager.setCurrentItem(1, true);
            } catch (Exception e) {
                Log.e("MainActivity", "发帖失败", e);
                Toast.makeText(this, "发表失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void manageOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                requestOverlayPermission();
            } else {
                startFloatBallService();
            }
        } else {
            startFloatBallService();
        }
    }

    private void requestOverlayPermission() {
        Toast.makeText(this, "请开启悬浮窗权限", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    private void startFloatBallService() {
        startService(new Intent(this, FloatBallService.class));
    }
}
