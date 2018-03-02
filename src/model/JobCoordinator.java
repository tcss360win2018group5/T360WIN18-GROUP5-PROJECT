
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class JobCoordinator implements Serializable {
    public static final int MAXIMUM_JOBS = 20;
    public static final int MAXIMUM_JOB_LENGTH = 3;
    public static final int MAXIMUM_DAYS_AWAY_TO_POST_JOB = 75;
    
    /** The current list of pending jobs. */
    private final ArrayList<Job> myJobList;

    /** The current date as a calendar. */
    private GregorianCalendar myCurrentDate;

    /**
     * Creates a new instance with empty job lists and the current date.
     */
    public JobCoordinator() {
        myJobList = new ArrayList<Job>();
        myCurrentDate = new GregorianCalendar();
    }

    // mutators
    /**
     * Adds a job to the pending list of jobs.
     * Preconditions: 1) checkBusinessRules(theJob) == 0
     * @param theJob job to be added.
     */
    public void addPendingJob(final Job theJob) {
        myJobList.add(theJob);
    }

    /**
     * Adds a specified user to a job.
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

    // queries
    /* DEPRECATED, PLEASE USE getJobListing(User) */
    @SuppressWarnings("unchecked")
    public ArrayList<Job> getPendingJobs() {
        return (ArrayList<Job>) myJobList.clone();
    }
    
    /**
     * Job Query for determining specific jobs to make visible to the user who is requesting
     * the job list. 
     * 
     * Precondition: theUser is a user of subclass Volunteer, ParkManager, or OfficeStaff
     * 
     * @return a job listing that shows the relevant jobs to the specific type of user 
     * as follows:
     *      For Volunteer: A list of jobs that they can currently sign up for which excludes
     *                     any jobs in the past, or jobs that overlap with currently signed
     *                     up for jobs.
     *      For ParkManager: A list of current and upcoming jobs that could potentially
     *                       conflict with a new job submission.
     *      For OfficeStaff: All jobs will be listed. 
     */
    public ArrayList<Job> getJobListing(User theUser) {
        ArrayList<Job> theModifiedList = new ArrayList<Job>();
        if (theUser instanceof Volunteer) {
            Volunteer theVolunteer = (Volunteer) theUser;
            for (Job aJob : this.myJobList) {
                if (theVolunteer.canSignUpForJob(aJob) == 0 &&
                                aJob.canAcceptVolunteers()) {
                    theModifiedList.add(aJob);
                }
            }
        } else if (theUser instanceof ParkManager) {
            ParkManager thePM = (ParkManager) theUser;
            for (Job aJob : this.myJobList) {
                if (thePM.isFutureJob(aJob)) {
                    theModifiedList.add(aJob);
                }
            }
        } else if (theUser instanceof OfficeStaff) {
            theModifiedList = (ArrayList<Job>) this.myJobList.clone();
        }
        
        return theModifiedList;
    }

    public GregorianCalendar getCurrentDate() {
        return (GregorianCalendar) this.myCurrentDate.clone();
    }

    // testers

    public boolean hasSpaceToAddJobs() {
        return myJobList.size() < JobCoordinator.MAXIMUM_JOBS;
    }

    /**
     * Job to be submitted must follow the business rules
     * as outlined below.
     * 
     * @param theJob
     * @return the appropriate Integer depending on business rules.
     */
    public int checkIfLegalToAddJob(Job candidateJob, ParkManager theParkManager) {
		int businessRuleCheck = 0;
		if (theParkManager.doesJobAlreadyExist(candidateJob, myJobList)) {
			businessRuleCheck = 1;
		}else if (theParkManager.isJobInPast(myCurrentDate, candidateJob.getStartDate())) {
			businessRuleCheck = 2;
		} else if (theParkManager.isTooFarFromToday(candidateJob)) {
			businessRuleCheck = 3;
		} else if (theParkManager.isLessJobsThanMaxInSystem(myJobList)) {
			businessRuleCheck = 4;
		} else if (theParkManager.isMaximumJobDuration(candidateJob)) {
			businessRuleCheck = 5;
		}
		return businessRuleCheck;
    }


    public int canAddJob(Job theJob) {
        int returnInt = 0;
        if (myJobList.contains(theJob)) {
            // warning, job already exists
            returnInt = 1;
        }
        else if (theJob.getJobLength() > JobCoordinator.MAXIMUM_JOB_LENGTH) {
            // warning, job exceeds maximum job length
            returnInt = 2;
        }
        else if (getDifferenceInDays(this.myCurrentDate, theJob
                        .getEndDate()) > JobCoordinator.MAXIMUM_DAYS_AWAY_TO_POST_JOB) {
            returnInt = 3;
            // warning, job is further than 75 days away
        }

        return returnInt;
    }

    // helper methods
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
}
