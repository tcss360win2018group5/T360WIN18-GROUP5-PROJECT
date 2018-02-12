package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;


import util.SystemConstants;

public class ParkManager extends User implements Serializable {
	
	private final int MAXIMUM_NUMBER_OF_DAYS = 3;
	private final int MAXIMUM_NUMBER_OF_JOB_END_DAYS = 75;

	private ArrayList<Job> jobsCreated;



	public JobCoordinator pendingJobs;
	
	/**
	 * Creates a park manager with the given username.
	 * 
	 * @param theUsername the username for the park manager user.
	 */
	public ParkManager(String theUsername) {
		super(theUsername, SystemConstants.PARK_MANAGER_ACCESS_LEVEL);
	}
	
	
	/**
	 * Checks if a job can be created or not.
	 * 
	 * @param candidateJob
	 * @return list of jobs created;
	 */
	public ArrayList<Job> createdANewJob(Job candidateJob) {
		if(isMaximumJobDays(candidateJob) && isMaximumEndDays(candidateJob) && isPendingJobs() ) {
			jobsCreated.add(candidateJob);
		}
		return jobsCreated;
		
	}
	
	
	public boolean isPendingJobs() {
		boolean pending = false;
		if(pendingJobs.getPendingJobs().size() > 20) {
			pending = true;

		}
		return pending;
	}


	/**
	 * Check that job cann't be specified if it takes more than the maximum number of days
	 * 
	 * @param theCondidateJob
	 * @return true if job takes less than the maximum number of days.
	 */
	public boolean isMaximumJobDays(Job theCondidateJob) {

		Calendar maximumDate = Calendar.getInstance();
		maximumDate.add(Calendar.DAY_OF_MONTH, MAXIMUM_NUMBER_OF_DAYS);


		return compare2Date(theCondidateJob.getStartDate(), maximumDate) <=0;
	}

	/**
	 * Check that job cann't be specified whose end date is more than the maximum number of days
	 * 
	 * @param theCondidateJob
	 * @return true if it end exactly or less than the maximum number of days.
	 */
	public boolean isMaximumEndDays(Job theCondidateJob) {

		Calendar maximumStartDate = Calendar.getInstance();
		maximumStartDate.add(Calendar.DAY_OF_MONTH, MAXIMUM_NUMBER_OF_JOB_END_DAYS);


		return compare2Date(theCondidateJob.getEndDate(), maximumStartDate) <=0;
	}




	/**
	 * Comparing two dates by Year, Month and day.
	 * 
	 * @param theFirstDate the first date.
	 * @param theSecondDate the second date.
	 * @return the compare result.
	 */
	public int compare2Date(final Calendar theFirstDate, final Calendar theSecondDate) {

		// The first date without the time.
		Calendar firstDate = Calendar.getInstance();
		firstDate.set(theFirstDate.get(Calendar.YEAR), 
				theFirstDate.get(Calendar.MONTH), 
				theFirstDate.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

		//The second date without the time.
		Calendar secondDate = Calendar.getInstance();
		secondDate.set(theSecondDate.get(Calendar.YEAR), 
				theSecondDate.get(Calendar.MONTH), 
				theSecondDate.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

		// Compare the two dates and return the result.
		return firstDate.compareTo(secondDate);

	}



}
