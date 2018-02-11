package model;

import java.io.Serializable;

import util.SystemConstants;

public class OfficeStaff extends User implements Serializable {
	/**
	 * Creates a park manager with the given username.
	 * 
	 * @param theUsername the username for the park manager user.
	 */
	public OfficeStaff(String theUsername) {
		super(theUsername, SystemConstants.OFFICE_STAFF_ACCESS_LEVEL);
	}
}
