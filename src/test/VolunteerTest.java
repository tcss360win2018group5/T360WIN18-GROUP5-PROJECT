
package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import model.Job;
import model.JobCoordinator;
import model.SystemCoordinator;
import model.Volunteer;



public class VolunteerTest {
    public JobCoordinator globalJobCoordinator;
    public SystemCoordinator globalSystemCoordinator;
    public Volunteer anyVolunteer;
    public GregorianCalendar today;
    private Job job_start_0101_end_0101;
    private Job job_start_0102_end_0102;
    private Job job_start_0102_end_0103;
    private Job job_start_0103_end_0103;
    private Job job_today;
    private Job job_starts_day_prior;
    private Job job_starts_day_greater_than_min_days;
    private Job job_starts_day_at_min_days;


    @Before
    public void setUp() {
        globalSystemCoordinator = new SystemCoordinator();
        globalJobCoordinator = new JobCoordinator(globalSystemCoordinator);
        anyVolunteer = new Volunteer("SomeOldVolunteer");
        today = globalJobCoordinator.getCurrentDate();

        job_start_0101_end_0101 = new Job("Job Start 0101 End 0101");
        job_start_0101_end_0101.setStartDate(new GregorianCalendar(2018, 01, 01));
        job_start_0101_end_0101.setEndDate(new GregorianCalendar(2018, 01, 01));

        job_start_0102_end_0102 = new Job("Job Start 0102 End 0102");
        job_start_0102_end_0102.setStartDate(new GregorianCalendar(2018, 01, 02));
        job_start_0102_end_0102.setEndDate(new GregorianCalendar(2018, 01, 02));

        job_start_0102_end_0103 = new Job("Job Start 0102 End 0103");
        job_start_0102_end_0103.setStartDate(new GregorianCalendar(2018, 01, 02));
        job_start_0102_end_0103.setEndDate(new GregorianCalendar(2018, 01, 03));

        job_start_0103_end_0103 = new Job("Job Start 0103 End 0103");
        job_start_0103_end_0103.setStartDate(new GregorianCalendar(2018, 01, 03));
        job_start_0103_end_0103.setEndDate(new GregorianCalendar(2018, 01, 03));

        job_today = new Job("Job Today");
        job_today.setStartDate(new GregorianCalendar(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)));
        job_today.setEndDate(new GregorianCalendar(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)));

        job_starts_day_prior = new Job("Job Day Prior");
        job_starts_day_prior.setStartDate(new GregorianCalendar
                (today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)-1));
        job_starts_day_prior.setEndDate(new GregorianCalendar(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)));

        job_starts_day_greater_than_min_days = new Job("Job Greater than Min");
        job_starts_day_greater_than_min_days.setStartDate(new GregorianCalendar(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH) + Volunteer.MINIMUM_DAYS_BEFORE_JOB_START + 1));
        job_starts_day_greater_than_min_days.setEndDate(new GregorianCalendar(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH) + Volunteer.MINIMUM_DAYS_BEFORE_JOB_START + 1));
        job_starts_day_at_min_days = new Job("Job Greater than Min");
        job_starts_day_at_min_days.setStartDate(new GregorianCalendar(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH) + Volunteer.MINIMUM_DAYS_BEFORE_JOB_START));
        job_starts_day_at_min_days.setEndDate(new GregorianCalendar(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH) + Volunteer.MINIMUM_DAYS_BEFORE_JOB_START));
    }

    /*
     * Business Rule: A volunteer cannot sign up for more than one job that
     * extends across any particular day
     */
    /*
     * Volunteer has no jobs already signed up for
     */
    @Test
    public void canApplyToJob_VolunteerNoJobsYet_True() {
        Volunteer volunteer_no_jobs = new Volunteer("Volunteer With No Jobs");
        volunteer_no_jobs.setCurrentDate(new GregorianCalendar(2018, 01, 00));
        Job pointlessButValidJob = new Job("Pointless But Valid Job");
        pointlessButValidJob.setStartDate(new GregorianCalendar(2018, 01, 02));
        assertTrue(volunteer_no_jobs.canApplyToJob(pointlessButValidJob) == 0);
    }

    /*
     * Volunteer has jobs already signed up for, this job does not extend across
     * any days as jobs already signed up for
     */
    @Test
    public void canApplyToJob_VolunteerJobsDoesNotExtendToConflictJob_True() {
        Volunteer volunteer_no_conflict_jobs = new Volunteer("Volunteer With No Conflict");
        volunteer_no_conflict_jobs.setCurrentDate(new GregorianCalendar(2018, 01, 00));
        volunteer_no_conflict_jobs.applyToJob(job_start_0101_end_0101);

        assertTrue(volunteer_no_conflict_jobs.canApplyToJob(job_start_0103_end_0103) == 0);
    }

    /*
     * Volunteer has jobs already signed up for, this job starts the same day as
     * the end of some job already signed up for
     */
    @Test
    public void canApplyToJob_VolunteerWithSameJob_False() {
        Volunteer volunteer_with_same_day_conflict =
                        new Volunteer("Volunteer With Same Day Conflict");
        volunteer_with_same_day_conflict.setCurrentDate(new GregorianCalendar(2018, 01, 00));
        volunteer_with_same_day_conflict.applyToJob(job_start_0102_end_0102);

        assertFalse(volunteer_with_same_day_conflict
                        .canApplyToJob(job_start_0102_end_0103) == 0);
    }

    /*
     * Volunteer has jobs already signed up for, this job ends the same day as
     * the start of some job already signed up for
     */
    @Test
    public void canApplyToJob_VolunteerWithSameDayAsEndDay_False() {
        Volunteer volunteer_with_end_day_conflict =
                        new Volunteer("Volunteer With Same Day Conflict");
        volunteer_with_end_day_conflict.setCurrentDate(new GregorianCalendar(2018, 01, 00));
        volunteer_with_end_day_conflict.applyToJob(job_start_0102_end_0103);

        assertFalse(volunteer_with_end_day_conflict
                        .canApplyToJob(job_start_0103_end_0103) == 0);
    }

    /*
     * Business Rule: A volunteer may sign up only if the job begins at least a
     * minimum number of calendar days after the current date
     */
    // Volunteer signs up for job that begins much more than the minimum number
    // of
    // calendar days from the current date
    @Test
    public final void canApplyToJob_MoreThanMinimumDaysAway_True() {
        Job jobMoreThanMinimumDaysAway = new Job("A Job");
        GregorianCalendar dateMoreThanMinimumDaysFromToday = (GregorianCalendar) today.clone();
        dateMoreThanMinimumDaysFromToday.add(GregorianCalendar.DAY_OF_YEAR, 3);

        jobMoreThanMinimumDaysAway.setStartDate(dateMoreThanMinimumDaysFromToday);
        jobMoreThanMinimumDaysAway.setEndDate(dateMoreThanMinimumDaysFromToday);

        assertTrue(anyVolunteer.canApplyToJob(jobMoreThanMinimumDaysAway) == 0);
    }

    // Volunteer signs up for job that begins exactly the minimum number of
    // calendar days
    // from the current date
    @Test
    public final void canApplyToJob_ExactlyMinimumDaysAway_True() {
        Job jobExactlyMinimumDaysAway = new Job("A Job");
        GregorianCalendar dateExactlyMinimumDaysAwayFromToday =
                        (GregorianCalendar) today.clone();
        dateExactlyMinimumDaysAwayFromToday.add(GregorianCalendar.DAY_OF_YEAR, 2);

        jobExactlyMinimumDaysAway.setStartDate(dateExactlyMinimumDaysAwayFromToday);
        jobExactlyMinimumDaysAway.setEndDate(dateExactlyMinimumDaysAwayFromToday);

        assertTrue(anyVolunteer.canApplyToJob(jobExactlyMinimumDaysAway) == 0);
    }

    // Volunteer signs up for job that begins less than the minimum number of
    // calendar days
    // from the current date
    @Test
    public final void canApplyToJob_LessThanMinimumDaysAwayFromToday_False() {
        Job jobLessThanMinimumDaysAway = new Job("A Job");
        GregorianCalendar dateLessThanMinimumDaysAwayFromToday =
                        (GregorianCalendar) today.clone();
        dateLessThanMinimumDaysAwayFromToday.add(GregorianCalendar.DAY_OF_YEAR, 1);

        jobLessThanMinimumDaysAway.setStartDate(dateLessThanMinimumDaysAwayFromToday);
        jobLessThanMinimumDaysAway.setEndDate(dateLessThanMinimumDaysAwayFromToday);

        assertFalse(anyVolunteer.canApplyToJob(jobLessThanMinimumDaysAway) == 0);
    }

