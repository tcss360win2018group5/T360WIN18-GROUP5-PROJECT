
package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

import model.Job;
import model.JobCoordinator;
import model.OfficeStaff;
import model.ParkManager;
import model.SystemCoordinator;
import model.Volunteer;
import util.SystemConstants;

public class UserInterfaceConsole {
    private static final String BACK = "0";
    private static final String EXIT = "0";
    private static final String LOGOUT = "0";
    private static final String SYSTEM_COORDINATOR_NAME = "data/SystemCoordinator.ser";
    private static final String JOB_COORDINATOR_NAME = "data/JobCoordinator.ser";

    private final SystemCoordinator mySystemCoordinator;
    private final JobCoordinator myJobCoordinator;
    private String myUserName;

    // 2: // Volunteer
    // 1: // Park Manager
    // 0: // Urban Parks Staff Member
    private static final int CHANGE_TO_CREATE_NEW_USER_ACCESS_LEVEL = 1;

    /**
     * Scanner to read user input from console.
     */
    Scanner myScanner;

    public UserInterfaceConsole() {

        myScanner = new Scanner(System.in);

        if (doesFileExist(SYSTEM_COORDINATOR_NAME) && doesFileExist(JOB_COORDINATOR_NAME)) {
            mySystemCoordinator = (SystemCoordinator) restoreObject(SYSTEM_COORDINATOR_NAME);
            myJobCoordinator = (JobCoordinator) restoreObject(JOB_COORDINATOR_NAME);
        }
        else {
            mySystemCoordinator = new SystemCoordinator();
            myJobCoordinator = new JobCoordinator();
        }
    }

    /*
     * Methods for initialization below
     */

    private boolean doesFileExist(String thisPath) {
        boolean fileExist = false;
        File file = new File(thisPath);
        if (file.exists()) {
            fileExist = true;
        }
        return fileExist;
    }

