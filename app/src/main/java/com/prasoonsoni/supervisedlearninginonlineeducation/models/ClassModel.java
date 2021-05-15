package com.prasoonsoni.supervisedlearninginonlineeducation.models;

public class ClassModel {
    String endTime;
    String mood;
    String startTime;

    public ClassModel() {
    }

    public ClassModel(String endTime, String mood, String startTime) {
        this.endTime = endTime;
        this.mood = mood;
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
