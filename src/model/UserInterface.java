package model;

import java.util.Scanner;

public class UserInterface {

    private static final String YES = "y";

    private static final String NO = "n";

    private static final String BACK = "b";

    private static final String EXIT = "x";
    
    private static final String DIRECTIONS = "Commands: Y (yes) | N (no) | B (back) | X (exit)";

    private final SystemCoordinator mySystemCoordinator;
    private final JobCoordinator myJobCoordinator;
    
    /**
     * Scanner to read user input from console.
     */
    Scanner myScanner;

    /*
     * UI prompts, will need to add more later.
     */
    private String[] myGreetings = {"Welcome, please enter a username"};

    public UserInterface() {
        myScanner = new Scanner(System.in);
        mySystemCoordinator = new SystemCoordinator();
        myJobCoordinator = new JobCoordinator();
    }

    public void start() {
        System.out.println(myGreetings[0]);
        String userInput;
        
        userInput = myScanner.next();
        
        //If the user exists
        if (mySystemCoordinator.signIn(userInput)) {
            //send them to login
        } else {
            //have them create username
        }
        
        while (true) {
            System.out.println(DIRECTIONS);
            userInput = myScanner.next();
            userInput = userInput.toLowerCase();

            if (userInput.equals(YES)) {
                System.out.println("Pressed Y");
            } else if (userInput.equals(NO)) {
                System.out.println("Pressed N");
            } else if (userInput.equals(BACK)) {
                System.out.println("Pressed B");
            } else if (userInput.equals(EXIT)) {
                System.out.println("Thank you. Goodbye.");
                System.exit(0);
            } else {
                System.out.println("Invalid command.");
            }

        }
    }





    public static void main(String[]args) {
        UserInterface test = new UserInterface();
        test.start();
    }

}
