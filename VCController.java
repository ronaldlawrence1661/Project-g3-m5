import java.util.*;

public class VCController {
// Arraylist to store Jobs created from Job
    private List<Job> jobs = new ArrayList<>();

    public void addJob(Job job) {
        jobs.add(job);
    }

// Finds the Completion for the Jobs in the System
    public void computeCompletionTime() {
        int completionTime = 0;
        for (Job job : jobs) {
            completionTime += job.getDuration();
            System.out.println("Job ID: " + job.getClientID() + " Duration: " + job.getDuration() + " Completion Time: " + completionTime);
        }
    }
}
