package jans.database;

import jans.common.Config;
import jans.common.Lib;

import java.util.Vector;

public class SelfTest {
	public static void main(String args[]) {
		Lib.enableDebugFlags("d");
		Config.load("server.conf");
		UserDAO userDAO = DAOFractory.getUserDAO();
		userDAO.connect();
		Vector<Double> scores = new Vector<Double>();
		scores.add(354.2);
		userDAO.addUser(new User("wxa", "sb", scores));
		User user2 = userDAO.getUser("wxa");
		user2.print();
	}
}