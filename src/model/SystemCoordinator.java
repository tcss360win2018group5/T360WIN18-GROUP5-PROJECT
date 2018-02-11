package model;

import java.io.Serializable;
import java.util.ArrayList;

public class SystemCoordinator implements Serializable {
	public ArrayList<User> myUsers;
	
    private ArrayList<Job> myVolunteerJobs;

	public SystemCoordinator() {
		myUsers = new ArrayList<User>();
		myVolunteerJobs = new ArrayList<Job>();
	}

	public boolean signIn(String theUsername) {
		boolean isUser = false;

		for (User u : this.myUsers) {
		    if (u.getUsername().equals(theUsername)) {
		        isUser = true;

		    }
		}
		
		return isUser;
	}

	public int getAccessLevel(String theUsername) {
	    // Number 99 to return if unsuccessful
	    int userAccessLevel = 99;

        for (User u : this.myUsers) {
            if (u.getUsername().equals(theUsername)) {
                userAccessLevel = u.getAccessLevel();
            }
        }

        return userAccessLevel;
    }

	public void addUser(User theUser) {
       for (User u : this.myUsers) {
            if (u.getUsername().equals(theUser.getUsername())) {
                // warning user already exists
            }
        }
       
	    myUsers.add(theUser);
	}

	/**
	 * The below 2 methods were my attempt at getting SystemCoordinator
	 * to save the jobs for the Volunteer's between runs.
	 * It fails because it relies on fetching the information from
	 * Volunteer. Need to have that info independently.
	 */
	public void addVolunteerJobs(Job theJob) {
		myVolunteerJobs.add(theJob);
	}
	
    public void resetVolunteerJobs() {
    	for (User u : this.myUsers) {
    		if (u instanceof Volunteer) {
    			for (Job j : this.myVolunteerJobs) {
    				if (((Volunteer) u).getCurrentJobs().contains(j)) {
        				((Volunteer) u).signUpForJob(j);
    				}

    			}
    		}
    	}
    }


	@SuppressWarnings("unchecked")
	public ArrayList<User> getUsers() {
		return (ArrayList<User>) myUsers.clone();
	}

//
//	public static void main(String[]args) {
//        SystemCoordinator sc = new SystemCoordinator();
//        sc.addUser(new Volunteer("test"));
//        sc.getUsers().forEach(System.out::println);
//    }
}
