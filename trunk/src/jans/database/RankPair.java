package jans.database;

/**
 * This class is used to represent user ranking ordered by averageScore
 * @author lqhl
 *
 */
public class RankPair implements Comparable<RankPair> {
	String username;
	Double averageScore;
	RankPair(String username, Double averageScore) {
		this.username = username;
		this.averageScore = averageScore;
	}
	@Override
	public int compareTo(RankPair arg0) {
		return (int) Math.signum(averageScore - arg0.averageScore);
	}
}
