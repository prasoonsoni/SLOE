package com.prasoonsoni.supervisedlearninginonlineeducation.models;

public class MoodModel {

    String Overall, date;

    public MoodModel(String Overall, String date, String lec1) {
        this.Overall = Overall;
        this.date = date;
    }

    public MoodModel() {
    }

    public String getOverall() {
        return Overall;
    }

    public void setOverall(String overall) {
        this.Overall = overall;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
