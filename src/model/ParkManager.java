
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class ParkManager extends User implements Serializable {
	private ArrayList<Job> mySubmittedJobs;
	private GregorianCalendar myCurrentDate;
	
	/**
	 * Creates a park manager with the given username.
	 * 
	 * @param theUsername the username for the park manager user.
	 */
	public ParkManager(String theUsername) {
		super(theUsername, SystemCoordinator.PARK_MANAGER_ACCESS_LEVEL);
		mySubmittedJobs = new ArrayList<Job>();
		myCurrentDate = new GregorianCalendar();
	}
	
	/**
	 * Precondition: Job to be added to submitted jobs must be accepted and added to a Job
	 * Coordinator first.
	 */
	public void addSubmittedJob(Job candidateJob) {
	    mySubmittedJobs.add(candidateJob);
	}
	
	/**
	 * Provides a copy of list of submitted jobs, changes to this list should not reflect in 
	 * actual list.
	 */
	@SuppressWarnings("unchecked")
    public ArrayList<Job> getSubmittedJobs() {
	    return (ArrayList<Job>) mySubmittedJobs.clone();
	}
	
	/**
	 * Provides a copy of list of FUTURE submitted jobs relative to myCurrentDate
	 */
	public ArrayList<Job> getFutureSubmittedJobs() {
	    ArrayList<Job> futureJobs = new ArrayList<Job>();
	    for (Job aJob : mySubmittedJobs) {
	        if (isFutureJob(aJob))
	            futureJobs.add(aJob);
	    }
        return futureJobs;
	}
	
	/**
	 * Removes specified job from submitted jobs if it exists, otherwise does nothing.
	 */
	public void removeSubmittedJob(Job candidateJob) {
	    if (mySubmittedJobs.contains(candidateJob))
	        mySubmittedJobs.remove(candidateJob);
	}
	
	
	public void setCurrentDate(GregorianCalendar theDate) {
	    myCurrentDate = theDate;
	}
	
	public boolean isFutureJob(Job theJob) {
	    return theJob.getStartDate().after(myCurrentDate);
	}
}
