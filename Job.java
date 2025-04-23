import java.io.Serializable;
import java.util.Date;

public class Job implements Serializable {
    private static final long serialVersionUID = 1L;
    private String clientID;
    private String overview;
    private int redundancy;
    private int duration;
    private Date deadline;
    private String date;
    private String description;

    public Job(String clientID, String overview, int redundancy, int duration) {
        this.clientID = clientID;
        this.overview = overview;
        this.redundancy = redundancy;
        this.duration = duration;
    }

    public Job(String clientID, Date deadline, int redundancy, int duration) {
        this.clientID = clientID;
        this.overview = overview;
        this.redundancy = redundancy;
        this.duration = duration;
        this.deadline = deadline; // Initialize the deadline
    }

    // Getters and setters
    public String getClientID() {
        return clientID;
    }

    public int getDuration() {
        return duration;
    }

    public int getRedundancy() {
        return redundancy;
    }

    public void setRedundancy(int redundancy) {
        this.redundancy = redundancy;
    }
        // Getter for the date field
        public String getDate() {
            return date;
        }
    
        // Setter for the date field (optional)
        public void setDate(String date) {
            this.date = date;
        }
       
    
        // Setter for the date field (optional)
        public void setDescription(String description) {
            this.description = description;
        }
        public String getDescription() {
            return description;
        }
        
    
    
        
    
      
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