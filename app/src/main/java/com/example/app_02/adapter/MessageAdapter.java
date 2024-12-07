package com.example.app_02.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.app_02.R;
import com.example.app_02.method.Message;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    private List<Message> messages;

    public MessageAdapter(Context context, List<Message> messages) {
        super(context, 0, messages);
        this.messages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_message, parent, false);
        }

        TextView contentView = convertView.findViewById(R.id.message_content);
        TextView timeView = convertView.findViewById(R.id.message_time);
        TextView postTitleView = convertView.findViewById(R.id.post_title);

        contentView.setText(message.getContent());
        timeView.setText(message.getTime());
        postTitleView.setText(message.getPostTitle());

        return convertView;
    }

    public void addMessage(Message message) {
        messages.add(0, message);
    }
}
