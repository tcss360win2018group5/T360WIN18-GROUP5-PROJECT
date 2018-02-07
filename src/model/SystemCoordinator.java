package model;

import java.util.ArrayList;

public class SystemCoordinator {
	private static ArrayList<User> myUsers;

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
	
	public void addUser(User theUser) {
       for (User u : this.myUsers) {
            if (u.getUsername().equals(theUser.getUsername())) {
                // warning user already exists
            }
        }
       
	    this.addUser(theUser);
	}


	@SuppressWarnings("unchecked")
	public ArrayList<String> getUsers() {
		return (ArrayList<String>) myUsers.clone();
	}
}
