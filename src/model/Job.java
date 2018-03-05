
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * Container for all job entries to hold pertinent information and provide the ability to 
 * compare against other jobs to determine if there is overlap, as well as whether or not 
 * the job is in the future.
 *
 */
@SuppressWarnings("serial")
public final class Job implements Serializable, Cloneable {
	/** String representation of the title for this job. */
	private String myJobTitle;

	/** Maximum amount of volunteers for this job. */
	private int myMaxVolunteers;

	/** List for the current volunteers applied to work on this job. */
	private ArrayList<Volunteer> myVolunteers = new ArrayList<Volunteer>();

	/** Calendar for start date. */
	private GregorianCalendar myStartDate;

	/** Calendar for end date. */
	private GregorianCalendar myEndDate;
	
	/** Location for the job. */
	private String myJobLocation = "";
	
	/** Contact name (Park Manager or otherwise). */
	private String myContactName = "";
	
	/** Contact number. */
	private String myContactNumber = "";
	
	/** Contact email. */
	private String myContactEmail = "";
	
	/** Difficulty of the job on a scale from 0 to 3. */
	private int myJobDifficulty = 0;
	
	/** Description of the overall job. */
	private String myJobDescription = "";
	
	/** Job role short description. */
	private String myJobRole = "";
	
	/** Job role long description. */
	private String myJobRoleDescription = "";
	
	/**
	 * Creates a job given the specified information.
	 * 
	 * @param theJobTitle Title/Headline of the job.
	 * @param theJobLength The length of the job in days.
	 * @param theMaxVolunteers The maximum amount of volunteers on a job.
	 * @param theStartDate The starting date of the job as a calendar.
	 * @param theEndDate The ending date of a job as a calendar.
	 */
	public Job(final String theJobTitle, int theMaxVolunteers,
			GregorianCalendar theStartDate, GregorianCalendar theEndDate) {
		this.myJobTitle = theJobTitle;
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
		this(theJobTitle, -1, new GregorianCalendar(2018, 01, 01),
				new GregorianCalendar(2018, 01, 01));
	}

	// mutators
	/**
	 * Adds a volunteer to this job.
	 * 
	 * PRECONDITIONS: this.canAcceptVolunteers() == true
	 *                theVolunteer.canApplyTo(this) == 0
	 *               
	 * @param theVolunteer to be added to this job.
	 */
	public void addVolunteer(final Volunteer theVolunteer) {
		if (!myVolunteers.contains(theVolunteer)) {
	        myVolunteers.add(theVolunteer);
		}
	}
	
	/**
     * Removes a volunteer from this job.
     * 
     * PRECONDITIONS: theVolunteer.canUnapplyFromJob(this) == 0
     * 
     * @param theVolunteer to be added to this job.
     */
	public void removeVolunteer(final Volunteer theVolunteer) {
	    if (myVolunteers.contains(theVolunteer)) {
	        myVolunteers.remove(theVolunteer);
	    }
	}

	// **Tests for business rules**
	/**
	 * Checks to see if this job begins on or after the specified date OR if it 
	 * ends on or after the specified date.
	 * 
	 */
   public boolean isFutureJob(GregorianCalendar theDateToday) {
        return JobCoordinator.getDifferenceInDays(theDateToday, this.getStartDate()) >= 0 ||
                    JobCoordinator.getDifferenceInDays(theDateToday, this.getEndDate()) >= 0;
    }
	   
	/**
	 * Checks to see if there is still space available for volunteers to be
	 * added.
	 * 
	 * @return True if space exists, false otherwise.
	 */
	public boolean canAcceptVolunteers() {
		return myVolunteers.size() < this.myMaxVolunteers;
	}

	/**
	 * Checks to see if this current job's timeframe overlaps in any way with the timeframe
	 * of the specified job.
	 * 
	 * PRECONDITIONS: this.myStartDate != NULL
	 *                this.myEndDate != NULL
	 *                theOtherJob.myStartDate != NULL
	 *                theOtherJob.myEndDate != NULL
	 * 
	 * @return true if there is overlap at any point in the timeframes, false otherwise.
	 */
	public boolean hasOverlap(Job theOtherJob) {
		return this.myStartDate.compareTo(theOtherJob.myStartDate) == 0
				// if listJob starts on the same day that applyingJob ends
				|| this.myStartDate.compareTo(theOtherJob.myEndDate) == 0
				// if listJob ends on the same day applyingJob starts
				|| this.myEndDate.compareTo(theOtherJob.myStartDate) == 0
				// if listJob overlaps on the left with applyingJob
				|| (this.myStartDate.compareTo(theOtherJob.myStartDate) < 0
						&& this.myEndDate.compareTo(theOtherJob.myStartDate) >= 0)
				// if listJob overlaps on the right with applyingJob
				|| (this.myEndDate.compareTo(theOtherJob.myEndDate) > 0
						&& this.myStartDate.compareTo(theOtherJob.myEndDate) <= 0)
				// if listJob overlaps inside applyingJob
				|| (this.myStartDate.compareTo(theOtherJob.myStartDate) > 0
						&& (this.myStartDate.compareTo(theOtherJob.myEndDate) < 0));
	}

