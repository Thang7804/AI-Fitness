package com.app.aifitness.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.app.aifitness.R;
import com.app.aifitness.viewmodel.QuestionVM;

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
    private QuestionVM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        initViews();
        viewModel = new ViewModelProvider(this).get(QuestionVM.class);

        Button[] optionButtons = {op1, op2, op3, op4};
        for (Button b : optionButtons) {
            b.setOnClickListener(v -> {
                selectedAnswer = b.getText().toString();
                for (Button btn : optionButtons) {
                    btn.setBackgroundColor(ContextCompat.getColor(this, R.color.buton));
                }
                b.setBackgroundColor(Color.CYAN);
            });
        }

        showQuestion(currentQuestion);

        btnSubmit.setOnClickListener(v -> {
            if (selectedAnswer == null) {
                Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.saveAnswer(questionKeys[currentQuestion], selectedAnswer);

            currentQuestion++;
            if (currentQuestion < questions.length) {
                showQuestion(currentQuestion);
                selectedAnswer = null;
                for (Button b : optionButtons) {
                    b.setBackgroundColor(ContextCompat.getColor(this, R.color.buton));
                }
            } else {
                viewModel.markUserAsNotNew();
            }
        });

        viewModel.getAnswerResult().observe(this, result -> {
            if ("error".equals(result)) {
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getUpdateStatus().observe(this, status -> {
            if ("updated".equals(status)) {

                finish();
            } else if (status != null && status.startsWith("error")) {
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
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

    private void initViews() {
        tvQuestion = findViewById(R.id.tvQuestion);
        op1 = findViewById(R.id.op1);
        op2 = findViewById(R.id.op2);
        op3 = findViewById(R.id.op3);
        op4 = findViewById(R.id.op4);
        btnSubmit = findViewById(R.id.btnSubmit);
    }
}