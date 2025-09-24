package com.app.aifitness;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView txSignUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        edtEmail = findViewById(R.id.Email);
        edtPassword = findViewById(R.id.Password);
        btnLogin = findViewById(R.id.btnSignUp);
        txSignUp = findViewById(R.id.txSignin);
        mAuth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(v -> loginUser());
        txSignUp.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, SignUp.class));
        });
    }

    private void loginUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser == null) {
                            Toast.makeText(Login.this, "User is null", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String uid = firebaseUser.getUid();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        db.collection("users").document(uid)
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        Boolean isNew = documentSnapshot.getBoolean("isNew");
                                        if (isNew != null && isNew) {
                                            startActivity(new Intent(Login.this, Question.class));
                                        } else {
                                            startActivity(new Intent(Login.this, MainActivity.class));
                                        }
                                    } else {
                                        Toast.makeText(Login.this, "No user data, redirecting...", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Login.this, Question.class));
                                    }
                                    finish();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(Login.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                );

                    } else {
                        Toast.makeText(Login.this, "Fail: " +
                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


}
