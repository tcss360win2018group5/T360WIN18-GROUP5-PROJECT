package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class Volunteer extends User implements Serializable {

	private static final int MIN_DAYS_BEFORE_START = 2;

	/** The priority level for Volunteer type users. */
	private static final int PRIORITY_LEVEL = 2;

	/** The current jobs this Volunteer is signed up for. */
	private ArrayList<Job> myCurrentJobs = new ArrayList<Job>();

	private GregorianCalendar myCurrentDay;


	/**
	 * Constructs a volunteer based on the specified name
	 * @param theName the name to give the user.
	 */
	public Volunteer(String theName) {
		super(theName, PRIORITY_LEVEL);
	}

	// mutator's
	/**
	 * Adds a current job to the volunteer's list of jobs.
	 *
	 * Preconditions:
	 *      1) Job must be valid.
	 *      2) Job must not overlap with any currently applied to jobs.
	 *
	 * @param theApplyingJob the job to add.
	 * @return True if job was successfully added, false otherwise.
	 */
	public int addToCurrentJobs(final Job theApplyingJob) {
		int returnInt = 0;
		boolean is_there_conflict = hasOverlap(theApplyingJob);
		boolean is_outside_timeframe = (getDifferenceInDays(myCurrentDay, theApplyingJob.getStartDate())
										<= MIN_DAYS_BEFORE_START);

		if (is_there_conflict) {
			returnInt = 1; //job was overlapping with another job on this day
		} else if (is_outside_timeframe) {
			returnInt = 2; //Volunteer tries to sign up for a job before minimum days before start date
		}

		return returnInt;
	}

	public void signUpForJob(final Job theJob) {
		myCurrentJobs.add(theJob);
	}
	
	public void setCurrentDay(GregorianCalendar theCurrentDay) {
		myCurrentDay = theCurrentDay;
	}


	// queries
	/**
	 * Retrieves the current list of applied to jobs.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Job> getCurrentJobs() {
		return (ArrayList<Job>) this.myCurrentJobs.clone();
	}

	// testers
	// deprecated, use isOverlapping
	public boolean isSameDayConflict(Job theJobFromList, Job theApplyingJob) {
		return theJobFromList.getStartDate().get(Calendar.DATE)
				== theApplyingJob.getStartDate().get(Calendar.DATE);
	}

	// deprecated, use isOverlapping
	public boolean isEndDayConflict(Job theJobFromList, Job theApplyingJob) {
		return theJobFromList.getEndDate().get(Calendar.DATE)
				== theApplyingJob.getStartDate().get(Calendar.DATE);
	}

	public int getMinDaysAway() {
		return MIN_DAYS_BEFORE_START;
	}

	/**
	 * Checks if the volunteer currently has any overlap with the specified job
	 * they wish to apply to.
	 *
	 * @param theApplyingJob the job to apply to.
	 * @return True if there is an overlap, false otherwise.
	 */
	public boolean hasOverlap(Job theApplyingJob) {
		return myCurrentJobs.stream()
				.anyMatch(aJobFromList -> areJobsOverlapping(aJobFromList,
						theApplyingJob));
	}

	// private helpers
	/**
	 * Private helper to test each individual overlap case for any two jobs.
	 *
	 * @param theFirstJob the first job chronologically.
	 * @param theSecondJob the second job chronologically.
	 * @return True if there is any overlap, False otherwise.
	 */
	private boolean areJobsOverlapping(Job theFirstJob, Job theSecondJob) {
		GregorianCalendar firstJobStart = theFirstJob.getStartDate();
		GregorianCalendar firstJobEnd = theFirstJob.getEndDate();
		GregorianCalendar secondJobStart = theSecondJob.getStartDate();
		GregorianCalendar secondJobEnd = theSecondJob.getEndDate();
		// same day conflict
		return firstJobStart.compareTo(secondJobStart) == 0
				// if listJob starts  on the same day that applyingJob ends
				|| firstJobStart.compareTo(secondJobEnd) == 0
				// if listJob ends on the same day applyingJob starts
				|| firstJobEnd.compareTo(secondJobStart) == 0
				// if listJob overlaps on the left with applyingJob
				|| (firstJobStart.compareTo(secondJobStart) < 0
						&& firstJobEnd.compareTo(secondJobStart) >= 0)
				// if listJob overlaps on the right with applyingJob
				|| (firstJobEnd.compareTo(secondJobEnd) > 0
						&& firstJobStart.compareTo(secondJobEnd) <= 0)
				// if listJob overlaps inside applyingJob
				|| (firstJobStart.compareTo(secondJobStart) > 0
						&& (firstJobStart.compareTo(secondJobEnd) < 0));
	}

	public static int getDifferenceInDays(GregorianCalendar theFirstDate,
										  GregorianCalendar theSecondDate) {
		long convertedTime = TimeUnit.DAYS.convert(theSecondDate.getTimeInMillis() , TimeUnit.MILLISECONDS)
				- TimeUnit.DAYS.convert(theFirstDate.getTimeInMillis(),  TimeUnit.MILLISECONDS);

		return Math.abs( (int) convertedTime);
	}


}