//    @Test
//    public final void doesJobStartOnCurrentDay_JobStartsOnCurrentDay_True() {
//        assertTrue(anyVolunteer.doesJobStartOnCurrentDay(job_today));
//    }
//
//    @Test
//    public final void doesMultiJobStartPriorToCurrentDay_JobStartsDayPrior_True() {
//        assertTrue(anyVolunteer.doesMultiJobStartPriorToCurrentDay(job_starts_day_prior));
//    }
//
//    @Test
//    public final void doesJobStartMoreThanMinDay_JobisMoreThanMinDays_True() {
//        assertTrue(anyVolunteer.doesJobStartMoreThanMinDay(job_starts_day_greater_than_min_days));
//    }
//
//    @Test
//    public final void doesJobStartMoreThanMinDay_JobisExactAtMinDays_True() {
//        assertTrue(anyVolunteer.doesJobStartMoreThanMinDay(job_starts_day_at_min_days));
//    }
//
//    @Test
//    public final void unvolunteerJob_JobPassesAllTests_True() {
//        anyVolunteer.applyToJob(job_starts_day_at_min_days);
//        assertTrue(anyVolunteer.unvolunteerJob(job_starts_day_at_min_days) == 0);
//    }


    @Test
    public final void methodTesting() {
    }
}
