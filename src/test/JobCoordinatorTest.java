package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import model.Job;
import model.JobCoordinator;
import model.OfficeStaff;
import model.ParkManager;
import model.SystemCoordinator;
import model.Volunteer;

public class JobCoordinatorTest {
    public JobCoordinator globalJobCoordinator;
    public SystemCoordinator globalSystemCoordinator;
    
    public Volunteer globalVolunteerJane;
    public ParkManager globalParkManagerSam;
    public OfficeStaff globalOfficeStaffAlex;
    
    public Job anyOldValidJob;
    public ParkManager anyParkManager;


    @Before
    public void setUp() throws Exception {
        globalSystemCoordinator = new SystemCoordinator();
        globalJobCoordinator = new JobCoordinator(globalSystemCoordinator);
        
        globalVolunteerJane = new Volunteer("Jane");
        globalVolunteerJane.setCurrentDate(globalJobCoordinator.getCurrentDate());
        
        globalParkManagerSam = new ParkManager("Sam");
        globalVolunteerJane.setCurrentDate(globalJobCoordinator.getCurrentDate());
        
        globalOfficeStaffAlex = new OfficeStaff("Alex");
        globalVolunteerJane.setCurrentDate(globalJobCoordinator.getCurrentDate());
        
        anyOldValidJob = new Job("anyOldValidJob");
        anyParkManager = new ParkManager("anyOldPM");
    }

    // Business Rule:
    // There can not be more than the maximum number of pending jobs at a time
    // in the entire system, default of 20

    // The system has far fewer than the maximum number of pending jobs
    @Test
    public final void hasSpaceToAddJobs_LessThanMaximumPendingJobs_ShouldReturnTrue() {
        // add 0 or 1 job so that # current jobs < maximum jobs
        globalJobCoordinator.submitJob(globalParkManagerSam, anyOldValidJob);

        assertTrue(globalJobCoordinator.hasSpaceToAddJobs());
    }

    // The system has one fewer than the maximum number of pending jobs
    @Test
    public final void hasSpaceToAddJobs__OneLessThanMaximumPendingJobs__ShouldReturnTrue() {
        // add 20 - 1 = 19 jobs to pending
        for (int i = 0; i < globalJobCoordinator.getCurrentMaximumJobs() - 1; i++) {
            Job generatedValidJob = new Job("generatedValidJob" + i);
            globalJobCoordinator.submitJob(globalParkManagerSam, generatedValidJob);
        }

        assertTrue(globalJobCoordinator.hasSpaceToAddJobs());
    }

    // The system has exactly the maximum number of pending jobs
    @Test
    public final void hasSpaceToAddJobs__ExactlyMaximumPendingJobs__ShouldReturnFalse() {
        // add exactly 20 (maximum) jobs to pending
        for (int i = 0; i < 20; i++) {
            Job generatedValidJob = new Job("generatedValidJob" + i);
            globalJobCoordinator.submitJob(globalParkManagerSam, generatedValidJob);
        }

        assertFalse(globalJobCoordinator.hasSpaceToAddJobs());
    }
    
    // Business Rule:
    // No job can be specified that takes more than the maximum number of days
    
    // The specified job takes one fewer than the maximum number of days
    @Test
    public final void canAddJob_JobOneLessThanMaxJobLength_ShouldBeTrue() {
        GregorianCalendar currentDate = globalJobCoordinator.getCurrentDate();

        // create new calendar date to add to generic job
        GregorianCalendar validDateOneWeekAhead = (GregorianCalendar) currentDate.clone();
        validDateOneWeekAhead.add(GregorianCalendar.DAY_OF_YEAR, 7);
        GregorianCalendar validDateOneWeekAheadPlusOneLessThanMaxLength = 
                        (GregorianCalendar) validDateOneWeekAhead.clone();
        validDateOneWeekAheadPlusOneLessThanMaxLength
            .add(GregorianCalendar.DAY_OF_YEAR, JobCoordinator.MAXIMUM_JOB_LENGTH - 1);
        
        Job lessThanMaxLengthJob = new Job("lessThanMaxLengthJob");
        lessThanMaxLengthJob.setStartDate(validDateOneWeekAhead);
        lessThanMaxLengthJob.setEndDate(validDateOneWeekAheadPlusOneLessThanMaxLength);
        
        assertTrue(globalJobCoordinator.canSubmitJob(lessThanMaxLengthJob) == 0);
        
    }
    
