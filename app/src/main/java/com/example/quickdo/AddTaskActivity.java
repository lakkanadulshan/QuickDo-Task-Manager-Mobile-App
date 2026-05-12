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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddTaskActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private EditText etTaskTitle, etDescription, etDay, etMonth, etYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        etTaskTitle = findViewById(R.id.etTaskTitle);
        etDescription = findViewById(R.id.etDescription);
        etDay = findViewById(R.id.etDay);
        etMonth = findViewById(R.id.etMonth);
        etYear = findViewById(R.id.etYear);
        MaterialButton btnCreate = findViewById(R.id.btnCreate);

        // Pre-fill current date
        Calendar c = Calendar.getInstance();
        etDay.setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
        etMonth.setText(String.valueOf(c.get(Calendar.MONTH) + 1));
        etYear.setText(String.valueOf(c.get(Calendar.YEAR)));

        btnCreate.setOnClickListener(v -> {
            String title = etTaskTitle.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String day = etDay.getText().toString().trim();
            String month = etMonth.getText().toString().trim();
            String year = etYear.getText().toString().trim();

            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a task title", Toast.LENGTH_SHORT).show();
                return;
            }
            if (day.isEmpty() || month.isEmpty() || year.isEmpty()) {
                Toast.makeText(this, "Please enter a valid date", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Calendar taskDate = Calendar.getInstance();
                taskDate.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day), 0, 0, 0);
                taskDate.set(Calendar.MILLISECOND, 0);
                saveTaskToFirestore(title, description, taskDate.getTimeInMillis());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigation Buttons
        findViewById(R.id.navDashboard).setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });

        findViewById(R.id.navCalendar).setOnClickListener(v -> {
            startActivity(new Intent(this, CalendarActivity.class));
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

    private void saveTaskToFirestore(String title, String description, long timestamp) {
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
        task.put("timestamp", timestamp);

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
