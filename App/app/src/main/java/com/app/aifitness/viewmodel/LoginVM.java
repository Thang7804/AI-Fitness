package com.app.aifitness.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.aifitness.model.User;
import com.app.aifitness.repository.AuthRepo;

public class LoginVM extends ViewModel {
    private final AuthRepo repository;
    private final MutableLiveData<String> loginResult = new MutableLiveData<>();
    private final MutableLiveData<User> userData = new MutableLiveData<>();

    public LoginVM() {
        repository = new AuthRepo();
    }

    public LiveData<String> getLoginResult() {
        return loginResult;
    }

    public LiveData<User> getUserData() {
        return userData;
    }

    public void loginUser(String email, String password) {
        repository.loginUser(email, password).observeForever(result -> {
            loginResult.setValue(result);
        });
    }

    public void fetchUserData() {
        repository.getUserData().observeForever(user -> {
            userData.setValue(user);
        });
    }
}