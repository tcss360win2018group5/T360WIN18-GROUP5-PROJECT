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

    public void start2() {
//        System.out.println(myGreetings[0]);
//        String userInput;
//
//        userInput = myScanner.next();
//
//        //If the user exists
//        if (mySystemCoordinator.signIn(userInput)) {
//            //send them to login
//        } else {
//            //have them create username
//        }
//
//        while (true) {
//            System.out.println(DIRECTIONS);
//            userInput = myScanner.next();
//            userInput = userInput.toLowerCase();
//
//            if (userInput.equals(YES)) {
//                System.out.println("Pressed Y");
//            } else if (userInput.equals(NO)) {
//                System.out.println("Pressed N");
//            } else if (userInput.equals(BACK)) {
//                System.out.println("Pressed B");
//            } else if (userInput.equals(EXIT)) {
//                System.out.println("Thank you. Goodbye.");
//                System.exit(0);
//            } else {
//                System.out.println("Invalid command.");
//            }
//
//        }
    }

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
                        welcomeUrbanParkStaff(userName);
                        break;
                    case 1: // Park Manager
                        welcomeParkManager(userName);
                        break;
                    case 2: // Volunteer
                        welcomeVolunteer(userName);
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

    private void welcomeVolunteer(String theUserName) {
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
                    displayVolunteerJobs();
                    displaySeperator();
                    displayVolunteerMenu();
                    break;
                case "2":
                    displayJobsSignedUp(new Volunteer(""));
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

    private void welcomeParkManager(String theUserName) {
        displaySeperator();
        displayGreeting(theUserName);
        displayDate();
        displayParkManagerMenu();
        boolean menuTrue = true;
        do {
            askForUserSelection();
            String userSelection = myScanner.nextLine();

            switch (userSelection) {
                case LOGOUT:
                    saveSystem();
                default:
                    System.out.println("Invalid Selection");
                    break;
            }
        } while(menuTrue);
    }

    private void welcomeUrbanParkStaff(String theUserName) {
        displaySeperator();
        displayGreeting(theUserName);
        displayDate();
        displayUrbanStaffMenu();
        boolean menuTrue = true;
        do {
            askForUserSelection();
            String userSelection = myScanner.nextLine();

            switch (userSelection) {
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
        System.out.println("1) View approved job\n" +
                "2) View pending jobs\n" +
                "0) Logout & Exit\n");
    }


    /*
        Volunteer UI Display Methods are below
     */

    private void displayVolunteerJobs() {
        System.out.println("Available jobs listed below:\n");
        boolean menuTrue = true;
        int loop = 0;
        do {
            int iter = 1;
            while (iter <= 5) {
                displayCycleJob(iter++ % 6, new Job(""));
            }
            displayCycleJobSelection();
            askForUserSelection();
            String userSelection = myScanner.nextLine();

            switch (userSelection) {
                case "1":
                    boolean isSignupSuccessful = displayCurrentJobSelection(new Job(""), new Volunteer(""));
                    menuTrue = !isSignupSuccessful;
                    break;
                case "2":
                    isSignupSuccessful = displayCurrentJobSelection(new Job(""), new Volunteer(""));
                    menuTrue = !isSignupSuccessful;;
                    break;
                case "3":
                    isSignupSuccessful = displayCurrentJobSelection(new Job(""), new Volunteer(""));
                    menuTrue = !isSignupSuccessful;
                    break;
                case "4":
                    isSignupSuccessful = displayCurrentJobSelection(new Job(""), new Volunteer(""));
                    menuTrue = !isSignupSuccessful;
                    break;
                case "5":
                    isSignupSuccessful = displayCurrentJobSelection(new Job(""), new Volunteer(""));
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

    private void displayJobsSignedUp(Volunteer theVolunteer) {
        ArrayList<Job> currentJobs = theVolunteer.getCurrentJobs();
        if (currentJobs.size() == 0) {
            System.out.println("You are currently signed up for no jobs!");
        } else {
            for (Job aJob : currentJobs) {
                int iter = 0;
                displayCycleJob(iter++, aJob);
            }
            boolean menuTrue = true;
            do {
                String userSelection = myScanner.nextLine();
                if ("1".equals(userSelection) && currentJobs.size() > 0) {
                    boolean isSignupSuccessful = displayCurrentJobSelection(currentJobs.get(0), theVolunteer);
                    menuTrue = !isSignupSuccessful;

                } else if ("2".equals(userSelection) && currentJobs.size() > 1) {
                    boolean isSignupSuccessful;
                    isSignupSuccessful = displayCurrentJobSelection(currentJobs.get(1), theVolunteer);
                    menuTrue = !isSignupSuccessful;

                } else if ("3".equals(userSelection) && currentJobs.size() > 2) {
                    boolean isSignupSuccessful;
                    isSignupSuccessful = displayCurrentJobSelection(currentJobs.get(2), theVolunteer);
                    menuTrue = !isSignupSuccessful;

                } else if ("4".equals(userSelection) && currentJobs.size() > 3) {
                    boolean isSignupSuccessful;
                    isSignupSuccessful = displayCurrentJobSelection(currentJobs.get(3), theVolunteer);
                    menuTrue = !isSignupSuccessful;

                } else if ("5".equals(userSelection) && currentJobs.size() > 4) {
                    boolean isSignupSuccessful;
                    isSignupSuccessful = displayCurrentJobSelection(currentJobs.get(4), theVolunteer);
                    menuTrue = !isSignupSuccessful;

                } else if (BACK.equals(userSelection)) {
                    menuTrue = false;

                } else {
                    System.out.println("Invalid Selection");

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

    private void displayCycleJobSelection() {
        System.out.println("8) Back to top of list\n" +
                "9) View more\n" +
                "0) Back\n");
    }

    private boolean displayCurrentJobSelection(Job theJob, Volunteer theVolunteer) {
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
        displaySignupForJob();
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

    private void displaySignupForJob() {
        System.out.println("1) Sign up for job\n" +
                "0) Go Back\n");
    }

    /*
     Park Manage Methods are below
     */


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
