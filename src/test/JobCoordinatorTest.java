package test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import model.Job;
import model.JobCoordinator;
import util.SystemConstants;

public class JobCoordinatorTest {
    public JobCoordinator globalJobCoordinator;
    public Job anyOldValidJob;

    @Before
    public void setUp() throws Exception {
        globalJobCoordinator = new JobCoordinator();
        anyOldValidJob = new Job("anyOldValidJob");
    }


    // Business Rule:
    // There can not be more than the maximum number of pending jobs at a time in the entire system, default of 20

    // The system has far fewer than the maximum number of pending jobs
    @Test
    public final void hasSpaceToAddJobs_LessThanMaximumPendingJobs_ShouldReturnTrue() {
        // add 0 or 1 job so that # current jobs < maximum jobs
        globalJobCoordinator.addPendingJob(anyOldValidJob);

        assertTrue(globalJobCoordinator.hasSpaceToAddJobs());
    }

    // The system has one fewer than the maximum number of pending jobs
    @Test
    public final void hasSpaceToAddJobs__OneLessThanMaximumPendingJobs__ShouldReturnTrue() {
        // add 20 - 1 = 19 jobs to pending
        for (int i = 0; i < SystemConstants.MAXIMUM_JOBS - 1; i++) {
            Job generatedValidJob = new Job("generatedValidJob" + i);
            globalJobCoordinator.addPendingJob(generatedValidJob);
        }

        assertTrue(globalJobCoordinator.hasSpaceToAddJobs());
    }

    // The system has exactly the maximum number of pending jobs
    @Test
    public final void hasSpaceToAddJobs__ExactlyMaximumPendingJobs__ShouldReturnFalse() {
        // add exactly 20 (maximum) jobs to pending
        for (int i = 0; i < 20; i++) {
            Job generatedValidJob = new Job("generatedValidJob" + i);
            globalJobCoordinator.addPendingJob(generatedValidJob);
        }

        assertFalse(globalJobCoordinator.hasSpaceToAddJobs());
    }


    // Business Rule:
    // No job can be specified whose end date is more than the maximum number of days from the current date, default of 75


    // The specified job ends one fewer than the maximum number of days from the current date
    @Test
    public final void canAddJob_JobEndsOneLessThanMaximumDays__ShouldBeTrue() {
        GregorianCalendar currentDate = globalJobCoordinator.getCurrentDate();
        
        // create new calendar date to add to generic job
        GregorianCalendar oneLessThanMaximumDaysAwayDate = (GregorianCalendar) currentDate.clone();
        oneLessThanMaximumDaysAwayDate.add(GregorianCalendar.DAY_OF_YEAR, 
                                           SystemConstants.MAXIMUM_DAYS_AWAY_TO_POST_JOB - 1);

        // specify generic job on that day
        Job jobOneLessThanMaximumDaysAway = new Job("jobOneLessThanMaximumDaysAway");
        jobOneLessThanMaximumDaysAway.setStartDate(oneLessThanMaximumDaysAwayDate);
        jobOneLessThanMaximumDaysAway.setEndDate(oneLessThanMaximumDaysAwayDate);

        // test
        assertTrue(globalJobCoordinator.canAddJob(jobOneLessThanMaximumDaysAway) == 0);
    }

    // The specified job ends the maximum number of days from the current date
    @Test
    public final void canAddJob_JobEndsExactlyMaximumDaysAway__ShouldBeTrue() {
        GregorianCalendar currentDate = globalJobCoordinator.getCurrentDate();
        
        // create new calendar date to add to generic job
        GregorianCalendar exactlyMaximumDaysAwayDate = (GregorianCalendar) currentDate.clone();
        exactlyMaximumDaysAwayDate.add(GregorianCalendar.DAY_OF_YEAR, 
                                       SystemConstants.MAXIMUM_DAYS_AWAY_TO_POST_JOB);

        // specify generic job on that day
        Job jobExactlyMaximumDaysAway = new Job("jobExactlyMaximumDaysAway");
        jobExactlyMaximumDaysAway.setStartDate(exactlyMaximumDaysAwayDate);
        jobExactlyMaximumDaysAway.setEndDate(exactlyMaximumDaysAwayDate);

        // test
        assertTrue(globalJobCoordinator.canAddJob(jobExactlyMaximumDaysAway) == 0);
    }

    // The specified job ends one more than the maximum number of days from the current date
    @Test
    public final void canAddJob_JobEndsOneMoreThanMaximumDays__ShouldBeFalse() {
        GregorianCalendar currentDate = globalJobCoordinator.getCurrentDate();
        
        // create new calendar date to add to generic job
        GregorianCalendar oneMoreThanMaximumDaysAwayDate = (GregorianCalendar) currentDate.clone();
        oneMoreThanMaximumDaysAwayDate.add(GregorianCalendar.DAY_OF_YEAR, 
                                       SystemConstants.MAXIMUM_DAYS_AWAY_TO_POST_JOB + 1);

        // specify generic job on that day
        Job jobOneMoreThanMaximumDaysAway = new Job("jobOneMoreThanMaximumDaysAway");
        jobOneMoreThanMaximumDaysAway.setStartDate(oneMoreThanMaximumDaysAwayDate);
        jobOneMoreThanMaximumDaysAway.setEndDate(oneMoreThanMaximumDaysAwayDate);

        // test
        assertFalse(globalJobCoordinator.canAddJob(jobOneMoreThanMaximumDaysAway) == 0);
    }

}
