package model;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class JobCoordinator {
    /** The default maximum number of pending jobs at any given time. */
    private final int MAXIMUM_JOBS = 20;
    
    /** The default maximum job length in days. */
    private final int MAXIMUM_JOB_LENGTH = 3;
    
    /** 
     * The default maximum number of days away 
     * from the current date a new pending job can be. 
     */
    private final int MAXIMUM_DAYS_FROM_TODAY = 75;
    
    /** The current list of pending jobs. */
    private final ArrayList<Job> myPendingJobList;
    
    /** The current list of finished jobs. */
    private final ArrayList<Job> myFinishedJobList;
    
    /** The current date as a calendar. */
    private final GregorianCalendar myCurrentDate;

    /**
     * Creates a new instance with empty job lists and the current date.
     */
    public JobCoordinator() {
        myPendingJobList = new ArrayList<Job>();
        myFinishedJobList = new ArrayList<Job>();
        myCurrentDate = new GregorianCalendar();
    }
    
    // mutators
    /**
     * Adds a job to the pending list of jobs.
     * 
     * Precondition: Job to be submitted must: 
     *      1) Not currently exist
     *      2) Be 3 or less days in length
     *      3) be no more than 75 days from the current date of submission
     *      
     * @param theJob job to be added.
     */
    public void addPendingJob(final Job theJob) {
        if (myPendingJobList.contains(theJob)) {
            // warning, job already exists
        } else if (theJob.getJobLength() > MAXIMUM_JOB_LENGTH) {
            // warning, job exceeds maximum job length
        } else if (getDifferenceInDays(this.myCurrentDate, theJob.getEndDate()) 
                        > MAXIMUM_DAYS_FROM_TODAY) {
            // warning, job is further than 75 days away
        } else {
            myPendingJobList.add(theJob);
        }
    }
    /** Adds a job to the finished jobs list.
     * 
     *  Preconditon: Job to be added must not end before the current date.
     *  
     *  @param theJob job to be added.
     */
    public void addFinishedJob(final Job theJob) {
        if (this.myCurrentDate.compareTo(theJob.getEndDate()) < 0) {
            // impossible for job to be finished, warning
        } else {
            myFinishedJobList.add(theJob);
        }
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
        theJob.addVolunteer((Volunteer) theUser);
    }
    
    
    //queries
    /**
     * Gets the current list of pending jobs.
     * 
     * I think this function may be tweaked - Jon
     * @return the list of pending jobs.
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Job> getPendingJobs() {
        return (ArrayList<Job>) myPendingJobList.clone();
    }
    
    
    /**
     * Gets the current list of finished jobs.
     * 
     * @return the list of finished jobs.
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Job> getFinshedJobs() {
        return (ArrayList<Job>) myFinishedJobList.clone();
    }
    
    /**
     *  Gets the current date in calendar form.
     */
    public GregorianCalendar getCurrentDate() {
        return (GregorianCalendar) this.myCurrentDate.clone();
    }
    
    
    // testers
    /**
     * Checks to see if a job can be currently added to the pending jobs list.
     * 
     * @return True if there is space, false otherwise.
     */
    public boolean hasSpaceToAddNewJob() {
        return myPendingJobList.size() < MAXIMUM_JOBS;
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
    private int getDifferenceInDays(GregorianCalendar theFirstDate, 
                                          GregorianCalendar theSecondDate) {
        long convertedTime = TimeUnit.DAYS.convert(theSecondDate.getTimeInMillis() , TimeUnit.MILLISECONDS)
                        - TimeUnit.DAYS.convert(theFirstDate.getTimeInMillis(),  TimeUnit.MILLISECONDS);
        return Math.abs( (int) convertedTime);
    } 
}
