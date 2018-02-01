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

    private Job job0101;
    private Job job0101_conflict;

    private Job job0102;
    private Job job0103;

    private Volunteer volunter_test;

    @Before
    public void setUp() {
        job0101 = new Job("Job 0101");
        job0101.setDate(new GregorianCalendar(2018,01,01));
        job0101_conflict = new Job("Job 0101 Conflict");
        job0101_conflict.setDate(new GregorianCalendar(2018,01,01));

        job0102 = new Job("Job 0102");
        job0102.setDate(new GregorianCalendar(2018,01,02));
        job0103 = new Job("Job 0103");
        job0103.setDate(new GregorianCalendar(2018,01,03));

        volunter_test = new Volunteer("Volunteer Test");
    }

    @Test
    /*
    Rule 1.a.ii
    Volunteer has no jobs already signed up for
     */
    public void testApplyWithNoSignedJobs() {
        volunter_test.applyToJob(job0101);
    }

    @Test
    /*
    Rule 1.a.iii
    Volunteer has jobs already signed up for,
    this job does not extend across any days as jobs already signed up for
     */
    public void testApplyToJobNotOverlap() {
        volunter_test.applyToJob(job0101);
        volunter_test.applyToJob(job0103);
    }

    @Test
    /*
    Rule 1.a.iv
    Volunteer has jobs already signed up for,
    this job starts the same day as the end of some job already signed up for
     */
    public void testApplyJobSameDayJobConflict() {
        volunter_test.applyToJob(job0101);
        try {
            volunter_test.applyToJob(job0101_conflict);
        }
        catch (RuntimeException e) {
            fail("Caught Job Conflict");
        }
    }

    @Test
    /*
    Rule 1.a.v
    Volunteer has jobs already signed up for,
    this job ends the same day as the start of some job already signed up for
     */
    public void testApplyJobEndSameDayConflict() {
        volunter_test.applyToJob(job0101);
        try {
            volunter_test.applyToJob(job0101_conflict);
        }
        catch (RuntimeException e) {
            fail("Caught Job Conflict");
        }
    }

}