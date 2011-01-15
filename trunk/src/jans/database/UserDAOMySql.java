package jans.database;

import jans.common.Config;
import jans.common.Lib;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * This class is used to access user data in JANS
 * through a MySQL database
 * @author lqhl
 *
 */
public class UserDAOMySql implements UserDAO {
	private String admin;
	private String adminPassword;
	private String superAdmin;
	private String superAdminPassword;
	
	private Connection connection;
	private Statement statement;
	
	protected static char dbgDatabase = 'd';
	
	public UserDAOMySql() {
		superAdmin = Config.getString("rootOfDatabase", "root");
		superAdminPassword = Config.getString("passwordOfRoot", "");
		admin = Config.getString("adminOfUserDatabase", "DJB");
		adminPassword = Config.getString("passwordOfUserDatabase", "JBD");
	}
	
	@Override
	public boolean addUser(User user) {
		try {
			statement.executeUpdate("INSERT INTO users VALUES ('" +
				user.getUsername() + "', '" +
				user.getPassword() + "', " +
				user.getAverageScore() + ", '" +
				user.getJoinDate() + "')");
			Vector<Double> scores = user.getScores();
			for (Double score : scores)
				statement.executeUpdate("INSERT INTO scores VALUES ('" + user.getUsername() + "', " + score + ")");
			return true;
		} catch (SQLException e) {
			if (Lib.test(dbgDatabase))
				e.printStackTrace();
			return false;
		}
	}

	@Override
	public Vector<RankPair> getRankList() {
		try {
			ResultSet rs = statement.executeQuery("SELECT username, averageScore FROM users ORDER BY averageScore");
			Vector<RankPair> result = new Vector<RankPair>();
			while (rs.next())
				result.add(new RankPair(rs.getString("username"), rs.getDouble("averageScore")));
			return result;
		} catch (SQLException e) {
			if (Lib.test(dbgDatabase))
				e.printStackTrace();
			return null;
		}
	}

	@Override
	public void connect() {
		try {
			String url = "jdbc:mysql://localhost:3306/JansUserDatabase";
			connection = DriverManager.getConnection(url, admin, adminPassword);
			statement = connection.createStatement();
		} catch (Exception e) {
			if (Lib.test(dbgDatabase))
				e.printStackTrace();
		}
	}

	@Override
	public void createUserDatabase() {
		try {
			Statement stmt;

			//Create a new database
			
			// Register the JDBC driver for MySQL.
			Class.forName("com.mysql.jdbc.Driver");

			// Define URL of database server for
			// database named mysql on the localhost
			// with the default port number 3306.
			String url = "jdbc:mysql://localhost:3306/mysql";

			// Get a connection to the database for a
			// user named root with a blank password.
			// This user is the default administrator
			// having full privileges to do anything.
			Connection con = DriverManager.getConnection(url, superAdmin, superAdminPassword);

			// Get a Statement object
			stmt = con.createStatement();

			// Create the new database
			stmt.executeUpdate("CREATE DATABASE JansUserDatabase");
			// Register a new user named auser on the
			// database named JunkDB with a password
			// drowssap enabling several different
			// privileges.
			stmt.executeUpdate("GRANT SELECT,INSERT,UPDATE,DELETE," +
					"CREATE,DROP " + "ON JansUserDatabase.* TO '" +
					admin + "'@'localhost' " +
					"IDENTIFIED BY '" + adminPassword + "';");
			con.close();
			
			//Create a new table
			
			// Define URL of database server for
			// database named JansUserDatabase on the localhost
			// with the default port number 3306.
			url = "jdbc:mysql://localhost:3306/JansUserDatabase";
			
			// Get a connection to the database for a
			// user named root with a blank password.
			// This user is the default administrator
			// having full privileges to do anything.
			con = DriverManager.getConnection(url, admin, adminPassword);
			
			// Get a Statement object
			stmt = con.createStatement();
			
			stmt.executeUpdate("CREATE TABLE users(username VARCHAR(512), password VARCHAR(256), averageScore DOUBLE, joinDate DATE, UNIQUE (username))");
			
			stmt.executeUpdate("CREATE TABLE scores(username VARCHAR(512), score DOUBLE)");
			
			con.close();
		} catch (Exception e) {
			if (Lib.test(dbgDatabase))
				e.printStackTrace();
		}
	}
	
