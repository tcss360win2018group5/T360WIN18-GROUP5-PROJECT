import java.util.ArrayList;

public class SystemCoordinator {
	
	//Should be some kind of date
	private int currentDate;
	
	private static ArrayList<String> myUsers = new ArrayList<String>();
	
	protected static boolean signIn(String theUsername) {
		boolean isUser = false;
		for (String s: myUsers) {
			if (s.equals(theUsername)) {
				isUser = true;
			}
		}
		return isUser;
	}

}
