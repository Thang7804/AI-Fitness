package com.app.aifitness.Firebase;

import com.app.aifitness.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public void  registerUser(String email, String password, AuthCallback callback){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task ->{
                if(task.isSuccessful()){
                    String uid = mAuth.getCurrentUser().getUid();
                    User basicUser = new User(email);
                    db.collection("User").document(uid).set(basicUser).addOnCompleteListener(dbtask ->{
                        if(dbtask.isSuccessful()){
                            callback.onSuccess();
                        }
                        else{
                            callback.onError("Save user erorr");
                        }
                    });

                }
                else {
                    callback.onError("Register erorr:" + task.getException().getMessage());
                }
                });}
    public void loginUser(String email, String password, AuthCallback callback){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task->{
            if(task.isSuccessful()){
                callback.onSuccess();
            }
            else {
                callback.onError("Signin error:"+ task.getException().getMessage());
            }
        });
    }
    public String getCurrentUserId() {
        return mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
    }

}
