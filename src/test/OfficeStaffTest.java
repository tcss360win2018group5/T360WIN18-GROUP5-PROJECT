package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import model.Job;
import model.JobCoordinator;
import model.OfficeStaff;
import model.ParkManager;
import model.SystemCoordinator;
import model.Volunteer;


/**
 * JUnit testing to test changing the max pending job and test if the job is between a start and end date.
 */
public class OfficeStaffTest {
	public OfficeStaff globalOfficeStaffAlex;
    
    @Before
    public void setUp() throws Exception {
        globalOfficeStaffAlex = new OfficeStaff("Alex");
    }
    
	/**
	 * Testing if the job is between the two given dates. 
	 */
	@Test
	public void isBetween2Dates_JobIsBetween2Dates_True() {
		GregorianCalendar dateTime = new GregorianCalendar();
		Job job = new Job("Cleaning XYZ park");
		job.setStartDate(dateTime);
		job.setEndDate(dateTime);
		
		GregorianCalendar startDateTime = new GregorianCalendar();
		startDateTime.add(GregorianCalendar.DAY_OF_YEAR, -5);
		GregorianCalendar endDateTime = new GregorianCalendar();
		endDateTime.add(GregorianCalendar.DAY_OF_YEAR, 5);
		globalOfficeStaffAlex.setStartDate(startDateTime);
		globalOfficeStaffAlex.setEndDate(endDateTime);
		
		
		ArrayList<Job> jobList = new ArrayList<Job>();
		jobList.add(job);

		assertTrue(globalOfficeStaffAlex.getJobsBetween2Dates(jobList).contains(job));
	}
	
	/**
	 * Testing if the job is NOT between the two given dates. 
	 * The Job start and end date is before the given two dates.
	 */
	@Test
	public void isBetween2Dates_JobDateIsBeforeGiven2Dates_False() {
        GregorianCalendar dateTime = new GregorianCalendar();
        Job job = new Job("Cleaning XYZ park");
        job.setStartDate(dateTime);
        job.setEndDate(dateTime);
        
        GregorianCalendar startDateTime = new GregorianCalendar();
        startDateTime.add(GregorianCalendar.DAY_OF_YEAR, 5);
        GregorianCalendar endDateTime = new GregorianCalendar();
        endDateTime.add(GregorianCalendar.DAY_OF_YEAR, 10);
        globalOfficeStaffAlex.setStartDate(startDateTime);
        globalOfficeStaffAlex.setEndDate(endDateTime);
        
        
        ArrayList<Job> jobList = new ArrayList<Job>();
        jobList.add(job);

        assertFalse(globalOfficeStaffAlex.getJobsBetween2Dates(jobList).contains(job));
	}
	
	/**
	 * Testing if the job is NOT between the two given dates. 
	 * The Job start and end date is after the given two dates.
	 */
	@Test
	public void isBetween2Dates_JobDateIsAfterGiven2Dates_False() {
        GregorianCalendar dateTime = new GregorianCalendar();
        Job job = new Job("Cleaning XYZ park");
        job.setStartDate(dateTime);
        job.setEndDate(dateTime);
        
        GregorianCalendar startDateTime = new GregorianCalendar();
        startDateTime.add(GregorianCalendar.DAY_OF_YEAR, -10);
        GregorianCalendar endDateTime = new GregorianCalendar();
        endDateTime.add(GregorianCalendar.DAY_OF_YEAR, -5);
        globalOfficeStaffAlex.setStartDate(startDateTime);
        globalOfficeStaffAlex.setEndDate(endDateTime);
        
        
        ArrayList<Job> jobList = new ArrayList<Job>();
        jobList.add(job);

        assertFalse(globalOfficeStaffAlex.getJobsBetween2Dates(jobList).contains(job));
	}

}
