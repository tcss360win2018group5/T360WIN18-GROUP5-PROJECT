
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class Volunteer extends User implements Serializable {
    public static final int MINIMUM_DAYS_BEFORE_JOB_START = 2;

    /** The current jobs this Volunteer is signed up for. */
    private ArrayList<Job> myCurrentJobs = new ArrayList<Job>();

    private GregorianCalendar myCurrentDay;

    /**
     * Constructs a volunteer based on the specified name
     * 
     * @param theName the name to give the user.
     */
    public Volunteer(String theName) {
        super(theName, SystemCoordinator.VOLUTNEER_ACCESS_LEVEL);
    }

    // mutator's
    public void setCurrentDay(GregorianCalendar theCurrentDay) {
        myCurrentDay = theCurrentDay;
    }

    /**
     * Adds a current job to the volunteer's list of jobs.
     *
     * Preconditions: 1) Job must be valid. 2) Job must not overlap with any
     * currently applied to jobs.
     *
     * @return True if job was successfully added, false otherwise.
     */
    public void signUpForJob(final Job theJob) {
        myCurrentJobs.add(theJob);
    }

    public boolean doesJobStartOnCurrentDay(final Job theJob) {
        int is_difference_of_day_zero = getDifferenceInDaysAgainstToday(theJob.getStartDate());
        return is_difference_of_day_zero == 0;
    }

    public boolean doesMultiJobStartPriorToCurrentDay(final Job theJob) {
        int is_difference_of_day_one = getDifferenceInDaysAgainstToday(theJob.getStartDate());
        return is_difference_of_day_one == -1;
    }

    public boolean doesJobStartMoreThanMinDay(final Job theJob) {
        int is_difference_greater_than_min = getDifferenceInDaysAgainstToday(theJob.getStartDate());
        return is_difference_greater_than_min >= Volunteer.MINIMUM_DAYS_BEFORE_JOB_START;
    }

    /*
    000 - No Error
    001 - Job Starts @ Current Day
    010 - Job Starts Prior to current day
    100 - Job is less than Min days
    ??? - Any Combination of the error codes above
     */
    public int unvolunteerJob(final Job theJob) {
        int job_starts_on_current_day = doesJobStartOnCurrentDay(theJob) ? 1 : 0;
        int job_starts_prior_to_current_day = doesMultiJobStartPriorToCurrentDay(theJob) ? 10 : 0;
        int job_starts_more_than_min = doesJobStartMoreThanMinDay(theJob) ? 0 : 100;
        int total_error_code = job_starts_on_current_day + job_starts_prior_to_current_day + job_starts_more_than_min;
        System.out.println(job_starts_on_current_day);
        System.out.println(job_starts_prior_to_current_day);
        System.out.println(job_starts_more_than_min);
        if (total_error_code == 0) myCurrentJobs.remove(theJob);
        return total_error_code;
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
        boolean is_outside_timeframe = (getDifferenceInDays(myCurrentDay, theApplyingJob
                        .getStartDate()) < MINIMUM_DAYS_BEFORE_JOB_START);
        // System.out.println("CurrentDate " + myCurrentDay.getTime() + "
        // JobDate " + theApplyingJob.getStartDate().getTime());
        // System.out.println("Difference = " +
        // getDifferenceInDays(myCurrentDay, theApplyingJob.getStartDate()));

        if (is_there_conflict) {
            returnInt = 1; // job was overlapping with another job on this day
        }
        else if (is_outside_timeframe) {
            returnInt = 2; // Volunteer tries to sign up for a job before
                           // minimum days before start date
        }

        return returnInt;
    }

    // private helpers
    private int getDifferenceInDays(GregorianCalendar theFirstDate,
                                    GregorianCalendar theSecondDate) {
        long convertedTime = TimeUnit.DAYS.convert(theSecondDate.getTimeInMillis(),
                                                   TimeUnit.MILLISECONDS)
                             - TimeUnit.DAYS.convert(theFirstDate.getTimeInMillis(),
                                                     TimeUnit.MILLISECONDS);

        return Math.abs((int) convertedTime);
    }

    private int getDifferenceInDaysAgainstToday(GregorianCalendar jobDate) {
        // All jobs are init. with only year, month, day
        // Because of that, issues with time happen when it account for seconds
        // The (today) & (today) job can result showing a day difference
        // if seconds are accounted for (today)
        GregorianCalendar init_today = (GregorianCalendar) GregorianCalendar.getInstance();
        GregorianCalendar today = new GregorianCalendar(init_today.get(Calendar.YEAR),
                init_today.get(Calendar.MONTH),
                init_today.get(Calendar.DAY_OF_MONTH));
        long convertedTime = TimeUnit.DAYS.convert(jobDate.getTimeInMillis(),
                TimeUnit.MILLISECONDS)
                - TimeUnit.DAYS.convert(today.getTimeInMillis(),
                TimeUnit.MILLISECONDS);
        return (int) convertedTime;
    }


    public static void main(String[]arg) {
//        System.out.println(GregorianCalendar.getInstance().getTime());
//
//        Volunteer vol = new Volunteer("SomeOldVolunteer");
//
//        GregorianCalendar today = (GregorianCalendar) GregorianCalendar.getInstance();
//        Job job = new Job("Job Start 0102 End 0103");
//        job.setStartDate(new GregorianCalendar(2018, 01, 17));
//        job.setEndDate(new GregorianCalendar(2018, 01, 17));
//        vol.signUpForJob(job);
//
//        System.out.println(vol.unvolunteerJob(job));
//
////        System.out.println(getDifferenceInDays2((GregorianCalendar) GregorianCalendar.getInstance(),
////                job.getStartDate()));
    }

}
