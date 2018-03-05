package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import model.Job;

public class JobTest {
    public GregorianCalendar theCurrentDate;
    
    @Before
    public void setUp() throws Exception {
        theCurrentDate = new GregorianCalendar();
    }

    @Test
    public final void isFutureJob_jobThatStartsOneLessThanToday_shouldBeFalse() {
        Job jobOneLessThanToday = new Job("jobOneLessThanMinimumDaysAway");
        GregorianCalendar dateOneLessThanToday = (GregorianCalendar) theCurrentDate.clone();
        dateOneLessThanToday.add(GregorianCalendar.DAY_OF_YEAR, -1);
        jobOneLessThanToday.setStartDate(dateOneLessThanToday);
        jobOneLessThanToday.setEndDate(dateOneLessThanToday);
        
        assertFalse(jobOneLessThanToday.isFutureJob(theCurrentDate));
    }

    @Test
    public final void isFutureJob_jobThatStartsExactlyToday_shouldBeTrue() {
        Job jobThatStartsExactlyToday = new Job("jobThatStartsExactlyToday");
        GregorianCalendar dateExactlyToday = (GregorianCalendar) theCurrentDate.clone();
        dateExactlyToday.add(GregorianCalendar.DAY_OF_YEAR, 0);
        jobThatStartsExactlyToday.setStartDate(dateExactlyToday);
        jobThatStartsExactlyToday.setEndDate(dateExactlyToday);
        
        assertTrue(jobThatStartsExactlyToday.isFutureJob(theCurrentDate));
    
    }
    
    @Test
    public final void isFutureJob_jobThatStartsOneMoreThanToday_shouldBeTrue() {
        Job jobOneMoreThanToday = new Job("jobOneLessThanMinimumDaysAway");
        GregorianCalendar dateOneMoreThanToday = (GregorianCalendar) theCurrentDate.clone();
        dateOneMoreThanToday.add(GregorianCalendar.DAY_OF_YEAR, 1);
        jobOneMoreThanToday.setStartDate(dateOneMoreThanToday);
        jobOneMoreThanToday.setEndDate(dateOneMoreThanToday);
        
        assertTrue(jobOneMoreThanToday.isFutureJob(theCurrentDate));
    
    }
}
