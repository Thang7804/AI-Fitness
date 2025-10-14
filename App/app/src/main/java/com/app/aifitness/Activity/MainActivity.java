package com.app.aifitness.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.aifitness.Firebase.DataCallBack;
import com.app.aifitness.Firebase.FirebaseHelper;
import com.app.aifitness.Model.User;
import com.app.aifitness.R;

import java.util.List;
import com.app.aifitness.Activity.Adapter.ScheduleAdapter;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvScheduleDays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvScheduleDays = findViewById(R.id.rvScheduleDays);
        rvScheduleDays.setLayoutManager(new LinearLayoutManager(this));

        loadScheduleDays();
    }

    private void loadScheduleDays() {
        FirebaseHelper.getInstance().getCurrentUser(FirebaseHelper.getInstance().getCurrentUserId(), new DataCallBack<User>() {
            @Override
            public void onSuccess(User user) {
                if (user == null || user.schedule == null || user.schedule.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No schedule days found", Toast.LENGTH_SHORT).show();
                    return;
                }

                ScheduleAdapter adapter = new ScheduleAdapter(MainActivity.this, user.schedule);
                rvScheduleDays.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}