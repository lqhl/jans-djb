package jans.database;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;

public class UserDAOTest implements UserDAO {
	private Hashtable<String, User> users = new Hashtable<String, User>();

	@Override
	public boolean addUser(User user) {
		if (users.containsKey(user.getUsername()))
			return false;
		else {
			users.put(user.getUsername(), user);
			return true;
		}
	}

	@Override
	public void connect() {
		// do nothing
	}

	@Override
	public void createUserDatabase() {
		// do nothing
	}

	@Override
	public void deleteUserDatabase() {
		// do nothing
	}

	@Override
	public void disconnect() {
		// do nothing
	}

	@Override
	public Vector<RankPair> getRankList() {
		Vector<RankPair> result = new Vector<RankPair>();
		for (User user : users.values())
			result.add(new RankPair(user.getUsername(), user.getAverageScore()));
		Collections.sort(result);
		return result;
	}

	@Override
	public User getUser(String username) {
		return users.get(username);
	}

	@Override
	public boolean removeUser(String username) {
		return users.remove(username) != null ? true : false;
	}

	@Override
	public boolean updateUser(User user) {
		if (users.get(user.getUsername()) != null) {
			users.put(user.getUsername(), user);
			return true;
		}
		else
			return false;
	}

}
