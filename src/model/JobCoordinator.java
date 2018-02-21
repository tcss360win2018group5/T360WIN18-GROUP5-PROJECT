
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
        myCurrentDate = new GregorianCalendar();
    }

    // mutators
    /**
     * Adds a job to the pending list of jobs.
     * Preconditions: 1) checkBusinessRules(theJob) == 0
     * @param theJob job to be added.
     */
    public void addPendingJob(final Job theJob) {
        myPendingJobList.add(theJob);
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
    @SuppressWarnings("unchecked")
    public ArrayList<Job> getPendingJobs() {
        return (ArrayList<Job>) myPendingJobList.clone();
    }

    public GregorianCalendar getCurrentDate() {
        return (GregorianCalendar) this.myCurrentDate.clone();
    }

    // testers

    public boolean hasSpaceToAddJobs() {
        return myPendingJobList.size() < SystemConstants.MAXIMUM_JOBS;
    }

    /**
     * Job to be submitted must follow the business rules
     * as outlined below.
     * 
     * @param theJob
     * @return the appropriate Integer depending on business rules.
     */
    public int checkBusinessRules(Job candidateJob, ParkManager theParkManager) {
		int businessRuleCheck = 0;
		if (theParkManager.doesJobAlreadyExist(candidateJob, myPendingJobList)) {
			businessRuleCheck = 1;
		}else if (theParkManager.isJobInPast(myCurrentDate, candidateJob.getStartDate())) {
			businessRuleCheck = 2;
		} else if (theParkManager.isTooFarFromToday(candidateJob)) {
			businessRuleCheck = 3;
		} else if (theParkManager.isLessJobsThanMaxInSystem(myPendingJobList)) {
			businessRuleCheck = 4;
		} else if (theParkManager.isMaximumJobDuration(candidateJob)) {
			businessRuleCheck = 5;
		}
		return businessRuleCheck;
    }

}
