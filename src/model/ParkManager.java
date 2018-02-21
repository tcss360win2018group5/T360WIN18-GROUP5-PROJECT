
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import util.SystemConstants;

public class ParkManager extends User implements Serializable {
	private ArrayList<Job> myJobsCreated;

	public JobCoordinator pendingJobs;

	GregorianCalendar myCurrentDate;
	/**
	 * Creates a park manager with the given username.
	 * 
	 * @param theUsername the username for the park manager user.
	 */
	public ParkManager(String theUsername) {
		super(theUsername, SystemConstants.PARK_MANAGER_ACCESS_LEVEL);
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
		return getDifferenceInDays(theCandidateJob.getStartDate(), theCandidateJob.getEndDate()) > SystemConstants.MAXIMUM_JOB_LENGTH;
	}

	/**
	 * Check that job cann't be specified whose end date is more than
	 * the maximum number of days away from today
	 * 
	 * @param theCondidateJob
	 * @return true if it end exactly or less than the maximum number of days.
	 */
	public boolean isTooFarFromToday(Job theCandidateJob) {
		return getDifferenceInDays(theCandidateJob.getStartDate(), myCurrentDate) > SystemConstants.MAXIMUM_DAYS_AWAY_TO_POST_JOB;
	}

	public boolean isLessJobsThanMaxInSystem(ArrayList<Job> theMasterList) {
		return theMasterList.size() >= SystemConstants.MAXIMUM_JOBS;
	}
	
	//Determines if today is too close to the 
	public boolean isTooCloseToAddOrRemove(Job theJob) {
		return getDifferenceInDays(theJob.getStartDate(), myCurrentDate) < SystemConstants.MINIMUM_DAYS_BEFORE_JOB_START;
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


//	/**
//	 * Comparing two dates by Year, Month and day.
//	 * 
//	 * @param theFirstDate the first date.
//	 * @param theSecondDate the second date.
//	 * @return the compare result.
//	 */
//	public int compare2Date(final Calendar theFirstDate, final Calendar theSecondDate) {
//
//		// The first date without the time.
//		Calendar firstDate = Calendar.getInstance();
//		firstDate.set(theFirstDate.get(Calendar.YEAR), 
//				theFirstDate.get(Calendar.MONTH), 
//				theFirstDate.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
//
//		//The second date without the time.
//		Calendar secondDate = Calendar.getInstance();
//		secondDate.set(theSecondDate.get(Calendar.YEAR), 
//				theSecondDate.get(Calendar.MONTH), 
//				theSecondDate.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
//
//		// Compare the two dates and return the result.
//		return firstDate.compareTo(secondDate);
//
//	}




}
