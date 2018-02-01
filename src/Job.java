import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Job {
	
	/*
	 * Title for this job
	 */
	private String myJobTitle;
	
	/*
	 * Length of this job in days
	 */
	private int myJobLength;

	private GregorianCalendar myDate = new GregorianCalendar();
	
	private ArrayList<Volunteer> myVolunteers = new ArrayList<Volunteer>();
	
	/*
	 *  Variables for start and end dates
	 */
	// Go here
	
	protected Job(final String theJobTitle) {
		myJobTitle = theJobTitle;
		
	}
	
	/**
	 * Adds a volunteer to this job and let's the user know they were added.
	 * @param theVolunteer to be added to this job.
	 */
	protected void addVolunteer(final Volunteer theVolunteer) {
		myVolunteers.add(theVolunteer);
		System.out.println("Added " + theVolunteer + " to " + myJobTitle);
	}
	
	
	protected String getJobTitle() {
		return myJobTitle;
	}
	
	protected int getJobLength() {
		return myJobLength;
	}


	protected GregorianCalendar getDate() {
		return myDate;
	}

	protected void setDate(GregorianCalendar theDate) {
		myDate = theDate;
	}

}
