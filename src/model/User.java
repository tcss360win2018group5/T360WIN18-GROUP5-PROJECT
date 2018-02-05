package model;

public abstract class User {
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



}
