package com.vcrts.controller;

import com.vcrts.model.Job;
import java.util.ArrayList;
import java.util.List;

public class VCController {
    private List<Job> jobs = new ArrayList<>();

    public void addJob(Job job) {
        jobs.add(job);
    }

    public void computeCompletionTime() {
        int completionTime = 0;
        for (Job job : jobs) {
            completionTime += job.getDuration();
            System.out.println("Job ID: " + job.getClientID() + 
                             " Duration: " + job.getDuration() + 
                             " Completion Time: " + completionTime);
        }
    }
}