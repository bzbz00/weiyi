package com.example.app_02.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.app_02.R;
public class DiscussionFragment extends Fragment {
    private DrawerLayout drawerLayout;
    private ImageButton drawerButton;
    private View rootView;
    private Fragment currentFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_discussion, container, false);
            initViews();
        }
        // 避免重复创建视图
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private void initViews() {
        setHasOptionsMenu(true);
        setupDrawer();
        setupToolbar();
        
        // 初始加载 Fragment
        if (currentFragment == null) {
            loadFragment(new RecommendFragment());
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
        if (itemId == R.id.Hot) {
            return new HotFragment();
        } else if (itemId == R.id.Recommend) {
            return new RecommendFragment();
        } else if (itemId == R.id.Concern) {
            return new ConrnFragment();
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.ttop_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}