package test;
import static org.junit.Assert.*;

import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import model.Job;
import model.JobCoordinator;
import model.Volunteer;

public class VolunteerTest {
    public JobCoordinator globalJobCoordinator;
    public Volunteer globalVolunteer;
    public Job globalGenericJob;
    public GregorianCalendar March1;
    public GregorianCalendar Jan3;
    public GregorianCalendar Jan2;
    public GregorianCalendar newYear;


    @Before
    public void setUp() throws Exception {
        globalJobCoordinator = new JobCoordinator();
        globalVolunteer = new Volunteer("GenericJane");
        newYear = new GregorianCalendar(2018, 1, 1, 0, 0);
        globalVolunteer.setToday(newYear);
    }

    // Testing Volunteer Business Rule B
    // A volunteer may sign up only if the job begins at least a minimum number of calendar days after the current date, default of 2

    // i) Volunteer signs up for job that begins much more than the minimum number of calendar days from the current date
    @Test
    public final void canAddJob_ManyDaysAway_ShouldReturnTrue() {
        // Volunteer tries to sign up for a job more than 2 days away.

        March1 = new GregorianCalendar(2018, 3, 1, 0, 0);
        Job jobFarAway = new Job("GenericJob", 1, 3, March1, new GregorianCalendar(2018, 3, 1, 5, 5)); //job that begins March 1st and ends after 5 hours.

        assertTrue(globalVolunteer.addToCurrentJobs(jobFarAway)); //chcecks to make sure job added is >= 2 days from today.
    }

    // ii) Volunteer signs up for job that begins exactly the minimum number of calendar days from the current date
    @Test
    public final void canAddJob_ExactlyMinimumDaysAway_ShouldReturnTrue() {
        // Volunteer tries to sign up for job exactly 2 days away.
        Jan3 = new GregorianCalendar(2018, 1, 3, 0, 0);
        Job jobExactlyMinAway = new Job("MinDaysAwayJob", 1, 5, Jan3, new GregorianCalendar(2018, 1, 3, 1, 1)); //job that begins January 3rd and ends after an hour.

        assertTrue(globalVolunteer.addToCurrentJobs(jobExactlyMinAway)); //chcecks to make sure job added is >= 2 days from today.
    }
    // iii) Volunteer signs up for job that begins less than the minimum number of calendar days from the current date
    @Test
    public final void canAddJob_LessThanMinimumDaysAway_ShouldReturnFalse() {
        // Volunteer tries to add a job less than 2 days away.
        Jan2 = new GregorianCalendar(2018, 1, 2, 11, 59);
        Job jobWithinIllegalTimeframe = new Job("JobWithin2Days", 1, 5, Jan2, new GregorianCalendar(2018, 1, 3, 1, 1)); //job that begins January 2nd and ends the next day.

        assertFalse(globalVolunteer.addToCurrentJobs(jobWithinIllegalTimeframe)); //chcecks to make sure job added is >= 2 days from today.
    }



}