    private static Object restoreObject(String thisName) {
        Object newObject = null;
        try {
            FileInputStream in = new FileInputStream(thisName);
            ObjectInputStream ois = new ObjectInputStream(in);
            newObject = ois.readObject();
            ois.close();
            // System.out.println("Success");
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return newObject;
    }

    private void saveSystem() {
        writeObjectToDisk(SYSTEM_COORDINATOR_NAME, mySystemCoordinator);
        writeObjectToDisk(JOB_COORDINATOR_NAME, myJobCoordinator);
        System.out.println("\nThank you. Goodbye.");
        System.exit(0);
    }

    private void saveVolunteerJobInfomation(Volunteer theVolunteer) {
        // mySystemCoordinator.updateUserInformationOnExit(theVolunteer);
    }

    private static void writeObjectToDisk(String thisName, Object thisObject) {
        try {
            FileOutputStream out = new FileOutputStream(thisName);
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(thisObject);
            oos.close();
            // System.out.println("Success");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Start the UI method below
     */

    public void start() {
        String userInput;

        do {
            displayGreeting("to Urban Parks");
            displayDate();
            displayIntroLogin();
            askForUserSelection();
            userInput = myScanner.nextLine();
            userInput = userInput.toLowerCase();

            switch (userInput) {
                case "1":
                    userLogin();
                    break;
                case "2":
                    createUserLogin();
                    break;
                case EXIT:
                    saveSystem();
                default:
                    System.out.println("\nInvalid command.\n");
                    break;
            }

        }
        while (true);
    }

    /*
     * Place all methods for UI navigation below here
     */

    private void userLogin() {
        boolean continueMethod = true;
        do {
            System.out.println("\nPlease enter a username or press 0 to go back:");
            String userName = myScanner.nextLine();

            int doesUserExist = mySystemCoordinator.signIn(userName);
            int userAccessLevel = mySystemCoordinator.getAccessLevel(userName);

            if (doesUserExist == 0) {
            	myUserName = userName;
                switch (userAccessLevel) {
                    case 0: // Urban Parks Staff Member
                        welcomeUrbanParkStaff(userName, (OfficeStaff) mySystemCoordinator
                                        .getUser(userName));
                        break;
                    case 1: // Park Manager
                        welcomeParkManager(userName, (ParkManager) mySystemCoordinator
                                        .getUser(userName));
                        break;
                    case 2: // Volunteer
                        welcomeVolunteer(userName,
                                         (Volunteer) mySystemCoordinator.getUser(userName));
                        break;
                    default:
                        System.out.println("Access level invalid");
                        break;
                }
            }
            else if (userName.equals(BACK)) {
                continueMethod = false;
            }
            else {
                System.out.println("User does not exist");
            }
        }
        while (continueMethod);
        displaySeperator();
    }

    private void createUserLogin() {
        boolean continueMethod = true;
        do {
            System.out.println("\nPlease enter a username or press 0 to go back:");
            String userName = myScanner.nextLine();
            int doesUserExist = mySystemCoordinator.signIn(userName);
            // int userAccessLevel =
            // mySystemCoordinator.getAccessLevel(userName);

            if (BACK.equals(userName.toLowerCase())) {
                System.out.println("Returning to previous menu\n");
                displaySeperator();
                continueMethod = false;

            }
            else if (doesUserExist != 0) {
                System.out.println("Username " + userName + " is created!");
                switch (CHANGE_TO_CREATE_NEW_USER_ACCESS_LEVEL) {
                    case 0:
                        mySystemCoordinator.addUser(new OfficeStaff(userName));
                        break;
                    case 1:
                        mySystemCoordinator.addUser(new ParkManager(userName));
                        break;
                    case 2:
                        mySystemCoordinator.addUser(new Volunteer(userName));
                        break;
                    default:
                        System.out.println("Invalid system access " + "to create new user");
                        break;
                }
                displaySeperator();
                continueMethod = false;

            }
            else if (doesUserExist == 0) {
                System.out.println("Username " + userName + " exists.\n"
                                   + "Please enter a different user name\n");

            }
            else {
                System.out.println("Try Again");

            }
        }
        while (continueMethod);
    }

    private void welcomeVolunteer(String theUserName, Volunteer theVolunteer) {
        displaySeperator();
        displayGreeting(theUserName);
        displayDate();
        theVolunteer.setCurrentDay(myJobCoordinator.getCurrentDate());
        displayVolunteerMenu();
        boolean menuTrue = true;
        do {
            askForUserSelection();
            String userSelection = myScanner.nextLine();

            switch (userSelection) {
                case "1":
                    // 1) Sign up for job
                    displayJobsVolunteer(theVolunteer);
                    displaySeperator();
                    displayVolunteerMenu();
                    break;
                case "2":
                    // 2) View signed up jobs
                    displayJobsSignedUpVolunteer(theVolunteer);
                    displaySeperator();
                    displayVolunteerMenu();
                    break;
                case LOGOUT:
                    saveVolunteerJobInfomation(theVolunteer);
                    saveSystem();
                    break;
                default:
                    System.out.println("Invalid Selection\n");
                    break;
            }
        }
        while (menuTrue);
    }

    private void welcomeParkManager(String theUserName, ParkManager theParkManager) {
        displaySeperator();
        displayGreeting(theUserName);
        displayDate();
        displayParkManagerMenu();
        boolean menuTrue = true;
        do {
            askForUserSelection();
            String userSelection = myScanner.nextLine();

            switch (userSelection) {
                case "1":
                    // 1) Submit new job
                    if (this.myJobCoordinator.hasSpaceToAddJobs()) {
                        displaySubmitJobParkManager();
                        displaySeperator();
                        displayParkManagerMenu();
                    } else {
                        System.out.println("\nMaximum Pending Jobs Reached!" 
                                           + " Cannot Submit new job \n");
                    }
                    break;
                case "2":
                    // 2) View posted jobs
                    displayJobsParkManager();
                    displaySeperator();
                    displayParkManagerMenu();
                    break;
                case LOGOUT:
                    saveSystem();
                default:
                    System.out.println("Invalid Selection");
                    break;
            }
        }
        while (menuTrue);
    }

    private void welcomeUrbanParkStaff(String theUserName, OfficeStaff theOfficeStaff) {
        displaySeperator();
        displayGreeting(theUserName);
        displayDate();
        displayUrbanStaffMenu();
        boolean menuTrue = true;
        do {
            askForUserSelection();
            String userSelection = myScanner.nextLine();

            switch (userSelection) {
                case "1":
                    // 1) View pending job
                    displayPendingJobsUrbanParkStaff(theOfficeStaff);
                    displaySeperator();
                    displayParkManagerMenu();
                    break;
                case LOGOUT:
                    saveSystem();
                default:
                    System.out.println("Invalid Selection");
                    break;
            }
        }
        while (menuTrue);
    }

    /*
     * Place all methods for UI Display Prompts are below here
     */

    private void displayGreeting(String theGreeting) {
        System.out.println("Welcome " + theGreeting + "!");
    }

    private void displayDate() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        System.out.println("Today is " + dateFormat.format(date) + "\n");
    }

    private void displayIntroLogin() {
        System.out.println("1) Login with username\n" + "2) Create user\n" + "0) Exit\n");
    }

    private void askForUserSelection() {
        System.out.println("\nEnter a number to select from the following options");
    }

    private void displayVolunteerMenu() {
        System.out.println("1) View posted jobs & sign up\n"
                           + "2) View jobs currently signed up for\n" + "0) Logout & Exit\n");
    }

    private void displayParkManagerMenu() {
        System.out.println("1) Submit new job\n" + "2) View posted jobs\n"
                           + "0) Logout & Exit\n");
    }

    private void displayUrbanStaffMenu() {
        System.out.println("1) View posted jobs\n" + "0) Logout & Exit\n");
    }

    /*
     * Volunteer UI Display Methods are below
     */

    private void displayJobsVolunteer(Volunteer theVolunteer) {
        System.out.println("Available jobs listed below:\n");
        boolean menuTrue = true;
        int loop = 0;
        do {
            int iter = 1;
            while (iter <= 5) {
                // grab from list of jobs
                // if my jobs is empty, break, otherwise show 5 jobs
                if (!myJobCoordinator.getPendingJobs().isEmpty()
                    && myJobCoordinator.getPendingJobs().size() > ((iter - 1) + (loop * 5))) {

                    displayCycleJob(iter % 6, myJobCoordinator.getPendingJobs()
                                    .get((iter - 1) + (loop * 5)));
                    iter++;

                }
                else {
                    if (myJobCoordinator.getPendingJobs().isEmpty()) {
                        System.out.println("No jobs have been added yet\n");
                    }
                    else {
                        System.out.println("No jobs remaining\n");
                    }
                    break;
                }
            }

            displayCycleJobSelectionVolunteer();
            askForUserSelection();
            String userSelection = myScanner.nextLine();

            switch (userSelection) {
                case "1":
                    boolean isSignupSuccessful =
                                    displayCurrentJobSelectionVolunteer(myJobCoordinator
                                                    .getPendingJobs()
                                                    .get(0 + (loop * 5)), theVolunteer);
                    menuTrue = !isSignupSuccessful;
                    break;
                case "2":
                    isSignupSuccessful = displayCurrentJobSelectionVolunteer(myJobCoordinator
                                    .getPendingJobs().get(1 + (loop * 5)), theVolunteer);
                    menuTrue = !isSignupSuccessful;
                    ;
                    break;
                case "3":
                    isSignupSuccessful = displayCurrentJobSelectionVolunteer(myJobCoordinator
                                    .getPendingJobs().get(2 + (loop * 5)), theVolunteer);
                    menuTrue = !isSignupSuccessful;
                    break;
                case "4":
                    isSignupSuccessful = displayCurrentJobSelectionVolunteer(myJobCoordinator
                                    .getPendingJobs().get(3 + (loop * 5)), theVolunteer);
                    menuTrue = !isSignupSuccessful;
                    break;
                case "5":
                    isSignupSuccessful = displayCurrentJobSelectionVolunteer(myJobCoordinator
                                    .getPendingJobs().get(4 + (loop * 5)), theVolunteer);
                    menuTrue = !isSignupSuccessful;
                    break;
                case "8":
                    loop = loop - 1;
                    System.out.println("");
                    break;
                case "9":
                    loop = loop + 1;
                    System.out.println("");
                    break;
                case BACK:
                    menuTrue = false;
                    break;
                default:
                    System.out.println("Invalid Selection");
                    break;
            }
        }
        while (menuTrue);
    }

    private void displayJobsSignedUpVolunteer(Volunteer theVolunteer) {
        ArrayList<Job> currentJobs = theVolunteer.getCurrentJobs();
        if (currentJobs.size() == 0) {
            System.out.println("You are currently signed up for no jobs!");
        }
        else {
            System.out.println();
            int iter = 1;
            for (Job aJob : currentJobs) {
                displayCycleJob(iter++, aJob);
            }
            boolean menuTrue = true;
            do {
                System.out.println("0) Go back\n");
                askForUserSelection();

                String userSelection = myScanner.nextLine();
                if ("1".equals(userSelection) && currentJobs.size() > 0) {
                    boolean isSignupSuccessful =
                                    displayCurrentJobSelectionVolunteer(currentJobs.get(0),
                                                                        theVolunteer);
                    menuTrue = !isSignupSuccessful;

                }
                else if ("2".equals(userSelection) && currentJobs.size() > 1) {
                    boolean isSignupSuccessful;
                    isSignupSuccessful =
                                    displayCurrentJobSelectionVolunteer(currentJobs.get(1),
                                                                        theVolunteer);
                    menuTrue = !isSignupSuccessful;

                }
                else if ("3".equals(userSelection) && currentJobs.size() > 2) {
                    boolean isSignupSuccessful;
                    isSignupSuccessful =
                                    displayCurrentJobSelectionVolunteer(currentJobs.get(2),
                                                                        theVolunteer);
                    menuTrue = !isSignupSuccessful;

                }
                else if ("4".equals(userSelection) && currentJobs.size() > 3) {
                    boolean isSignupSuccessful;
                    isSignupSuccessful =
                                    displayCurrentJobSelectionVolunteer(currentJobs.get(3),
                                                                        theVolunteer);
                    menuTrue = !isSignupSuccessful;

                }
                else if ("5".equals(userSelection) && currentJobs.size() > 4) {
                    boolean isSignupSuccessful;
                    isSignupSuccessful =
                                    displayCurrentJobSelectionVolunteer(currentJobs.get(4),
                                                                        theVolunteer);
                    menuTrue = !isSignupSuccessful;

                }
                else if (BACK.equals(userSelection)) {
                    menuTrue = false;

                }
                else {
                    System.out.println("Invalid Selection\n");

                }
            }
            while (menuTrue);
        }
    }

    private void displayCycleJob(int theIteration, Job theJob) {
        String jobName = theJob.getJobTitle();
        String jobLocation = theJob.getMyAddress();
        GregorianCalendar jobStartDate = theJob.getStartDate();
        System.out.println(theIteration + ") " + jobName + " | " + "Location: " + jobLocation
                           + " | " + "Start Date: " + (jobStartDate.get(Calendar.MONTH) + 1) + "/"
                           + jobStartDate.get(Calendar.DAY_OF_MONTH) + "/"
                           + jobStartDate.get(Calendar.YEAR) + "\n");
    }

    private void displayCycleJobSelectionVolunteer() {
        System.out.println("8) Back to top of list\n" + "9) View more\n" + "0) Back\n");
    }

    private boolean displayCurrentJobSelectionVolunteer(Job theJob, Volunteer theVolunteer) {
        boolean successfulSignup = false;
        String jobName = theJob.getJobTitle();
        String jobLocation = theJob.getMyAddress();
        GregorianCalendar jobStartDate = theJob.getStartDate();
        GregorianCalendar jobEndDate = theJob.getEndDate();
        String contactName = theJob.getMyContactName();
        String contactNumber = theJob.getMyContactNumber();
        String contactEmail = theJob.getMyContactEmail();
        int numberOfVolunteers = theJob.getCurrentVolunteers().size();
        String jobDifficulty = theJob.getMyDifficulty();
        System.out.println("Name:\t\t\t\t" + jobName + "\n" + "Location:\t\t\t" + jobLocation
                           + "\n" + "Start Date:\t\t\t" + printJobDate(jobStartDate) + "\n"
                           + "End Date:\t\t\t" + printJobDate(jobEndDate) + "\n"
                           + "Contact:\t\t\t" + contactName + "\n" + "Contact Phone:\t\t"
                           + contactNumber + "\n" + "Contact Email:\t\t" + contactEmail + "\n"
                           + "Current Volunteers:\t" + numberOfVolunteers + "\n"
                           + "Difficulty:\t\t\t" + jobDifficulty + "\n");
        displaySignupForJobVolunteer();
        boolean menuTrue = true;
        do {
            askForUserSelection();
            String userSelection = myScanner.nextLine();

            switch (userSelection) {
                case "1":
                    int canAddToJob = theVolunteer.canSignUpForJob(theJob);
                    if (canAddToJob == 0) {
                        System.out.println("Congrats! " + theJob.getJobTitle() + " is added!");
                        theVolunteer.signUpForJob(theJob);
                        theJob.addVolunteer(theVolunteer);
                        successfulSignup = true;
                    }
                    else if (canAddToJob == 1) {
                        displaySeperator();
                        System.out.println("Unable to signup for job");
                        System.out.println("Reason: The selected job overlaps "
                                           + "with a job you've already signed up for.\n");
                    }
                    else if (canAddToJob == 2) {
                        displaySeperator();
                        System.out.println("Unable to signup for job");
                        System.out.println("Reason: Could not sign up "
                                           + "for a job less than "
                                           + Volunteer.MINIMUM_DAYS_BEFORE_JOB_START
                                           + " days away\n");
                    }
                    menuTrue = false;
                    break;
                case BACK:
                    menuTrue = false;
                    break;
                default:
                    System.out.println("Invalid Selection");
                    break;
            }
        }
        while (menuTrue);
        return successfulSignup;
    }

    private void displaySignupForJobVolunteer() {
        System.out.println("1) Sign up for job\n" + "0) Go Back\n");
    }

    /*
     * Park Manage UI Methods are below
     */

    private void displaySubmitJobParkManager() {
		// 0123456789
		System.out.println("Please provide job listing headline:");
		String jobName = myScanner.nextLine();
		System.out.println("Please indicate Start Date (MM/DD/YYYY):");
		String startDateString = myScanner.nextLine();
		GregorianCalendar startDate = new GregorianCalendar(Integer.parseInt(startDateString
				.substring(6, 10)), Integer.parseInt(startDateString.substring(0, 2)) - 1, //minus 1 for gregorian calendar purposes (months start at 0)
				Integer.parseInt(startDateString
						.substring(3, 5)));
		System.out.println("Please indicate End Date (MM/DD/YYYY):");
		String endDateString = myScanner.nextLine();
		GregorianCalendar endDate =
				new GregorianCalendar(Integer.parseInt(endDateString.substring(6, 10)),
						Integer.parseInt(endDateString.substring(0, 2)) - 1, //minus 1 for gregorian calendar purposes (months start at 0)
						Integer.parseInt(endDateString.substring(3, 5)));

		Job newJob = new Job(jobName);
		newJob.setStartDate(startDate);
		newJob.setEndDate(endDate);
		System.out.println("Start Date for this job: " + printJobDate(startDate));
		System.out.println("End Date for this job: " + printJobDate(endDate));
		if (tryToAddJob(newJob)) {
			System.out.println("Please indicate Address (Street, City, State, ZIP):");
			String address = myScanner.nextLine();
			System.out.println("Please provide contact name:");
			String contactName = myScanner.nextLine();
			System.out.println("Please provide contact phone:");
			String contactNum = myScanner.nextLine();
			System.out.println("Please provide contact email:");
			String contactEmail = myScanner.nextLine();
			System.out.println("Please provide job description:");
			String jobDescription = myScanner.nextLine();
			System.out.println("Please provide a job role:");
			String jobRole = myScanner.nextLine();
			System.out.println("Please provide a difficulty (easy, medium, hard): ");
			String difficulty = myScanner.nextLine();
			System.out.println("Please indicate maximum number of volunteers:");
			int numVolunteers = Integer.parseInt(myScanner.nextLine());

			// Leaving the loop blank for now to add multiple job roles
			// Job api needs to be extended to make that work

			newJob.setMyAddress(address);
			newJob.setMyContactName(contactName);
			newJob.setMyContactNumber(contactNum);
			newJob.setMyContactEmail(contactEmail);
			newJob.setMyJobDescription(jobDescription);
			newJob.setMyJobRole(jobRole);
			if (difficulty.toLowerCase().equals("easy")) {
				newJob.setMyDifficulty(0);
			}
			else if (difficulty.toLowerCase().equals("medium")) {
				newJob.setMyDifficulty(1);
			}
			else if (difficulty.toLowerCase().equals("hard")) {
				newJob.setMyDifficulty(3);
			}
			else {
				newJob.setMyDifficulty(99);
			}
			newJob.setMaxVolunteers(numVolunteers);

			displayJobListingParkManager(newJob);
			displayAskToPostJobParkManager(newJob);

		}
	}

    private void displayJobListingParkManager(Job theJob) {
        System.out.println("Job Listing Overview:\n");
        displaySeperator();
        System.out.println("Name:\t\t\t" + theJob.getJobTitle());
        System.out.println("Location:\t\t" + theJob.getMyAddress());
        System.out.println("Start Date:\t\t" + printJobDate(theJob.getStartDate()));
        System.out.println("End Date:\t\t" + printJobDate(theJob.getEndDate()));

        System.out.println("Contact Phone\t" + theJob.getMyContactNumber());
        System.out.println("Contact Email:\t" + theJob.getMyContactEmail());
        System.out.println("Description:\t" + theJob.getMyJobDescription());
        System.out.println();
        System.out.println("Job Roles:");
        System.out.println("Role:\t\t" + theJob.getMyJobRole());
        System.out.println("Difficulty: " + theJob.getMyDifficulty());
        System.out.println("Volunteers:\t" + theJob.getMaxVolunteers());
        // System.out.println("Description:\t" +
        // theJob.getMyJobRoleDescription());
        System.out.println();
    }

    private void displayAskToPostJobParkManager(Job theJob) {
        boolean selectionTrue = true;
        do {
            System.out.println("\nWould you like to submit job? (Y/N): ");
            String userInput = myScanner.nextLine();
            if (userInput.toLowerCase().equals("y")) {
                tryToAddJob(theJob);
                selectionTrue = false;
            }
            else if (userInput.toLowerCase().equals("n")) {
                System.out.println("Job Not Submitted\n");
                selectionTrue = false;
            }
            else {
                System.out.println("Invalid Response");
            }
        }
        while (selectionTrue);
    }

	private boolean tryToAddJob(Job theJob) {
		boolean canAddJob = false;
		int result = myJobCoordinator.checkBusinessRules(theJob, 
				(ParkManager) mySystemCoordinator.getUser(myUserName)); // SOMETHING UP WITH THIS
		if (result == 1) {
			System.out.println("Sorry, this job already exists!\n");
		}
		else if (result == 2) {
			System.out.println("This job is longer than the maximum allowed job of "
					+ JobCoordinator.MAXIMUM_JOB_LENGTH + " days");
		}
		else if (result == 3) {
			System.out.println("This job is further away than the maximum allowed "
					+ JobCoordinator.MAXIMUM_DAYS_AWAY_TO_POST_JOB
					+ " days from today");
		} else if (result == 4) {
			System.out.println("Sorry, there is already the maximum allowed jobs in the system");
		} else {
			myJobCoordinator.addPendingJob(theJob);
			((ParkManager) mySystemCoordinator.getUser(myUserName)).addCreatedJob(theJob); // SOMETHING UP WITH THISS
			canAddJob = true;
		}
		return canAddJob;
	}

    private void displayJobsParkManager() {
        boolean menuTrue = true;
        int loop = 0;
        do {
            int iter = 1;
            while (iter <= 5) {
                // grab from list of jobs
                // if my jobs is empty, break, otherwise show 5 jobs
                if (!myJobCoordinator.getPendingJobs().isEmpty()
                    && myJobCoordinator.getPendingJobs().size() > ((iter - 1) + (loop * 5))) {

                    displayCycleJob(iter % 6, myJobCoordinator.getPendingJobs()
                                    .get((iter - 1) + (loop * 5)));
                    iter++;

                }
                else {
                    if (myJobCoordinator.getPendingJobs().isEmpty()) {
                        System.out.println("No jobs have been added yet\n");
                    }
                    else {
                        System.out.println("No jobs remaining\n");
                    }
                    break;
                }
            }

            displayCycleJobSelectionVolunteer();
            askForUserSelection();
            String userSelection = myScanner.nextLine();
            Job theJob = null;
            switch (userSelection) {
                case "1":
                    theJob = myJobCoordinator.getPendingJobs()
                                    .get(Integer.parseInt(userSelection) - 1 + (loop * 5));
                    if (theJob != null) {
                        displayJobListingParkManager(theJob);
                    }
                    break;
                case "2":
                    theJob = myJobCoordinator.getPendingJobs()
                                    .get(Integer.parseInt(userSelection) - 1 + (loop * 5));
                    if (theJob != null) {
                        displayJobListingParkManager(theJob);
                    }
                    break;
                case "3":
                    theJob = myJobCoordinator.getPendingJobs()
                                    .get(Integer.parseInt(userSelection) - 1 + (loop * 5));
                    if (theJob != null) {
                        displayJobListingParkManager(theJob);
                    }
                    break;
                case "4":
                    theJob = myJobCoordinator.getPendingJobs()
                                    .get(Integer.parseInt(userSelection) - 1 + (loop * 5));
                    if (theJob != null) {
                        displayJobListingParkManager(theJob);
                    }
                    break;
                case "5":
                    theJob = myJobCoordinator.getPendingJobs()
                                    .get(Integer.parseInt(userSelection) - 1 + (loop * 5));
                    if (theJob != null) {
                        displayJobListingParkManager(theJob);
                    }
                    break;
                case "8":
                    loop = loop - 1;
                    System.out.println("");
                    break;
                case "9":
                    loop = loop + 1;
                    System.out.println("");
                    break;
                case BACK:
                    menuTrue = false;
                    break;
                default:
                    System.out.println("Invalid Selection");
                    break;
            }
        }
        while (menuTrue);
    }

    /*
     * Office Staff UI Method are below
     * 
     * // THERE IS NO USER STORY FOR URBAN PARK STAFF // ONLY DISPLAYING JOBS IS
     * IMPLEMENTED
     * 
     */
    private void displayPendingJobsUrbanParkStaff(OfficeStaff theOfficeStaff) {
        ArrayList<Job> pendingJobs = myJobCoordinator.getPendingJobs();
        if (pendingJobs.size() == 0) {
            System.out.println("There are no pending jobs!");
        }
        else {
            System.out.println();
            for (Job aJob : pendingJobs) {
                int iter = 1;
                displayCycleJob(iter++, aJob);
            }
            boolean menuTrue = true;
            do {
                System.out.println("0) Go back\n");
                askForUserSelection();

                String userSelection = myScanner.nextLine();

                if (BACK.equals(userSelection)) {
                    menuTrue = false;

                }
                else {
                    System.out.println("Invalid Selection\n");

                }
            }
            while (menuTrue);
        }
    }

    /**
     * Prints date of job in correct format
     */
    private String printJobDate(final GregorianCalendar theJob) {
        return ((theJob.get(Calendar.MONTH) + 1) + "/" + theJob.get(Calendar.DAY_OF_MONTH) + "/"
                + theJob.get(Calendar.YEAR));
    }

    /*
     * Separator below
     */

    public void displaySeperator() {
        System.out.println("\n" + "-------------\n");
    }

    public static void main(String[] args) {
        UserInterfaceConsole test = new UserInterfaceConsole();
        test.start();
    }

}
