package com.example.app_02.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app_02.R;
import com.example.app_02.method.Message;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    public MessageAdapter(Context context, List<Message> messages) {
        super(context, 0, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 获取当前消息对象
        Message message = getItem(position);

        // 检查是否需要重新使用已存在的视图
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_message, parent, false);
        }

        // 获取视图中的 TextView 和 ImageView
        TextView titleTextView = convertView.findViewById(R.id.message_title);
        ImageView iconImageView = convertView.findViewById(R.id.message_icon); // 新增图标的引用

        // 设置文本内容
        titleTextView.setText(message.getTitle());

        // 设置图标（假设 Message 类中有一个方法 getIconResId() 返回图标的资源ID）
        iconImageView.setImageResource(message.getIconResId()); // 设置图标

        return convertView;
    }
}
