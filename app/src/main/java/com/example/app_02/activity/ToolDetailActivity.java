package com.example.app_02.activity;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.app_02.R;

public class ToolDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_detail);

        // 设置工具栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // 获取传递的参数
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");

        // 设置标题和内容
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        TextView contentText = findViewById(R.id.content_text);
        contentText.setText(content);
    }
}