import java.util.ArrayList;

public class ParkManager extends User {
	
	private static final int PRIORITY_LEVEL = 2;
	
	private ArrayList<Job> mySubmittedJobs = new ArrayList<Job>();
	
	protected ParkManager(String theUsername) {
		super(theUsername, PRIORITY_LEVEL);
	}

   public boolean addToSubmittedJobs(final Job theSubmittedJob) {
        boolean is_there_conflict = false;
        boolean is_job_added = false;
        
        // all other checks done when job is being submitted
        if (this.hasPreviouslySubmittedJob(theSubmittedJob)) {
            is_there_conflict = true;
        }
        // if no conflicts, add job
        if (!is_there_conflict) {
            mySubmittedJobs.add(theSubmittedJob);
            is_job_added = true;
        }
        
        return is_job_added;
    }


    public ArrayList<Job> getSubmittedJobs() {
        return mySubmittedJobs;
    }
    
    public boolean hasPreviouslySubmittedJob(final Job theJob) {
        return mySubmittedJobs.contains(theJob);
    }
}
