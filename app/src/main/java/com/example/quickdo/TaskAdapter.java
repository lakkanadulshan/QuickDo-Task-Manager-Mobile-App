package com.example.quickdo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<TaskModel> tasks;
    private OnTaskActionListener actionListener;

    public interface OnTaskActionListener {
        void onDeleteClick(TaskModel task);
        void onStatusToggle(TaskModel task);
    }

    public TaskAdapter(List<TaskModel> tasks, OnTaskActionListener actionListener) {
        this.tasks = tasks;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskModel task = tasks.get(position);
        holder.tvTaskTitle.setText(task.getTitle());
        
        holder.tvTaskTime.setVisibility(View.GONE);
        
        if ("Done".equals(task.getStatus())) {
            holder.ivCheck.setImageResource(R.drawable.ic_radio_on);
            holder.ivCheck.setColorFilter(android.graphics.Color.parseColor("#4CAF50"));
        } else if ("In Progress".equals(task.getStatus())) {
            holder.ivCheck.setImageResource(R.drawable.ic_progress);
            holder.ivCheck.setColorFilter(android.graphics.Color.parseColor("#FF9800"));
        } else {
            holder.ivCheck.setImageResource(R.drawable.ic_radio_off);
            holder.ivCheck.setColorFilter(android.graphics.Color.parseColor("#000000"));
        }

        holder.ivCheck.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onStatusToggle(task);
            }
        });

        holder.ivDelete.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDeleteClick(task);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaskTitle, tvTaskTime;
        ImageView ivCheck, ivDelete;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvTaskTime = itemView.findViewById(R.id.tvTaskTime);
            ivCheck = itemView.findViewById(R.id.ivCheck);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}
