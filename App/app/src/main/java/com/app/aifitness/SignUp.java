package com.app.aifitness;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private Button btnSignUp;
    private EditText edtEmail, edtPass, edtRPass;
    private TextView txSignIn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sign_up);
        btnSignUp = findViewById(R.id.btnSignIn);
        edtEmail = findViewById(R.id.Email);
        edtPass= findViewById(R.id.Password);
        edtRPass = findViewById(R.id.RPassword);
        txSignIn =findViewById(R.id.txSignin);
        mAuth = FirebaseAuth.getInstance();
        txSignIn.setOnClickListener(v -> startActivity(new Intent(SignUp.this, Login.class)));
        btnSignUp.setOnClickListener(v -> Register());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void saveUserToFirestore(String uid, String email, Runnable onSuccess) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("createdAt", FieldValue.serverTimestamp());
        userData.put("isNew", true);

        db.collection("users")
                .document(uid)
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SignUp.this, "User info saved", Toast.LENGTH_SHORT).show();
                    if (onSuccess != null) onSuccess.run(); // gọi callback
                })
                .addOnFailureListener(e ->
                        Toast.makeText(SignUp.this, "Error saving user info", Toast.LENGTH_SHORT).show()
                );
    }
    private void Register(){
        String email = edtEmail.getText().toString().trim();
        String password = edtPass.getText().toString().trim();
        String rpassword = edtRPass.getText().toString().trim();
        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!rpassword.equals(password)){
            Toast.makeText(this, "Not match Repeat Pass and Pass", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if(firebaseUser != null) {
                            saveUserToFirestore(firebaseUser.getUid(), email, () -> {
                                Toast.makeText(SignUp.this, "Sign Up successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUp.this, Login.class));
                                finish();
                            });
                        }

                    } else {
                        Toast.makeText(SignUp.this, "Error: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

}