	@Override
	public void deleteUserDatabase() {
		try {
			Statement stmt;

			// Register the JDBC driver for MySQL.
			Class.forName("com.mysql.jdbc.Driver");

			// Define URL of database server for
			// database named mysql on the localhost
			// with the default port number 3306.
			String url = "jdbc:mysql://localhost:3306/mysql";

			// Get a connection to the database for a
			// user named root with a blank password.
			// This user is the default administrator
			// having full privileges to do anything.
			Connection con = DriverManager.getConnection(url, "root", "");

			// Get a Statement object
			stmt = con.createStatement();

			// Remove the user named auser
			stmt.executeUpdate("REVOKE ALL PRIVILEGES ON *.* "
					+ "FROM '" + admin + "'@'localhost'");
			stmt.executeUpdate("REVOKE GRANT OPTION ON *.* "
					+ "FROM '" + admin + "'@'localhost'");
			stmt.executeUpdate("DELETE FROM mysql.user WHERE "
					+ "User='" + admin + "' and Host='localhost'");
			stmt.executeUpdate("FLUSH PRIVILEGES");

			// Delete the database
			stmt.executeUpdate("DROP DATABASE JansUserDatabase");

			con.close();
		} catch (Exception e) {
			if (Lib.test(dbgDatabase))
				e.printStackTrace();
		}
	}

	@Override
	public boolean removeUser(String username) {
		try {
			statement.executeUpdate("DELETE FROM users WHERE username = '" + username + "'");
			statement.executeUpdate("DELETE FROM scores WHERE username = '" + username + "'");
			return true;
		} catch (SQLException e) {
			if (Lib.test(dbgDatabase))
				e.printStackTrace();
			return false;
		}
	}

	@Override
	public void disconnect() {
		try {
			connection.close();
		} catch (SQLException e) {
			if (Lib.test(dbgDatabase))
				e.printStackTrace();
		}
	}

	@Override
	public User getUser(String username) {
		try {
			ResultSet userResult = statement.executeQuery("SELECT * FROM users WHERE username = '" + username + "'");
			if (!userResult.next())
				return null;
			String password = userResult.getString("password");
			Date date = userResult.getDate("joinDate");
			ResultSet userScores = statement.executeQuery("SELECT score FROM scores WHERE username = '" + username + "'");
			Vector<Double> scores = new Vector<Double>();
			while (userScores.next()) {
				scores.add(userScores.getDouble("score"));
			}
			return new User(username, password, scores, date);
		} catch (SQLException e) {
			if (Lib.test(dbgDatabase))
				e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean updateUser(User user) {
		try {
			ResultSet userResult = statement.executeQuery("SELECT * FROM users WHERE username = '" + user.getUsername() + "'");
			if (!userResult.next())
				return false;
			statement.executeUpdate("UPDATE users SET password = '" +
				user.getPassword() + "', averageScore = " +
				user.getAverageScore() + ", joinDate = '" +
				user.getJoinDate() + "' WHERE username = '" + user.getUsername() + "'");
			statement.executeUpdate("DELETE FROM scores WHERE username = '" + user.getUsername() + "'");
			Vector<Double> scores = user.getScores();
			for (Double score : scores)
				statement.executeUpdate("INSERT INTO scores VALUES ('" + user.getUsername() + "', " + score + ")");
			return true;
		} catch (SQLException e) {
			if (Lib.test(dbgDatabase))
				e.printStackTrace();
			return false;
		}
	}
}
