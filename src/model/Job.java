
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class Job implements Serializable {

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
    private String myJobLocation = "";
    private String myContactName = "";
    private String myContactNumber = "";
    private String myContactEmail = "";
    private int myJobDifficulty = 0;
    private String myAddress = "";
    private String myJobDescription = "";
    private String myJobRole = "";
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
<<<<<<< HEAD
        this.myJobLength = getDifferenceInDays(theStartDate, theEndDate);
=======
>>>>>>> ac2a9e2e65684843f07de876c5556c5906191c1f
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

    /**
     * Creates a completely empty, invalid job.
     */
    public Job() {
        this("", -1, null, null);
    }

    // mutators
    /**
     * Adds a volunteer to this job and lets the user know they were added.
     * 
     * Preconditons: 1) Volunteer must not already be applied to this job. 2)
     * There must be space for on the job for the volunteer to apply 3) There
     * must exist no conflicts with any of the volunteer's current jobs.
     * 
     * @param theVolunteer to be added to this job.
     */
    public void addVolunteer(final Volunteer theVolunteer) {
        if (myVolunteers.contains(theVolunteer)) {
            // volunteer already applied
        }
        else if (theVolunteer.canSignUpForJob(this) != 0) {
            // if there exists conflicts with volunteer's current jobs
            // volunteer automatically gets added to job if there doesnt exist
            // conflicts
        }
        myVolunteers.add(theVolunteer);
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
     * 
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
        return getDifferenceInDays(this.myStartDate, this.myEndDate);
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
     * Retrieves the maximum number of volunteers for this job
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
     * Checks to see if there is still space available for volunteers to be
     * added.
     * 
     * @return True if space exists, false otherwise.
     */
    public boolean canAcceptVolunteers() {
        return myVolunteers.size() < this.myMaxVolunteers;
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
    
    // private methods
    /**
     * Helper method to calculate the difference in days of two calendar dates.
     *
     * @param theFirstDate The first date chronologically.
     * @param theSecondDate The second date chronologically.
     *
     * @return The positive difference in days.
     */
    public static int getDifferenceInDays(GregorianCalendar theFirstDate,
                                          GregorianCalendar theSecondDate) {
        long convertedTime = TimeUnit.DAYS.convert(theSecondDate.getTimeInMillis(),
                                                   TimeUnit.MILLISECONDS)
                             - TimeUnit.DAYS.convert(theFirstDate.getTimeInMillis(),
                                                     TimeUnit.MILLISECONDS);

        return Math.abs((int) convertedTime);
    }
    
    @Override
    public boolean equals(Object theObject) {
    	boolean isEqual = false;
    	if (this == theObject) {
    		isEqual = true;
    	} else {
    		if (theObject != null && theObject.getClass() == this.getClass()) {
    			Job test = (Job) theObject;
    			if (test.getJobTitle().equals(this.getJobTitle())
    				&& getDifferenceInDays(test.getStartDate(), this.getStartDate()) == 0
    				&& getDifferenceInDays(test.getEndDate(), this.getEndDate()) == 0) 
    			{
    				isEqual = true;
    			}
    		}
    	}
    	return isEqual;
    }
    
    
    
}
