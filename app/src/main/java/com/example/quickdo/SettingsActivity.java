package com.example.quickdo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupNavigation();
    }

    private void setupNavigation() {
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
        findViewById(R.id.navProfile).setOnClickListener(v -> {
            startActivity(new Intent(this, UserInfoActivity.class));
            finish();
        });
    }
}
