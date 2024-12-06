package com.example.app_02.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.app_02.R;
import com.example.app_02.adapter.MessageAdapter;
import com.example.app_02.method.Message;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {

    private ListView messageList;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_messages, container, false);
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
        messageList = rootView.findViewById(R.id.message_list);
        setupMessageList();
    }

    private void setupMessageList() {
        List<Message> messages = createMessages();
        MessageAdapter adapter = new MessageAdapter(getContext(), messages);
        messageList.setAdapter(adapter);
        messageList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        messageList.setOnItemClickListener((parent, view1, position, id) -> {
            Fragment newFragment = getFragmentForPosition(position);
            if (newFragment != null) {
                switchFragment(newFragment);
            }
        });
    }

    private List<Message> createMessages() {
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            messages.add(new Message("消息 " + (i + 1), R.drawable.message_blue));
        }
        return messages;
    }

    private Fragment getFragmentForPosition(int position) {
        switch (position) {
            case 0:
                return new OneFragment();
            case 1:
                return new TwoFragment();
            case 2:
                return new ThreeFragment();
            default:
                return null;
        }
    }

    private void switchFragment(Fragment fragment) {
        if (getActivity() != null) {
            FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.fragment_container, fragment);
            
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
