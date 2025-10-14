package com.app.aifitness.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.aifitness.Firebase.DataCallBack;
import com.app.aifitness.Firebase.FirebaseHelper;
import com.app.aifitness.Model.Exercise;
import com.app.aifitness.R;
import com.google.android.material.button.MaterialButton;

import java.io.Serializable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExerciseDetail extends AppCompatActivity {

    private TextView tvExerciseName, tvExerciseId, tvDescription, tvCameraAngle, tvCalories, tvDuration;
    private WebView webViewYoutube;
    private MaterialButton btnStartExercise;
    private TextView btnBack ;
    private String exerciseType;
    private int exerciseValue;
    private String exerciseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        initViews();
        Serializable data = getIntent().getSerializableExtra("exerciseData");
        Map<String, Object> exerciseMap = (Map<String, Object>) data;
        exerciseId = (String) exerciseMap.get("id");
        exerciseType = (String) exerciseMap.get("type");
        Object value = exerciseMap.get("value");
        if (value instanceof Long) {

            exerciseValue = ((Long) value).intValue();
        } else if (value instanceof Integer) {
            exerciseValue = (Integer) value;
        } else {

            exerciseValue = 0;

        }

        String targetText = exerciseType.equals("time")
                ? "Time: " + exerciseValue + " sec"
                : "Reps: " + exerciseValue;
        tvDuration.setText(targetText);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ExerciseDetail.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        setupWebView();
        loadExerciseDetails();
    }

    private void initViews() {
        tvExerciseName = findViewById(R.id.tvExerciseName);
        tvExerciseId = findViewById(R.id.tvExerciseId);
        tvDescription = findViewById(R.id.tvDescription);
        tvCameraAngle = findViewById(R.id.tvCameraAngle);
        tvCalories = findViewById(R.id.tvCalories);
        tvDuration = findViewById(R.id.tvDuration);
        webViewYoutube = findViewById(R.id.webViewYoutube);
        btnStartExercise = findViewById(R.id.btnStartExercise);
        btnBack=findViewById(R.id.btnBack);
    }

    private void setupWebView() {
        WebSettings settings = webViewYoutube.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        webViewYoutube.setWebViewClient(new WebViewClient());
    }

    private void loadExerciseDetails() {
        FirebaseHelper.getInstance().getExerciseById(exerciseId, new DataCallBack<Exercise>() {
            @Override
            public void onSuccess(Exercise exercise) {
                if (exercise == null) {
                    Toast.makeText(ExerciseDetail.this, "Exercise not found", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                tvExerciseName.setText(exercise.name);
                tvExerciseId.setText("ID: " + exercise.id);
                tvDescription.setText(exercise.description);
                tvCameraAngle.setText("Camera: " + exercise.cameraSide);
                tvCalories.setText("Calories/min: " + exercise.caloriesPerMin);

                if (exercise.videoUrl != null && !exercise.videoUrl.isEmpty()) {
                    String videoId = extractYoutubeId(exercise.videoUrl);
                    if (!videoId.isEmpty()) {
                        String html = "<html><body style='margin:0;padding:0;'>" +
                                "<iframe width='100%' height='100%' " +
                                "src='https://www.youtube.com/embed/" + videoId + "?autoplay=0&modestbranding=1&rel=0' " +
                                "frameborder='0' allowfullscreen></iframe>" +
                                "</body></html>";
                        webViewYoutube.loadData(html, "text/html", "utf-8");
                    }
                }
                btnStartExercise.setOnClickListener(v -> {

                });
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(ExerciseDetail.this, "Failed to load exercise: " + errorMessage, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private String extractYoutubeId(String url) {
        if (url == null || url.isEmpty()) return "";
        String pattern = "(?<=v=|/videos/|embed/|youtu.be/)[^#&?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);
        return matcher.find() ? matcher.group() : "";
    }
}
