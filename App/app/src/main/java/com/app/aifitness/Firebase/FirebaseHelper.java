package com.app.aifitness.Firebase;

import com.app.aifitness.Model.Exercise;
import com.app.aifitness.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseHelper {

    private static FirebaseHelper instance;
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;

    private FirebaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public static FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }

    public void registerUser(String email, String password, Callback authCallback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        User newUser = new User(email);
                        db.collection("Users").document(uid).set(newUser)
                                .addOnSuccessListener(aVoid -> authCallback.onSuccess())
                                .addOnFailureListener(e -> authCallback.onError("Save user error: " + e.getMessage()));
                    } else {
                        authCallback.onError("Register error: " + task.getException().getMessage());
                    }
                });
    }

    public void loginUser(String email, String password, Callback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError("Sign in error: " + task.getException().getMessage());
                    }
                });
    }

    public void updateUser(User user, Callback updateCallback) {
        String uid = getCurrentUserId();
        if (uid == null) {
            updateCallback.onError("User has not logged in");
            return;
        }

        if (user == null) {
            updateCallback.onError("User data is null");
            return;
        }

        DocumentReference userRef = db.collection("Users").document(uid);
        Map<String, Object> updates = new HashMap<>();

        if (user.goal != null && !user.goal.isEmpty()) updates.put("goal", user.goal);
        if (user.availableTime != null && user.availableTime > 0) updates.put("availableTime", user.availableTime);
        if (user.experience != null && !user.experience.isEmpty()) updates.put("experience", user.experience);
        if (user.hasEquipment != null) updates.put("hasEquipment", user.hasEquipment);
        if (user.focusArea != null && !user.focusArea.isEmpty()) updates.put("focusArea", user.focusArea);
        if (user.Dob != null && !user.Dob.isEmpty()) updates.put("Dob", user.Dob);
        if (user.gender != null && !user.gender.isEmpty()) updates.put("gender", user.gender);
        if (user.height != null ) updates.put("height", user.height);
        if (user.weight != null ) updates.put("weight", user.weight);
        if (user.goalWeight != null ) updates.put("goalWeight", user.goalWeight);
        if (user.level != null ) updates.put("level", user.level);
        if (user.currentDay != null ) updates.put("currentDay", user.currentDay);
        if (user.dayPerWeek != null ) updates.put("dayPerWeek", user.dayPerWeek);
        if (user.healthIssue != null && !user.healthIssue.isEmpty()) updates.put("healthIssue", user.healthIssue);
        if (user.schedule != null && !user.schedule.isEmpty()) updates.put("schedule", user.schedule);

        if (updates.isEmpty()) {
            updateCallback.onError("No updates to apply");
            return;
        }

        userRef.update(updates)
                .addOnSuccessListener(aVoid -> updateCallback.onSuccess())
                .addOnFailureListener(e -> updateCallback.onError(e.getMessage()));
    }

    public void getCurrentUser(String userId, DataCallBack<User> callback) {
        if (userId == null || userId.isEmpty()) {
            callback.onError("Invalid user ID");
            return;
        }

        db.collection("Users")
                .document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        User user = doc.toObject(User.class);
                        callback.onSuccess(user);
                    } else {
                        callback.onError("User not found");
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void getAllExercises(DataCallBack<List<Exercise>> callback) {
        db.collection("exercises")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Exercise> list = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            list.add(doc.toObject(Exercise.class));
                        }
                        callback.onSuccess(list);
                    } else {
                        callback.onError(task.getException() != null ? task.getException().getMessage() : "Unknown error");
                    }
                });
    }

    public void getExerciseById(String exerciseId, DataCallBack<Exercise> callback) {
        db.collection("exercises").document(exerciseId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        callback.onSuccess(documentSnapshot.toObject(Exercise.class));
                    } else {
                        callback.onError("Exercise not found");
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }
    public void updateIsNewStatus(String uid,boolean isNewStatus, Callback updateCallback) {
        if (uid == null) {
            updateCallback.onError("User has not logged in");
            return;
        }

        DocumentReference userRef = db.collection("Users").document(uid);
        Map<String, Object> update = new HashMap<>();

        update.put("isNew", isNewStatus);

        userRef.update(update)
                .addOnSuccessListener(aVoid -> updateCallback.onSuccess())
                .addOnFailureListener(e -> updateCallback.onError( e.getMessage()));
    }

    public String getCurrentUserId() {
        return mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
    }

    public String getCurrentUserMail() {
        return mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;
    }
}
