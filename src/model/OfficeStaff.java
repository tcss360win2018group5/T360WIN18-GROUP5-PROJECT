package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public final class OfficeStaff extends User implements Serializable {

	private int tempMaxPendingJobs = 5;

	/**
	 * Creates a park manager with the given username.
	 * 
	 * @param theUsername the username for the park manager user.
	 */
	public OfficeStaff(String theUsername) {
		super(theUsername, SystemCoordinator.OFFICE_STAFF_ACCESS_LEVEL);
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

		// TEMP var to implement the GUI <- REMOVE when implemented
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
	
	public ArrayList<Job> getJobsBetween2Dates(Date start, Date end) throws Exception {
		
		final ArrayList<Job> jobs = new ArrayList<Job>();

		
		for (int index = 0; index < jobs.size(); index++) {
			
			Job job = jobs.get(index);
			
			if (isJobBetween2Dates(job, start, end)) {
				
				jobs.add(job);
			}
			
		}
		
		return jobs;
	}

	private boolean isJobBetween2Dates(Job job, Date start, Date end) {
		return job.getStartDate().getTime().compareTo(start) >= 0 && 
				job.getEndDate().getTime().compareTo(end) <= 0;
	}

    @Override
    public Object clone() {
        OfficeStaff cloneOfficeStaff = new OfficeStaff(this.getUsername());
        return cloneOfficeStaff;
    }
}
