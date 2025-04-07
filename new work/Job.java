package com.vcrts.model;

public class Job {
    private String clientID;
    private String overview;
    private int redundancy;
    private int duration;

    public Job(String clientID, String overview, int redundancy, int duration) {
        this.clientID = clientID;
        this.overview = overview;
        this.redundancy = redundancy;
        this.duration = duration;
    }

    // Getters and setters
    public String getClientID() { return clientID; }
    public int getDuration() { return duration; }
    public int getRedundancy() { return redundancy; }
    public void setRedundancy(int redundancy) { this.redundancy = redundancy; }

    @Override
    public String toString() {
        return "Job{" +
                "clientID='" + clientID + '\'' +
                ", overview='" + overview + '\'' +
                ", redundancy=" + redundancy +
                ", duration=" + duration +
                '}';
    }
}