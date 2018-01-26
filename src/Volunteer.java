import java.util.ArrayList;

public class Volunteer extends User {
	
	private static final int PRIORITY_LEVEL = 1;
	
	private ArrayList<Job> myCurrentJobs = new ArrayList<Job>();
	
	/**
	 * Constructs a volunteer who can sign up for jobs
	 * @param theName
	 */
	protected Volunteer(String theName) {
		super(theName, PRIORITY_LEVEL);
	}
	
	protected void addJob(final Job theJob) {
		myCurrentJobs.add(theJob);
		System.out.println("Added " + theJob.getJobTitle() + " to my pending jobs.");
	}
	
	protected ArrayList getCurrentJobs() {
		return myCurrentJobs;
	}
	
	
	
}
