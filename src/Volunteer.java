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
	
	private void addToCurrentJobs(final Job theJob) {
		myCurrentJobs.add(theJob);
		System.out.println("Added " + theJob.getJobTitle() + " to my pending jobs.");
	}


	protected ArrayList getCurrentJobs() {
		return myCurrentJobs;
	}


    protected void applyToJob(Job theJob) {
        ArrayList<Job> compareJobs = getCurrentJobs();

        compareJobs.forEach(job -> {
            // Check current job date on list to job date
            if (job.getDate().get(Calendar.DATE) == theJob.getDate().get(Calendar.DATE)) {
                throw new RuntimeException("This job starts the same day as the end of some job already signed up for\n");
            }
        });

        addToCurrentJobs(theJob);
    }



	public static void main(String[]args) {
        Volunteer test = new Volunteer("Volunteer Test");

//        GregorianCalendar testDate = new GregorianCalendar();
//        testDate.set(2018, 02, 22);
//
//        System.out.println(testDate.get(Calendar.YEAR));
//        System.out.println(testDate.get(Calendar.MONTH));
//        System.out.println(testDate.get(Calendar.DATE));

        test.myCurrentJobs.forEach(System.out::println);

        Job job_test1 = new Job("Job Test1");
        job_test1.setDate(new GregorianCalendar(2018,01,01));
        Job job_test2 = new Job("Job Test2");
        job_test2.setDate(new GregorianCalendar(2018,01,02));
        Job job_test3 = new Job("Job Test2");
        job_test3.setDate(new GregorianCalendar(2018,01,02));

        test.applyToJob(job_test1);
        test.applyToJob(job_test2);
//        test.addToCurrentJobs(job_test3);
//        test.applyToJob(job_test3);

        test.myCurrentJobs.forEach(job -> System.out.println(job.getJobTitle()));
    }
}
