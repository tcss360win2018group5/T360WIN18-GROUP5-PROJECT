package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import util.SystemConstants;

public class Volunteer extends User implements Serializable {

	/** The current jobs this Volunteer is signed up for. */
	private ArrayList<Job> myCurrentJobs = new ArrayList<Job>();

	private GregorianCalendar myCurrentDay;


	/**
	 * Constructs a volunteer based on the specified name
	 * @param theName the name to give the user.
	 */
	public Volunteer(String theName) {
		super(theName, SystemConstants.VOLUTNEER_ACCESS_LEVEL);
	}

	// mutator's
	public void setCurrentDay(GregorianCalendar theCurrentDay) {
	    myCurrentDay = theCurrentDay;
    }

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
	public void signUpForJob(final Job theJob) {
		myCurrentJobs.add(theJob);
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
    public int canSignUpForJob(final Job theApplyingJob) {
        int returnInt = 0;
        boolean is_there_conflict = myCurrentJobs.stream()
                        .anyMatch(aJobFromList -> aJobFromList.hasOverlap(theApplyingJob));
        boolean is_outside_timeframe = (getDifferenceInDays(myCurrentDay, theApplyingJob.getStartDate())
                                        < SystemConstants.MINIMUM_DAYS_BEFORE_JOB_START);
        //System.out.println("CurrentDate " + myCurrentDay.getTime() + " JobDate " + theApplyingJob.getStartDate().getTime());
        //System.out.println("Difference = " + getDifferenceInDays(myCurrentDay, theApplyingJob.getStartDate()));

        if (is_there_conflict) {
            returnInt = 1; //job was overlapping with another job on this day
        } else if (is_outside_timeframe) {
            returnInt = 2; //Volunteer tries to sign up for a job before minimum days before start date
        }

        return returnInt;
    }
    
    
    // private helpers
	private int getDifferenceInDays(GregorianCalendar theFirstDate,
										  GregorianCalendar theSecondDate) {
		long convertedTime = TimeUnit.DAYS.convert(theSecondDate.getTimeInMillis() , TimeUnit.MILLISECONDS)
				- TimeUnit.DAYS.convert(theFirstDate.getTimeInMillis(),  TimeUnit.MILLISECONDS);
		
		return Math.abs((int) convertedTime);
	}


}
