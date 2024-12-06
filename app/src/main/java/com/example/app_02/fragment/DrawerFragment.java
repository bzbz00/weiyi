package com.example.app_02.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_02.R;

public class DrawerFragment extends Fragment {

    private ListView historyList;
    private ListView followedPostsList;
    private TextView menuItem1;
    private TextView menuItem2;
    private TextView menuItem3;
    private TextView menuItem4;
    private TextView menuItem5;
    private TextView menuItem6;
    private ImageButton btnScan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.drawer_menu, container, false);

        // 初始化视图
        initializeViews(view);

        // 设置菜单项点击事件
        setMenuItemClickListeners();

        // 设置扫描按钮点击事件
        setScanButtonClickListeners();

        // TODO: 填充历史足迹列表和关注贴列表的数据

        return view;
    }

    // 初始化视图
    private void initializeViews(View view) {
        historyList = view.findViewById(R.id.history_list);
        followedPostsList = view.findViewById(R.id.followed_posts_list);
        menuItem1 = view.findViewById(R.id.menu_item_1);
        menuItem2 = view.findViewById(R.id.menu_item_2);
        menuItem3 = view.findViewById(R.id.menu_item_3);
        menuItem4 = view.findViewById(R.id.menu_item_4);
        menuItem5 = view.findViewById(R.id.menu_item_5);
        menuItem6 = view.findViewById(R.id.menu_item_6);
        btnScan = view.findViewById(R.id.btn_scan);
    }

    // 设置菜单项点击事件
    private void setMenuItemClickListeners() {
        menuItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理菜单项1点击事件
            }
        });

        menuItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理菜单项2点击事件
            }
        });

        menuItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理菜单项3点击事件
            }
        });

        menuItem4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理菜单项4点击事件
            }
        });

        menuItem5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理菜单项5点击事件
            }
        });

        menuItem6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理菜单项6点击事件
            }
        });
    }

    // 设置扫描按钮点击事件
    private void setScanButtonClickListeners() {
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理扫描按钮点击事件
            }
        });
    }
}
