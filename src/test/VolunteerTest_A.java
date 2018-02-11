package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import model.Job;
import model.Volunteer;

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

    /*
    Rule 1.a.i
    Volunteer has no jobs already signed up for
     */
    @Test
    public void canSignUpForJob_VolunteerNoJobsYet_ShouldBeTrue() {
        Volunteer volunteer_no_jobs = new Volunteer("Volunteer With No Jobs");
        Job pointlessButValidJob = new Job("Pointless But Valid Job");
        
        assertTrue(volunteer_no_jobs.canSignUpForJob(pointlessButValidJob) == 0);
    }
    
    /*
    Rule 1.a.ii
    Volunteer has jobs already signed up for,
    this job does not extend across any days as jobs already signed up for
     */
    @Test
    public void canSignUpForJob_VolunteerJobsDoesNotExtendToConflictJob_ShouldBeTrue() {
        Volunteer volunteer_no_conflict_jobs = new Volunteer("Volunteer With No Conflict");
        volunteer_no_conflict_jobs.signUpForJob(job_start_0101_end_0101);
        
        assertTrue(volunteer_no_conflict_jobs.canSignUpForJob(job_start_0103_end_0103) == 0);
    }

    /*
    Rule 1.a.iii
    Volunteer has jobs already signed up for,
    this job starts the same day as the end of some job already signed up for
     */
    @Test
    public void canSignUpForJob_VolunteerWithSameJob_ShouldBeFalse() {
        Volunteer volunteer_with_same_day_conflict = new Volunteer("Volunteer With Same Day Conflict");
        volunteer_with_same_day_conflict.signUpForJob(job_start_0102_end_0102);
        
        assertFalse(volunteer_with_same_day_conflict.canSignUpForJob(job_start_0102_end_0103) 
                    == 0);
    }

    /*
    Rule 1.a.iv
    Volunteer has jobs already signed up for,
    this job ends the same day as the start of some job already signed up for
     */
    @Test
    public void canSignUpForJob_VolunteerWithSameDayAsEndDay_ShouldBeFalse() {
        Volunteer volunteer_with_end_day_conflict = new Volunteer("Volunteer With Same Day Conflict");
        volunteer_with_end_day_conflict.signUpForJob(job_start_0102_end_0103);
        
        assertFalse(volunteer_with_end_day_conflict.canSignUpForJob(job_start_0103_end_0103) 
                   == 0);
    }
}