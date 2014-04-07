package lbank.untouchable;

public class SecurityAccessManager {
	
	public boolean canAccess(String user, String account) {
		if ("admin".equals(user))
			return true;
		return false;
	}

}
