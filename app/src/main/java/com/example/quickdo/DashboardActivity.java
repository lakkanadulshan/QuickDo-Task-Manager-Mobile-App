package com.example.quickdo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private TextView tvTotal, tvDone, tvPending, tvProgressPercent;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        tvTotal = findViewById(R.id.tvTotalTasks);
        tvDone = findViewById(R.id.tvDoneTasks);
        tvPending = findViewById(R.id.tvPendingTasks);
        tvProgressPercent = findViewById(R.id.tvProgressPercent);
        progressBar = findViewById(R.id.progressBar);

        fetchStats();
        setupNavigation();
    }

    private void fetchStats() {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (userId == null) return;

        db.collection("users")
                .document(userId)
                .collection("tasks")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error fetching stats", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        int total = value.size();
                        int done = 0;
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            String status = doc.getString("status");
                            if ("Done".equals(status)) {
                                done++;
                            }
                        }
                        int pending = total - done;

                        tvTotal.setText(String.valueOf(total));
                        tvDone.setText(String.valueOf(done));
                        tvPending.setText(String.valueOf(pending));

                        if (total > 0) {
                            int progress = (done * 100) / total;
                            progressBar.setProgress(progress);
                            tvProgressPercent.setText(progress + "%");
                        } else {
                            progressBar.setProgress(0);
                            tvProgressPercent.setText("0%");
                        }
                    }
                });
    }

    private void setupNavigation() {
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
}