    // The specified job takes the maximum number of days
    @Test
    public final void canAddJob_JobMaxJobLength_ShouldBeTrue() {        
        GregorianCalendar currentDate = globalJobCoordinator.getCurrentDate();

        // create new calendar date to add to generic job
        GregorianCalendar validDateOneWeekAhead = (GregorianCalendar) currentDate.clone();
        validDateOneWeekAhead.add(GregorianCalendar.DAY_OF_YEAR, 7);
        GregorianCalendar validDateOneWeekAheadPlusMaxLength = 
                        (GregorianCalendar) validDateOneWeekAhead.clone();
        validDateOneWeekAheadPlusMaxLength
            .add(GregorianCalendar.DAY_OF_YEAR, JobCoordinator.MAXIMUM_JOB_LENGTH);
        
        Job exactlyMaxLengthJob = new Job("exactlyMaxLengthJob");
        exactlyMaxLengthJob.setStartDate(validDateOneWeekAhead);
        exactlyMaxLengthJob.setEndDate(validDateOneWeekAheadPlusMaxLength);
        
        assertTrue(globalJobCoordinator.canSubmitJob(exactlyMaxLengthJob) == 0);
    }
    
    // The specified job takes one more than the maximum number of days
    @Test
    public final void canAddJob_JobOneMoreThanMaxJobLength_ShouldBeTrue() {
        GregorianCalendar currentDate = globalJobCoordinator.getCurrentDate();

        // create new calendar date to add to generic job
        GregorianCalendar validDateOneWeekAhead = (GregorianCalendar) currentDate.clone();
        validDateOneWeekAhead.add(GregorianCalendar.DAY_OF_YEAR, 7);
        GregorianCalendar validDateOneWeekAheadPlusOneMoreThanMaxLength = 
                        (GregorianCalendar) validDateOneWeekAhead.clone();
        validDateOneWeekAheadPlusOneMoreThanMaxLength
            .add(GregorianCalendar.DAY_OF_YEAR, JobCoordinator.MAXIMUM_JOB_LENGTH + 1);
        
        Job moreThanMaxLengthJob = new Job("moreThanMaxLengthJob");
        moreThanMaxLengthJob.setStartDate(validDateOneWeekAhead);
        moreThanMaxLengthJob.setEndDate(validDateOneWeekAheadPlusOneMoreThanMaxLength);

        assertFalse(globalJobCoordinator.canSubmitJob(moreThanMaxLengthJob) == 0);
        
    }

    // Business Rule:
    // No job can be specified whose end date is more than the maximum number of
    // days from the current date, default of 75

    // The specified job ends one fewer than the maximum number of days from the
    // current date
    @Test
    public final void canAddJob_JobEndsOneLessThanMaximumDays__ShouldBeTrue() {
        GregorianCalendar currentDate = globalJobCoordinator.getCurrentDate();

        // create new calendar date to add to generic job
        GregorianCalendar oneLessThanMaximumDaysAwayDate =
                        (GregorianCalendar) currentDate.clone();
        oneLessThanMaximumDaysAwayDate.add(GregorianCalendar.DAY_OF_YEAR,
                                           JobCoordinator.MAXIMUM_DAYS_AWAY_TO_POST_JOB - 1);

        // specify generic job on that day
        Job jobOneLessThanMaximumDaysAway = new Job("jobOneLessThanMaximumDaysAway");
        jobOneLessThanMaximumDaysAway.setStartDate(oneLessThanMaximumDaysAwayDate);
        jobOneLessThanMaximumDaysAway.setEndDate(oneLessThanMaximumDaysAwayDate);

        // test
        assertTrue(globalJobCoordinator.canSubmitJob(jobOneLessThanMaximumDaysAway) == 0);
    }

    // The specified job ends the maximum number of days from the current date
    @Test
    public final void canAddJob_JobEndsExactlyMaximumDaysAway__ShouldBeTrue() {
        GregorianCalendar currentDate = globalJobCoordinator.getCurrentDate();

        // create new calendar date to add to generic job
        GregorianCalendar exactlyMaximumDaysAwayDate = (GregorianCalendar) currentDate.clone();
        exactlyMaximumDaysAwayDate.add(GregorianCalendar.DAY_OF_YEAR,
                                       JobCoordinator.MAXIMUM_DAYS_AWAY_TO_POST_JOB);

        // specify generic job on that day
        Job jobExactlyMaximumDaysAway = new Job("jobExactlyMaximumDaysAway");
        jobExactlyMaximumDaysAway.setStartDate(exactlyMaximumDaysAwayDate);
        jobExactlyMaximumDaysAway.setEndDate(exactlyMaximumDaysAwayDate);

        // test
        assertTrue(globalJobCoordinator.canSubmitJob(jobExactlyMaximumDaysAway) == 0);
    }

