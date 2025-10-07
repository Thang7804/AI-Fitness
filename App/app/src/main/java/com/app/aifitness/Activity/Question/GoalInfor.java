package com.app.aifitness.Activity.Question;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.aifitness.Model.User;
import com.app.aifitness.R;

import kotlinx.coroutines.scheduling.GlobalQueue;

public class GoalInfor extends AppCompatActivity {
    private RadioGroup rgGoal;
    private EditText edtGoalWeight;
    private Button btnSubmit;
    private User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goalinfo);
        rgGoal = findViewById(R.id.rgFitnessGoal);
        edtGoalWeight= findViewById(R.id.edtTargetWeight);
        btnSubmit = findViewById(R.id.btnNextGoal);
        currentUser = (User) getIntent().getSerializableExtra("user") ;
        btnSubmit.setOnClickListener(v->{
            int selectedId = rgGoal.getCheckedRadioButtonId();
            if (selectedId == R.id.rbLoseWeight) {
                currentUser.goal = "Lose Weight";
            } else if (selectedId == R.id.rbGainMuscle) {
                currentUser.goal = "Gain Muscle";
            }  else if (selectedId == R.id.rbMaintainFitness){
                currentUser.focusArea="Maintain Fitness";
            } else if (selectedId == R.id.rbIncreaseStamina){
                currentUser.focusArea="Increase Stamina";
            }
            else {
                Toast.makeText(this, "Please select!", Toast.LENGTH_SHORT).show();
                return;
            }
            String goalWeightstr = edtGoalWeight.getText().toString().trim().replace(',', '.');;
            try {
                float goalWeight = Float.parseFloat(goalWeightstr);

                if ( goalWeight<= 0) {
                    Toast.makeText(this, "Height and weight must be greater than 0!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(selectedId== R.id.rbLoseWeight && goalWeight>= currentUser.weight){
                    Toast.makeText(this, "Height and weight must be lower than your weight!", Toast.LENGTH_SHORT).show();
                }

                currentUser.goalweight = goalWeight;

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid number!", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(GoalInfor.this, SheduleInfo.class);
            intent.putExtra("user", currentUser);
            startActivity(intent);

        });
    }
}