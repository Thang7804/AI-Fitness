package com.app.aifitness.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.app.aifitness.R;
import com.app.aifitness.viewmodel.SignUpVM;

public class SignUp extends AppCompatActivity {
    private EditText edtEmail, edtPass, edtRPass;
    private Button btnSignUp;
    private TextView txSignIn;
    private SignUpVM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        initViews();
        viewModel = new ViewModelProvider(this).get(SignUpVM.class);

        txSignIn.setOnClickListener(v -> startActivity(new Intent(SignUp.this, Login.class)));

        btnSignUp.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPass.getText().toString().trim();
            String rpassword = edtRPass.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!rpassword.equals(password)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.registerUser(email, password);
        });

        viewModel.getResult().observe(this, result -> {
            if ("success".equals(result)) {
                Toast.makeText(SignUp.this, "Sign Up successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUp.this, Login.class));
                finish();
            } else {
                Toast.makeText(SignUp.this, result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        edtEmail = findViewById(R.id.Email);
        edtPass = findViewById(R.id.Password);
        edtRPass = findViewById(R.id.RPassword);
        btnSignUp = findViewById(R.id.btnSignIn);
        txSignIn = findViewById(R.id.txSignin);
    }
}