package com.example.quickdo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
    private List<TaskModel> filteredList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentFilter = "All";
    private TextView tabAll, tabTodo, tabInProgress, tabDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tasks);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        rvTasks = findViewById(R.id.rvTasks);
        taskList = new ArrayList<>();
        filteredList = new ArrayList<>();
        
        tabAll = findViewById(R.id.tabAll);
        tabTodo = findViewById(R.id.tabTodo);
        tabInProgress = findViewById(R.id.tabInProgress);
        tabDone = findViewById(R.id.tabDone);

        if (rvTasks != null) {
            rvTasks.setLayoutManager(new LinearLayoutManager(this));
            adapter = new TaskAdapter(filteredList, new TaskAdapter.OnTaskActionListener() {
                @Override
                public void onDeleteClick(TaskModel task) {
                    deleteTask(task);
                }

                @Override
                public void onStatusToggle(TaskModel task) {
                    toggleTaskStatus(task);
                }
            });
            rvTasks.setAdapter(adapter);
        }

        setupTabListeners();

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

        findViewById(R.id.navDashboard).setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
        });

        findViewById(R.id.navCalendar).setOnClickListener(v -> {
            startActivity(new Intent(this, CalendarActivity.class));
        });

        findViewById(R.id.navSettings).setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });

        fetchTasks();
    }

    private void setupTabListeners() {
        tabAll.setOnClickListener(v -> updateFilter("All"));
        tabTodo.setOnClickListener(v -> updateFilter("To Do"));
        tabInProgress.setOnClickListener(v -> updateFilter("In Progress"));
        tabDone.setOnClickListener(v -> updateFilter("Done"));
    }

    private void updateFilter(String filter) {
        currentFilter = filter;
        updateTabUI();
        applyFilter();
    }

    private void updateTabUI() {
        resetTab(tabAll);
        resetTab(tabTodo);
        resetTab(tabInProgress);
        resetTab(tabDone);

        TextView selectedTab;
        switch (currentFilter) {
            case "To Do": selectedTab = tabTodo; break;
            case "In Progress": selectedTab = tabInProgress; break;
            case "Done": selectedTab = tabDone; break;
            default: selectedTab = tabAll; break;
        }

        selectedTab.setBackgroundResource(R.drawable.bg_tab_selected);
        selectedTab.setTextColor(ContextCompat.getColor(this, R.color.white));
        selectedTab.setBackgroundTintList(null); // Remove tint to show the purple background
    }

    private void resetTab(TextView tab) {
        tab.setBackgroundResource(R.drawable.about_box_bg);
        tab.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
        tab.setTextColor(ContextCompat.getColor(this, R.color.black));
    }

    private void applyFilter() {
        filteredList.clear();
        for (TaskModel task : taskList) {
            if (currentFilter.equals("All") || currentFilter.equals(task.getStatus())) {
                filteredList.add(task);
            }
        }
        adapter.notifyDataSetChanged();
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
                        applyFilter();
                    }
                });
    }

    private void toggleTaskStatus(TaskModel task) {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (userId == null) return;

        String currentStatus = task.getStatus();
        String newStatus;

        if ("To Do".equals(currentStatus) || currentStatus == null) {
            newStatus = "In Progress";
        } else if ("In Progress".equals(currentStatus)) {
            newStatus = "Done";
        } else {
            newStatus = "To Do";
        }

        db.collection("users")
                .document(userId)
                .collection("tasks")
                .document(task.getTaskId())
                .update("status", newStatus)
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show();
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
