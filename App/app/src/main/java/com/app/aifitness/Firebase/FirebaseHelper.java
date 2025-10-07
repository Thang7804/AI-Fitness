package com.app.aifitness.Firebase;

import com.app.aifitness.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {
    private static FirebaseHelper instance;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private FirebaseHelper() {
       mAuth= FirebaseAuth.getInstance();
       db= FirebaseFirestore.getInstance();
    }
    public static FirebaseHelper getInstance(){
        if(instance == null){
            instance= new FirebaseHelper();
        }
        return instance;
    }

    public void  registerUser(String email, String password, Callback authcallback){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task ->{
                if(task.isSuccessful()){
                    String uid = mAuth.getCurrentUser().getUid();
                    User basicUser = new User(email);
                    db.collection("User").document(uid).set(basicUser).addOnCompleteListener(dbtask ->{
                        if(dbtask.isSuccessful()){
                            authcallback.onSuccess();
                        }
                        else{
                            authcallback.onError("Save user erorr");
                        }
                    });

                }
                else {
                    authcallback.onError("Register erorr:" + task.getException().getMessage());
                }
                });}
    public void loginUser(String email, String password, Callback callback){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task->{
            if(task.isSuccessful()){
                callback.onSuccess();
            }
            else {
                callback.onError("Signin error:"+ task.getException().getMessage());
            }
        });
    }
    public void updateUser(User user, Callback updatecallback) {
        String uid = getCurrentUserId();
        if (uid == null){
            updatecallback.onError("Not Login Yet");
            return;
        }

        DocumentReference userRef = db.collection("Users").document(uid);
        Map<String, Object> updates = new HashMap<>();
        updates.put("goal", user.goal);
        updates.put("availableTime", user.availableTime);
        updates.put("experience", user.experience);
        updates.put("hasEquipment", user.hasEquipment);
        updates.put("focusArea", user.focusArea);
        updates.put("Dob", user.Dob);
        updates.put("gender", user.gender);
        updates.put("height", user.height);
        updates.put("weight", user.weight);
        updates.put("goalweight", user.goalweight);
        updates.put("level", user.level);
        updates.put("dayPerWeek", user.dayPerWeek);
        updates.put("healthIssue", user.healthIssue);
        updates.put("updatedAt", com.google.firebase.firestore.FieldValue.serverTimestamp());

        userRef.update(updates)
                .addOnCompleteListener(updatetask->{
                    if(updatetask.isSuccessful()){
                        updatecallback.onSuccess();
                    }
                    else{
                        updatecallback.onError("Update Error");
                    }
                });

    }

    public String getCurrentUserId() {
        return mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
    }
    public String getCurrentUserMail(){
        return mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;
    }
}
