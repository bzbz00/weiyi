package com.example.app_02.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app_02.R;
import com.example.app_02.model.ToolService;
import java.util.List;

public class ToolServiceAdapter extends RecyclerView.Adapter<ToolServiceAdapter.ViewHolder> {
    private List<ToolService> tools;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ToolService tool, int position);
    }

    public ToolServiceAdapter(List<ToolService> tools) {
        this.tools = tools;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tool_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToolService tool = tools.get(position);
        holder.icon.setImageResource(tool.getIconResId());
        holder.name.setText(tool.getName());
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(tool, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tools.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;

        ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.tool_icon);
            name = itemView.findViewById(R.id.tool_name);
        }
    }
} 