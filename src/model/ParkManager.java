
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Objects;
//l.Calendar;
import java.util.concurrent.TimeUnit;

public final class ParkManager extends User implements Serializable {
	private ArrayList<Job> myJobsCreated;
	private ArrayList<Job> mySubmittedJobs;
	private GregorianCalendar myCurrentDate;

	/**
	 * Creates a park manager with the given username.
	 * 
	 * @param theUsername the username for the park manager user.
	 */
	public ParkManager(String theUsername) {
		super(theUsername, SystemCoordinator.PARK_MANAGER_ACCESS_LEVEL);
		myJobsCreated = new ArrayList<Job>();
		mySubmittedJobs = new ArrayList<Job>();
		myCurrentDate = new GregorianCalendar();
	}

	/*
	 * Adds a created job to this ParkManagers list of created jobs
	 */
	public void addCreatedJob(Job candidateJob) {
		myJobsCreated.add(candidateJob);
	}

	public ArrayList<Job> getCreatedJobs() {
		return (ArrayList<Job>) myJobsCreated.clone();
	}
	
	/**
	 * Precondition: Job to be added to submitted jobs must be accepted and added to a Job
	 * Coordinator first.
	 */
	public void addSubmittedJob(Job candidateJob) {
		mySubmittedJobs.add(candidateJob);
	}

	/**
	 * Provides the list of submitted jobs.
	 */
	public ArrayList<Job> getSubmittedJobs() {
		return (ArrayList<Job>) mySubmittedJobs.clone();
	}

	/*
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
	 * Precondition: the candidateJob has been checked and verified that
	 * if is within business rules to remove.
	 * 
	 * Removes specified job from submitted jobs if it exists, otherwise does nothing.
	 */
	public void removeSubmittedJob(Job candidateJob) {
		if (mySubmittedJobs.contains(candidateJob))
			mySubmittedJobs.remove(candidateJob);
	}


	public void setCurrentDate(GregorianCalendar theDate) {
		myCurrentDate = theDate;
	}
	
	public boolean isJobInPast(Job theJob) {
	    return theJob.getStartDate().before(myCurrentDate);
	}

	public boolean isFutureJob(Job theJob) {
		return JobCoordinator.getDifferenceInDays(myCurrentDate, theJob.getStartDate()) >= Volunteer.MINIMUM_DAYS_BEFORE_JOB_START;
	}

    @SuppressWarnings("unchecked")
    @Override
    public Object clone() {
        ParkManager cloneParkManager = new ParkManager(this.getUsername());
        cloneParkManager.myCurrentDate = (GregorianCalendar) this.myCurrentDate.clone();
        cloneParkManager.mySubmittedJobs = (ArrayList<Job>) this.mySubmittedJobs.clone();
        cloneParkManager.myJobsCreated = (ArrayList<Job>) this.myJobsCreated.clone();
        return cloneParkManager;
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), myCurrentDate);
    }
    
    @Override
    public boolean equals(Object theObject) {
        boolean result = false;
        if (this == theObject) {
            result = true;
        } else if (theObject == null) {
            result = false;
        } else if (this.getClass() == theObject.getClass()) {
            ParkManager theOtherPM = (ParkManager) theObject;
            result = super.equals(theObject) &&
                            Objects.equals(this.myCurrentDate, theOtherPM.myCurrentDate);
        }
        
        return result;
    }
}
