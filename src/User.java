
public abstract class User {
	
	private int myAccessLevel;
	
	private String myUsername;
	
	protected User(final String theUsername, final int theAccessLevel) {
		myUsername = theUsername;
		myAccessLevel = theAccessLevel;
	}
	
	public String getUsername() {
		return myUsername;
	}
	
	public int getAccessLevel() {
		return myAccessLevel;
	}
	
	

}
