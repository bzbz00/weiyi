package com.example.app_02.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.app_02.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

import com.example.app_02.fragment.ConcernListFragment;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConcernFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConcernFragment extends Fragment {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private View emptyState;
    private Button btnExplore;
    private List<String> categories = Arrays.asList("医生", "医院", "科室", "话题");

    public ConcernFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_corn.
     */
    // TODO: Rename and change types and number of parameters
    public static ConcernFragment newInstance(String param1, String param2) {
        ConcernFragment fragment = new ConcernFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String param1 = getArguments().getString("param1");
            String param2 = getArguments().getString("param2");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_concern, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupViewPager();
        checkEmptyState();
    }

    private void initViews(View view) {
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);
        emptyState = view.findViewById(R.id.empty_state);
        btnExplore = view.findViewById(R.id.btn_explore);

        btnExplore.setOnClickListener(v -> {
            // 跳转到发现页面
            Toast.makeText(getContext(), "跳转到发现页面", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupViewPager() {
        // 设置ViewPager适配器
        ConcernPagerAdapter adapter = new ConcernPagerAdapter(this, categories);
        viewPager.setAdapter(adapter);

        // 将TabLayout与ViewPager关联
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> 
            tab.setText(categories.get(position))).attach();
    }

    private void checkEmptyState() {
        // 检查是否有关注内容，这里模拟没有关注内容的情况
        boolean hasContent = false;
        emptyState.setVisibility(hasContent ? View.GONE : View.VISIBLE);
        viewPager.setVisibility(hasContent ? View.VISIBLE : View.GONE);
    }

    // ViewPager适配器
    private static class ConcernPagerAdapter extends FragmentStateAdapter {
        private final List<String> categories;

        public ConcernPagerAdapter(@NonNull Fragment fragment, List<String> categories) {
            super(fragment);
            this.categories = categories;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // 根据位置返回对应的Fragment
            return ConcernListFragment.newInstance(categories.get(position));
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }
    }
}