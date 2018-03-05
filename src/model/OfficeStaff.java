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
		
		// TODO: I need a way to change the maximum pending jobs.

//		Field oldValue = ProgramConstants.class.getDeclaredField("MAX_PENDING_JOBS");
//		oldValue.setAccessible(true);
//
//		Field newValue = Field.class.getDeclaredField("modifiers");
//		newValue.setAccessible(true);
//
//		newValue.setInt(oldValue, oldValue.getModifiers() & ~ Modifier.FINAL);
//
//		oldValue.set(ProgramConstants.class, (int) newMaxPendingJobs);

		// TEMP var to implement the GUI <- REMOVE when properly implemented
		tempMaxPendingJobs = newMaxPendingJobs;
	}

	public int getMaxPendingJobs() {
		// Calling to display current value on GUI
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

	public ArrayList<Job> getJobsBetween2Dates(ArrayList<Job> listOfJobs) {
		
		final ArrayList<Job> jobs = new ArrayList<Job>();
		
		for (int index = 0; index < listOfJobs.size(); index++) {
			
			Job job = listOfJobs.get(index);

			if (getDifferenceInDays(startDate, job.getStartDate()) >= 0 &&
					getDifferenceInDays(job.getEndDate(), endDate) >= 0) {
				jobs.add(job);
			}
		}
		
		return jobs;
	}

	// private helpers
	private int getDifferenceInDays(GregorianCalendar theFirstDate,
									GregorianCalendar theSecondDate) {
		long convertedTime = TimeUnit.DAYS.convert(theSecondDate.getTimeInMillis(),
				TimeUnit.MILLISECONDS)
				- TimeUnit.DAYS.convert(theFirstDate.getTimeInMillis(),
				TimeUnit.MILLISECONDS);

		return ((int) convertedTime);
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
