import java.util.ArrayList;

public class JobCoordinator {
	
	private static ArrayList<Job> myJobList = new ArrayList<Job>();
	
	/**
	 * Adds a job to my list of jobs
	 * @param theJob that needs to be added
	 */
	private static void addJob(final Job theJob) {
		myJobList.add(theJob);
	}
	
	/**
	 * I think this function may be tweaked - Jon
	 * @return the list of jobs
	 */
	protected static ArrayList<Job> getJobs() {
		return myJobList;
	}


	protected void applyToJob(Job theJob) {

	}
}
