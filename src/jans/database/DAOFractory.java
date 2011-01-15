package jans.database;

import jans.common.Config;
import jans.common.Lib;

public class DAOFractory {
	/**
	 * Construct a UserDAO object according to the property UserDAOImpl in configuration file
	 */
	public static UserDAO getUserDAO() {
		String UserDAOName = Config.getString("UserDAOImpl");
		return (UserDAO) Lib.constructObject(UserDAOName);
	}
}
