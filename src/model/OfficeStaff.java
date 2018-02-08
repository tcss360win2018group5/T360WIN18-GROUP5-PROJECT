package model;

import java.io.Serializable;
import java.util.ArrayList;

public class OfficeStaff extends User implements Serializable {
    /** The priority level for this park manager. */
	private static final int PRIORITY_LEVEL = 2;
	
	/** The list of submitted jobs for this park manager. */
	private ArrayList<Job> mySubmittedJobs = new ArrayList<Job>();
	
	/**
	 * Creates a park manager with the given username.
	 * 
	 * @param theUsername the username for the park manager user.
	 */
	public OfficeStaff(String theUsername) {
		super(theUsername, PRIORITY_LEVEL);
	}
	
	// mutators
    /**
     * Adds a current job to the park manager's list of submitted jobs.
     * 
     * Preconditions:
     *      1) Job must be valid.
     *      2) Job must not have been previously submitted.
     *      
     * @param theApplyingJob the job to add.
     * @return True if job was successfully added, false otherwise.
     */
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

    // queries
    /**
     * Retrieves the current list of submitted jobs.
     */
    public ArrayList<Job> getSubmittedJobs() {
        return mySubmittedJobs;
    }
    
    // testers
    /**
     * Checks if the given job has previously been submitted by this park manager.
     * 
     * @param theJob the job to be checked.
     * @return True if it has been submitted before by this park manager, false otherwise.
     */
    public boolean hasPreviouslySubmittedJob(final Job theJob) {
        return mySubmittedJobs.contains(theJob);
    }
}
