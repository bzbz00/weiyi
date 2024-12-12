package com.example.app_02.fragment;

import android.content.Intent;
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
import com.example.app_02.activity.ToolDetailActivity;
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

        // 创建功能列表
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
            setupFeatureClick(itemView, feature);
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
            setupFeatureClick(itemView, feature);
            featuresRow2.addView(itemView);
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
            new HealthNews(R.drawable.new2, "新冠病毒最新研究进展",
                "最新研究表明，新冠病毒变异株...", "2小时前"),
            new HealthNews(R.drawable.new2, "秋季养生小贴士",
                "秋季养生要注意以下点...", "4小时前"),
            new HealthNews(R.drawable.new2, "高血压预防指南",
                "预防高血压的生活方式建议...", "6小时前"),
            new HealthNews(R.drawable.new2, "糖尿病饮食指南",
                "糖尿病患者的饮食注意事项...", "8小时前"),
            new HealthNews(R.drawable.new2, "儿童健康成长指南",
                "如何帮助孩子健康成长...", "10小时前")
        );

        HealthNewsAdapter adapter = new HealthNewsAdapter(newsList);
        adapter.setOnItemClickListener((news, position) -> {
            Toast.makeText(getContext(), "点击了: " + news.getTitle(), Toast.LENGTH_SHORT).show();
        });

        healthNewsList.setAdapter(adapter);
    }

    private void setupFeatureClick(View featureView, Feature feature) {
        featureView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ToolDetailActivity.class);
            intent.putExtra("title", feature.getName());
            intent.putExtra("content", getToolContent(feature.getName()));
            startActivity(intent);
        });
    }

    private String getToolContent(String toolName) {
        switch (toolName) {
            case "相互作用":
                return "药物相互作用查询工具\n\n" +
                       "• 支持快速查询两种或多种药物之间的相互作用\n" +
                       "• 提供详细的相互作用机制说明\n" +
                       "• 包含用药建议和注意事项\n" +
                       "• 定期更新药物数据库";

            case "医学计算":
                return "常用医学计算工具集\n\n" +
                       "• 体质指数(BMI)计算\n" +
                       "• 体表面积计算\n" +
                       "• 肾小球滤过率估算\n" +
                       "• 药物剂量换算\n" +
                       "• 临床评分量表";

            case "医学检验":
                return "检验结果解读助手\n\n" +
                       "• 常见检验项目参考值查询\n" +
                       "• 异常结果分析与解释\n" +
                       "• 检验结果趋势分析\n" +
                       "• 相关疾病提示";

            // 添加其他工具的内容...
            default:
                return toolName + "\n\n该功能正在开发中，敬请期待...";
        }
    }
}
