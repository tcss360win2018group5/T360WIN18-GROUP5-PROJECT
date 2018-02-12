package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import util.SystemConstants;

public class JobCoordinator implements Serializable {
    /** The current list of pending jobs. */
    private final ArrayList<Job> myPendingJobList;

    /** The current date as a calendar. */
    private GregorianCalendar myCurrentDate;
    
    
    /**
     * Creates a new instance with empty job lists and the current date.
     */
    public JobCoordinator() {
        myPendingJobList = new ArrayList<Job>();
        myCurrentDate = new GregorianCalendar(2018, 2, 12);
    }

    // mutators
    /**
     * Adds a job to the pending list of jobs.
     *
     * Preconditions: 
     *  1) canAddJob(theJob) == 0 has evaluated to TRUE
     *  2) hasSpaceToAddNewJob() has evaluated to TRUE
     *
     * @param theJob job to be added.
     */
    public void addPendingJob(final Job theJob) {
            myPendingJobList.add(theJob);
    }

    /**
     * Adds a specified user to a job.
     *    
     * @param theJob
     */
    public void applyToJob(User theUser, Job theJob) {
        if (theUser.getAccessLevel() != 1) {
            // warning, user applying is not a volunteer
        }
        Volunteer theVolunteer = (Volunteer) theUser;
        
        if (theJob.canAcceptVolunteers() && theVolunteer.canSignUpForJob(theJob) == 0) {
            theJob.addVolunteer(theVolunteer);
            theVolunteer.signUpForJob(theJob);
        }
        
        
    }

    //queries
    @SuppressWarnings("unchecked")
    public ArrayList<Job> getPendingJobs() {
        return (ArrayList<Job>) myPendingJobList.clone();
    }
    
    public GregorianCalendar getCurrentDate() {
        return (GregorianCalendar) this.myCurrentDate.clone();
    }


    // testers
    /**
     * Checks to see if a job can be currently added to the pending jobs list.
     *
     * @return True if there is space, false otherwise.
     */
    public boolean hasSpaceToAddJobs() {
        return myPendingJobList.size() < SystemConstants.MAXIMUM_JOBS;
    }
    
    /**
     * Job to be submitted must:
     *      1) Not currently exist
     *      2) Be 3 or less days in length
     *      3) be no more than 75 days from the current date of submission
     *      
     * @param theJob
     * @return
     */
    public int canAddJob(Job theJob) {
        int returnInt = 0;
        if (myPendingJobList.contains(theJob)) {
            // warning, job already exists
            returnInt = 1;
        } else if (theJob.getJobLength() > SystemConstants.MAXIMUM_JOB_LENGTH) {
            // warning, job exceeds maximum job length
            returnInt = 2;
        } else if (getDifferenceInDays(this.myCurrentDate, theJob.getEndDate())
                        > SystemConstants.MAXIMUM_DAYS_AWAY_TO_POST_JOB) {
            returnInt = 3;
            // warning, job is further than 75 days away
        } 
        
        return returnInt;
    }


    // private methods
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
        long convertedTime = TimeUnit.DAYS.convert(theSecondDate.getTimeInMillis() , TimeUnit.MILLISECONDS)
                        - TimeUnit.DAYS.convert(theFirstDate.getTimeInMillis(),  TimeUnit.MILLISECONDS);
        
        return Math.abs( (int) convertedTime);
    }
}
