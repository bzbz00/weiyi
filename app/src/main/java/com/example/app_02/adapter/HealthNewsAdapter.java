package com.example.app_02.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_02.R;
import com.example.app_02.model.HealthNews;

import java.util.List;

public class HealthNewsAdapter extends RecyclerView.Adapter<HealthNewsAdapter.ViewHolder> {
    
    private final List<HealthNews> newsList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(HealthNews news, int position);
    }

    public HealthNewsAdapter(List<HealthNews> newsList) {
        this.newsList = newsList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_health_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HealthNews news = newsList.get(position);
        holder.bind(news);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView newsImage;
        private final TextView newsTitle;
        private final TextView newsSummary;
        private final TextView newsTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.news_image);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsSummary = itemView.findViewById(R.id.news_summary);
            newsTime = itemView.findViewById(R.id.news_time);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(newsList.get(position), position);
                }
            });
        }

        public void bind(HealthNews news) {
            newsImage.setImageResource(news.getImageResId());
            newsTitle.setText(news.getTitle());
            newsSummary.setText(news.getSummary());
            newsTime.setText(news.getTime());
        }
    }
} 