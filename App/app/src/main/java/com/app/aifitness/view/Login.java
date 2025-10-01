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
import com.app.aifitness.viewmodel.LoginVM;

public class Login extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView txSignUp;
    private LoginVM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initViews();
        viewModel = new ViewModelProvider(this).get(LoginVM.class);

        txSignUp.setOnClickListener(v -> startActivity(new Intent(Login.this, SignUp.class)));

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.loginUser(email, password);
        });

        viewModel.getLoginResult().observe(this, result -> {
            if ("success".equals(result)) {
                viewModel.fetchUserData();
            } else {
                Toast.makeText(Login.this, result, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getUserData().observe(this, user -> {
            if (user != null) {
                if (user.isNew()) {
                    startActivity(new Intent(Login.this, Question.class));
                } else {
                    finish();
                }
                finish();
            } else {
                Toast.makeText(Login.this, "User data not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        edtEmail = findViewById(R.id.Email);
        edtPassword = findViewById(R.id.Password);
        btnLogin = findViewById(R.id.btnSignIn);
        txSignUp = findViewById(R.id.txSignin);
    }
}

