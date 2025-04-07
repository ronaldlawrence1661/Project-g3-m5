import java.io.Serializable;
import java.util.Date;

public class Client implements Serializable {
    private String ClientID;
    private Date Deadline;
    private int Duration;
    private String JobDesc;

    public Client(String clientID, Date deadline, int duration, String jobDesc) {
        this.ClientID = clientID;
        this.Deadline = deadline;
        this.Duration = duration;
        this.JobDesc = jobDesc;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        this.ClientID = clientID;
    }

    public Date getDeadline() {
        return Deadline;
    }

    public void setDeadline(Date deadline) {
        this.Deadline = deadline;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        this.Duration = duration;
    }

    public String getJobDesc() {
        return JobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.JobDesc = jobDesc;
    }

    public Job createJob(String jobDesc, Date deadline, int redundancy, int duration) {
        Job newJob = new Job(jobDesc, deadline, redundancy, duration);
        System.out.println("Job created: " + jobDesc);
        return newJob;
    }
}