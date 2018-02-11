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

public class JobCoordinatorTest {
    public JobCoordinator globalJobCoordinator;
    public Job globalGenericJob;

    @Before
    public void setUp() throws Exception {
        globalJobCoordinator = new JobCoordinator();
        globalGenericJob = new Job("GenericGlobalJob");
    }


    // Testing Park Manager Business Rule 2A
    // There can not be more than the maximum number of pending jobs at a time in the entire system, default of 20

    // i) The system has far fewer than the maximum number of pending jobs
    @Test
    public final void canAddJob_LessThanMaximumPendingJobs_ShouldReturnTrue() {
        // add 0 or 1 job so that # current jobs < maximum jobs
        globalJobCoordinator.addPendingJob(globalGenericJob);

        assertTrue(globalJobCoordinator.hasSpaceToAddNewJob());
    }

    // ii) The system has one fewer than the maximum number of pending jobs
    @Test
    public final void canAddJob__OneLessThanMaximumPendingJobs__ShouldReturnTrue() {
        // add 20 - 1 = 19 jobs to pending
        for (int i = 0; i < 19; i++) {
            Job generatedGenericJob = new Job("generatedGenericJob" + i);
            globalJobCoordinator.addPendingJob(generatedGenericJob);
        }

        assertTrue(globalJobCoordinator.hasSpaceToAddNewJob());
    }

    // iii) The system has exactly the maximum number of pending jobs
    @Test
    public final void canAddJob__ExactlyMaximumPendingJobs__ShouldReturnFalse() {
        // add exactly 20 (maximum) jobs to pending
        for (int i = 0; i < 20; i++) {
            Job generatedGenericJob = new Job("generatedGenericJob" + i);
            globalJobCoordinator.addPendingJob(generatedGenericJob);
        }

        assertFalse(globalJobCoordinator.hasSpaceToAddNewJob());
    }


    // Testing Park Manager Business Rule C
    // No job can be specified whose end date is more than the maximum number of days from the current date, default of 75


    // i) The specified job ends one fewer than the maximum number of days from the current date
    @Test
    public final void addPendingJob_JobEndsOneLessThanMaximumDays__ShouldAddJob() {
        Date currentDate = globalJobCoordinator.getCurrentDate().getTime();

        // create new date 74 days away
        long currentDateDaysPlus74Days = TimeUnit.DAYS.convert(currentDate.getTime(), TimeUnit.MILLISECONDS) + 74;
        long convertedCurrentDatePlus74 = TimeUnit.MILLISECONDS.convert(currentDateDaysPlus74Days, TimeUnit.DAYS);

        // create new calendar date to add to generic job
        GregorianCalendar newCalendarDate = new GregorianCalendar();
        newCalendarDate.setTime(new Date(convertedCurrentDatePlus74));

        // specify generic job on that day
        globalGenericJob.setStartDate(newCalendarDate);
        globalGenericJob.setEndDate(newCalendarDate);

        // attempt to add and test
        globalJobCoordinator.addPendingJob(globalGenericJob);
        assertTrue(globalJobCoordinator.getPendingJobs().contains(globalGenericJob));
    }

    // ii) The specified job ends the maximum number of days from the current date
    @Test
    public final void addPendingJob_JobEndsExactlyMaximumDaysAway__ShouldAddJob() {
        Date currentDate = globalJobCoordinator.getCurrentDate().getTime();

        // create new date 75 days away
        long currentDateDaysPlus75Days = TimeUnit.DAYS.convert(currentDate.getTime(), TimeUnit.MILLISECONDS) + 75;
        long convertedCurrentDatePlus75 = TimeUnit.MILLISECONDS.convert(currentDateDaysPlus75Days, TimeUnit.DAYS);

        // create new calendar date to add to generic job
        GregorianCalendar newCalendarDate = new GregorianCalendar();
        newCalendarDate.setTime(new Date(convertedCurrentDatePlus75));

        // specify generic job on that day
        globalGenericJob.setStartDate(newCalendarDate);
        globalGenericJob.setEndDate(newCalendarDate);

        // attempt to add and test
        globalJobCoordinator.addPendingJob(globalGenericJob);
        assertTrue(globalJobCoordinator.getPendingJobs().contains(globalGenericJob));
    }

    // iii) The specified job ends one more than the maximum number of days from the current date
    @Test
    public final void addPendingJob_JobEndsOneMoreThanMaximumDays__ShouldNotAddJob() {
        Date currentDate = globalJobCoordinator.getCurrentDate().getTime();

        // create new date 76 days away
        long currentDateDaysPlus76Days = TimeUnit.DAYS.convert(currentDate.getTime(), TimeUnit.MILLISECONDS) + 76;
        long convertedCurrentDatePlus76 = TimeUnit.MILLISECONDS.convert(currentDateDaysPlus76Days, TimeUnit.DAYS);

        // create new calendar date to add to generic job
        GregorianCalendar newCalendarDate = new GregorianCalendar();
        newCalendarDate.setTime(new Date(convertedCurrentDatePlus76));

        // specify generic job on that day
        globalGenericJob.setStartDate(newCalendarDate);
        globalGenericJob.setEndDate(newCalendarDate);

        // attempt to add and test
        globalJobCoordinator.addPendingJob(globalGenericJob);
        assertFalse(globalJobCoordinator.getPendingJobs().contains(globalGenericJob));
    }

}
