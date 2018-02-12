
package model;

import java.io.Serializable;

import util.SystemConstants;

public class ParkManager extends User implements Serializable {
    /**
     * Creates a park manager with the given username.
     * 
     * @param theUsername the username for the park manager user.
     */
    public ParkManager(String theUsername) {
        super(theUsername, SystemConstants.PARK_MANAGER_ACCESS_LEVEL);
    }
}
