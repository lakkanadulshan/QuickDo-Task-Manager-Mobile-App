package com.example.quickdo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    private RecyclerView rvTasks;
    private TaskAdapter adapter;
    private List<TaskModel> taskList;
    private List<TaskModel> filteredList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private TextView tvSelectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        CalendarView calendarView = findViewById(R.id.calendarView);
        rvTasks = findViewById(R.id.rvTasks);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);

        taskList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new TaskAdapter(filteredList, new TaskAdapter.OnTaskActionListener() {
            @Override
            public void onDeleteClick(TaskModel task) {
                deleteTask(task);
            }

            @Override
            public void onStatusToggle(TaskModel task) {
                toggleTaskStatus(task);
            }

            @Override
            public void onEditClick(TaskModel task) {
                editTask(task);
            }
        });
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        rvTasks.setAdapter(adapter);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth, 0, 0, 0);
            selectedDate.set(Calendar.MILLISECOND, 0);
            
            tvSelectedDate.setText("Tasks for " + dayOfMonth + "/" + (month + 1) + "/" + year);
            filterTasks(selectedDate.getTimeInMillis());
        });

        fetchTasks();
        setupNavigation();
    }

    private void fetchTasks() {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (userId == null) return;

        db.collection("users")
                .document(userId)
                .collection("tasks")
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
                        // Initially show today's tasks
                        Calendar today = Calendar.getInstance();
                        today.set(Calendar.HOUR_OF_DAY, 0);
                        today.set(Calendar.MINUTE, 0);
                        today.set(Calendar.SECOND, 0);
                        today.set(Calendar.MILLISECOND, 0);
                        filterTasks(today.getTimeInMillis());
                    }
                });
    }

    private void filterTasks(long selectedTimestamp) {
        filteredList.clear();
        for (TaskModel task : taskList) {
            if (isSameDay(task.getTimestamp(), selectedTimestamp)) {
                filteredList.add(task);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private boolean isSameDay(long t1, long t2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(t1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(t2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
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
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show());
    }

    private void editTask(TaskModel task) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtra("taskId", task.getTaskId());
        intent.putExtra("title", task.getTitle());
        intent.putExtra("description", task.getDescription());
        intent.putExtra("timestamp", task.getTimestamp());
        startActivity(intent);
    }

    private void deleteTask(TaskModel task) {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (userId == null) return;

        db.collection("users")
                .document(userId)
                .collection("tasks")
                .document(task.getTaskId())
                .delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Task Deleted", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete task", Toast.LENGTH_SHORT).show());
    }

    private void setupNavigation() {
        findViewById(R.id.navDashboard).setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });
        findViewById(R.id.navTasks).setOnClickListener(v -> {
            startActivity(new Intent(this, MyTasksActivity.class));
            finish();
        });
        findViewById(R.id.navSettings).setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
        });
        findViewById(R.id.navProfile).setOnClickListener(v -> {
            startActivity(new Intent(this, UserInfoActivity.class));
            finish();
        });
    }
}
