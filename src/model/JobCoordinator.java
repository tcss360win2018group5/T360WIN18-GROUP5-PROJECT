
package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public final class JobCoordinator implements Serializable {
    public static final int DEFAULT_MAXIMUM_JOBS = 10;
    public static final int MAXIMUM_JOB_LENGTH = 4;
    public static final int MAXIMUM_DAYS_AWAY_TO_POST_JOB = 60;
    
    
    /** To allow listening of updates via observer design pattern. */
    private final PropertyChangeSupport myPropertyChangeHandler;
    
    private int myCurrentMaximumJobs;
    
    /** The current list of pending jobs. */
    private final ArrayList<Job> myJobList;
    
    private final SystemCoordinator mySystem;

    /** The current date as a calendar. */
    private GregorianCalendar myCurrentDate;

    /**
     * Creates a new instance with empty job lists and the current date.
     */
    public JobCoordinator(SystemCoordinator theSystem) {
        this.myCurrentMaximumJobs = DEFAULT_MAXIMUM_JOBS;
        this.myPropertyChangeHandler = new PropertyChangeSupport(this);
        this.myJobList = new ArrayList<Job>();
        this.myCurrentDate = new GregorianCalendar();
        this.mySystem = theSystem;
    }
    
    /** Method to allow attachment of listeners to the instance of this object. */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        myPropertyChangeHandler.addPropertyChangeListener(listener);
    }
    
    
    /* mutators */
    
    public void setMaximumJobsInSystem(final User theUser, final int theMax) {
        if (theUser.getAccessLevel() != SystemCoordinator.OFFICE_STAFF_ACCESS_LEVEL 
                        || !(theUser instanceof OfficeStaff)) {
            throw new IllegalArgumentException();
        }
        
        this.myCurrentMaximumJobs = theMax;
        myPropertyChangeHandler.firePropertyChange(SystemEvents.MAX_JOBS_CHANGE.name(), 
                                                   null, null);
    }
    
    /**
     * Submits a job to the system.
     * 
     * PRECONDITION: canSubmitJob(theJob) == 0
     *               hasSpaceToAddJob() == true
     */
    public void submitJob(final User theUser, final Job theJob) {
        if (theUser.getAccessLevel() != SystemCoordinator.PARK_MANAGER_ACCESS_LEVEL 
                        || !(theUser instanceof ParkManager)) {
            throw new IllegalArgumentException();
        }
        
        ParkManager theParkManager = (ParkManager) theUser;
        theParkManager.addCreatedJob(theJob);
        theParkManager.addSubmittedJob(theJob);
        myJobList.add(theJob);
        myPropertyChangeHandler.firePropertyChange(SystemEvents.SUBMIT_JOB.name(), 
                                                       null, null);
    }
    
    /**
     * Unsubmits the jobs if the business rule criteria are met.
     * 
     * PRECONDITON: canUnsubmitJob(theJob) == 0
     * 
     * @return an integer representation of -> broken business rules XOR the job can be legally removed
     */
    public void unsubmitJob(final User theUser, final Job theJob) {
        if (theUser.getAccessLevel() != SystemCoordinator.PARK_MANAGER_ACCESS_LEVEL 
                        || !(theUser instanceof ParkManager)) {
            throw new IllegalArgumentException();
        }
        
        Job theSystemJob = myJobList.stream().filter(job -> job.equals(theJob)).findFirst().get();
        ParkManager theParkManager = (ParkManager) theUser;
        theParkManager.removeSubmittedJob(theSystemJob);
        myJobList.remove(theSystemJob);
        theSystemJob.getCurrentVolunteers().stream().forEach(vol -> ((Volunteer) mySystem.getUser(vol.getUsername())).unapplyForJob(theSystemJob));
        myPropertyChangeHandler.firePropertyChange(SystemEvents.UNSUBMIT_JOB.name(), 
                                                   null, null);
    }

    /**
     * Adds a specified user to a job.
     * 
     * PRECONDITION: theUser instanceof Volunteer == true
     *               theUser.canApplyToJob(theJob) == 0
     *               theJob.canAcceptVolunteers() == true
     * 
     * @param theJob
     */
    public void applyToJob(User theUser, Job theJob) {
        if (theUser.getAccessLevel() != SystemCoordinator.VOLUNTEER_ACCESS_LEVEL 
                        || !(theUser instanceof Volunteer)) {
            throw new IllegalArgumentException();
        }
        
        Job theSystemJob = myJobList.stream().filter(job -> job.equals(theJob)).findFirst().get();
        Volunteer theVolunteer = (Volunteer) theUser;

        theSystemJob.addVolunteer(theVolunteer);
        theVolunteer.applyToJob(theSystemJob);
        myPropertyChangeHandler.firePropertyChange(SystemEvents.APPLY_JOB.name(), 
                                                       null, null);
    }
    
    /**
     *  Removes a specified user from a job.
     */
    public void unapplyFromJob(User theUser, Job theJob) {
        if (theUser.getAccessLevel() != SystemCoordinator.VOLUNTEER_ACCESS_LEVEL
                        || !(theUser instanceof Volunteer)){
            throw new IllegalArgumentException();
        }        

        Job theSystemJob = myJobList.stream().filter(job -> job.equals(theJob)).findFirst().get();
        Volunteer theVolunteer = (Volunteer) theUser;
        if (theVolunteer.canUnapplyFromJob(theSystemJob) == 0) {
            theSystemJob.removeVolunteer(theVolunteer);
            theVolunteer.unapplyForJob(theSystemJob);
            myPropertyChangeHandler.firePropertyChange(SystemEvents.UNAPPLY_JOB.name(), 
                                                       null, null);
        } else {
            myPropertyChangeHandler.firePropertyChange(SystemEvents.ERROR.name(), 
                                                       null, null);
        }
    }

    // queries
    public int getCurrentMaximumJobs() {
        return this.myCurrentMaximumJobs;
    }
    
    
    public ArrayList<Job> getPendingJobs() {
        return myJobList;
    }
    
    /**
     * Job Query for determining specific jobs to make visible to the user who is requesting
     * the job list. 
     * 
     * Precondition: theUser is a user of subclass Volunteer, ParkManager, or OfficeStaff
     * 
     * @return a job listing that shows the relevant jobs to the specific type of user 
     * as follows:
     *      For Volunteer: A list of jobs that they can currently sign up for which excludes
     *                     any jobs in the past, or jobs that overlap with currently signed
     *                     up for jobs.
     *      For ParkManager: A list of current and upcoming jobs that could potentially
     *                       conflict with a new job submission.
     *      For OfficeStaff: All jobs will be listed. 
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Job> getSystemJobListing(User theUser) {
        ArrayList<Job> theModifiedList = new ArrayList<Job>();
        if (theUser instanceof Volunteer) {
            Volunteer theVolunteer = (Volunteer) theUser;
            for (Job aJob : this.myJobList) {
                if (theVolunteer.canApplyToJob(aJob) == 0 &&
                                aJob.canAcceptVolunteers()) {
                    theModifiedList.add(aJob);
                }
            }
        } else if (theUser instanceof ParkManager) {
            ParkManager thePM = (ParkManager) theUser;
            for (Job aJob : this.myJobList) {
                if (!thePM.isJobInPast(aJob)) {
                    theModifiedList.add(aJob);
                }
            }
        } else if (theUser instanceof OfficeStaff) {
            theModifiedList.addAll(this.myJobList);
        }
        
        return (ArrayList<Job>) theModifiedList.clone();
    }

    
    /**
     * Job Query for determining specific jobs to make visible to the user who is requesting
     * the job list. 
     * 
     * Precondition: theUser is a user of subclass Volunteer, ParkManager, or OfficeStaff
     * 
     * @return a job listing that shows the relevant jobs to the specific type of user 
     * as follows:
     *      For Volunteer: A list of jobs that they can currently sign up for which excludes
     *                     any jobs in the past, or jobs that overlap with currently signed
     *                     up for jobs.
     *      For ParkManager: A list of current and upcoming jobs that could potentially
     *                       conflict with a new job submission.
     *      For OfficeStaff: All jobs will be listed. 
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Job> getUserJobListing(User theUser) {
        ArrayList<Job> theModifiedList = new ArrayList<Job>();
        if (theUser instanceof Volunteer) {
            Volunteer theVolunteer = (Volunteer) theUser;
            for (Job aJob : this.myJobList) {
                if (aJob.getCurrentVolunteers().contains(theVolunteer)) {
                    theModifiedList.add(aJob);
                }
            }
        } else if (theUser instanceof ParkManager) {
            ParkManager thePM = (ParkManager) theUser;
            for (Job aJob : this.myJobList) {
                if (thePM.getSubmittedJobs().contains(aJob)) {
                    theModifiedList.add(aJob);
                }
            }
        } else if (theUser instanceof OfficeStaff) {
            OfficeStaff theStaff = (OfficeStaff) theUser;
            for (Job aJob : this.myJobList) {
                if (!theStaff.getStartDate().after(aJob.getStartDate()) &&
                                !theStaff.getEndDate().before(aJob.getStartDate())) {
                    theModifiedList.add(aJob);
                }
            }
        }
        
        return (ArrayList<Job>) theModifiedList.clone();
    }
    
    public GregorianCalendar getCurrentDate() {
        return (GregorianCalendar) this.myCurrentDate.clone();
    }

    // testers

    public boolean hasSpaceToAddJobs() {
        return myJobList.size() < this.myCurrentMaximumJobs;
    }

    /**
     * 
     * @param theJob
     * @return integer representation of the broken business rule as follows:
     */
    public int canSubmitJob(Job theJob) {
        int returnInt = 0;
        
        if (myJobList.contains(theJob)) {
            // warning, job already exists
            returnInt = 1;
        } else if (theJob.getJobLength() >= JobCoordinator.MAXIMUM_JOB_LENGTH) {
            // warning, job exceeds maximum job length
            returnInt = 2;
        } else if (daysFromToday(theJob.getStartDate()) > JobCoordinator.MAXIMUM_DAYS_AWAY_TO_POST_JOB) {
            returnInt = 3;
            // warning, job is further than max days away
        } else if (daysFromToday(theJob.getStartDate()) < Volunteer.MINIMUM_DAYS_BEFORE_JOB_START) {
            System.out.println("TODAY: " + myCurrentDate.getTime() 
            + "\nJOB DAY: " + theJob.getStartDate().getTime() 
            + "\nDAYS FROM TODAY: " + daysFromToday(theJob.getStartDate()));
            returnInt = 4;
            // warning, job starts before any volunteer can sign up
        }

        return returnInt;
    }
    
    public int canUnsubmitJob(Job theJob) {
        int returnInt = 0;
        
        if (!myJobList.contains(theJob)) {
            returnInt = 1;
            // warning job does not exist in systme to unsubmit
        } else if (daysFromToday(theJob.getStartDate()) < Volunteer.MINIMUM_DAYS_BEFORE_JOB_START) {
            returnInt = 2;
            // warning job starts sooner than the allowed minimum days before starts for unsubmission
        }
        
        return returnInt;
    }

    /**
     * Calculates the days theDate is from myCurrentDate, so < 0 days if theDate is prior
     * to the current date, >= 0 otherwise.
     */
    public int daysFromToday(GregorianCalendar theDate) {
        return JobCoordinator.getDifferenceInDays(myCurrentDate, theDate);
    }
    
    /**
     * Helper method to calculate the difference in days of two calendar dates relative to the
     * first date.
     *
     * @return The difference in days.
     */
    public static int getDifferenceInDays(GregorianCalendar theFirstDate, GregorianCalendar theSecondDate) {
          long milliBetweenDays = theSecondDate.getTimeInMillis() - theFirstDate.getTimeInMillis();
          return (int) Math.ceil(milliBetweenDays / (1.0*1000*60*60*24));
    }
}
