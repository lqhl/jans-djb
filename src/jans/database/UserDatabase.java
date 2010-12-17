package jans.database;

import jans.common.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class UserDatabase implements UserDAO {
	private static String defaultAdmin = "DJB";
	private static String defaultAdminPassword = "JBD";
	
	private String admin;
	private String adminPassword;
	
	private Connection connection;
	private Statement statement;
	
	public UserDatabase() {
		admin = Config.getString("adminOfUserDatabase", defaultAdmin);
		adminPassword = Config.getString("passwordOfUserDatabase", defaultAdminPassword);
	}
	
	@Override
	public boolean add(User user) {
		try {
			statement.executeUpdate("INSERT INTO users VALUES ('" +
				user.getUsername() + "', '" +
				user.getPassword() + "', " +
				user.getAverageScore() + ", '" +
				user.getJoinDate() + "')");
			statement.executeUpdate("CREATE TABLE " + user.getUsername() + "Scores(score double)");
			Vector<Double> scores = user.getScores();
			for (Double score : scores)
				statement.executeUpdate("INSERT INTO " + user.getUsername() + "Scores VALUES (" + score + ")");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Vector<Double> averageScoreList() {
		try {
			ResultSet rs = statement.executeQuery("SELECT averageScore FROM users ORDERED by averageScore");
			Vector<Double> result = new Vector<Double>();
			while (rs.next())
				result.add(rs.getDouble("averageScore"));
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean connect() {
		try {
			String url = "jdbc:mysql://localhost:3306/JansUserDatabase";
			connection = DriverManager.getConnection(url, admin, adminPassword);
			statement = connection.createStatement();
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean createUserDatabase() {
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
			Connection con = DriverManager.getConnection(url, "root", "");

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
			
			// Get a connection to the database for a
			// user named DJB with the password
			// JBD.
			con = DriverManager.getConnection(url, "DJB", "JBD");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean delete(String username) {
		try {
			statement.executeUpdate("DELETE FROM users WHERE username = " + username);
			statement.executeUpdate("DROP TABLE " + username + "Scores");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean disconnect() {
		try {
			connection.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public User getUser(String username) {
		try {
			ResultSet userResult = statement.executeQuery("SELECT * FROM users WHERE username='" + username + "'");
			ResultSet userScores = statement.executeQuery("SELECT score FROM " + username + "Scores");
			Vector<Double> scores = new Vector<Double>();
			while (userScores.next()) {
				scores.add(userScores.getDouble("score"));
			}
			if (!userResult.next())
				return null;
			return new User(username, userResult.getString("password"), scores, userResult.getDate("joinDate"));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean updateUser(User user) {
		try {
			ResultSet userResult = statement.executeQuery("SELECT * FROM users WHERE username = " + user.getUsername());
			if (!userResult.next())
				return false;
			statement.executeUpdate("UPDATE users SET password = '" +
				user.getPassword() + "', averageScore = " +
				user.getAverageScore() + ", joinDate = '" +
				user.getJoinDate() + "' WHERE username = '" + user.getUsername() + "'");
			statement.executeUpdate("DELETE * FROM " + user.getUsername() + "Scores");
			Vector<Double> scores = user.getScores();
			for (Double score : scores)
				statement.executeUpdate("INSERT INTO " + user.getUsername() + "Scores VALUES (" + score + ")");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
