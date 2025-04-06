import java.io.Serializable;
import java.util.Date;

public class Job implements Serializable {	
    // Attributes of the Job Method
        private String clientID;
        private String overview;
        private int redundancy;
        private int duration;
        private Date deadline;
    
    // Creates a Job Object
        public Job(String clientID, Date deadline, int redundancy, int duration) {
            this.clientID = clientID;
            this.deadline = deadline;
            this.redundancy = redundancy;
            this.duration = duration;
        }
    
        public String getClientID() {
            return clientID;
        }
    
        public int getDuration() {
            return duration;
        }
    
    // Returns a String with Information on the Job
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
    