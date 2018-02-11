package test;

import model.Job;
import model.Volunteer;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.GregorianCalendar;

/*
Test for

    As a Volunteer I want to sign up for a job

    Rule:
    A volunteer cannot sign up for more than one job that extends across any particular day

 */
public class VolunteerTest_A {

    private Job job_start_0101_end_0101;
    private Job job_start_0102_end_0102;
    private Job job_start_0102_end_0103;
    private Job job_start_0103_end_0103;

    @Before
    public void setUp() {
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

    @Test
    /*
    Rule 1.a.i
    Volunteer has no jobs already signed up for
     */
    public void isSameDayConflict_VolunteerNoJobsYet_false() {
        Volunteer volunteer_no_jobs = new Volunteer("Volunteer With No Jobs");
        assertFalse(volunteer_no_jobs.isSameDayConflict(new Job("Pointless Job"), job_start_0101_end_0101));
    }

    @Test
    public void addToCurrentJobs_VolunteerNoJobsYet_true() {
        //Volunteer volunteer_no_jobs = new Volunteer("Volunteer With No Jobs");
        //assertTrue(volunteer_no_jobs.addToCurrentJobs(job_start_0101_end_0101));
    }

    @Test
    /*
    Rule 1.a.ii
    Volunteer has jobs already signed up for,
    this job does not extend across any days as jobs already signed up for
     */
    public void isEndDayConflict_VolunteerJobsDoesNotExtendToConflictJob_false() {
        Volunteer volunteer_no_conflict_jobs = new Volunteer("Volunteer With No Conflict");
        assertFalse(volunteer_no_conflict_jobs.isEndDayConflict(job_start_0101_end_0101, job_start_0103_end_0103));
    }

    @Test
    public void addToCurrentJobs_VolunteerJobsDoesNotExtendToConflictJob_true() {
        Volunteer volunteer_no_conflict_jobs = new Volunteer("Volunteer With No Conflict");
        volunteer_no_conflict_jobs.addToCurrentJobs(job_start_0101_end_0101);
        //assertTrue(volunteer_no_conflict_jobs.addToCurrentJobs(job_start_0103_end_0103));
    }


    @Test
    /*
    Rule 1.a.iii
    Volunteer has jobs already signed up for,
    this job starts the same day as the end of some job already signed up for
     */
    public void isSameDayConflict_VolunteerWithSameJob_true() {
        Volunteer volunter_with_same_day_conflict = new Volunteer("Volunteer With Same Day Conflict");
        assertTrue(volunter_with_same_day_conflict.isSameDayConflict(job_start_0102_end_0102, job_start_0102_end_0103));
    }

    @Test
    public void addToCurrentJobs_VolunteerWithSameJob_false() {
        Volunteer volunteer_with_same_day_conflict = new Volunteer("Volunteer With Same Day Conflict");
        volunteer_with_same_day_conflict.addToCurrentJobs(job_start_0102_end_0102);
        //assertFalse(volunteer_with_same_day_conflict.addToCurrentJobs(job_start_0102_end_0103));
    }

    @Test
    /*
    Rule 1.a.iv
    Volunteer has jobs already signed up for,
    this job ends the same day as the start of some job already signed up for
     */
    public void isEndDayConflict_VolunteerWithSameDayAsEndDay_true() {
        Volunteer volunter_with_end_day_conflict = new Volunteer("Volunteer With Same Day Conflict");
        assertTrue(volunter_with_end_day_conflict.isEndDayConflict(job_start_0102_end_0103, job_start_0103_end_0103));
    }

    @Test
    public void addToCurrentJobs_VolunteerWithSameDayAsEndDay_false() {
        Volunteer volunteer_with_end_day_conflict = new Volunteer("Volunteer With Same Day Conflict");
        volunteer_with_end_day_conflict.addToCurrentJobs(job_start_0102_end_0103);
        //assertFalse(volunteer_with_end_day_conflict.addToCurrentJobs(job_start_0103_end_0103));
    }
}