	/*
	 * Queries
	 */
	/** Provides the difficult of the job based on numeric value and translates that as:
	 *     0 = "Easy"
	 *     1 = "Medium"
	 *     3 = "Hard"
	 */
	public String getMyDifficulty() {
		String retString = "invalid";
		if (myJobDifficulty == 0) {
			retString = "Easy";
		}
		else if (myJobDifficulty == 1) {
			retString = "Medium";
		}
		else if (myJobDifficulty == 3) {
			retString = "Hard";
		}
		return retString;
	}

	public void setMyDifficulty(int myDifficulty) {
		this.myJobDifficulty = myDifficulty;
	}
	
	public String getJobTitle() {
		return this.myJobTitle;
	}

	public int getJobLength() {
		return getDifferenceInDays(this.myEndDate);
	}
	
	public void setStartDate(GregorianCalendar theDate) {
		this.myStartDate = theDate;
	}
	
	public GregorianCalendar getStartDate() {
		return this.myStartDate;
	}

	public GregorianCalendar getEndDate() {
		return this.myEndDate;
	}

	public int getMaxVolunteers() {
		return this.myMaxVolunteers;
	}

	public int getCurrentNumberOfVolunteers() {
		return myVolunteers.size();
	}

	public ArrayList<Volunteer> getCurrentVolunteers() {
		return this.myVolunteers;
	}
	
	public void setEndDate(GregorianCalendar theDate) {
		this.myEndDate = theDate;
	}

	public void setMaxVolunteers(int theMaxVolunteers) {
		this.myMaxVolunteers = theMaxVolunteers;
	}
	public String getMyJobLocation() {
		return myJobLocation;
	}

	public void setMyJobLocation(String theJobLocation) {
		this.myJobLocation = theJobLocation;
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

	public void setMyJobDifficulty(int myJobDifficulty) {
		this.myJobDifficulty = myJobDifficulty;
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
	
	/**
     * Calculates the difference in days relative to the day this job STARTS.
     * 
     * PRECONDITIONS: this.myStartDate != NULL
     *                theDate != NULL
     */
    public int getDifferenceInDays(GregorianCalendar theDate) {
        return JobCoordinator.getDifferenceInDays(myStartDate, theDate);
    }
    
	/* Java object implementation methods. */
	
	/** 
	 * Allows for sensitive instances of the object to be cloned. 
	 */
	@SuppressWarnings("unchecked")
    @Override
	public Job clone() {
	    Job jobClone = new Job(this.myJobTitle, this.myMaxVolunteers,
	                           (GregorianCalendar) this.myStartDate.clone(), 
	                           (GregorianCalendar) this.myEndDate.clone());
	       
	    jobClone.myVolunteers = (ArrayList<Volunteer>) this.myVolunteers.clone();
	    jobClone.myJobLocation = this.myJobLocation;
	    jobClone.myContactName = this.myContactName;
	    jobClone.myContactNumber = this.myContactNumber;
	    jobClone.myContactEmail = this.myContactEmail;
	    jobClone.myJobDifficulty = this.myJobDifficulty;
	    jobClone.myJobDescription = this.myJobDescription;
	    jobClone.myJobRole = this.myJobRole;
	    jobClone.myJobRoleDescription = this.myJobRoleDescription;
	    
	    return jobClone;
	}
	    
	/** 
	 * Simple implementation and override of equals method for checking pure equality between
	 * objects.
	 */
	@Override
	public boolean equals(Object theObject) {
	    boolean result = false;
	    if (this == theObject) {
	        result = true;
	    } else if (theObject == null) {
	        result = false;
	    } else if (this.getClass() == theObject.getClass()) {
	        Job theOtherJob = (Job) theObject;
	        
	        result = Objects.equals(this.myJobTitle, theOtherJob.myJobTitle)
                        && Objects.equals(this.myMaxVolunteers, theOtherJob.myMaxVolunteers)
                        && Objects.equals(this.myStartDate, theOtherJob.myStartDate)
                        && Objects.equals(this.myEndDate, theOtherJob.myEndDate)
                        && Objects.equals(this.myJobLocation, theOtherJob.myJobLocation)
                        && Objects.equals(this.myContactName, theOtherJob.myContactName)
                        && Objects.equals(this.myContactNumber, theOtherJob.myContactNumber)
                        && Objects.equals(this.myContactEmail, theOtherJob.myContactEmail)
                        && Objects.equals(this.myJobDifficulty, theOtherJob.myJobDifficulty)
                        && Objects.equals(this.myJobDescription, theOtherJob.myJobDescription)
                        && Objects.equals(this.myJobRole, theOtherJob.myJobRole)
                        && Objects.equals(this.myJobRoleDescription, theOtherJob.myJobRoleDescription);
	    }
	
        return result;
	}

	/** 
	 * For mapping purposes, a proper hash of this object is available.
	 */
    @Override
    public int hashCode() {
        return Objects.hash(this.myJobTitle, this.myMaxVolunteers, this.myStartDate, 
                            this.myEndDate, this.myJobLocation,
                            this.myContactName, this.myContactNumber, this.myContactEmail,
                            this.myJobDifficulty, this.myJobDescription, this.myJobRole,
                            this.myJobRoleDescription);
    }
}
