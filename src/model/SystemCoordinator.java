package model;

import java.io.Serializable;
import java.util.ArrayList;

public class SystemCoordinator implements Serializable {
	public ArrayList<User> myUsers;

	public SystemCoordinator() {
		myUsers = new ArrayList<User>();
	}

	//	public static void addUser(User theUser) {
	//
	//		if (theUser instanceof User) {
	//			myUsers.add(theUser);
	//			System.out.println("Added user: " + theUser.getUsername());
	//		} else {
	//			System.out.println("Not a valid User type");
	//		}
	//
	//	}

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
	
//	public void signUserUpForJob(Job theJob, Volunteer theVolunteer) {
//		for (User u : this.myUsers) {
//			if (u == theVolunteer) {
//				Volunteer temp = (Volunteer) theVolunteer;
//				temp.si
//			}
//		}
//
//	}


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
