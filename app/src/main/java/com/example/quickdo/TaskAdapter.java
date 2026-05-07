package com.example.quickdo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<TaskModel> tasks;
    private OnTaskDeleteListener deleteListener;

    public interface OnTaskDeleteListener {
        void onDeleteClick(TaskModel task);
    }

    public TaskAdapter(List<TaskModel> tasks, OnTaskDeleteListener deleteListener) {
        this.tasks = tasks;
        this.deleteListener = deleteListener;
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
        
        // Format timestamp to time string
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm\na", Locale.getDefault());
        String timeStr = sdf.format(new Date(task.getTimestamp()));
        holder.tvTaskTime.setText(timeStr);
        
        if ("Done".equals(task.getStatus())) {
            holder.ivCheck.setImageResource(R.drawable.ic_radio_on);
        } else {
            holder.ivCheck.setImageResource(R.drawable.ic_radio_off);
        }

        holder.ivDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClick(task);
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
