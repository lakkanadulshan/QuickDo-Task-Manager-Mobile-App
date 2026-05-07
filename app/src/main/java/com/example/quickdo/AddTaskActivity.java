package com.example.quickdo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AddTaskActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        EditText etTaskTitle = findViewById(R.id.etTaskTitle);
        EditText etDescription = findViewById(R.id.etDescription);
        MaterialButton btnCreate = findViewById(R.id.btnCreate);

        // Navigation Buttons
        ImageButton navDashboard = findViewById(R.id.navDashboard);
        ImageButton navTasks = findViewById(R.id.navTasks);
        ImageButton navProfile = findViewById(R.id.navProfile);

        btnCreate.setOnClickListener(v -> {
            String title = etTaskTitle.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            
            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a task title", Toast.LENGTH_SHORT).show();
            } else {
                saveTaskToFirestore(title, description);
            }
        });

        if (navDashboard != null) {
            navDashboard.setOnClickListener(v -> {
                startActivity(new Intent(AddTaskActivity.this, MyTasksActivity.class));
                finish();
            });
        }

        if (navTasks != null) {
            navTasks.setOnClickListener(v -> {
                startActivity(new Intent(AddTaskActivity.this, MyTasksActivity.class));
                finish();
            });
        }

        if (navProfile != null) {
            navProfile.setOnClickListener(v -> {
                startActivity(new Intent(AddTaskActivity.this, UserInfoActivity.class));
                finish();
            });
        }
    }

    private void saveTaskToFirestore(String title, String description) {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String taskId = db.collection("users").document(userId).collection("tasks").document().getId();

        Map<String, Object> task = new HashMap<>();
        task.put("taskId", taskId);
        task.put("title", title);
        task.put("description", description);
        task.put("status", "To Do"); // Default status
        task.put("timestamp", System.currentTimeMillis());

        db.collection("users")
                .document(userId)
                .collection("tasks")
                .document(taskId)
                .set(task)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Task Created Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
