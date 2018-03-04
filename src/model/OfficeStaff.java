package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.Serializable;

import java.util.Scanner;


public final class OfficeStaff extends User implements Serializable {
	

	private static final int DEFAULT_MAX_PENDING_JOBS = 20;
	private static int maxPendingJobs;
	private static final String fileName = "ConstantsFile.txt";

	/**
	 * Creates a park manager with the given username.
	 * 
	 * @param theUsername the username for the park manager user.
	 */
	public OfficeStaff(String theUsername) {
		super(theUsername, SystemCoordinator.OFFICE_STAFF_ACCESS_LEVEL);

	}

	
	/**
	 * Load the data from the text file and store it into the variables.
	 * 
	 * @return the status of the load true if it was successful and false otherwise.
	 */
	public static void loadData() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(fileName));
		maxPendingJobs = scanner.nextInt();
		scanner.close();
	}
	
	/**
	 * Save the current date to a file.
	 * 
	 * @return the status of the saving true if it was successful and false otherwise.
	 */
	public static void saveData() throws FileNotFoundException {
		PrintStream printStream = new PrintStream(new File(fileName));
		printStream.print(maxPendingJobs);
		printStream.close();
	}

	public static int getMaxPendingJobs() {
		return maxPendingJobs;
	}

	/**
	 * Change the maximum pending jobs value to the given one.
	 * Precondition: the given value should be integer greater than 0.
	 */
	public static void setMaxPendingJobs(int theMaxPendingJobs) {
		maxPendingJobs = theMaxPendingJobs;
	}

	/**
	 * Change the maximum pending jobs value to the default value.
	 */
	public static void setDefaultMaxPendingJobs() {
		maxPendingJobs = DEFAULT_MAX_PENDING_JOBS;
	}


	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return null;
	}
}
