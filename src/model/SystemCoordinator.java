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

	public boolean signIn(User theUser) {
		boolean isUser = false;
		if (theUser instanceof Volunteer 
				|| theUser instanceof ParkManager
				|| theUser instanceof OfficeStaff) {
			for (User s: myUsers) {
				if (s.getUsername().equals(theUser.getUsername())) {
					isUser = true;
				} else {
					isUser = false;
					System.out.println("Not a current user");
					//sign up instead.
				}
			}
		} else {
			System.out.println("Invalid type added");
		}
		
		return isUser;
	}


	@SuppressWarnings("unchecked")
	public ArrayList<String> getUsers() {
		return (ArrayList<String>) myUsers.clone();
	}
}
