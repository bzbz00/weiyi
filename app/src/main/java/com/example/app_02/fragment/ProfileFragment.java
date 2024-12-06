package com.example.app_02.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_02.R;
import com.example.app_02.adapter.ToolServiceAdapter;
import com.example.app_02.model.ToolService;

import java.util.Arrays;
import java.util.List;

public class ProfileFragment extends Fragment {
    
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        // 设置WindowInsets监听器
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolsGrid();
    }

    private void setupToolsGrid() {
        RecyclerView toolsGrid = rootView.findViewById(R.id.tools_grid);
        
        List<ToolService> tools = Arrays.asList(
            new ToolService(R.drawable.image_11, "患者管理"),
            new ToolService(R.drawable.image_12, "我的病历"),
            new ToolService(R.drawable.image_13, "我的处方"),
            new ToolService(R.drawable.image_14, "已预约药单"),
            new ToolService(R.drawable.image_15, "康复任务"),
            new ToolService(R.drawable.image_16, "线上服务评价"),
            new ToolService(R.drawable.image_17, "我的收藏"),
            new ToolService(R.drawable.image_18, "关注的科普号"),
            new ToolService(R.drawable.image_19, "我的暖心"),
            new ToolService(R.drawable.image_20, "发票管理"),
            new ToolService(R.drawable.image_21, "用药日记"),
            new ToolService(R.drawable.image_22, "看病日记"),
            new ToolService(R.drawable.image_23, "诊后评价"),
            new ToolService(R.drawable.image_24, "填写的调查表"),
            new ToolService(R.drawable.image_25, "我是医生")
        );

        ToolServiceAdapter adapter = new ToolServiceAdapter(tools);
        adapter.setOnItemClickListener((tool, position) -> {
            // 处理工具项点击事件
            Toast.makeText(getContext(), "点击了: " + tool.getName(), Toast.LENGTH_SHORT).show();
        });

        toolsGrid.setAdapter(adapter);
        toolsGrid.setLayoutManager(new GridLayoutManager(getContext(), 4));
    }
}