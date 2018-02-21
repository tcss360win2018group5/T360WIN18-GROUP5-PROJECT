package test;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import model.Job;
import model.JobCoordinator;
import model.ParkManager;
import util.SystemConstants;

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

	    @Before
	    public void setUp() {
	        globalJobCoordinator = new JobCoordinator();
	        anyParkManager = new ParkManager("SomeOldParkManager");
	        
	        //Denotes one day for "today" to be compared with test dates
	        testDateToday = new GregorianCalendar();
	        testDatePriorToCurrentDay = new GregorianCalendar(Calendar.YEAR, 
	        												  Calendar.MONTH,
	        												  Calendar.DAY_OF_MONTH - 1);
	        testDateMoreThanMinDaysAway = new GregorianCalendar(Calendar.YEAR, 
	        													Calendar.MONTH,
	        													Calendar.DAY_OF_MONTH 
	        													+ SystemConstants.MINIMUM_DAYS_BEFORE_JOB_START 
	        													+ 2);
	        testDateExactlyMinDaysAway = new GregorianCalendar(Calendar.YEAR, 
	        												   Calendar.MONTH,
	        												   Calendar.DAY_OF_MONTH 
	        												   + SystemConstants.MINIMUM_DAYS_BEFORE_JOB_START);
	        
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

	    @Test
	    public void canUnsubmitJob_jobStartsOnCurrentDay_False() {
	    	assertTrue(anyParkManager.isTooCloseToAddOrRemove(jobStartsOnCurrentDay));
	    }
	    
	    @Test
	    public void canUnsubmitJob_jobStartsPriorToCurrentDay_False() {
	    	assertFalse(anyParkManager.isJobInPast(testDateToday, 
	    				 jobStartsPriorToCurrentDay.getStartDate()));
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
