package com.app.aifitness.model;

import java.util.List;

public class Exercise {
    private String exercise_id;
    private String camera_angle;
    private long created_at;
    private int duration_s;
    private List<String> tags;
    private String thumbnail_url;
    private String visibility;
    private String yt_video_id;
    private String name;
    private String description;

    public Exercise() {

    }

    // Getters
    public String getExercise_id() {
        return exercise_id;
    }

    public String getCamera_angle() {
        return camera_angle;
    }

    public long getCreated_at() {
        return created_at;
    }

    public int getDuration_s() {
        return duration_s;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getYt_video_id() {
        return yt_video_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setExercise_id(String exercise_id) {
        this.exercise_id = exercise_id;
    }

    public void setCamera_angle(String camera_angle) {
        this.camera_angle = camera_angle;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public void setDuration_s(int duration_s) {
        this.duration_s = duration_s;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public void setYt_video_id(String yt_video_id) {
        this.yt_video_id = yt_video_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYouTubeUrl() {
        return "https://www.youtube.com/watch?v=" + yt_video_id;
    }

    public String getFormattedDuration() {
        int minutes = duration_s / 60;
        int seconds = duration_s % 60;
        if (minutes > 0) {
            return String.format("%d:%02d", minutes, seconds);
        }
        return String.format("0:%02d", seconds);
    }
}