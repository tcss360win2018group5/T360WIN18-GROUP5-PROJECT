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
	
	private int myMaxVolunteers;

	private ArrayList<Volunteer> myVolunteers = new ArrayList<Volunteer>();
	
	/*
	 *  Variables for start and end dates
	 */
	private GregorianCalendar myStartDate = new GregorianCalendar();

	private GregorianCalendar myEndDate = new GregorianCalendar();


	public Job(final String theJobTitle) {
		myJobTitle = theJobTitle;
		
	}

   
    // mutators
	/**
	 * Adds a volunteer to this job and let's the user know they were added.
	 * @param theVolunteer to be added to this job.
	 */
	public void addVolunteer(final Volunteer theVolunteer) {
	    if (myVolunteers.contains(theVolunteer)) {
	        // volunteer already applied
	    } else if (myVolunteers.size() >= this.myMaxVolunteers) {
	        // no more space to apply
	        // also need to add check for role-specific applying later
	    } else if (!theVolunteer.addToCurrentJobs(this)) {
	        // if there exists conflicts with volunteer's current jobs
	        // volunteer automatically gets added to job if there doesnt exist conflicts
	    }
		myVolunteers.add(theVolunteer);
		System.out.println("Added " + theVolunteer + " to " + myJobTitle);
	}

	public void setStartDate(GregorianCalendar theDate) {
	    this.myStartDate = theDate;
	}

	public void setEndDate(GregorianCalendar theDate) {
	    this.myEndDate = theDate;
	}
	
	public void setMaxVolunteers(int theMaxVolunteers) {
	    this.myMaxVolunteers = theMaxVolunteers;
	}
	
	// queries
    public String getJobTitle() {
        return this.myJobTitle;
    }
    
    public int getJobLength() {
        return this.myJobLength;
    }


    public GregorianCalendar getStartDate() {
        return (GregorianCalendar) this.myStartDate.clone();
    }

    public GregorianCalendar getEndDate() {  
        return (GregorianCalendar) this.myEndDate.clone();
    }
}
