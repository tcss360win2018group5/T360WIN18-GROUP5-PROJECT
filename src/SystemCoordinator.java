import java.util.ArrayList;
import java.util.GregorianCalendar;

public class SystemCoordinator {
	
	//Should be some kind of date
	private static GregorianCalendar myCurrentDate = new GregorianCalendar();
	
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
	
	public static GregorianCalendar getCurrentDate() {
	    return myCurrentDate;
	}

}