    // The specified job ends one more than the maximum number of days from the
    // current date
    @Test
    public final void canAddJob_JobEndsOneMoreThanMaximumDays__ShouldBeFalse() {
        GregorianCalendar currentDate = globalJobCoordinator.getCurrentDate();

        // create new calendar date to add to generic job
        GregorianCalendar oneMoreThanMaximumDaysAwayDate =
                        (GregorianCalendar) currentDate.clone();
        oneMoreThanMaximumDaysAwayDate.add(GregorianCalendar.DAY_OF_YEAR,
                                           JobCoordinator.MAXIMUM_DAYS_AWAY_TO_POST_JOB + 1);

        // specify generic job on that day
        Job jobOneMoreThanMaximumDaysAway = new Job("jobOneMoreThanMaximumDaysAway");
        jobOneMoreThanMaximumDaysAway.setStartDate(oneMoreThanMaximumDaysAwayDate);
        jobOneMoreThanMaximumDaysAway.setEndDate(oneMoreThanMaximumDaysAwayDate);

        // test
        assertFalse(globalJobCoordinator.canSubmitJob(jobOneMoreThanMaximumDaysAway) == 0);
    }
    
    
    @Test
    public final void getJobListing_NoJobsWithAnyUser_ShouldBeEmpty() {
        assertTrue(globalJobCoordinator.getSystemJobListing(globalVolunteerJane).isEmpty());
        assertTrue(globalJobCoordinator.getSystemJobListing(globalParkManagerSam).isEmpty());
        assertTrue(globalJobCoordinator.getSystemJobListing(globalOfficeStaffAlex).isEmpty());
    }
    
    @Test
    public final void getJobListing_OneFutureJobWithVolunteer_ShouldOnlyHaveFutureJob() {
        Job futureJob = new Job("Job in the Future");
        GregorianCalendar futureDate = new GregorianCalendar();
        futureDate.add(GregorianCalendar.DAY_OF_YEAR, 7);
        futureJob.setStartDate(futureDate);
        futureJob.setEndDate(futureDate);
        futureJob.setMaxVolunteers(10);
        globalJobCoordinator.submitJob(globalParkManagerSam, futureJob);

        assertTrue(globalJobCoordinator.getSystemJobListing(globalVolunteerJane).contains(futureJob));
    }
    
    @Test
    public final void getJobListing_OnePastJobWithVolunteer_ShouldBeEmpty() {
        Job pastJob = new Job("Job in the Past");
        GregorianCalendar pastDate = new GregorianCalendar();
        pastDate.add(GregorianCalendar.DAY_OF_YEAR, -7);
        pastJob.setStartDate(pastDate);
        pastJob.setEndDate(pastDate);
        globalJobCoordinator.submitJob(globalParkManagerSam, pastJob);
        assertTrue(globalJobCoordinator.getSystemJobListing(globalVolunteerJane).isEmpty());
    }
    
    @Test
    public final void getJobListing_OneFutureOnePastJobWithVolunteer_ShouldOnlyHaveFutureJob() {
        Job futureJob = new Job("Job in the Future");
        GregorianCalendar futureDate = new GregorianCalendar();
        futureDate.add(GregorianCalendar.DAY_OF_YEAR, 7);
        futureJob.setStartDate(futureDate);
        futureJob.setEndDate(futureDate);
        futureJob.setMaxVolunteers(10);
        globalJobCoordinator.submitJob(globalParkManagerSam, futureJob);
        
        Job pastJob = new Job("Job in the Past");
        GregorianCalendar pastDate = new GregorianCalendar();
        pastDate.add(GregorianCalendar.DAY_OF_YEAR, -7);
        pastJob.setStartDate(pastDate);
        pastJob.setEndDate(pastDate);
        futureJob.setMaxVolunteers(10);
        globalJobCoordinator.submitJob(globalParkManagerSam, pastJob);

        assertFalse(globalJobCoordinator.getSystemJobListing(globalVolunteerJane).contains(pastJob));
        assertTrue(globalJobCoordinator.getSystemJobListing(globalVolunteerJane).contains(futureJob));
    }

