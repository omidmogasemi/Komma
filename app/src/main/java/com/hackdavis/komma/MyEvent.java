package com.hackdavis.komma;

public class MyEvent {
    public String name, description, startTime, startDate, location;
    public int attendees;
  
    public MyEvent(String n, String d, String st, String sd, int a, String l) {
        name = n;
        description = d;
        startTime = st;
        startDate = sd;
        attendees = 0;
        location = l;
    }
    //interesting
    public MyEvent() {

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

    public void setStartDate(String startDate) { this.startDate = startDate; }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAttendees(int attendees) {
        this.attendees = attendees;
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

    public String getStartDate() { return startDate; }

    public String getLocation() {
        return location;
    }

    public int getAttendees() {
        return attendees;
    }
}
