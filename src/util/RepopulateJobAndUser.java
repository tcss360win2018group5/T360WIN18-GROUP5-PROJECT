package util;

import model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.GregorianCalendar;


// Run this class to repopulate jobs & users after making backend changes
public class RepopulateJobAndUser {

    private static final String SYSTEM_COORDINATOR_NAME = "data/SystemCoordinator.ser";
    private static final String JOB_COORDINATOR_NAME = "data/JobCoordinator.ser";
    private final SystemCoordinator mySystemCoordinator = new SystemCoordinator();
    private final JobCoordinator myJobCoordinator = new JobCoordinator();

    public RepopulateJobAndUser() {
        if (doesFileExist(SYSTEM_COORDINATOR_NAME) && doesFileExist(JOB_COORDINATOR_NAME)) {
            System.out.println("Serializable objects exist\n\n" +
                    "Please delete and try again if you would like restore to a working demo.");
            System.exit(0);
        }
    }

    private boolean doesFileExist(String thisPath) {
        boolean fileExist = false;
        File file = new File(thisPath);
        if (file.exists()) {
            fileExist = true;
        }
        return fileExist;
    }

    private void saveSystem() {
        writeObjectToDisk(SYSTEM_COORDINATOR_NAME, mySystemCoordinator);
        writeObjectToDisk(JOB_COORDINATOR_NAME, myJobCoordinator);
        System.out.println("\nNew serializable objects are created.");
        System.exit(0);
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


    private void tryToAddJob(Job theJob) {
        int result = myJobCoordinator.canAddJob(theJob);
        if (result == 1) {
            System.out.println("This job already exists!\n");
        }
        else if (result == 2) {
            System.out.println("This job is longer than the maximum allowed job of "
                    + JobCoordinator.MAXIMUM_JOB_LENGTH + " days");
        }
        else if (result == 3) {
            System.out.println("This job is further away than the maximum allowed "
                    + JobCoordinator.MAXIMUM_DAYS_AWAY_TO_POST_JOB
                    + " days from today");
        }
        else {
            myJobCoordinator.addPendingJob(theJob);
            System.out.println("Job Submitted!\n");
        }
    }

    private void createUserLogin(String userName, int accessLevel) {
        int doesUserExist = mySystemCoordinator.signIn(userName);
        if (doesUserExist != 0) {
            System.out.println("Username " + userName + " is created!");
            switch (accessLevel) {
                case 0:    // 0: // Urban Parks Staff Member
                    mySystemCoordinator.addUser(new OfficeStaff(userName));
                    break;
                case 1:    // 1: // Park Manager
                    mySystemCoordinator.addUser(new ParkManager(userName));
                    break;
                case 2:    // 2: // Volunteer
                    mySystemCoordinator.addUser(new Volunteer(userName));
                    break;
                default:
                    System.out.println("Invalid system access " +
                            "to create new user");
                    break;
            }
        } else {
            System.out.println("Try Again");
        }
    }

    private void submitJobs(String theStartDate,
                            String theEndDate,
                            String theAddress,
                            String theContactName,
                            String theContactNum,
                            String theContactEmail,
                            String theJobHeadline,
                            String theJobDescription,
                            String theJobRole,
                            String theJobRoleDescription,
                            String theJobDifficulty,
                            int theNumOfVolunteers) {
        // "Please indicate Start Date (MM/DD/YYYY):");
        String startDateString = theStartDate;
        GregorianCalendar startDate = new GregorianCalendar(
                Integer.parseInt(startDateString.substring(6,10)),
                Integer.parseInt(startDateString.substring(0,2)),
                Integer.parseInt(startDateString.substring(3,5)));
        // "Please indicate End Date (MM/DD/YYYY):");
        String endDateString = theEndDate;
        GregorianCalendar endDate = new GregorianCalendar(
                Integer.parseInt(endDateString.substring(6,10)),
                Integer.parseInt(endDateString.substring(0,2)),
                Integer.parseInt(endDateString.substring(3,5))
        );
        // "Please indicate Address (Street, City, State, ZIP):");
        String address = theAddress;
        // "Please provide contact name:");
        String contactName = theContactName;
        // "Please provide contact phone:");
        String contactNum = theContactNum;
        // Please provide contact email:");
        String contactEmail = theContactEmail;
        // "Please provide job listing headline:");
        String jobName = theJobHeadline;
        // "Please provide job description:");
        String jobDescription = theJobDescription;
        // "Please provide a job role:");
        String jobRole = theJobRole;
        // "Please provide a description for this role:");
        String jobRoleDescription = theJobRoleDescription;
        // "Please provide a difficulty (easy, medium, hard): ");
        String difficulty = theJobDifficulty;
        // Please indicate maximum number of volunteers:");
        int numVolunteers = theNumOfVolunteers;

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
        tryToAddJob(newJob);
    }

    public void createJob_03022018() {
        // (MM/DD/YYYY)
        submitJobs("03/02/2018",
                "03/02/2018",
                "4503 Beach Dr SW, Seattle, WA 98116",
                "Rio Del Montana",
                "555-555-4503",
                "rio@gmail.com",
                "Habitat Revival Fridays",
                "Join us for a restoration work party at Me-Kwa-Mook",
                "Work",
                "Retore the Me-Kwa-Mook",
                "easy",
                5);
    }

    public void createJob_03032018() {
        // (MM/DD/YYYY)
        submitJobs("03/03/2018",
                "03/03/2018",
                "10505 35th Ave NE, Seattle WA 98125",
                "Lucy Weinberg",
                "555-555-1050",
                "lucy@gmail.com",
                "Saturday Work at Meadowbrook",
                "Join us for a restoration work party at Meadowbrook Playfield",
                "Work",
                "Do work",
                "medium",
                5);
    }

    public void createJob_03032018_conflict() {
        // (MM/DD/YYYY)
        submitJobs("03/03/2018",
                "03/03/2018",
                "8011 Fauntleroy Way, Seattle, WA, 98136",
                "Sharon Baker",
                "555-555-8011",
                "shar@gmail.com",
                "Lincoln Park Volunteer Work Party",
                "Join us for a restoration work party at Lincoln Park",
                "Work",
                "Work Party",
                "easy",
                5);
    }

    public static void main(String[]args) {
        RepopulateJobAndUser re = new RepopulateJobAndUser();
        re.createUserLogin("test_vol", 2);
        re.createUserLogin("test_pm", 1);
        re.createUserLogin("test_staff", 0);
        re.createJob_03022018();
        re.createJob_03032018();
        re.createJob_03032018_conflict();
        re.saveSystem();
    }
}
