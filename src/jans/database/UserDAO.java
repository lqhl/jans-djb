package jans.database;

import java.util.Vector;

public interface UserDAO {
	public boolean createUserDatabase();
	public boolean connect();
	public boolean disconnect();
	public boolean add(User user);
	public boolean delete(String username);
	public boolean updateUser(User user);
	public User getUser(String username);
	public Vector<Double> averageScoreList();
}
