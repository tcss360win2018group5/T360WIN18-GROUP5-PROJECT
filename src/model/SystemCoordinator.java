
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
    
    private ArrayList<User> myUsers;

    public SystemCoordinator() {
        myPropertyChangeHandler = new PropertyChangeSupport(this);
        myUsers = new ArrayList<User>();
    }    
    
    /** Method to allow attachment of listeners to the instance of this object. */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        myPropertyChangeHandler.addPropertyChangeListener(listener);
    }
    
    
    /**
     * Checks to see that the user trying to sign in exists in the system.
     * 
     * @return 0 if the user exists in the system, 1 otherwise.
     */
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
    
    /**
     * Adds the specified user to the current list of users.
     * 
     * PRECONDITION: canAddUser(theUser) must evaluate to TRUE
     * 
     */
    public void addUser(User theUser) {
        for (User u : this.myUsers) {
            if (u.getUsername().equals(theUser.getUsername())) {
                // warning user already exists
            }
        }
        myUsers.add(theUser);
    }

    /**
     * Checks to see if the user is of the proper subclass.
     * 
     * @return true if theUser is of class type Volunteer, ParkManager, or OfficeStaff
     */
    public boolean canAddUser(User theUser) {
        return theUser instanceof Volunteer || theUser instanceof ParkManager
               || theUser instanceof OfficeStaff;
    }

    /** 
     * Provides the access level of the user that corresponds with theUsername
     * 
     * @return the access level as an integer corresponding to VOLUTNEER_ACCESS_LEVEL, 
     *         PARK_MANAGER_ACCESS_LEVEL, or OFFICE_STAFF_ACCESS_LEVEL, 99 otherwise
     */
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

    /**
     * Provides the user specified by theUserName.
     * 
     * PRECONDITION: signIn(theUserName) == 0
     * 
     */
    public User getUser(String theUserName) {
        return myUsers.stream().filter(u -> u.getUsername().equals(theUserName)).findFirst()
                        .get();
    }

    /** Provides the list of current users. */
    public ArrayList<User> getUsers() {
        return myUsers;
    }
    
    
    /* Helper Methods */
    /**
     * This method exists to update the user information to save job information that the user 
     * committed.
     */
    public void updateUserInformationOnExit(User theUser) {
        for (int i = 0; i < this.myUsers.size(); i++) {
            if (this.myUsers.get(i).getUsername().equals(theUser.getUsername())) {
                // user exist, so must replace it with current user
                this.myUsers.set(i, theUser);
            }
        }
    }

}
