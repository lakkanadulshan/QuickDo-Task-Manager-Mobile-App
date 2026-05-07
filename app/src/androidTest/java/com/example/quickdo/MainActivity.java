package com.example.quickdo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String userId;

    Button btnLogout, btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // ⚠SAFE CHECK (important)
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        userId = mAuth.getCurrentUser().getUid();

        // UI buttons
        btnLogout = findViewById(R.id.btnLogout);
        btnAdd = findViewById(R.id.btnAdd);

        // ADD TASK (TEST)
        btnAdd.setOnClickListener(v -> addTask("My First Task"));

        // LOGOUT
        btnLogout.setOnClickListener(v -> logoutUser());
    }

    // ADD TASK
    private void addTask(String title) {

        String taskId = db.collection("users")
                .document(userId)
                .collection("tasks")
                .document()
                .getId();

        Map<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("taskId", taskId);

        db.collection("users")
                .document(userId)
                .collection("tasks")
                .document(taskId)
                .set(data)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Task Added ✔", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // LOGOUT
    private void logoutUser() {

        FirebaseAuth.getInstance().signOut();

        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}
