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
	 * delete the database from the system.
	 */
	public void deleteUserDatabase();
	/**
	 * connect to the database, it must be called in initialization.
	 */
	public void connect();
	/**
	 * call it before exit.
	 */
	public void disconnect();
	/**
	 * add user to the database.
	 * @param user
	 * @return true if success.
	 */
	public boolean addUser(User user);
	/**
	 * delete the user that name is username
	 * @param username
	 * @return true if success.
	 */
	public boolean removeUser(String username);
	/**
	 * update user in the database.
	 * @param user
	 * @return true if success.
	 */
	public boolean updateUser(User user);
	public User getUser(String username);
	public Vector<RankPair> getRankList();
}