    @Test
    public final void getJobListing_OneFutureJobWithParkManager_ShouldOnlyHaveFutureJob() {
        Job futureJob = new Job("Job in the Future");
        GregorianCalendar futureDate = new GregorianCalendar();
        futureDate.add(GregorianCalendar.DAY_OF_YEAR, 7);
        futureJob.setStartDate(futureDate);
        futureJob.setEndDate(futureDate);
        globalJobCoordinator.submitJob(globalParkManagerSam, futureJob);

        assertTrue(globalJobCoordinator.getSystemJobListing(globalParkManagerSam).contains(futureJob));
    }
    
    @Test
    public final void getJobListing_OnePastJobWithParkManager_ShouldBeEmpty() {        
        Job pastJob = new Job("Job in the Past");
        GregorianCalendar pastDate = new GregorianCalendar();
        pastDate.add(GregorianCalendar.DAY_OF_YEAR, -7);
        pastJob.setStartDate(pastDate);
        pastJob.setEndDate(pastDate);
        globalJobCoordinator.submitJob(globalParkManagerSam, pastJob);
        
        assertTrue(globalJobCoordinator.getSystemJobListing(globalParkManagerSam).isEmpty());
    }
    
    @Test
    public final void getJobListing_OneFutureOnePastJobWithParkManager_ShouldOnlyHaveFutureJob() {
        Job futureJob = new Job("Job in the Future");
        GregorianCalendar futureDate = new GregorianCalendar();
        futureDate.add(GregorianCalendar.DAY_OF_YEAR, 7);
        futureJob.setStartDate(futureDate);
        futureJob.setEndDate(futureDate);
        globalJobCoordinator.submitJob(globalParkManagerSam, futureJob);
        
        Job pastJob = new Job("Job in the Past");
        GregorianCalendar pastDate = new GregorianCalendar();
        pastDate.add(GregorianCalendar.DAY_OF_YEAR, -7);
        pastJob.setStartDate(pastDate);
        pastJob.setEndDate(pastDate);
        globalJobCoordinator.submitJob(globalParkManagerSam, pastJob);

        assertFalse(globalJobCoordinator.getSystemJobListing(globalParkManagerSam).contains(pastJob));
        assertTrue(globalJobCoordinator.getSystemJobListing(globalParkManagerSam).contains(futureJob));
    }
    
    @Test
    public final void getJobListing_OneFutureJobWithOfficeStaff_ShouldOnlyHaveFutureJob() {
        Job futureJob = new Job("Job in the Future");
        GregorianCalendar futureDate = new GregorianCalendar();
        futureDate.add(GregorianCalendar.DAY_OF_YEAR, 7);
        futureJob.setStartDate(futureDate);
        futureJob.setEndDate(futureDate);
        globalJobCoordinator.submitJob(globalParkManagerSam, futureJob);
        
        assertTrue(globalJobCoordinator.getSystemJobListing(globalOfficeStaffAlex).contains(futureJob));
    }
    
    @Test
    public final void getJobListing_OnePastJobWithOfficeStaff_ShouldOnlyHavePastJob() {
        Job pastJob = new Job("Job in the Past");
        GregorianCalendar pastDate = new GregorianCalendar();
        pastDate.add(GregorianCalendar.DAY_OF_YEAR, -7);
        pastJob.setStartDate(pastDate);
        pastJob.setEndDate(pastDate);
        globalJobCoordinator.submitJob(globalParkManagerSam, pastJob);

        assertTrue(globalJobCoordinator.getSystemJobListing(globalOfficeStaffAlex).contains(pastJob));
    }
    
    @Test
    public final void getJobListing_OneFutureOnePastJobWithOfficeStaff_ShouldHaveBothJobs() {
        Job futureJob = new Job("Job in the Future");
        GregorianCalendar futureDate = new GregorianCalendar();
        futureDate.add(GregorianCalendar.DAY_OF_YEAR, 7);
        futureJob.setStartDate(futureDate);
        futureJob.setEndDate(futureDate);
        globalJobCoordinator.submitJob(globalParkManagerSam, futureJob);
        
        Job pastJob = new Job("Job in the Past");
        GregorianCalendar pastDate = new GregorianCalendar();
        pastDate.add(GregorianCalendar.DAY_OF_YEAR, -7);
        pastJob.setStartDate(pastDate);
        pastJob.setEndDate(pastDate);
        globalJobCoordinator.submitJob(globalParkManagerSam, pastJob);

        assertTrue(globalJobCoordinator.getSystemJobListing(globalOfficeStaffAlex).contains(pastJob));
        assertTrue(globalJobCoordinator.getSystemJobListing(globalOfficeStaffAlex).contains(futureJob));
    }
}
