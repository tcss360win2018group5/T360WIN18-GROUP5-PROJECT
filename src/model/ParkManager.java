
package model;

import java.io.Serializable;
import java.util.ArrayList;
//l.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.GregorianCalendar;
import util.SystemConstants;

public class ParkManager extends User implements Serializable {
	private ArrayList<Job> myJobsCreated;

	public JobCoordinator pendingJobs;


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
		myCurrentDate = new GregorianCalendar();

	}

	/**
	 * @param theJob to be removed
	 * @param theMasterList from JobCoordinator containing all the jobs
	 */
	public boolean removeJobFromList(final Job theJob, ArrayList<Job> theMasterList) {
		boolean isJobRemoved = false;
		if (!isTooCloseToAddOrRemove(theJob)) {
			if (theMasterList.contains(theJob)) {
				theMasterList.remove(theJob);
				isJobRemoved = true;
			}
			if (myJobsCreated.contains(theJob)) {
				myJobsCreated.remove(theJob);
			}
		}
		return isJobRemoved;
	}

	/*
	 * Adds a created job to this ParkManagers list of created jobs
	 */
	public void addCreatedJob(Job candidateJob) {
		myJobsCreated.add(candidateJob);
	}

	public ArrayList<Job> getCreatedJobs() {
		return myJobsCreated;
	}

	public boolean doesJobAlreadyExist(Job candidateJob, ArrayList<Job> theMasterList) {
		boolean jobExists = false;
		for (Job j : theMasterList) {
			jobExists = jobExists || (j.getJobTitle().equals(candidateJob.getJobTitle()));
		}
		return jobExists;
	}

	public boolean isMaximumJobDuration(Job theCandidateJob) {
		return getDifferenceInDays(theCandidateJob.getStartDate(), theCandidateJob.getEndDate()) > JobCoordinator.MAXIMUM_JOB_LENGTH;
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
	 * Check that job cann't be specified whose end date is more than
	 * the maximum number of days away from today
	 * 
	 * @param theCondidateJob
	 * @return true if it end exactly or less than the maximum number of days.
	 */
	public boolean isTooFarFromToday(Job theCandidateJob) {
		return getDifferenceInDays(theCandidateJob.getStartDate(), myCurrentDate) > JobCoordinator.MAXIMUM_DAYS_AWAY_TO_POST_JOB;
	}

	public boolean isLessJobsThanMaxInSystem(ArrayList<Job> theMasterList) {
		return theMasterList.size() >= JobCoordinator.MAXIMUM_JOBS;
	}

	//Determines if today is too close to the 
	public boolean isTooCloseToAddOrRemove(Job theJob) {
		return getDifferenceInDays(theJob.getStartDate(), myCurrentDate) < Volunteer.MINIMUM_DAYS_BEFORE_JOB_START;
	}


	/**
	 * Helper method to calculate the difference in days of two calendar dates.
	 *
	 * @param theFirstDate The first date chronologically.
	 * @param theSecondDate The second date chronologically.
	 *
	 * @return The positive difference in days.
	 */
	public static int getDifferenceInDays(GregorianCalendar theFirstDate,
			GregorianCalendar theSecondDate) {
		long convertedTime = TimeUnit.DAYS.convert(theSecondDate.getTimeInMillis(),
				TimeUnit.MILLISECONDS)
				- TimeUnit.DAYS.convert(theFirstDate.getTimeInMillis(),
						TimeUnit.MILLISECONDS);

		return Math.abs((int) convertedTime);
	}

	public boolean isJobInPast(GregorianCalendar theCurrentDate, GregorianCalendar theJobDate) {
		return (TimeUnit.DAYS.convert(theCurrentDate.getTimeInMillis(),
				TimeUnit.MILLISECONDS)
				- TimeUnit.DAYS.convert(theJobDate.getTimeInMillis(),
						TimeUnit.MILLISECONDS)) < 0;
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
