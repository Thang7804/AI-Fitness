package com.app.aifitness.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.aifitness.repository.ExerciseRepo;
import com.app.aifitness.model.Exercise;

public class ExerciseVM extends ViewModel {
    private final ExerciseRepo exerciseRepository;
    private final MutableLiveData<Exercise> exerciseLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>();

    public ExerciseVM() {
        this.exerciseRepository = new ExerciseRepo();
    }

    public MutableLiveData<Exercise> getExerciseLiveData() {
        return exerciseLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public MutableLiveData<Boolean> getIsLoadingLiveData() {
        return isLoadingLiveData;
    }

    public void loadExercise(String exerciseId) {
        if (exerciseId == null || exerciseId.isEmpty()) {
            errorLiveData.setValue("Invalid exercise ID");
            return;
        }

        isLoadingLiveData.setValue(true);
        exerciseRepository.getExerciseById(exerciseId, exerciseLiveData, errorLiveData);
    }
}