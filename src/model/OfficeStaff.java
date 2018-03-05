package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class OfficeStaff extends User implements Serializable {

	private int tempMaxPendingJobs = 10;
	private GregorianCalendar startDate;
	private GregorianCalendar endDate;

	/**
	 * Creates a park manager with the given username.
	 * 
	 * @param theUsername the username for the park manager user.
	 */
	public OfficeStaff(String theUsername) {
		super(theUsername, SystemCoordinator.OFFICE_STAFF_ACCESS_LEVEL);
		// Default Vals.
		startDate = new GregorianCalendar(2000, 00, 01);
		endDate = new GregorianCalendar(3000, 11, 01);
	}

	/* Exceptions for input validation. */
	public class ZeroInputException extends Exception {
		public ZeroInputException() {}
	    public ZeroInputException(String message) {
	        super(message);
	    }
	}
	
	public class NegativeInputException extends Exception {
		public NegativeInputException() {}
	    public NegativeInputException(String message) {
	        super(message);
	    }
	}
	
	public class NonIntegerInputException extends Exception {
		public NonIntegerInputException() {}
	    public NonIntegerInputException(String message) {
	        super(message);
	    }
	}
	
	/**
	 * Sets the maximum pending jobs requested by the Office Staff for further usage in 
	 * the system. 
	 * 
	 * PRECONDITION: newMaxPendingJobs >= current # of jobs in the system.
	 * 
	 * @throws ZeroInputException if zero is specified as the value
	 * @throws NegativeInputException if a negative value is specified
	 * @throws NonIntegerInputException if anything other than an int is provided
	 */
	public void setMaxPendingJobs(int newMaxPendingJobs) throws Exception {
		
		if (!isInputInteger(newMaxPendingJobs)) {
			throw new NonIntegerInputException();
		}

		if (isInputEqualZero((int) newMaxPendingJobs)) {
			throw new ZeroInputException();
		}

		if (isInputNegative((int) newMaxPendingJobs)) {
			throw new NegativeInputException();
		}
		
		tempMaxPendingJobs = newMaxPendingJobs;
	}

	public int getMaxPendingJobs() {
		return tempMaxPendingJobs;
	}

	public boolean isInputEqualZero(int input) {
		return input == 0;
	}
	
	public boolean isInputNegative(int input) {
		return input < 0;
	}
	
	public boolean isInputInteger(Object newMaxPendingJobs) {
		return newMaxPendingJobs instanceof Integer;
	}

	// Required methods for GUI & User story - Allows Returning Restricted JobList
	public void setStartDate(GregorianCalendar theStartDate) {
		startDate = theStartDate;
	}
	public void setEndDate(GregorianCalendar theEendDate) {
		endDate = theEendDate;
	}
	public GregorianCalendar getStartDate() {
		return startDate;
	}
	public GregorianCalendar getEndDate() {
		return endDate;
	}

	/**
	 * Given a specified list of jobs, provides back the pruned list of jobs corresponding to
	 * jobs within the time frame [startDate, endDate]
	 * 
	 */
	public ArrayList<Job> getJobsBetween2Dates(ArrayList<Job> listOfJobs) {
		
		final ArrayList<Job> jobs = new ArrayList<Job>();
		
		for (int index = 0; index < listOfJobs.size(); index++) {
			
			Job job = listOfJobs.get(index);

			if (JobCoordinator.getDifferenceInDays(startDate, job.getStartDate()) >= 0 &&
					JobCoordinator.getDifferenceInDays(job.getEndDate(), endDate) >= 0) {
				jobs.add(job);
			}
		}
		
		return jobs;
	}

    @Override
    public Object clone() {
        OfficeStaff cloneOfficeStaff = new OfficeStaff(this.getUsername());
        cloneOfficeStaff.tempMaxPendingJobs = this.tempMaxPendingJobs;
        cloneOfficeStaff.startDate = (GregorianCalendar) this.startDate.clone();
        cloneOfficeStaff.endDate = (GregorianCalendar) this.endDate.clone();
        
        return cloneOfficeStaff;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), startDate, endDate);
    }
    
    @Override
    public boolean equals(Object theObject) {
        boolean result = false;
        if (this == theObject) {
            result = true;
        } else if (theObject == null) {
            result = false;
        } else if (this.getClass() == theObject.getClass()) {
            OfficeStaff theOtherOS = (OfficeStaff) theObject;
            result = super.equals(theObject) &&
                            Objects.equals(this.startDate, theOtherOS.startDate) &&
                            Objects.equals(this.endDate, theOtherOS.endDate);
        }
        
        return result;
    }
}
