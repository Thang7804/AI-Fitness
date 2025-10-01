package com.app.aifitness.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.aifitness.repository.AuthRepo;
import com.app.aifitness.repository.QuestionRepo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class QuestionVM extends ViewModel {
    private final QuestionRepo questionRepo;
    private final AuthRepo authRepo;
    private final MutableLiveData<String> answerResult = new MutableLiveData<>();
    private final MutableLiveData<String> updateStatus = new MutableLiveData<>();

    public QuestionVM() {
        questionRepo = new QuestionRepo();
        authRepo = new AuthRepo();
    }

    public LiveData<String> getAnswerResult() {
        return answerResult;
    }

    public LiveData<String> getUpdateStatus() {
        return updateStatus;
    }

    public void saveAnswer(String key, String value) {
        questionRepo.saveAnswer(key, value)
                .addOnSuccessListener(aVoid -> answerResult.setValue("success"))
                .addOnFailureListener(e -> answerResult.setValue("error: " + e.getMessage()));
    }

    public void markUserAsNotNew() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            authRepo.updateUserAsNotNew(user.getUid())
                    .addOnSuccessListener(aVoid -> updateStatus.setValue("updated"))
                    .addOnFailureListener(e -> updateStatus.setValue("error: " + e.getMessage()));
        }
    }
}