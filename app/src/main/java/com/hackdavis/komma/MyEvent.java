package com.hackdavis.komma;

public class MyEvent {
    private String name, description, startTime, startDate, endTime, endDate;

    public MyEvent(String n, String d, String st, String sd, String et, String ed) {
        name = n;
        description = d;
        startTime = st;
        startDate = sd;
        endTime = et;
        endDate = ed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getEndDate() {
        return endDate;
    }
}
