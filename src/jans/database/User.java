package jans.database;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class User {
	private String username;
	private String password;
	private Date joinDate;
	private Vector<Double> scores;
	private double averageScore;
	
	public User(String username, String password, Vector<Double> scores, Date joinDate) {
		this.username = username;
		this.password = password;
		this.scores = new Vector<Double>(scores);
		calcAverageScore();
		this.joinDate = joinDate;
	}
	
	public User(String username, String password, Vector<Double> scores) {
		this.username = username;
		this.password = password;
		this.scores = new Vector<Double>(scores);
		calcAverageScore();
		java.util.Date currentDate = new java.util.Date();
		java.text.SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = dateFormat.format(currentDate);
		this.joinDate = Date.valueOf(dateStr);
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setScores(Vector<Double> scores) {
		this.scores = new Vector<Double>(scores);
		calcAverageScore();
	}
	
	public Vector<Double> getScores() {
		return new Vector<Double>(scores);
	}
	
	public double getAverageScore() {
		return averageScore;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public Date getJoinDate() {
		return joinDate;
	}
	
	private void calcAverageScore() {
		if (scores.size() > 0) {
			double sum = 0;
			for (Double score : scores)
				sum += score;
			averageScore = sum / scores.size();
		}
		else
			averageScore = 0;
	}
}
