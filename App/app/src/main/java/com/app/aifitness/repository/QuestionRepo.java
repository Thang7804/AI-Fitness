package com.app.aifitness.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;


import java.util.HashMap;
import java.util.Map;

public class QuestionRepo {
    private final FirebaseFirestore db;

    public QuestionRepo() {
        db = FirebaseFirestore.getInstance();
    }

    public Task<Void> saveAnswer(String key, String value) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            Map<String, Object> answer = new HashMap<>();
            answer.put(key, value);
            return db.collection("users").document(uid).set(answer, SetOptions.merge());
        }
        return null;
    }
}