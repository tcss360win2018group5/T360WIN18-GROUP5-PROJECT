import java.util.ArrayList;

public class SystemCoordinator {
	private final ArrayList<String> myUsers;
	
	public SystemCoordinator() {
	    myUsers = new ArrayList<String>();
	}
	
	protected boolean signIn(String theUsername) {
		boolean isUser = false;
		for (String s: this.myUsers) {
			if (s.equals(theUsername)) {
				isUser = true;
			}
		}
		return isUser;
	}
	
	@SuppressWarnings("unchecked")
    protected ArrayList<String> getUsers() {
	    return (ArrayList<String>) myUsers.clone();
	}
}
