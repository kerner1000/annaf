package de.mpg.mpiz.koeln.anna;

import de.kerner.commons.ee.beans.UserBean;

public class UserBeanAnna extends UserBean {
	
	private static final long serialVersionUID = -1948089856911975592L;
	private boolean whantsToLogIn;
	
	// Getter / Setter //

	public void setWhantsToLogIn(boolean whantsToLogIn) {
		this.whantsToLogIn = whantsToLogIn;
	}

	public boolean isWhantsToLogIn() {
		return whantsToLogIn;
	}

}
