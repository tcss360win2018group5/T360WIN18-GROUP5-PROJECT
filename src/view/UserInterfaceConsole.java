package view;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

import model.*;

public class UserInterfaceConsole {

    private static final String BACK = "0";
    private static final String EXIT = "0";
    private static final String LOGOUT = "0";
    private static final String SYSTEM_COORDINATOR_NAME = "SystemCoordinator.ser";
    private static final String JOB_COORDINATOR_NAME = "JobCoordinator.ser";

    private final SystemCoordinator mySystemCoordinator;
    private final JobCoordinator myJobCoordinator;
    
    /**
     * Scanner to read user input from console.
     */
    Scanner myScanner;

    public UserInterfaceConsole() {


        myScanner = new Scanner(System.in);

        if (doesFileExist(SYSTEM_COORDINATOR_NAME)
                && doesFileExist(JOB_COORDINATOR_NAME))
        {
            mySystemCoordinator = (SystemCoordinator) restoreObject(SYSTEM_COORDINATOR_NAME);
            myJobCoordinator = (JobCoordinator) restoreObject(JOB_COORDINATOR_NAME);
        } else {
            mySystemCoordinator = new SystemCoordinator();
            myJobCoordinator = new JobCoordinator();
        }
    }


    /*
    Methods for initialization below
     */

