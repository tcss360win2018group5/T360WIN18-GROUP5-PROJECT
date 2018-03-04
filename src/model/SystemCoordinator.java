
package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;

public final class SystemCoordinator implements Serializable {
    public static final int DEFAULT_ACCESS_LEVEL = 2;
    public static final int VOLUNTEER_ACCESS_LEVEL = 2;
    public static final int PARK_MANAGER_ACCESS_LEVEL = 1;
    public static final int OFFICE_STAFF_ACCESS_LEVEL = 0;
    
    private final PropertyChangeSupport myPropertyChangeHandler;
    
    private final ArrayList<User> myUsers;

    public SystemCoordinator() {
        myPropertyChangeHandler = new PropertyChangeSupport(this);
        myUsers = new ArrayList<User>();
    }    
    
    /** Method to allow attachment of listeners to the instance of this object. */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        myPropertyChangeHandler.addPropertyChangeListener(listener);
    }
    
    
    public int signIn(String theUsername) {
        int isUser = 1;

        for (User u : this.myUsers) {
            if (u.getUsername().equals(theUsername)) {
                isUser = 0;
                myPropertyChangeHandler.firePropertyChange(SystemEvents.SIGNIN.name(),
                                                           null, u.getUsername());
            }
        }

        return isUser;
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
//        System.out.println("Success");
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

    public ArrayList<User> getUsers() {
        return myUsers;
    }
    
    
    /* Helper Methods */
    
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

}
