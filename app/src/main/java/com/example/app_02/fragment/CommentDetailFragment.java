package com.example.app_02.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.app_02.R;
import com.example.app_02.method.Message;

public class CommentDetailFragment extends Fragment {
    private static final String ARG_MESSAGE = "message";
    
    public static CommentDetailFragment newInstance(Message message) {
        CommentDetailFragment fragment = new CommentDetailFragment();
        Bundle args = new Bundle();
        args.putString("content", message.getContent());
        args.putString("time", message.getTime());
        args.putString("postTitle", message.getPostTitle());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_detail, container, false);
        
        if (getArguments() != null) {
            TextView contentView = view.findViewById(R.id.comment_content);
            TextView timeView = view.findViewById(R.id.comment_time);
            TextView postTitleView = view.findViewById(R.id.post_title);
            
            contentView.setText(getArguments().getString("content"));
            timeView.setText(getArguments().getString("time"));
            postTitleView.setText(getArguments().getString("postTitle"));
        }
        
        return view;
    }
} 