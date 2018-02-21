package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import model.Job;
import model.ParkManager;

public class ParkManagerTest {
    ParkManager john_doe_the_park_manager;
    GregorianCalendar theCurrentDate;
    Job jobThatIsNotSubmitted;
    @Before
    public void setUp() throws Exception {
        john_doe_the_park_manager = new ParkManager("John Doe");
        theCurrentDate = new GregorianCalendar();
        jobThatIsNotSubmitted = new Job("Job that isnt submitted");
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

}
