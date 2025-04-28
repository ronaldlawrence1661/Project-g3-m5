
public class Job{	
    // Attributes of the Job Method
        private String clientID;
        private String overview;
        private int redundancy;
        private int duration;
    
    // Creates a Job Object
        public Job(String clientID, String overview, int redundancy, int duration) {
            this.clientID = clientID;
            this.overview = overview;
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
    