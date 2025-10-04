package com.app.aifitness.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.aifitness.Firebase.AuthCallback;
import com.app.aifitness.Firebase.FirebaseHelper;
import com.app.aifitness.R;

public class SignInActivity extends AppCompatActivity {
    private EditText edtemail, edtpassword;
    private Button btnSignIn, btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        edtemail = findViewById(R.id.Email);
        edtpassword= findViewById(R.id.Password);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp= findViewById(R.id.btnSignUp);
        btnSignIn.setOnClickListener(v->{
            String email= edtemail.getText().toString().trim();
            String password= edtpassword.getText().toString().trim();
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this,"Please enter email and password",Toast.LENGTH_SHORT ).show();
            }
            FirebaseHelper.getInstance().loginUser(email, password, new AuthCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(SignInActivity.this, "SignIn Successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(SignInActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        });
        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}