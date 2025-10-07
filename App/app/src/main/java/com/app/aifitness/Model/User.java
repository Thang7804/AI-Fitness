package com.app.aifitness.Model;

import java.io.Serializable;

public class User implements Serializable {
    public String email;
    public String goal;
    public int availableTime;
    public String experience;
    public String hasEquipment;
    public String focusArea;
    public boolean isNew;
    public String Dob;
    public String gender;
    public  float height;
    public  float weight;
    public  float goalweight;
    public String level;
    public int dayPerWeek;
    public String healthIssue;
    public User(){}
    public User(String email) {
        this.email = email;
        this.goal = "";
        this.experience = "";
        this.availableTime = 0;
        this.hasEquipment = "";
        this.focusArea = "";
        this.isNew= true;
        this.Dob="";
        this.gender="";
        this.height=0;
        this.weight=0;
        this.goalweight=0;
        this.level="";
        this.dayPerWeek=0;
        this.healthIssue="";
    }

    public User(String email, String goal, int availableTime, String experience, String hasEquipment, String focusArea, boolean isNew, String dob, String gender, float height, float weight, float goalweight, String level, int dayPerWeek, String healthIssue) {
        this.email = email;
        this.goal = goal;
        this.availableTime = availableTime;
        this.experience = experience;
        this.hasEquipment = hasEquipment;
        this.focusArea = focusArea;
        this.isNew = isNew;
        this.Dob = dob;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.goalweight = goalweight;
        this.level = level;
        this.dayPerWeek = dayPerWeek;
        this.healthIssue = healthIssue;
    }
}
