package com.example.app_02.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_02.R;
import com.example.app_02.model.Feature;

import java.util.List;

public class FeaturesAdapter extends RecyclerView.Adapter<FeaturesAdapter.FeatureViewHolder> {
    
    private final List<Feature> features;
    private OnFeatureClickListener listener;

    public FeaturesAdapter(List<Feature> features) {
        this.features = features;
    }

    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feature, parent, false);
        return new FeatureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        Feature feature = features.get(position);
        holder.bind(feature);
    }

    @Override
    public int getItemCount() {
        return features.size();
    }

    public void setOnFeatureClickListener(OnFeatureClickListener listener) {
        this.listener = listener;
    }

    public interface OnFeatureClickListener {
        void onFeatureClick(Feature feature, int position);
    }

    class FeatureViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iconView;
        private final TextView nameView;

        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            iconView = itemView.findViewById(R.id.feature_icon);
            nameView = itemView.findViewById(R.id.feature_name);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onFeatureClick(features.get(position), position);
                }
            });
        }

        public void bind(Feature feature) {
            iconView.setImageResource(feature.getIconResId());
            nameView.setText(feature.getName());
        }
    }
} 