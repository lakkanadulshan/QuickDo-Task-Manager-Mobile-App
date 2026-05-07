package com.example.quickdo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class MyTasksActivity extends AppCompatActivity {

    private RecyclerView rvTasks;
    private TaskAdapter adapter;
    private List<TaskModel> taskList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tasks);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        rvTasks = findViewById(R.id.rvTasks);
        taskList = new ArrayList<>();
        
        if (rvTasks != null) {
            rvTasks.setLayoutManager(new LinearLayoutManager(this));
            adapter = new TaskAdapter(taskList, task -> deleteTask(task));
            rvTasks.setAdapter(adapter);
        }

        View btnAdd = findViewById(R.id.btnAdd);
        if (btnAdd != null) {
            btnAdd.setOnClickListener(v -> {
                startActivity(new Intent(MyTasksActivity.this, AddTaskActivity.class));
            });
        }

        View navProfile = findViewById(R.id.navProfile);
        if (navProfile != null) {
            navProfile.setOnClickListener(v -> {
                startActivity(new Intent(MyTasksActivity.this, UserInfoActivity.class));
            });
        }

        fetchTasks();
    }

    private void fetchTasks() {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (userId == null) return;

        db.collection("users")
                .document(userId)
                .collection("tasks")
                .orderBy("timestamp")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error fetching tasks", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        taskList.clear();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            TaskModel task = doc.toObject(TaskModel.class);
                            if (task != null) {
                                taskList.add(task);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void deleteTask(TaskModel task) {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (userId == null) return;

        db.collection("users")
                .document(userId)
                .collection("tasks")
                .document(task.getTaskId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Task Deleted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to delete task", Toast.LENGTH_SHORT).show();
                });
    }
}
