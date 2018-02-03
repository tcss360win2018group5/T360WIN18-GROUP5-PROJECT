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


    public boolean isSameDayConflict(Job theJobFromList, Job theApplyingJob) {
        boolean conflict = false;
        conflict = theJobFromList.getStartDate().get(Calendar.DATE)
                == theApplyingJob.getStartDate().get(Calendar.DATE)
                || conflict;
        System.out.println(conflict);
        return conflict;
    }

    public boolean isEndDayConflict(Job theJobFromList, Job theApplyingJob) {
        boolean conflict = false;
        conflict = theJobFromList.getEndDate().get(Calendar.DATE)
                == theApplyingJob.getStartDate().get(Calendar.DATE)
                || conflict;
        System.out.println(conflict);
        return conflict;
    }

	public boolean addToCurrentJobs(final Job theApplyingJob) {
        boolean is_there_conflict = myCurrentJobs.stream()
                .anyMatch(aJobFromList -> isSameDayConflict(aJobFromList, theApplyingJob)
                                       || isEndDayConflict(aJobFromList, theApplyingJob));
        boolean is_job_added = false;
        if (!is_there_conflict) {
            myCurrentJobs.add(theApplyingJob);
            is_job_added = true;
        }
        return is_job_added;
    }


	protected ArrayList getCurrentJobs() {
		return myCurrentJobs;
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
