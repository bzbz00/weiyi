package com.example.app_02.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_02.R;
import com.example.app_02.adapter.FeaturesAdapter;
import com.example.app_02.model.Feature;

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
    }

    private void setupFeaturesGrid() {
        RecyclerView featuresGrid = rootView.findViewById(R.id.features_grid);
        
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

        FeaturesAdapter adapter = new FeaturesAdapter(features);
        adapter.setOnFeatureClickListener((feature, position) -> {
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
        });
        
        featuresGrid.setAdapter(adapter);
        featuresGrid.setLayoutManager(new GridLayoutManager(getContext(), 4));
    }
}
