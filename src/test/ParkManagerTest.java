package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import model.Job;
import model.JobCoordinator;
import model.ParkManager;
import model.Volunteer;

/**
 * Class to test the functionality of ParkManager and ensuring
 * that they can unsubmit a job.
 * @author Jon
 *
 */
public class ParkManagerTest {
	
		public JobCoordinator globalJobCoordinator;
	    public ParkManager anyParkManager;
	    public GregorianCalendar testDateToday;
	    public GregorianCalendar testDatePriorToCurrentDay;
	    public GregorianCalendar testDateMoreThanMinDaysAway;
	    public GregorianCalendar testDateExactlyMinDaysAway;
	    private Job jobStartsOnCurrentDay;
	    private Job jobStartsPriorToCurrentDay;
	    private Job jobStartsMoreThanMinDaysAway;
	    private Job jobStartsExactlyMinDaysAway;
	    private Job testJobStartsToday;
	    ParkManager john_doe_the_park_manager;
	    GregorianCalendar theCurrentDate;
	    Job jobThatIsNotSubmitted;

	    @Before
	    public void setUp() {
	        globalJobCoordinator = new JobCoordinator();
	        anyParkManager = new ParkManager("SomeOldParkManager");
	        
	        john_doe_the_park_manager = new ParkManager("John Doe");
	        theCurrentDate = new GregorianCalendar();
	        jobThatIsNotSubmitted = new Job("Job that isnt submitted");
	        
	        //Denotes one day for "today" to be compared with test dates
	        testDateToday = new GregorianCalendar();
	        testDatePriorToCurrentDay = new GregorianCalendar(Calendar.YEAR, 
	        												  Calendar.MONTH,
	        												  Calendar.DAY_OF_MONTH - 1);
	        testDateMoreThanMinDaysAway = new GregorianCalendar(Calendar.YEAR, 
	        													Calendar.MONTH,
	        													Calendar.DAY_OF_MONTH 
	        													+ Volunteer.MINIMUM_DAYS_BEFORE_JOB_START 
	        													+ 2);
	        testDateExactlyMinDaysAway = new GregorianCalendar(Calendar.YEAR, 
	        												   Calendar.MONTH,
	        												   Calendar.DAY_OF_MONTH 
	        												   + Volunteer.MINIMUM_DAYS_BEFORE_JOB_START);
	        
	        testJobStartsToday = new Job("Job Today");
	        testJobStartsToday.setStartDate(new GregorianCalendar());

	        //Job on same day as today's date
	        jobStartsOnCurrentDay = new Job("Job Starts On Current Day");
	        jobStartsOnCurrentDay.setStartDate(testDateToday);
	        jobStartsOnCurrentDay.setEndDate(testDateToday);
	        jobStartsOnCurrentDay.setMaxVolunteers(5);

	        jobStartsPriorToCurrentDay = new Job("Job Start Prior To Current Day");
	        jobStartsPriorToCurrentDay.setStartDate(testDatePriorToCurrentDay);
	        jobStartsPriorToCurrentDay.setEndDate(testDateToday);
	        jobStartsPriorToCurrentDay.setMaxVolunteers(5);
	        
	        jobStartsMoreThanMinDaysAway = new Job("Job Start More Than Min Days Away");
	        jobStartsMoreThanMinDaysAway.setStartDate(testDateMoreThanMinDaysAway);
	        jobStartsMoreThanMinDaysAway.setEndDate(testDateMoreThanMinDaysAway);
	        jobStartsMoreThanMinDaysAway.setMaxVolunteers(5);
	        
	        jobStartsExactlyMinDaysAway = new Job("Job Starts Exactly Min Days Away");
	        jobStartsExactlyMinDaysAway.setStartDate(testDateExactlyMinDaysAway);
	        jobStartsExactlyMinDaysAway.setEndDate(testDateExactlyMinDaysAway);
	        jobStartsExactlyMinDaysAway.setMaxVolunteers(5);

	    }
	    
	    /*
	     * Testing for getFutureJobs() to ensure that only future submitted jobs are shown.
	     */
	    
	    // make sure no jobs are shown when there are no submitted jobs
	    @Test
	    public final void getFutureJobs_noJobs_shouldReturnEmptyList() {
	        ArrayList<Job> futureSubmittedJobs = john_doe_the_park_manager.getFutureSubmittedJobs();
	        
	        assertTrue(futureSubmittedJobs.isEmpty());
	        assertFalse(futureSubmittedJobs.contains(jobThatIsNotSubmitted));
	    }
	    