    private boolean doesFileExist(String thisPath) {
        boolean fileExist = false;
        File file = new File(thisPath);
        if(file.exists()) {
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
//            System.out.println("Success");
        } catch (IOException | ClassNotFoundException e) {
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

    private static void writeObjectToDisk(String thisName, Object thisObject) {
        try {
            FileOutputStream out = new FileOutputStream(thisName);
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(thisObject);
            oos.close();
//            System.out.println("Success");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Start the UI method below
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

        } while (true);
    }



    /*
    Place all methods for UI navigation below here
     */

    private void userLogin(){
        boolean continueMethod = true;
        do {
            System.out.println("\nPlease enter a username or press 0 to go back:");
            String userName = myScanner.nextLine();

            boolean doesUserExist = mySystemCoordinator.signIn(userName);
            int userAccessLevel = mySystemCoordinator.getAccessLevel(userName);


            if (doesUserExist) {
                switch(userAccessLevel) {
                    case 0: // Urban Parks Staff Member
                        welcomeUrbanParkStaff(userName, new OfficeStaff(userName));
                        break;
                    case 1: // Park Manager
                        welcomeParkManager(userName, new ParkManager(userName));
                        break;
                    case 2: // Volunteer
                        welcomeVolunteer(userName, new Volunteer(userName));
                        break;
                    default:
                        System.out.println("Access level invalid");
                        break;
                }
            } else if (userName.equals(BACK)){
                continueMethod = false;
            } else {
                System.out.println("User does not exist");
            }
        } while (continueMethod);
        displaySeperator();
    }


    private void createUserLogin() {
        boolean continueMethod = true;
        do {
            System.out.println("\nPlease enter a username or press 0 to go back:");
            String userName = myScanner.nextLine();
            boolean doesUserExist = mySystemCoordinator.signIn(userName);
            int userAccessLevel = mySystemCoordinator.getAccessLevel(userName);

            if (BACK.equals(userName.toLowerCase())) {
                System.out.println("Returning to previous menu\n");
                displaySeperator();
                continueMethod = false;

            } else if (doesUserExist == false) {
                System.out.println("Username " + userName + " is created!");
                // Change this new Volunteer to switch access level on user creation to test
                mySystemCoordinator.addUser(new Volunteer(userName));
                displaySeperator();
                continueMethod = false;

            } else if (doesUserExist == true) {
                System.out.println("Username " + userName + " exists.\n" +
                    "Please enter a different user name\n");

            } else {
                System.out.println("Try Again");

            }
        } while (continueMethod);
    }

    private void welcomeVolunteer(String theUserName, Volunteer theVolunteer) {
        displaySeperator();
        displayGreeting(theUserName);
        displayDate();
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
                    saveSystem();
                    break;
                default:
                    System.out.println("Invalid Selection\n");
                    break;
            }
        } while(menuTrue);
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
                    displaySummitJobParkManager(theParkManager);
                    displaySeperator();
                    displayParkManagerMenu();
                    break;
                case "2":
                    // 2) View posted jobs
                    displayJobsPostedParkManager(theParkManager);
                    displaySeperator();
                    displayParkManagerMenu();
                    break;
                case LOGOUT:
                    saveSystem();
                default:
                    System.out.println("Invalid Selection");
                    break;
            }
        } while(menuTrue);
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
                case "2":
                    // 2) View finished jobs
                    displayFinishedJobsUrbanParkStaff(theOfficeStaff);
                    displaySeperator();
                    displayParkManagerMenu();
                    break;
                case LOGOUT:
                    saveSystem();
                default:
                    System.out.println("Invalid Selection");
                    break;
            }
        } while(menuTrue);
    }

    /*
    Place all methods for UI Display Prompts are below here
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
        System.out.println("1) Login with username\n" +
                            "2) Create user\n" +
                            "0) Exit\n");
    }

    private void askForUserSelection() {
        System.out.println("Enter a number to select from the following options");
    }

    private void displayVolunteerMenu() {
        System.out.println("1) Sign up for job\n" +
                            "2) View signed up jobs\n" +
                            "0) Logout & Exit\n");
    }

    private void displayParkManagerMenu() {
        System.out.println("1) Submit new job\n" +
                "2) View posted jobs\n" +
                "0) Logout & Exit\n");
    }

    private void displayUrbanStaffMenu() {
        System.out.println("1) View pending job\n" +
                "2) View finished jobs\n" +
                "0) Logout & Exit\n");
    }


    /*
        Volunteer UI Display Methods are below
     */

    private void displayJobsVolunteer(Volunteer theVolunteer) {
        System.out.println("Available jobs listed below:\n");
        boolean menuTrue = true;
        int loop = 0;
        do {
            int iter = 1;
            while (iter <= 5) {
                displayCycleJob(iter++ % 6, new Job(""));
            }
            displayCycleJobSelectionVolunteer();
            askForUserSelection();
            String userSelection = myScanner.nextLine();

            switch (userSelection) {
                case "1":
                    boolean isSignupSuccessful = displayCurrentJobSelectionVolunteer(new Job(""), theVolunteer);
                    menuTrue = !isSignupSuccessful;
                    break;
                case "2":
                    isSignupSuccessful = displayCurrentJobSelectionVolunteer(new Job(""), theVolunteer);
                    menuTrue = !isSignupSuccessful;;
                    break;
                case "3":
                    isSignupSuccessful = displayCurrentJobSelectionVolunteer(new Job(""), theVolunteer);
                    menuTrue = !isSignupSuccessful;
                    break;
                case "4":
                    isSignupSuccessful = displayCurrentJobSelectionVolunteer(new Job(""), theVolunteer);
                    menuTrue = !isSignupSuccessful;
                    break;
                case "5":
                    isSignupSuccessful = displayCurrentJobSelectionVolunteer(new Job(""), theVolunteer);
                    menuTrue = !isSignupSuccessful;
                    break;
                case "9":
                    loop = loop + 1;
                    System.out.println(loop);
                    break;
                case BACK:
                    menuTrue = false;
                    break;
                default:
                    System.out.println("Invalid Selection");
                    break;
            }
        } while (menuTrue);
    }

    private void displayJobsSignedUpVolunteer(Volunteer theVolunteer) {
        ArrayList<Job> currentJobs = theVolunteer.getCurrentJobs();
        if (currentJobs.size() == 0) {
            System.out.println("You are currently signed up for no jobs!");
        } else {
            System.out.println();
            for (Job aJob : currentJobs) {
                int iter = 1;
                displayCycleJob(iter++, aJob);
            }
            boolean menuTrue = true;
            do {
                System.out.println("0) Go back\n");
                askForUserSelection();

                String userSelection = myScanner.nextLine();
                if ("1".equals(userSelection) && currentJobs.size() > 0) {
                    boolean isSignupSuccessful = displayCurrentJobSelectionVolunteer(currentJobs.get(0), theVolunteer);
                    menuTrue = !isSignupSuccessful;

                } else if ("2".equals(userSelection) && currentJobs.size() > 1) {
                    boolean isSignupSuccessful;
                    isSignupSuccessful = displayCurrentJobSelectionVolunteer(currentJobs.get(1), theVolunteer);
                    menuTrue = !isSignupSuccessful;

                } else if ("3".equals(userSelection) && currentJobs.size() > 2) {
                    boolean isSignupSuccessful;
                    isSignupSuccessful = displayCurrentJobSelectionVolunteer(currentJobs.get(2), theVolunteer);
                    menuTrue = !isSignupSuccessful;

                } else if ("4".equals(userSelection) && currentJobs.size() > 3) {
                    boolean isSignupSuccessful;
                    isSignupSuccessful = displayCurrentJobSelectionVolunteer(currentJobs.get(3), theVolunteer);
                    menuTrue = !isSignupSuccessful;

                } else if ("5".equals(userSelection) && currentJobs.size() > 4) {
                    boolean isSignupSuccessful;
                    isSignupSuccessful = displayCurrentJobSelectionVolunteer(currentJobs.get(4), theVolunteer);
                    menuTrue = !isSignupSuccessful;

                } else if (BACK.equals(userSelection)) {
                    menuTrue = false;

                } else {
                    System.out.println("Invalid Selection\n");

                }
            } while (menuTrue);
        }
    }

    private void displayCycleJob(int theIteration, Job theJob) {
        String jobName = theJob.getJobTitle();
        String jobLocation = theJob.getMyJobLocation();
        GregorianCalendar jobStartDate = theJob.getStartDate();
        System.out.println(theIteration + ") " + jobName + "\n" +
                "Location:\t\t" + jobLocation + "\n" +
                "Date:\t\t" + jobStartDate + "\n");
    }

    private void displayCycleJobSelectionVolunteer() {
        System.out.println("8) Back to top of list\n" +
                "9) View more\n" +
                "0) Back\n");
    }

    private boolean displayCurrentJobSelectionVolunteer(Job theJob, Volunteer theVolunteer) {
        boolean successfulSignup = false;
        String jobName = theJob.getJobTitle();
        String jobLocation = theJob.getMyJobLocation();
        GregorianCalendar jobStartDate = theJob.getStartDate();
        GregorianCalendar jobEndDate = theJob.getEndDate();
        String contactName = theJob.getMyContactName();
        String contactNumber = theJob.getMyContactNumber();
        String contactEmail = theJob.getMyContactEmail();
        int numberOfVolunteers = theJob.getCurrentVolunteers().size();
        int jobDifficulty = theJob.getMyJobDifficulty();
        System.out.println("Name:\t\t\t" + jobName + "\n" +
                "Location:\t\t\t" + jobLocation + "\n" +
                "Start Date:\t\t\t" + jobStartDate + "\n" +
                "End Date:\t\t\t" + jobEndDate + "\n" +
                "Contact:\t\t\t" + contactName + "\n" +
                "Contact Phone:\t\t\t" + contactNumber + "\n" +
                "Contact Email:\t\t\t" + contactEmail + "\n" +
                "Current Volunteers:\t\t " + numberOfVolunteers + "\n" +
                "Difficulty:\t\t\t" + jobDifficulty + "\n");
        displaySignupForJobVolunteer();
        boolean menuTrue = true;
        do {
            askForUserSelection();
            String userSelection = myScanner.nextLine();

            switch (userSelection) {
                case "1":
                    boolean isAdded = theVolunteer.addToCurrentJobs(theJob);
                    if (isAdded) {
                        System.out.println("Congrats! " + theJob.getJobTitle() + " is added!");
                        successfulSignup = true;
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
        } while(menuTrue);
        return successfulSignup;
    }

    private void displaySignupForJobVolunteer() {
        System.out.println("1) Sign up for job\n" +
                "0) Go Back\n");
    }

    /*
     Park Manage UI Methods are below
     */

    private void displaySummitJobParkManager(ParkManager theParkManager) {
        //                                              0123456789
        System.out.println("Please indicate Start Date (MM/DD/YYYY):");
        String startDateString = myScanner.nextLine();
        GregorianCalendar startDate = new GregorianCalendar(
                Integer.parseInt(startDateString.substring(0,2)),
                Integer.parseInt(startDateString.substring(3,5)),
                Integer.parseInt(startDateString.substring(6,10)));
        System.out.println("Please indicate End Date (MM/DD/YYYY):");
        String endDateString = myScanner.nextLine();
        GregorianCalendar endDate = new GregorianCalendar(
                Integer.parseInt(endDateString.substring(0,2)),
                Integer.parseInt(endDateString.substring(3,5)),
                Integer.parseInt(endDateString.substring(6,10)));
        System.out.println("Please indicate Address (Street, City, State, ZIP):");
        String address = myScanner.nextLine();
        System.out.println("Please provide contact name:");
        String contactName = myScanner.nextLine();
        System.out.println("Please provide contact phone:");
        String contactNum = myScanner.nextLine();
        System.out.println("Please provide contact email:");
        String contactEmail = myScanner.nextLine();
        System.out.println("Please provide job listing headline:");
        String jobName = myScanner.nextLine();
        System.out.println("Please provide job description:");
        String jobDescription = myScanner.nextLine();
        System.out.println("Please provide a job role:");
        String jobRole = myScanner.nextLine();
        System.out.println("Please provide a description for this role:");
        String jobRoleDescription = myScanner.nextLine();
        System.out.println("Please provide a difficulty (easy, medium, hard): ");
        String difficulty = myScanner.nextLine();
        System.out.println("Please indicate maximum number of volunteers:");
        int numVolunteers = Integer.parseInt(myScanner.nextLine());

        // Leaving the loop blank for now to add multiple job roles
        // Job api needs to be extended to make that work

        Job newJob = new Job(jobName);
        newJob.setStartDate(startDate);
        newJob.setEndDate(endDate);
        newJob.setMyAddress(address);
        newJob.setMyContactName(contactName);
        newJob.setMyContactNumber(contactNum);
        newJob.setMyContactEmail(contactEmail);
        newJob.setMyJobDescription(jobDescription);
        newJob.setMyJobRole(jobRole);
        newJob.setMyJobDescription(jobRoleDescription);
        if (difficulty.toLowerCase().equals("easy")) {
            newJob.setMyDifficulty(0);
        } else if (difficulty.toLowerCase().equals("medium")) {
            newJob.setMyDifficulty(1);
        } else if (difficulty.toLowerCase().equals("hard")) {
            newJob.setMyDifficulty(3);
        } else {
            newJob.setMyDifficulty(99);
        }
        newJob.setMaxVolunteers(numVolunteers);

        displaySummitJobListingParkManager(newJob, theParkManager);
        displayAskToPostJobParkManager(newJob, theParkManager);
    }

    private void displaySummitJobListingParkManager(Job theJob, ParkManager theParkManager) {
        System.out.println("Job Listing Overview:\n");
        displaySeperator();
        System.out.println("Name:\t" + theJob.getJobTitle());
        System.out.println("Location:\t" + theJob.getMyAddress());
        System.out.println("Start Date:\t" + theJob.getStartDate());
        System.out.println("End Date:\t" + theJob.getEndDate());
        System.out.println("Contact Name:\t" + theJob.getMyContactName());
        System.out.println("Contact Phone\t" + theJob.getMyContactNumber());
        System.out.println("Contact Email:\t" + theJob.getMyContactEmail());
        System.out.println("Description:\t" + theJob.getMyJobDescription());
        System.out.println();
        System.out.println("Job Roles:");
        System.out.println("Role:\t" + theJob.getMyJobRole());
        int difficulty = theJob.getMyDifficulty();
        if (difficulty == 0) {
            System.out.println("Difficulty: Easy");
        } else if (difficulty == 1) {
            System.out.println("Difficulty: Medium");
        } else if (difficulty == 2) {
            System.out.println("Difficulty: Hard");
        } else {
            System.out.println("Difficulty: Unknown");
        }
        System.out.println("Volunteers:\t" + theJob.getMaxVolunteers());
        System.out.println("Description:\t" + theJob.getMyJobRoleDescription());
        System.out.println();
    }

    private void displayAskToPostJobParkManager(Job theJob, ParkManager theParkManager) {
        boolean selectionTrue = true;
        do {
            System.out.println("\nWould you like to submit job? (Y/N): ");
            String userInput = myScanner.nextLine();
            if (userInput.toLowerCase().equals("y")) {
                System.out.println("Job Submitted!\n");
                theParkManager.addToSubmittedJobs(theJob);
                selectionTrue = false;
            } else if (userInput.toLowerCase().equals("n")) {
                System.out.println("Job Not Submitted\n");
                selectionTrue = false;
            } else {
                System.out.println("Invalid Response");
            }
        } while (selectionTrue);
    }

    private void displayJobsPostedParkManager(ParkManager theParkManager) {
        ArrayList<Job> currentJobs = theParkManager.getSubmittedJobs();
        if (currentJobs.size() == 0) {
            System.out.println("You have submitted no jobs!");
        } else {
            System.out.println();
            for (Job aJob : currentJobs) {
                int iter = 1;
                displayCycleJob(iter++, aJob);
            }
            boolean menuTrue = true;
            do {
                System.out.println("0) Go back\n");
                askForUserSelection();

                String userSelection = myScanner.nextLine();
                if ("1".equals(userSelection) && currentJobs.size() > 0) {
                    displaySummitJobListingParkManager(currentJobs.get(0), theParkManager);

                } else if ("2".equals(userSelection) && currentJobs.size() > 1) {
                    displaySummitJobListingParkManager(currentJobs.get(1), theParkManager);

                } else if ("3".equals(userSelection) && currentJobs.size() > 2) {
                    displaySummitJobListingParkManager(currentJobs.get(2), theParkManager);

                } else if ("4".equals(userSelection) && currentJobs.size() > 3) {
                    displaySummitJobListingParkManager(currentJobs.get(3), theParkManager);

                } else if ("5".equals(userSelection) && currentJobs.size() > 4) {
                    displaySummitJobListingParkManager(currentJobs.get(4), theParkManager);

                } else if (BACK.equals(userSelection)) {
                    menuTrue = false;

                } else {
                    System.out.println("Invalid Selection\n");

                }
            } while (menuTrue);
        }
    }

    /*
    Office Staff UI Method are below

    // THERE IS NO USER STORY FOR URBAN PARK STAFF
    // ONLY DISPLAYING JOBS IS IMPLEMENTED

     */

    private void displayFinishedJobsUrbanParkStaff(OfficeStaff theOfficeStaff) {
        ArrayList<Job> currentJobs = myJobCoordinator.getFinshedJobs();
        if (currentJobs.size() == 0) {
            System.out.println("There are no approved jobs!");
        } else {
            System.out.println();
            for (Job aJob : currentJobs) {
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

                } else {
                    System.out.println("Invalid Selection\n");

                }
            } while (menuTrue);
        }
    }

    private void displayPendingJobsUrbanParkStaff(OfficeStaff theOfficeStaff) {
        ArrayList<Job> pendingJobs = myJobCoordinator.getPendingJobs();
        if (pendingJobs.size() == 0) {
            System.out.println("There are no pending jobs!");
        } else {
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

                } else {
                    System.out.println("Invalid Selection\n");

                }
            } while (menuTrue);
        }
    }

    /*
    Separator below
     */

    public void displaySeperator() {
        System.out.println("\n" +
                "-------------\n");
    }

	public static void main(String[]args) {
        UserInterfaceConsole test = new UserInterfaceConsole();
        test.start();
    }

}