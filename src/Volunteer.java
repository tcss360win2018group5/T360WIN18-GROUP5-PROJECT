import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

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

	// deprecated, use isOverlapping
    public boolean isSameDayConflict(Job theJobFromList, Job theApplyingJob) {
        return theJobFromList.getStartDate().get(Calendar.DATE)
                == theApplyingJob.getStartDate().get(Calendar.DATE);
    }

    // deprecated, use isOverlapping
    public boolean isEndDayConflict(Job theJobFromList, Job theApplyingJob) {
        return theJobFromList.getEndDate().get(Calendar.DATE)
                == theApplyingJob.getStartDate().get(Calendar.DATE);
    }
    
    // for Job 1 and 2 
    // possible conflict cases:
    // [1,2][ , ][ , ][ , ] (same day)
    // [1, ][1,2][ ,2][ , ] (end day conflict)
    // [ ,2][1,2][1, ][ , ] (end day conflict, mirror case)
    // [1, ][1,2][1,2][ ,2] (left (relative to 2) overlap conflict
    // [ ,2][1,2][1,2][1, ] (right (relative to 2) overlap conflict
    // [ ,2][1,2][ ,2][ , ] (inside conflict)
    // since we are only using start and end dates as comparison points, we must consider all
    // types of overlap, especially those not immediately discernable via a quick check of 
    // same day or end day conflict
    // there might be some better way of doing this, though
    
    public boolean isOverlapping(Job theJobFromList, Job theApplyingJob) {
        GregorianCalendar listJobStart = theJobFromList.getStartDate();
        GregorianCalendar listJobEnd = theJobFromList.getEndDate();
        GregorianCalendar applyingJobStart = theApplyingJob.getStartDate();
        GregorianCalendar applyingJobEnd = theApplyingJob.getEndDate();
                        // same day conflict
        return listJobStart.compareTo(applyingJobStart) == 0
                        // if listJob starts  on the same day that applyingJob ends
                        || listJobStart.compareTo(applyingJobEnd) == 0 
                        // if listJob ends on the same day applyingJob starts
                        || listJobEnd.compareTo(applyingJobStart) == 0
                        // if listJob overlaps on the left with applyingJob
                        || (listJobStart.compareTo(applyingJobStart) < 0 && listJobEnd.compareTo(applyingJobStart) >= 0)
                        // if listJob overlaps on the right with applyingJob
                        || (listJobEnd.compareTo(applyingJobEnd) > 0 && listJobStart.compareTo(applyingJobEnd) <= 0)
                        // if listJob overlaps inside applyingJob
                        || (listJobStart.compareTo(applyingJobStart) > 0 && (listJobStart.compareTo(applyingJobEnd) < 0));
    }

	public boolean addToCurrentJobs(final Job theApplyingJob) {
        boolean is_there_conflict = myCurrentJobs.stream()
                .anyMatch(aJobFromList -> isOverlapping(aJobFromList, theApplyingJob));
        boolean is_job_added = false;
        if (!is_there_conflict) {
            myCurrentJobs.add(theApplyingJob);
            is_job_added = true;
        }
        return is_job_added;
    }


	@SuppressWarnings("unchecked")
    protected ArrayList<Job> getCurrentJobs() {
		return (ArrayList<Job>) this.myCurrentJobs.clone();
	}




	public static void main(String[]args) {
//        Volunteer test = new Volunteer("Volunteer Test");
//
//        GregorianCalendar testDate = new GregorianCalendar();
//        testDate.set(2018, 02, 22);
//
//        System.out.println(testDate.get(Calendar.YEAR));
//        System.out.println(testDate.get(Calendar.MONTH));
//        System.out.println(testDate.get(Calendar.DATE));
//
//        test.myCurrentJobs.forEach(System.out::println);
//
//        Job job_test1 = new Job("Job Test1");
//        job_test1.setStartDate(new GregorianCalendar(2018,01,01));
//        job_test1.setEndDate(new GregorianCalendar(2018,01,01));
//        Job job_test2 = new Job("Job Test2");
//        job_test2.setStartDate(new GregorianCalendar(2018,01,02));
//        job_test2.setEndDate(new GregorianCalendar(2018,01,03));
//        Job job_test3 = new Job("Job Test3");
//        job_test3.setStartDate(new GregorianCalendar(2018,01,03));
//        job_test3.setEndDate(new GregorianCalendar(2018,01,03));
//
//
//        test.addToCurrentJobs(job_test2);
//        test.addToCurrentJobs(job_test3);
//
//
//        test.myCurrentJobs.forEach(job -> System.out.println(job.getJobTitle()));
    }
}
