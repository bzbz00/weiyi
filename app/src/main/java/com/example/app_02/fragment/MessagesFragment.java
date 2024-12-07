package com.example.app_02.fragment;

import android.content.Intent;
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
import com.example.app_02.activity.MessageActivity;
import com.example.app_02.event.CommentPostedEvent;
import com.example.app_02.method.Message;
import com.example.app_02.model.Comment;
import com.example.app_02.model.Story;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MessagesFragment extends Fragment {

    private ListView messageList;
    private View rootView;
    private List<Message> messages = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_messages, container, false);
            initViews();
        }
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
        messages = createMessages();
        MessageAdapter adapter = new MessageAdapter(getContext(), messages);
        messageList.setAdapter(adapter);
        messageList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        messageList.setOnItemClickListener((parent, view1, position, id) -> {
            Message message = messages.get(position);
            CommentDetailFragment detailFragment = CommentDetailFragment.newInstance(message);
            getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .commit();
        });
    }

    private List<Message> createMessages() {
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            messages.add(new Message(
                "消息 " + (i + 1),
                "2023-06-08 12:00",
                0,
                "帖子标题 " + (i + 1),
                i + 1
            ));
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommentPosted(CommentPostedEvent event) {
        Comment comment = event.getComment();
        Story story = comment.getStory();
        
        Message message = new Message(
            String.format("%s 评论了你的帖子: %s", comment.getAuthorName(), comment.getContent()),
            comment.getTime(),
            0,
            story.getTitle(),
            story.getId()
        );
        
        addNewMessage(message);
    }

    private void addNewMessage(Message message) {
        if (messageList != null && messageList.getAdapter() != null) {
            MessageAdapter adapter = (MessageAdapter) messageList.getAdapter();
            adapter.addMessage(message);
            adapter.notifyDataSetChanged();
        }
    }
}
