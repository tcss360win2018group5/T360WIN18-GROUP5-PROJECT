package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Volunteer extends User implements Serializable {
	/** The priority level for Volunteer type users. */
	private static final int PRIORITY_LEVEL = 2;

	/** The current jobs this Volunteer is signed up for. */
	private ArrayList<Job> myCurrentJobs = new ArrayList<Job>();

	private GregorianCalendar today = new GregorianCalendar(2018, 1, 1, 0, 0);
	
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
    public boolean addToCurrentJobs(final Job theApplyingJob) {
        boolean is_there_conflict = this.hasOverlap(theApplyingJob);
		boolean is_outside_timeframe = (JobCoordinator.getDifferenceInDays(today, theApplyingJob.getStartDate()) > 2);
        boolean is_job_added = false;

        if (!is_there_conflict || !is_outside_timeframe) {
            myCurrentJobs.add(theApplyingJob);
            is_job_added = true;
        }

        return is_job_added;
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

		public void setToday(GregorianCalendar theSetDate) {
			   this.today = theSetDate;
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


	public static void main(String[]args) {
//        Volunteer test = new Volunteer("Volunteer Test");
//
//        GregorianCalendar testDate = new GregorianCalendar();
//        testDate.set(2018, 02, 22);
//
//        System.out.println(testDate.get(Calendar.YEAR));
//        System.out.println(testDate.get(Calendar.MONTH));
//        System.out.println(testDate.get(Calendar.DATE));
//
//        test.myCurrentJobs.forEach(System.out::println);
//
//        Job job_test1 = new Job("Job Test1");
//        job_test1.setStartDate(new GregorianCalendar(2018,01,01));
//        job_test1.setEndDate(new GregorianCalendar(2018,01,01));
//        Job job_test2 = new Job("Job Test2");
//        job_test2.setStartDate(new GregorianCalendar(2018,01,02));
//        job_test2.setEndDate(new GregorianCalendar(2018,01,03));
//        Job job_test3 = new Job("Job Test3");
//        job_test3.setStartDate(new GregorianCalendar(2018,01,03));
//        job_test3.setEndDate(new GregorianCalendar(2018,01,03));
//
//
//        test.addToCurrentJobs(job_test2);
//        test.addToCurrentJobs(job_test3);
//
//
//        test.myCurrentJobs.forEach(job -> System.out.println(job.getJobTitle()));
    }
}
