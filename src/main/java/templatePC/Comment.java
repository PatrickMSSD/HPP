package templatePC;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Comment {
	private Date ActualTime;
	private Date temps;
	private Long CommentID;
	private Long UserID;
	private String contenu;
	private String user;
	private Long CommentReplied;
	private Long PostCommented;
	private Integer score = 10;

	public Comment(String Comment, Date ActualTime) throws ParseException {
		super();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

		String[] SplitComment = Comment.split("[|]");
		this.ActualTime = ActualTime;
		this.temps = sdf.parse(SplitComment[0]);
		this.CommentID = Long.parseLong(SplitComment[1]);
		this.UserID = Long.parseLong(SplitComment[2]);
		this.contenu = SplitComment[3];
		this.user = SplitComment[4];
		this.CommentReplied = (SplitComment[5] == "") ? Long.parseLong(SplitComment[5]) : 0;
		this.PostCommented = (SplitComment.length == 7) ? Long.parseLong(SplitComment[6]) : 0;
	}

	public Date getTemps() {
		return temps;
	}

	public void setTemps(Date temps) {
		this.temps = temps;
	}

	public Long getCommentID() {
		return CommentID;
	}

	public void setCommentID(Long commentID) {
		CommentID = commentID;
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

	public Long getCommentReplied() {
		return CommentReplied;
	}

	public void setCommentReplied(Long commentReplied) {
		CommentReplied = commentReplied;
	}

	public Long getPostCommented() {
		return PostCommented;
	}

	public void setPostCommented(Long postCommented) {
		PostCommented = postCommented;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public void changeScore() {

	}

	@Override
	public String toString() {
		return "Comment [temps=" + temps + ", CommentID=" + CommentID + ", UserID=" + UserID + ", contenu=" + contenu
				+ ", user=" + user + ", CommentReplied=" + CommentReplied + ", PostCommented=" + PostCommented
				+ ", score=" + score + "]";
	}

}
