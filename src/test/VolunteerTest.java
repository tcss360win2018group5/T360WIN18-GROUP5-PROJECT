package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import model.Job;
import model.JobCoordinator;
import model.Volunteer;

public class VolunteerTest {
    public JobCoordinator globalJobCoordinator;
    public Volunteer validAnyOldVolunteer;
    public GregorianCalendar today;
    private Job job_start_0101_end_0101;
    private Job job_start_0102_end_0102;
    private Job job_start_0102_end_0103;
    private Job job_start_0103_end_0103;


    @Before
    public void setUp() {
        globalJobCoordinator = new JobCoordinator();
        validAnyOldVolunteer = new Volunteer("SomeOldVolunteer");
        today = globalJobCoordinator.getCurrentDate();
        validAnyOldVolunteer.setCurrentDay(today);
        
        job_start_0101_end_0101 = new Job("Job Start 0101 End 0101");
        job_start_0101_end_0101.setStartDate(new GregorianCalendar(2018,01,01));
        job_start_0101_end_0101.setEndDate(new GregorianCalendar(2018,01,01));

        job_start_0102_end_0102 = new Job("Job Start 0102 End 0102");
        job_start_0102_end_0102.setStartDate(new GregorianCalendar(2018,01,02));
        job_start_0102_end_0102.setEndDate(new GregorianCalendar(2018,01,02));

        job_start_0102_end_0103 = new Job("Job Start 0102 End 0103");
        job_start_0102_end_0103.setStartDate(new GregorianCalendar(2018,01,02));
        job_start_0102_end_0103.setEndDate(new GregorianCalendar(2018,01,03));

        job_start_0103_end_0103 = new Job("Job Start 0103 End 0103");
        job_start_0103_end_0103.setStartDate(new GregorianCalendar(2018,01,03));
        job_start_0103_end_0103.setEndDate(new GregorianCalendar(2018,01,03));
    }

    
    /* 
     * Business Rule:
     * A volunteer cannot sign up for more than one job that extends across any particular day
     */
    /*
    Volunteer has no jobs already signed up for
     */
    @Test
    public void canSignUpForJob_VolunteerNoJobsYet_ShouldBeTrue() {
        Volunteer volunteer_no_jobs = new Volunteer("Volunteer With No Jobs");
        volunteer_no_jobs.setCurrentDay(new GregorianCalendar(2018,01,00));
        Job pointlessButValidJob = new Job("Pointless But Valid Job");
        pointlessButValidJob.setStartDate(new GregorianCalendar(2018,01,02));
        assertTrue(volunteer_no_jobs.canSignUpForJob(pointlessButValidJob) == 0);
    }
    
    /*
    Volunteer has jobs already signed up for,
    this job does not extend across any days as jobs already signed up for
     */
    @Test
    public void canSignUpForJob_VolunteerJobsDoesNotExtendToConflictJob_ShouldBeTrue() {
        Volunteer volunteer_no_conflict_jobs = new Volunteer("Volunteer With No Conflict");
        volunteer_no_conflict_jobs.setCurrentDay(new GregorianCalendar(2018,01,00));
        volunteer_no_conflict_jobs.signUpForJob(job_start_0101_end_0101);
        
        assertTrue(volunteer_no_conflict_jobs.canSignUpForJob(job_start_0103_end_0103) == 0);
    }

    /*
    Volunteer has jobs already signed up for,
    this job starts the same day as the end of some job already signed up for
     */
    @Test
    public void canSignUpForJob_VolunteerWithSameJob_ShouldBeFalse() {
        Volunteer volunteer_with_same_day_conflict = new Volunteer("Volunteer With Same Day Conflict");
        volunteer_with_same_day_conflict.setCurrentDay(new GregorianCalendar(2018,01,00));
        volunteer_with_same_day_conflict.signUpForJob(job_start_0102_end_0102);
        
        assertFalse(volunteer_with_same_day_conflict.canSignUpForJob(job_start_0102_end_0103) 
                    == 0);
    }

    /*
    Volunteer has jobs already signed up for,
    this job ends the same day as the start of some job already signed up for
     */
    @Test
    public void canSignUpForJob_VolunteerWithSameDayAsEndDay_ShouldBeFalse() {
        Volunteer volunteer_with_end_day_conflict = new Volunteer("Volunteer With Same Day Conflict");
        volunteer_with_end_day_conflict.setCurrentDay(new GregorianCalendar(2018,01,00));
        volunteer_with_end_day_conflict.signUpForJob(job_start_0102_end_0103);
        
        assertFalse(volunteer_with_end_day_conflict.canSignUpForJob(job_start_0103_end_0103) 
                   == 0);
    }

    /* Business Rule:
     * A volunteer may sign up only if the job begins at least a minimum number of 
     * calendar days after the current date
     */
    // Volunteer signs up for job that begins much more than the minimum number of 
    // calendar days from the current date
    @Test
    public final void canSignUpForJob_MoreThanMinimumDaysAway_ShouldReturnTrue() {
        Job jobMoreThanMinimumDaysAway = new Job("A Job");
        GregorianCalendar dateMoreThanMinimumDaysFromToday = (GregorianCalendar) today.clone();
        dateMoreThanMinimumDaysFromToday.add(GregorianCalendar.DAY_OF_YEAR, 3);
        
        jobMoreThanMinimumDaysAway.setStartDate(dateMoreThanMinimumDaysFromToday);
        jobMoreThanMinimumDaysAway.setEndDate(dateMoreThanMinimumDaysFromToday);
        
        assertTrue(validAnyOldVolunteer.canSignUpForJob(jobMoreThanMinimumDaysAway) == 0);
    }

    // Volunteer signs up for job that begins exactly the minimum number of calendar days 
    // from the current date
    @Test
    public final void canSignUpForJob_ExactlyMinimumDaysAway_ShouldReturnTrue() {
        Job jobExactlyMinimumDaysAway = new Job("A Job");
        GregorianCalendar dateExactlyMinimumDaysAwayFromToday = (GregorianCalendar) today.clone();
        dateExactlyMinimumDaysAwayFromToday.add(GregorianCalendar.DAY_OF_YEAR, 2);
        
        jobExactlyMinimumDaysAway.setStartDate(dateExactlyMinimumDaysAwayFromToday);
        jobExactlyMinimumDaysAway.setEndDate(dateExactlyMinimumDaysAwayFromToday);
        
        assertTrue(validAnyOldVolunteer.canSignUpForJob(jobExactlyMinimumDaysAway) == 0);
    }
    // Volunteer signs up for job that begins less than the minimum number of calendar days
    // from the current date
    @Test
    public final void canSignUpForJob_LessThanMinimumDaysAwayFromToday_ShouldReturnFalse() {
        Job jobLessThanMinimumDaysAway = new Job("A Job");
        GregorianCalendar dateLessThanMinimumDaysAwayFromToday = (GregorianCalendar) today.clone();
        dateLessThanMinimumDaysAwayFromToday.add(GregorianCalendar.DAY_OF_YEAR, 1);
        
        jobLessThanMinimumDaysAway.setStartDate(dateLessThanMinimumDaysAwayFromToday);
        jobLessThanMinimumDaysAway.setEndDate(dateLessThanMinimumDaysAwayFromToday);
        
        assertFalse(validAnyOldVolunteer.canSignUpForJob(jobLessThanMinimumDaysAway) == 0);
    }
}
