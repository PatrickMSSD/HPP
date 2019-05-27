package templatePC;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class Post {
	private Date ActualTime;
	private Date temps;
	private Long PostID;
	private Long UserID;
	private String contenu;
	private String user;
	private Integer score = 10;
	private Integer totalScore = 0;
	private Comment LastCom = null;

	private Map<Long, ArrayList<Comment>> IDPost2Com = new HashMap<Long, ArrayList<Comment>>(1000000);

	public Post(String post, Date ActualTime, Map<Long, ArrayList<Comment>> IDPost2Com) throws ParseException {
		super();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

		String[] SplitPost = post.split("[|]");
		this.ActualTime = ActualTime;
		this.temps = sdf.parse(SplitPost[0]);
		this.PostID = Long.parseLong(SplitPost[1]);
		this.UserID = Long.parseLong(SplitPost[2]);
		this.contenu = SplitPost[3];
		this.user = SplitPost[4];
		this.IDPost2Com = IDPost2Com;
	}

	public Date getTemps() {
		return temps;
	}

	public void setTemps(Date temps) {
		this.temps = temps;
	}

	public Long getPostID() {
		return PostID;
	}

	public void setPostID(Long postID) {
		PostID = postID;
	}

	public Long getUserID() {
		return UserID;
	}

	public void setUserID(Long userID) {
		UserID = userID;
	}

	public String getContenu() {
		return contenu;
	}

	public void setContenu(String contenu) {
		this.contenu = contenu;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	public Comment getLastCom() {
		return LastCom;
	}

	public void setLastCom(Comment lastCom) {
		LastCom = lastCom;
	}

	public void changeScore() {
		if (this.score - 1 < 0) {
			this.score = 0;
		} else {
			this.score--;
		}
	}

	public void changeTotalScore() {

	}

	@Override
	public String toString() {
		return "Post [temps=" + temps + ", PostID=" + PostID + ", UserID=" + UserID + ", contenu=" + contenu + ", user="
				+ user + ", score=" + score + ", totalScore=" + totalScore + "]";
	}

}