	    // make sure if there is a future submitted jobs, that it does show it
	    @Test
	    public final void getFutureJobs_oneFutureJob_shouldReturnListWithFutureJob() {
	        // setup future job
	        GregorianCalendar futureDate = (GregorianCalendar) theCurrentDate.clone();
	        futureDate.add(GregorianCalendar.DAY_OF_YEAR, 7);
	        Job futureJobOneWeekAhead = new Job("A Job 1 Week In The Future");
	        futureJobOneWeekAhead.setStartDate(futureDate);
	        futureJobOneWeekAhead.setEndDate(futureDate);
	        
	        john_doe_the_park_manager.addSubmittedJob(futureJobOneWeekAhead);
	        ArrayList<Job> futureSubmittedJobs = john_doe_the_park_manager.getFutureSubmittedJobs();
	        
	        // make sure list shows the future job
	        assertTrue(futureSubmittedJobs.contains(futureJobOneWeekAhead));
	        assertFalse(futureSubmittedJobs.contains(jobThatIsNotSubmitted));
	    }
	    
	    // make sure that if there are past jobs, that it DOESNT show it
	    @Test
	    public final void getFutureJobs_onePastJob_shouldReturnEmptyList() {
	        // setup past job
	        GregorianCalendar pastDate = (GregorianCalendar) theCurrentDate.clone();
	        pastDate.add(GregorianCalendar.DAY_OF_YEAR, -7);
	        Job pastJobOneWeekBefore = new Job("A Job 1 Week In The Future");
	        pastJobOneWeekBefore.setStartDate(pastDate);
	        pastJobOneWeekBefore.setEndDate(pastDate);
	        
	        john_doe_the_park_manager.addSubmittedJob(pastJobOneWeekBefore);
	        ArrayList<Job> futureSubmittedJobs = john_doe_the_park_manager.getFutureSubmittedJobs();

	        // make sure past job does not show up in list and (in this case) that it is empty
	        assertTrue(futureSubmittedJobs.isEmpty());
	        assertFalse(futureSubmittedJobs.contains(jobThatIsNotSubmitted));
	    }
	    
	    // make sure that if there exist past and future jobs, only show the future jobs
	    @Test
	    public final void getFutureJobs_onePastJobAndOneFutureJob_shouldReturnListWithFutureJob() {
	        // setup past job
	        GregorianCalendar pastDate = (GregorianCalendar) theCurrentDate.clone();
	        pastDate.add(GregorianCalendar.DAY_OF_YEAR, -7);
	        Job pastJobOneWeekBefore = new Job("A Job 1 Week In The Future");
	        pastJobOneWeekBefore.setStartDate(pastDate);
	        pastJobOneWeekBefore.setEndDate(pastDate);
	        
	        // setup future job
	        GregorianCalendar futureDate = (GregorianCalendar) theCurrentDate.clone();
	        futureDate.add(GregorianCalendar.DAY_OF_YEAR, 7);
	        Job futureJobOneWeekAhead = new Job("A Job 1 Week In The Future");
	        futureJobOneWeekAhead.setStartDate(futureDate);
	        futureJobOneWeekAhead.setEndDate(futureDate);
	        
	        john_doe_the_park_manager.addSubmittedJob(pastJobOneWeekBefore);
	        john_doe_the_park_manager.addSubmittedJob(futureJobOneWeekAhead);
	        ArrayList<Job> futureSubmittedJobs = john_doe_the_park_manager.getFutureSubmittedJobs();

	        // make sure list only shows the future job
	        assertFalse(futureSubmittedJobs.contains(pastJobOneWeekBefore));
	        assertTrue(futureSubmittedJobs.contains(futureJobOneWeekAhead));
	        assertFalse(futureSubmittedJobs.contains(jobThatIsNotSubmitted));
	    }

	    @Test
	    public void canUnsubmitJob_jobStartsOnCurrentDay_False() {
	    	assertTrue(anyParkManager.isMaxDistanceAwayToAddOrRemove(jobStartsOnCurrentDay));
	    }
	    
	    @Test
	    public void canUnsubmitJob_jobStartsPriorToCurrentDay_False() {
	    	assertFalse(anyParkManager.isJobInPast(jobStartsPriorToCurrentDay.getStartDate()));
	    }
	    
	    @Test
	    public void canUnsubmitJob_jobStartsMoreThanMinDaysAway_True() {
	    	assertTrue(anyParkManager.isTooFarFromToday(jobStartsMoreThanMinDaysAway));
	    }
	    
	    @Test
	    public void canUnsubmitJob_jobStartsExactlyMinDaysAway_True() {
	    	assertTrue(anyParkManager.isTooFarFromToday(jobStartsExactlyMinDaysAway));
	    }

}
