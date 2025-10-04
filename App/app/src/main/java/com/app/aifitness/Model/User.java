package com.app.aifitness.Model;

public class User {
    public String email;
    public String goal;
    public int availableTime;
    public String experience;
    public boolean hasEquipment;
    public String focusArea;
    public boolean isNew;

    public User(){}
    public User(String email) {
        this.email = email;
        this.goal = "";
        this.experience = "";
        this.availableTime = 0;
        this.hasEquipment = false;
        this.focusArea = "";
        this.isNew= true;
    }

    public User(String email, String goal, int availableTime, String experience, boolean hasEquipment, String focusArea, boolean isNew) {
        this.email = email;
        this.goal = goal;
        this.availableTime = availableTime;
        this.experience = experience;
        this.hasEquipment = hasEquipment;
        this.focusArea = focusArea;
        this.isNew=isNew;
    }
}
