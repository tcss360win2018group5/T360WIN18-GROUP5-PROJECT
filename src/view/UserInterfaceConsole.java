package view;

import java.io.*;
import java.util.Scanner;

import model.JobCoordinator;
import model.SystemCoordinator;
import model.User;
import model.Volunteer;

public class UserInterfaceConsole {

    private static final String BACK = "9";
    private static final String EXIT = "0";
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
            System.out.println("Success");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return newObject;
    }

    private void saveSystem() {
        writeObjectToDisk(SYSTEM_COORDINATOR_NAME, mySystemCoordinator);
        writeObjectToDisk(JOB_COORDINATOR_NAME, myJobCoordinator);

    }

    private static void writeObjectToDisk(String thisName, Object thisObject) {
        try {
            FileOutputStream out = new FileOutputStream(thisName);
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(thisObject);
            oos.close();
            System.out.println("Success");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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
            displayGreeting();
            displayIntroLogin();
            askForUserSelection();
            userInput = myScanner.nextLine();
            userInput = userInput.toLowerCase();

            switch (userInput) {
                case "1":
                    userLogin(myScanner, userInput);
                    break;
                case "2":
                    createUserLogin(myScanner, userInput);
                    break;
                case EXIT:
                    System.out.println("\nThank you. Goodbye.");
                    saveSystem();
                    System.exit(0);
                default:
                    System.out.println("\nInvalid command.\n");
                    break;
            }

        } while (true);
    }

    /*
    Place all methods for UI navigation below here
     */

    private void userLogin(Scanner myScanner, String userInput){
        boolean continueMethod = true;
        do {
            System.out.println("\nPlease enter a username or press 9 to go back:");
            userInput = myScanner.nextLine();
            boolean doesUserExist = mySystemCoordinator.signIn(userInput);
            int userAccessLevel = mySystemCoordinator.getAccessLevel(userInput);

            if (doesUserExist) {
                switch(userAccessLevel) {
                    case 1:
                        System.out.println(userAccessLevel);
                        break;
                    case 2:
                        System.out.println(userAccessLevel);
                        break;
                    case 3:
                        System.out.println(userAccessLevel);
                        break;
                    default:
                        System.out.println("Access level invalid");
                        break;
                }
            } else if (userInput.equals(BACK)){
                continueMethod = false;
            } else {
                System.out.println("User does not exist");
            }
        } while (continueMethod);
        seperator();
    }


    private void createUserLogin(Scanner myScanner, String userInput) {
        boolean continueMethod = true;
        do {
            System.out.println("\nPlease enter a username or press 9 to go back:");
            userInput = myScanner.nextLine();
            boolean doesUserExist = mySystemCoordinator.signIn(userInput);
            int userAccessLevel = mySystemCoordinator.getAccessLevel(userInput);

            if (BACK.equals(userInput.toLowerCase())) {
                System.out.println("Returning to previous menu\n");
                seperator();
                continueMethod = false;

            } else if (doesUserExist == false) {
                System.out.println("Username " + userInput + " is created!");
                mySystemCoordinator.addUser(new Volunteer(userInput));
                seperator();
                continueMethod = false;

            } else if (doesUserExist == true) {
                System.out.println("Username " + userInput + " exists.\n" +
                    "Please enter a different user name\n");

            } else {
                System.out.println("Try Again");

            }
        } while (continueMethod);
    }

    /*
    Place all methods for UI Display Prompts are below here
     */

    private void displayGreeting() {
        System.out.println("Welcome to Urban Parks!\n");
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
        System.out.println("Welcome volunteer_test\n" +
                            "Today is 01/30/2018\n" +
                            "\n" +
                            "1) Sign up for job\n" +
                            "2) View signed up jobs\n" +
                            "0) Logout\n");
    }

    public void seperator() {
        System.out.println("\n" +
                "-------------\n");
    }

	public static void main(String[]args) {
        UserInterfaceConsole test = new UserInterfaceConsole();
        test.start();
    }

}
