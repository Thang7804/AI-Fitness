package com.app.aifitness.BuildExLogic;

import com.app.aifitness.Model.Exercise;
import com.app.aifitness.Model.User;
import java.util.*;

public class Rules {

    public double calculateScore(User user, Exercise ex) {
        double score = 100.0;

        float bmi = (float) (user.weight / (user.height * user.height));

        if (bmi > 30 && ex.intensity >= 3) score -= 30;
        else if (bmi < 18.5 && ex.intensity >= 3) score -= 15;

        String issue = user.healthIssue.toLowerCase();
        String exName = ex.name.toLowerCase();

        if (issue.contains("knee") && exName.contains("jump")) score -= 25;
        if (issue.contains("back") && (exName.contains("deadlift") || exName.contains("crunch"))) score -= 25;
        if (issue.contains("heart") && ex.intensity >= 3) score -= 30;

        switch (user.goal.toLowerCase()) {
            case "lose weight":
                score += ex.caloriesPerMin * 3;
                break;
            case "gain muscle":
                if (ex.muscle_group.containsKey("chest") || ex.muscle_group.containsKey("arms"))
                    score += 15;
                break;
            case "increase stamina":
                if (ex.intensity == 2) score += 10;
                break;
        }

        int diffGap = Math.abs(ex.difficulty - user.level);
        if (diffGap <= 1) score += 10;
        else if (diffGap >= 3) score -= 20;

        if (user.focusArea.equalsIgnoreCase(ex.focusArea)) score += 10;

        if (Boolean.FALSE.equals(user.hasEquipment) && ex.requiresEquipment)
            score -= 25;

        return Math.max(0, Math.min(100, score));
    }

    public List<Exercise> filterRecommended(User user, List<Exercise> allExercises) {
        List<Exercise> result = new ArrayList<>();
        for (Exercise ex : allExercises) {
            double s = calculateScore(user, ex);
            if (s >= 40) result.add(ex);
        }
        return result;
    }
}
