package com.app.aifitness.repository;

import androidx.lifecycle.MutableLiveData;

import com.app.aifitness.model.Exercise;
import com.google.firebase.firestore.FirebaseFirestore;

public class ExerciseRepo {
    private final FirebaseFirestore db;

    public ExerciseRepo() {
        this.db = FirebaseFirestore.getInstance();
    }


    public void getExerciseById(String exerciseId,
                                MutableLiveData<Exercise> exerciseLiveData,
                                MutableLiveData<String> errorLiveData) {
        db.collection("exercises")
                .document(exerciseId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Exercise exercise = documentSnapshot.toObject(Exercise.class);
                        if (exercise != null) {
                            exercise.setExercise_id(documentSnapshot.getId());
                            exerciseLiveData.setValue(exercise);
                        } else {
                            errorLiveData.setValue("Failed to parse exercise data");
                        }
                    } else {
                        errorLiveData.setValue("Exercise not found");
                    }
                })
                .addOnFailureListener(e ->
                        errorLiveData.setValue("Error: " + e.getMessage())
                );
    }
}