import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class JobCoordinator {
    private final int MAXIMUM_JOBS = 20; // 20 pending jobs max
    private final int MAXIMUM_JOB_LENGTH = 3; // days
    private final int MAXIMUM_DAYS_FROM_TODAY = 75; // 3 month away maximum
    
    private final ArrayList<Job> myPendingJobList;
    private final ArrayList<Job> myFinishedJobList;
    
    private static GregorianCalendar myCurrentDate = new GregorianCalendar();
    
    public JobCoordinator() {
        myPendingJobList = new ArrayList<Job>();
        myFinishedJobList = new ArrayList<Job>();
        
    }
    /**
     * Adds a job to my list of jobs
     * @param theJob that needs to be added
     */
    public void addPendingJob(final Job theJob) {
        if (myPendingJobList.contains(theJob)) {
            // warning, job already exists
        } else if (theJob.getJobLength() > MAXIMUM_JOB_LENGTH) {
            // warning, job exceeds maximum job length
        } else if (getDifferenceInDays(JobCoordinator.myCurrentDate,
                                                              theJob.getStartDate()) 
                        > MAXIMUM_DAYS_FROM_TODAY) {
            // warning, job is further than 75 days away
        } else {
            myPendingJobList.add(theJob);
        }
    }
    
    public void addFinishedJob(final Job theJob) {
        if (JobCoordinator.myCurrentDate.compareTo(theJob.getEndDate()) < 0) {
            // impossible for job to be finished, warning
        } else {
            myFinishedJobList.add(theJob);
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
    
    
    // testers
    // can only potentially add a job if there are less than 20 pending jobss
    public boolean canAddJob() {
        return myPendingJobList.size() < MAXIMUM_JOBS;
    }

    
    // private methods 
    private int getDifferenceInDays(GregorianCalendar theFirstDate, 
                                          GregorianCalendar theSecondDate) {
        long milliTime = theFirstDate.getTimeInMillis() - theSecondDate.getTimeInMillis();
        TimeUnit.MILLISECONDS.convert(milliTime, TimeUnit.DAYS);
        return (int) milliTime;
    }   
}
