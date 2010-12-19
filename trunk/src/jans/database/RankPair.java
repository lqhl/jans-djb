package jans.database;

/**
 * This class is used to represent user ranking ordered by averageScore
 * @author lqhl
 *
 */
public class RankPair {
	String username;
	Double averageScore;
	RankPair(String username, Double averageScore) {
		this.username = username;
		this.averageScore = averageScore;
	}
}
