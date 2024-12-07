package com.example.app_02.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_02.R;
import com.example.app_02.adapter.HealthNewsAdapter;
import com.example.app_02.model.Feature;
import com.example.app_02.model.HealthNews;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
        }
        // 避免重复创建视图
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupFeaturesGrid();
        setupHealthNewsList();
    }

    private void setupFeaturesGrid() {
        // 获取功能区域的 LinearLayout
        LinearLayout featuresRow1 = rootView.findViewById(R.id.features_row_1);
        LinearLayout featuresRow2 = rootView.findViewById(R.id.features_row_2);

        List<Feature> features = Arrays.asList(
            new Feature(R.drawable.xianghuzuoyong, getString(R.string.xianghuzuoyong)),
            new Feature(R.drawable.yixuejisuan, getString(R.string.yixuejisuan)),
            new Feature(R.drawable.xunzhengyixue, getString(R.string.xunzhengyixue)),
            new Feature(R.drawable.yixuejiancha, getString(R.string.yixuejianyan)),
            new Feature(R.drawable.yongyaozhushouicon, getString(R.string.yongyaozhushou)),
            new Feature(R.drawable.yixueyingxiang, getString(R.string.yixueyingxiang)),
            new Feature(R.drawable.laonianyixue, getString(R.string.laonianyixue)),
            new Feature(R.drawable.yixuefenhui, getString(R.string.yixuefenhui)),
            new Feature(R.drawable.yixuebiaozhi, getString(R.string.yixuebiaozhi)),
            new Feature(R.drawable.zhongzhengyixue, getString(R.string.zhongzhengyixue)),
            new Feature(R.drawable.yixuezhengming, getString(R.string.yixuezhengming)),
            new Feature(R.drawable.yixuekepu, getString(R.string.yixuekepu)),
            new Feature(R.drawable.yaowuchaxun, getString(R.string.peiwujinji)),
            new Feature(R.drawable.yixueziliao, getString(R.string.yixueziliao)),
            new Feature(R.drawable.yundong, getString(R.string.yundong)),
            new Feature(R.drawable.more, getString(R.string.more))
        );

        // 将前8个功能添加到第一行
        for (int i = 0; i < 8; i++) {
            Feature feature = features.get(i);
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_feature, featuresRow1, false);
            ImageView iconView = itemView.findViewById(R.id.feature_icon);
            TextView nameView = itemView.findViewById(R.id.feature_name);
            iconView.setImageResource(feature.getIconResId());
            nameView.setText(feature.getName());
            featuresRow1.addView(itemView);
        }

        // 将后8个功能添加到第二行
        for (int i = 8; i < features.size(); i++) {
            Feature feature = features.get(i);
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_feature, featuresRow2, false);
            ImageView iconView = itemView.findViewById(R.id.feature_icon);
            TextView nameView = itemView.findViewById(R.id.feature_name);
            iconView.setImageResource(feature.getIconResId());
            nameView.setText(feature.getName());
            featuresRow2.addView(itemView);
        }

        // 设置功能点击事件
        View.OnClickListener onFeatureClickListener = v -> {
            int position = (int) v.getTag();
            Feature feature = features.get(position);
            // 处理特性点击事件
            switch (position) {
                case 0: // 相互作用
                    // TODO: 处理相互作用点击
                    break;
                case 1: // 医学计算
                    // TODO: 处理医学计算点击
                    break;
                // ... 处理其他特性点击
            }
        };

        // 为每个功能项设置点击事件和位置标记
        for (int i = 0; i < featuresRow1.getChildCount(); i++) {
            View itemView = featuresRow1.getChildAt(i);
            itemView.setTag(i);
            itemView.setOnClickListener(onFeatureClickListener);
        }
        for (int i = 0; i < featuresRow2.getChildCount(); i++) {
            View itemView = featuresRow2.getChildAt(i);
            itemView.setTag(i + 8);
            itemView.setOnClickListener(onFeatureClickListener);
        }
    }

    private void setupHealthNewsList() {
        RecyclerView healthNewsList = rootView.findViewById(R.id.health_news_list);
        
        // 添加触摸事件拦截
        healthNewsList.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });

        // 设置自定义布局管理器来处理滑动
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), 
            LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean canScrollHorizontally() {
                return true;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        healthNewsList.setLayoutManager(layoutManager);

        // 添加滑动监听
        healthNewsList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            private float startX;
            private static final float CLICK_THRESHOLD = 5f;

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = e.getX();
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float deltaX = Math.abs(e.getX() - startX);
                        if (deltaX > CLICK_THRESHOLD) {
                            rv.getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        rv.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

        List<HealthNews> newsList = Arrays.asList(
            new HealthNews(R.drawable.new1, "新冠病毒最新研究进展",
                "最新研究表明，新冠病毒变异株...", "2小时前"),
            new HealthNews(R.drawable.new2, "秋季养生小贴士",
                "秋季养生要注意以下几点...", "4小时前"),
            new HealthNews(R.drawable.new3, "高血压预防指南",
                "预防高血压的生活方式建议...", "6小时前"),
            new HealthNews(R.drawable.new4, "糖尿病饮食指南",
                "糖尿病患者的饮食注意事项...", "8小时前"),
            new HealthNews(R.drawable.new5, "儿童健康成长指南",
                "如何帮助孩子健康成长...", "10小时前")
        );

        HealthNewsAdapter adapter = new HealthNewsAdapter(newsList);
        adapter.setOnItemClickListener((news, position) -> {
            Toast.makeText(getContext(), "点击了: " + news.getTitle(), Toast.LENGTH_SHORT).show();
        });

        healthNewsList.setAdapter(adapter);
    }
}
