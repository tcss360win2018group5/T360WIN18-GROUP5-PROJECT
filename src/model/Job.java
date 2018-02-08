package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Job implements Serializable {
	
	/** String representation of the title for this job. */
	private String myJobTitle;
	
	/** Length of this job in days. */
	private int myJobLength;
	
	/** Maximum amount of volunteers for this job. */
	private int myMaxVolunteers;

	/** List for the current volunteers applied to work on this job. */
	private ArrayList<Volunteer> myVolunteers = new ArrayList<Volunteer>();
	
	/** Calendar for start date. */
	private GregorianCalendar myStartDate;
	
    /** Calendar for end date. */
	private GregorianCalendar myEndDate;

	private String myJobLocation = "";

	private String myContactName = "";

	private String myContactNumber = "";

	private String myContactEmail = "" ;
	private int myJobDifficulty = 0;
	private String myAddress = "";
	private String myJobDescription = "";
	private String myJobRole = "";
	private String myJobRoleDescription = "";
	private int myDifficulty = 0;


	/**
	 * Creates a job given the specified information.
	 * 
	 * @param theJobTitle Title/Headline of the job.
	 * @param theJobLength The length of the job in days.
	 * @param theMaxVolunteers The maximum amount of volunteers on a job.
	 * @param theStartDate The starting date of the job as a calendar.
	 * @param theEndDate The ending date of a job as a calendar.
	 */
	public Job(final String theJobTitle, int theJobLength, int theMaxVolunteers, 
	           GregorianCalendar theStartDate, GregorianCalendar theEndDate) {
	    this.myJobTitle = theJobTitle;
	    this.myJobLength = theJobLength;
	    this.myMaxVolunteers = theMaxVolunteers;
	    this.myStartDate = theStartDate;
	    this.myEndDate = theEndDate;
	}

	 /**
     * Creates a job with the specified title and invalid other fields.
     * 
     * @param theJobTitle the title of the job.
     */
    public Job(final String theJobTitle) {
        // Need dates that doesn't have null pointers to test the UI
        this(theJobTitle, -1, -1,
                new GregorianCalendar(2018,01,01),
                new GregorianCalendar(2018,01,01));
    } 
    
    /**
     * Creates a completely empty, invalid job.
     */
    public Job() {
        this("", -1, -1, null, null);
    }
   
    // mutators
	/**
	 * Adds a volunteer to this job and lets the user know they were added.
	 * 
	 * Preconditons:
	 *     1) Volunteer must not already be applied to this job.
	 *     2) There must be space for on the job for the volunteer to apply
	 *     3) There must exist no conflicts with any of the volunteer's current jobs.
	 * @param theVolunteer to be added to this job.
	 */
	public void addVolunteer(final Volunteer theVolunteer) {
	    if (myVolunteers.contains(theVolunteer)) {
	        // volunteer already applied
	    } else if (!theVolunteer.addToCurrentJobs(this)) {
	        // if there exists conflicts with volunteer's current jobs
	        // volunteer automatically gets added to job if there doesnt exist conflicts
	    }
		myVolunteers.add(theVolunteer);
		// should probably be a UI feature
		System.out.println("Added " + theVolunteer + " to " + myJobTitle);
	}

	public void setStartDate(GregorianCalendar theDate) {
	    this.myStartDate = theDate;
	}

	/**
	 * Sets the end date of the job to the specified date.
	 * 
	 * @param theDate
	 */
	public void setEndDate(GregorianCalendar theDate) {
	    this.myEndDate = theDate;
	}
	
	/**
	 * Allows the 
	 * @param theMaxVolunteers
	 */
	public void setMaxVolunteers(int theMaxVolunteers) {
	    this.myMaxVolunteers = theMaxVolunteers;
	}
	
    // queries
    /**
     * Retrieves the title of this job.
     * 
     * @return The title of this job as a String.
     */
	public String getJobTitle() {
        return this.myJobTitle;
    }
    
    /**
     * Retrieves the length of the job in days.
     *
     */
    public int getJobLength() {
        return this.myJobLength;
    }

    /**
     * Retrieves the calendar representation of the start date for this job.
     * 
     * @return Calendar object representing the start date.
     */
    public GregorianCalendar getStartDate() {
    	// Can't access when it's return as a clone()
        return (GregorianCalendar) this.myStartDate;
    }

    /**
     * Retrieves the calendar representation of the end date for this job.
     * 
     * @return Calendar object representing the end date.
     */
    public GregorianCalendar getEndDate() {
		// Can't access when it's return as a clone()
		return (GregorianCalendar) this.myEndDate;
    }
    
    /**
     *  Retrieves the maximum number of volunteers for this job
     */
    public int getMaxVolunteers() {
        return this.myMaxVolunteers;
    }
    
    /**
     * Retrieves the current number of volunteers applied to work on this job.
     */
    public int getCurrentNumberOfVolunteers() {
        return myVolunteers.size();
    }
    
    /**
     * Retrieves the list of current volunteers on this job.
     * 
     * @return The List of current volunteers.
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Volunteer> getCurrentVolunteers() {
        return (ArrayList<Volunteer>) this.myVolunteers.clone();
    }
    
    // testers
    
    /**
     * Checks to see if there is still space available for volunteers to be added.
     * 
     * @return True if space exists, false otherwise.
     */
    public boolean canAcceptVolunteers() {
        return myVolunteers.size() < this.myMaxVolunteers;
    }

	public String getMyJobLocation() {
		return myJobLocation;
	}

	public void setMyJobLocation(String myJobLocation) {
		this.myJobLocation = myJobLocation;
	}

	public String getMyContactName() {
		return myContactName;
	}

	public void setMyContactName(String myContactName) {
		this.myContactName = myContactName;
	}

	public String getMyContactNumber() {
		return myContactNumber;
	}

	public void setMyContactNumber(String myContactNumber) {
		this.myContactNumber = myContactNumber;
	}

	public String getMyContactEmail() {
		return myContactEmail;
	}

	public void setMyContactEmail(String myContactEmail) {
		this.myContactEmail = myContactEmail;
	}

	public int getMyJobDifficulty() {
		return myJobDifficulty;
	}

	public void setMyJobDifficulty(int myJobDifficulty) {
		this.myJobDifficulty = myJobDifficulty;
	}

    public String getMyAddress() {
        return myAddress;
    }

    public void setMyAddress(String myAddress) {
        this.myAddress = myAddress;
    }

    public String getMyJobDescription() {
        return myJobDescription;
    }

    public void setMyJobDescription(String myJobDescription) {
        this.myJobDescription = myJobDescription;
    }

    public String getMyJobRole() {
        return myJobRole;
    }

    public void setMyJobRole(String myJobRole) {
        this.myJobRole = myJobRole;
    }

    public String getMyJobRoleDescription() {
        return myJobRoleDescription;
    }

    public void setMyJobRoleDescription(String myJobRoleDescription) {
        this.myJobRoleDescription = myJobRoleDescription;
    }

    public int getMyDifficulty() {
        return myDifficulty;
    }

    public void setMyDifficulty(int myDifficulty) {
        this.myDifficulty = myDifficulty;
    }
}
