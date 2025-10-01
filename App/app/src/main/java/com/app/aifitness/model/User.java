package com.app.aifitness.model;

import java.util.Map;

public class User {
    private String email;
    private boolean isNew;
    private Map<String, Object> answers;

    public User() {}

    public User(String email, boolean isNew, Map<String, Object> answers) {
        this.email = email;
        this.isNew = isNew;
        this.answers = answers;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isNew() { return isNew; }
    public void setNew(boolean aNew) { isNew = aNew; }

    public Map<String, Object> getAnswers() { return answers; }
    public void setAnswers(Map<String, Object> answers) { this.answers = answers; }
}