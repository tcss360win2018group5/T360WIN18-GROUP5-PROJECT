
package model;

import java.io.Serializable;
import java.util.Objects;

public abstract class User implements Serializable, Cloneable {
    /** The access level of the user. */
    private int myAccessLevel;

    /** The username of the user. */
    private String myUsername;

    /**
     * Creates a user based on the specified username and access level.
     *
     * @param theUsername The username to give this user.
     * @param theAccessLevel The access level to give this user.
     */
    public User(final String theUsername, final int theAccessLevel) {
        myUsername = theUsername;
        myAccessLevel = theAccessLevel;
    }

    /**
     * Retrieves the String representation of the username for this user.
     */
    public String getUsername() {
        return myUsername;
    }

    /**
     * Retrieves the access level of this user.
     */
    public int getAccessLevel() {
        return myAccessLevel;
    }
    
    @Override
    public abstract Object clone();
    
    @Override
    public int hashCode() {
        return Objects.hash(myAccessLevel, myUsername);
    }
    
    @Override
    public boolean equals(Object theObject) {
        boolean result = false;
        if (this == theObject) {
            result = true;
        } else if (theObject == null) {
            result = false;
        } else if (this.getClass() == theObject.getClass()) {
            User theOtherUser = (User) theObject;
            
            result = Objects.equals(this.myAccessLevel, theOtherUser.myAccessLevel) &&
                            Objects.equals(this.myUsername, theOtherUser.myUsername);
        }
        
        return result;
    }
}
