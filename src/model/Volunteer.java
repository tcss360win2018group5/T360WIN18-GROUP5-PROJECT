
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Objects;

public final class Volunteer extends User implements Serializable {
    public static final int MINIMUM_DAYS_BEFORE_JOB_START = 3;

    /** The current jobs this Volunteer is signed up for. */
    private ArrayList<Job> myCurrentJobs;

    private GregorianCalendar myCurrentDate;

    /**
     * Constructs a volunteer based on the specified name
     * 
     * @param theName the name to give the user.
     */
    public Volunteer(String theName) {
        super(theName, SystemCoordinator.VOLUNTEER_ACCESS_LEVEL);
        this.myCurrentJobs = new ArrayList<Job>();
        this.myCurrentDate = new GregorianCalendar();
    }

    // mutator's
    public void setCurrentDate(GregorianCalendar theCurrentDay) {
        myCurrentDate = theCurrentDay;
    }

    /**
     * Adds a current job to the volunteer's list of jobs.
     *
     * Preconditions: 1) Job must be valid. 2) Job must not overlap with any
     * currently applied to jobs.
     *
     * @return True if job was successfully added, false otherwise.
     */
    public void applyToJob(final Job theJob) {
        myCurrentJobs.add(theJob);
    }

    /**
     * Allows the volunteer to unapply for a job.
     * 
     * PRECONDITION: canUnapplyFromJob(theJob) == 0
     */
    public void unapplyForJob(final Job theJob) {
        myCurrentJobs.remove(theJob);
    }

    // queries
    /**
     * Retrieves the current list of applied to jobs.
     */
    public ArrayList<Job> getCurrentJobs() {
        return (ArrayList<Job>) this.myCurrentJobs.clone();
    }
    
    public GregorianCalendar getCurrentDate() {
        return this.myCurrentDate;
    }

    // testers
    /**
     * Checks to see if the volunteer can apply to the specified job.
     * 
     * PRECONDITION: theApplyingJob.getStartDate() != NULL
     *               theApplyingJob.getEndDate() != NULL
     * @return 1 if there is an overlap of this job and a job the volunteer is currently
     *         applied to
     *         2 if the job is closer than the minimum number of days away
     *         0 otherwise
     */
    public int canApplyToJob(final Job theApplyingJob) {
        int returnInt = 0;
        boolean is_there_conflict = myCurrentJobs.stream()
                        .anyMatch(aJobFromList -> aJobFromList.hasOverlap(theApplyingJob));
        boolean is_outside_timeframe = (daysFromToday(theApplyingJob.getStartDate()) 
                        < MINIMUM_DAYS_BEFORE_JOB_START);
        if (is_there_conflict) {
            returnInt = 1; // job was overlapping with another job on this day
        }
        else if (is_outside_timeframe) {
            returnInt = 2; // Volunteer tries to sign up for a job before
                           // minimum days before start date
        }

        return returnInt;
    }
    
    /**
     * Chekcs to see if the user can unapply from the specified job.
     * 
     * PRECONDITION: theUnapplyingJob.getStartDate() != NULL
     *               theUnapplyingJob.getEndDate() != NULL
     * @return 1 if the volunteer is not currently applied to the job
     *         2 if the job is closer than the minimum days away
     *         0 otherwise
     */
    public int canUnapplyFromJob(final Job theUnapplyingJob) {
        int returnInt = 0;
        boolean is_not_currently_applied = !myCurrentJobs.contains(theUnapplyingJob);
        boolean is_outside_timeframe = daysFromToday(theUnapplyingJob.getStartDate()) 
                        < MINIMUM_DAYS_BEFORE_JOB_START;
        
        if (is_not_currently_applied) {
            returnInt = 1;
        } else if (is_outside_timeframe) {
            returnInt = 2;
        }
        
        return returnInt;
    }

    // helpers
    /** Calculates the days the jobDate is from myCurrentDate */
    public int daysFromToday(GregorianCalendar jobDate) {
        return JobCoordinator.getDifferenceInDays(myCurrentDate, jobDate);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object clone() {
        Volunteer cloneVolunteer = new Volunteer(this.getUsername());
        cloneVolunteer.myCurrentDate = (GregorianCalendar) this.myCurrentDate.clone();
        cloneVolunteer.myCurrentJobs = (ArrayList<Job>) this.myCurrentJobs.clone();
        return cloneVolunteer;
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
            Volunteer theOtherVol = (Volunteer) theObject;
            result = super.equals(theObject) && 
                            this.myCurrentDate == theOtherVol.myCurrentDate;
        }
        
        return result;
    }

}
