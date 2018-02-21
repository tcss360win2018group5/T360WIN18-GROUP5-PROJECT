
package model;

import java.io.Serializable;
import java.util.ArrayList;

public class SystemCoordinator implements Serializable {
    public static final int DEFAULT_ACCESS_LEVEL = 2;
    public static final int VOLUTNEER_ACCESS_LEVEL = 2;
    public static final int PARK_MANAGER_ACCESS_LEVEL = 1;
    public static final int OFFICE_STAFF_ACCESS_LEVEL = 0;
    
    public ArrayList<User> myUsers;

    public SystemCoordinator() {
        myUsers = new ArrayList<User>();
    }
    public int signIn(String theUsername) {
        int isUser = 1;

        for (User u : this.myUsers) {
            if (u.getUsername().equals(theUsername)) {
                isUser = 0;
            }
        }

        return isUser;
    }

    // This method exists to update the user information
    // to save job information that the user committed on console
    // Un-needed - use for testing purposes until deleted
    public void updateUserInformationOnExit(User theUser) {
        for (int i = 0; i < this.myUsers.size(); i++) {
            if (this.myUsers.get(i).getUsername().equals(theUser.getUsername())) {
                // user exist, so must replace it with current user
                this.myUsers.set(i, theUser);
            }
        }
    }

    /***
     * PRECONDITION: canAddUser(theUser) must evaluate to TRUE
     * 
     * @param theUser
     */
    public void addUser(User theUser) {
        for (User u : this.myUsers) {
            if (u.getUsername().equals(theUser.getUsername())) {
                // warning user already exists
            }
        }

        myUsers.add(theUser);
    }

    public boolean canAddUser(User theUser) {
        return theUser instanceof Volunteer || theUser instanceof ParkManager
               || theUser instanceof OfficeStaff;
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

    public User getUser(String theUserName) {
        return myUsers.stream().filter(u -> u.getUsername().equals(theUserName)).findFirst()
                        .get();
    }

    @SuppressWarnings("unchecked")
    public ArrayList<User> getUsers() {
        return (ArrayList<User>) myUsers.clone();
    }
}
