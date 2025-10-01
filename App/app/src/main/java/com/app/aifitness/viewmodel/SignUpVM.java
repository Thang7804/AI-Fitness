package com.app.aifitness.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.aifitness.repository.AuthRepo;

public class SignUpVM extends ViewModel {
    private final AuthRepo repository;
    private final MutableLiveData<String> result = new MutableLiveData<>();

    public SignUpVM() {
        repository = new AuthRepo();
    }

    public LiveData<String> getResult() {
        return result;
    }

    public void registerUser(String email, String password) {
        repository.registerUser(email, password).observeForever(s -> result.setValue(s));
    }
}