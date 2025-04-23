

import java.util.Date;

public class Client {
    private String clientID;
    private Date deadline;
    private int duration;
    private String jobDesc;

    public Client(String clientID, Date deadline, int duration, String jobDesc) {
        this.clientID = clientID;
        this.deadline = deadline;
        this.duration = duration;
        this.jobDesc = jobDesc;
    }

    // Getters
    public String getClientID() { return clientID; }
    public Date getDeadline() { return deadline; }
    public int getDuration() { return duration; }
    public String getJobDesc() { return jobDesc; }

    public Job createJob(String jobDesc, Date deadline, int redundancy, int duration) {
        Job newJob = new Job(jobDesc, deadline, redundancy, duration);
        System.out.println("Job created: " + jobDesc);
        return newJob;
    }
}