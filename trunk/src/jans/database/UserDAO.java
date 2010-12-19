package jans.database;

import java.util.Vector;

/**
 * User Data Access Object
 * @author lqhl
 *
 */
public interface UserDAO {
	/**
	 * create a database to store user information of JANS. 
	 */
	public void createUserDatabase();
	/**
	 * connect to the database, it must be called.
	 */
	public void connect();
	public void disconnect();
	public boolean add(User user);
	public boolean delete(String username);
	public boolean updateUser(User user);
	public User getUser(String username);
	public Vector<RankPair> getRankList();
}
