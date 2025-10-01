package com.app.aifitness.repository;

import androidx.lifecycle.MutableLiveData;

import com.app.aifitness.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthRepo {
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;

    public AuthRepo() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<String> registerUser(String email, String password) {
        MutableLiveData<String> result = new MutableLiveData<>();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            saveUserToFirestore(firebaseUser.getUid(), email)
                                    .addOnSuccessListener(aVoid -> result.setValue("success"))
                                    .addOnFailureListener(e -> result.setValue("error: " + e.getMessage()));
                        }
                    } else {
                        result.setValue("error: " + task.getException().getMessage());
                    }
                });
        return result;
    }

    public MutableLiveData<String> loginUser(String email, String password) {
        MutableLiveData<String> result = new MutableLiveData<>();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        result.setValue("success");
                    } else {
                        result.setValue("error: " + task.getException().getMessage());
                    }
                });
        return result;
    }

    public MutableLiveData<User> getUserData() {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            db.collection("users").document(firebaseUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            userLiveData.setValue(user);
                        } else {
                            userLiveData.setValue(null);
                        }
                    })
                    .addOnFailureListener(e -> userLiveData.setValue(null));
        } else {
            userLiveData.setValue(null);
        }
        return userLiveData;
    }

    public Task<Void> saveUserToFirestore(String uid, String email) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("createdAt", FieldValue.serverTimestamp());
        userData.put("isNew", true);

        return db.collection("users").document(uid).set(userData);
    }

    public Task<Void> updateUserAsNotNew(String uid) {
        return db.collection("users").document(uid).update("isNew", false);
    }
}