package com.app.aifitness;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Question extends AppCompatActivity {
    private TextView tvQuestion;
    private Button op1, op2, op3, op4, btnSubmit;
    private String[] questionKeys = {
            "exerciseFrequency",
            "exerciseType",
            "exerciseTime"
    };
    private String[] questions = {
            "Bạn có thường xuyên tập thể dục không?",
            "Bạn thích tập cardio hay strength?",
            "Bạn thường tập vào buổi nào?"
    };

    private String[][] options = {
            {"Không bao giờ", "Thỉnh thoảng", "Thường xuyên", "Hàng ngày"},
            {"Cardio", "Strength", "Cả hai", "Không thích"},
            {"Sáng", "Trưa", "Chiều", "Tối"}
    };
    private int currentQuestion = 0;
    private String selectedAnswer = null;
    int defaultColor = ContextCompat.getColor(this, R.color.buton);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        tvQuestion = findViewById(R.id.tvQuestion);
        op1 = findViewById(R.id.op1);
        op2 = findViewById(R.id.op2);
        op3 = findViewById(R.id.op3);
        op4 = findViewById(R.id.op4);
        btnSubmit = findViewById(R.id.btnSubmit);
        Button[] optionButtons = {op1, op2, op3, op4};
        for (Button b : optionButtons) {
            b.setOnClickListener(v -> {
                selectedAnswer = b.getText().toString();
                for (Button btn : optionButtons) {
                    btn.setBackgroundColor(Color.LTGRAY);
                }
                b.setBackgroundColor(Color.CYAN);
            });
        }
        showQuestion(currentQuestion);
        btnSubmit.setOnClickListener(v -> {
            if (selectedAnswer == null) return;

            saveAnswerToFirestore(currentQuestion, selectedAnswer);

            currentQuestion++;
            if (currentQuestion < questions.length) {
                showQuestion(currentQuestion);
                selectedAnswer = null;
                for (Button b : optionButtons) {
                    b.setBackgroundColor(defaultColor);
                }
            } else {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("users").document(firebaseUser.getUid())
                            .update("isNew", false)
                            .addOnSuccessListener(aVoid -> {
                                startActivity(new Intent(Question.this, MainActivity.class));
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                startActivity(new Intent(Question.this, MainActivity.class));
                                finish();
                            });
                } else {
                    startActivity(new Intent(Question.this, MainActivity.class));
                    finish();
                }
            }
        });

    }

    private void showQuestion(int index) {
        tvQuestion.setText(questions[index]);
        op1.setText(options[index][0]);
        op2.setText(options[index][1]);
        op3.setText(options[index][2]);
        op4.setText(options[index][3]);
    }
    private void saveAnswerToFirestore(int questionIndex, String answer) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> answerMap = new HashMap<>();
            answerMap.put(questionKeys[questionIndex], answer);

            db.collection("users").document(firebaseUser.getUid())
                    .set(answerMap, SetOptions.merge());
        }
    }

}