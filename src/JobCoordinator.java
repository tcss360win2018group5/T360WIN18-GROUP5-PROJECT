import java.util.ArrayList;

import util.CalendarCalculator;

public class JobCoordinator {
	private final int MAXIMUM_JOBS = 20;
	private final int MAXIMUM_JOB_LENGTH = 3; // days
	private final int MAXIMUM_DAYS_FROM_TODAY = 75;
	
	private static ArrayList<Job> myPendingJobList = new ArrayList<Job>();
	private static ArrayList<Job> myFinishedJobList = new ArrayList<Job>();
	/**
	 * Adds a job to my list of jobs
	 * @param theJob that needs to be added
	 */
	public void addJob(final Job theJob) {
	    if (myPendingJobList.contains(theJob)) {
	        // warning, job already exists
	    } else if (myPendingJobList.size() >= MAXIMUM_JOBS) {
	        // warning, pending jobs at maximum
	    } else if (theJob.getJobLength() > MAXIMUM_JOB_LENGTH) {
	        // warning, job exceeds maximum job length
	    } else if (CalendarCalculator.getDifferenceInDays(SystemCoordinator.getCurrentDate(),
	                                                          theJob.getStartDate()) 
	                    > MAXIMUM_DAYS_FROM_TODAY) {
	        // warning, job is further than 75 days away
	    } else {
	        myPendingJobList.add(theJob);
	    }
	}
	
	public void applyToJob(Job theJob) {

	}
	
	
	
	/**
	 * I think this function may be tweaked - Jon
	 * @return the list of jobs
	 */
	@SuppressWarnings("unchecked")
    public ArrayList<Job> getPendingJobs() {
		return (ArrayList<Job>) myPendingJobList.clone();
	}
	
    @SuppressWarnings("unchecked")
    public ArrayList<Job> getFinshedJobs() {
        return (ArrayList<Job>) myFinishedJobList.clone();
    }


}
