package consommation;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import templatePC.*;

public class Transformer implements Runnable {

	Date ActualTime;
	BlockingQueue<String> PostsQueue = null;
	BlockingQueue<String> CommentsQueue = null;
	BlockingQueue<Object> TotalQueue = null;

	Map<Long, ArrayList<Comment>> IDPost2Com = new HashMap<Long, ArrayList<Comment>>(1000000);
	Map<Long, Post> ID2Post = new HashMap<Long, Post>(1000000);
	Map<Long, Long> IDCom2IDPost = new HashMap<Long, Long>(1000000);
	List<Post> result = null;

	public Transformer(BlockingQueue<String> postsQueue, BlockingQueue<String> commentsQueue,
			BlockingQueue<Object> totalQueue, Map<Long, ArrayList<Comment>> IDPost2Com, Map<Long, Post> ID2Post,
			Map<Long, Long> IDCom2IDPost, List<Post> result, Date ActualTime) {
		super();

		this.PostsQueue = postsQueue;
		this.CommentsQueue = commentsQueue;
		this.TotalQueue = totalQueue;
		this.IDPost2Com = IDPost2Com;
		this.ID2Post = ID2Post;
		this.IDCom2IDPost = IDCom2IDPost;
		this.result = result;
		this.ActualTime = ActualTime;

	}

	public void run() {

		try {
			Thread.sleep(90);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}

		Post post = null;
		Comment comment = null;
		while ((PostsQueue.peek() != "*" || CommentsQueue.peek() != "*")) {
			if (PostsQueue.peek() == null) {
				continue;
			}

			if (CommentsQueue.peek() == null) {
				continue;
			}

			if (PostsQueue.peek() == "*") {
				try {
					comment = new Comment(CommentsQueue.take(), this.ActualTime);
					TotalQueue.put(comment);
					this.ActualTime = comment.getTemps();

					if (comment.getPostCommented() > 0) {
						IDCom2IDPost.put(comment.getCommentID(), comment.getPostCommented());
						if (IDPost2Com.get(comment.getPostCommented()) == null) {
							IDPost2Com.replace(comment.getPostCommented(), new ArrayList<Comment>());
						}
						// Pour savoir si le com n'as pas encore disparu
						if (ID2Post.get(comment.getPostCommented()) != null) {
							IDPost2Com.get(comment.getPostCommented()).add(comment);
							ID2Post.get(comment.getPostCommented()).setLastCom(comment);
						} else {
							comment = null;
						}

						// Si c'est une réponse a un com
					} else {
						IDCom2IDPost.put(comment.getCommentID(), IDCom2IDPost.get(comment.getCommentReplied()));

						if (ID2Post.get(IDCom2IDPost.get(comment.getCommentReplied())) != null) {
							IDPost2Com.get(IDCom2IDPost.get(comment.getCommentReplied())).add(comment);
							ID2Post.get(IDCom2IDPost.get(comment.getCommentReplied())).setLastCom(comment);
						} else {
							comment = null;
						}

					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (result.isEmpty()) {
					continue;
				} else {
					this.calculScore();
				}
				continue;
			}
			if (CommentsQueue.peek() == "*")

			{
				try {
					post = new Post(PostsQueue.take(), this.ActualTime, this.IDPost2Com);
					TotalQueue.put(post);
					IDPost2Com.put(post.getPostID(), null);
					ID2Post.put(post.getPostID(), post);
					result.add(post);
					this.ActualTime = post.getTemps();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (result.isEmpty()) {
					continue;
				} else {
					this.calculScore();
				}
				continue;
			}

			try {
				post = new Post(PostsQueue.peek(), this.ActualTime, this.IDPost2Com);
				comment = new Comment(CommentsQueue.peek(), this.ActualTime);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			if ((post.getTemps().compareTo(comment.getTemps())) <= 0) {
				try {

					PostsQueue.take();
					TotalQueue.put(post);
					IDPost2Com.put(post.getPostID(), null);
					ID2Post.put(post.getPostID(), post);
					result.add(post);
					this.ActualTime = post.getTemps();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				try {
					CommentsQueue.take();
					TotalQueue.put(comment);
					this.ActualTime = comment.getTemps();

					if (comment.getPostCommented() > 0) {
						IDCom2IDPost.put(comment.getCommentID(), comment.getPostCommented());
						if (IDPost2Com.get(comment.getPostCommented()) == null) {
							IDPost2Com.replace(comment.getPostCommented(), new ArrayList<Comment>());
						}
						// Pour savoir si le post n'as pas encore était annulé
						if (ID2Post.get(comment.getPostCommented()) != null) {
							IDPost2Com.get(comment.getPostCommented()).add(comment);
							ID2Post.get(comment.getPostCommented()).setLastCom(comment);
						} else {
							comment = null;
						}

						// Si c'est une réponse a un com
					} else {
						IDCom2IDPost.put(comment.getCommentID(), IDCom2IDPost.get(comment.getCommentReplied()));

						if (ID2Post.get(IDCom2IDPost.get(comment.getCommentReplied())) != null) {
							IDPost2Com.get(IDCom2IDPost.get(comment.getCommentReplied())).add(comment);
							ID2Post.get(IDCom2IDPost.get(comment.getCommentReplied())).setLastCom(comment);
						} else {
							comment = null;
						}

					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (result.isEmpty()) {
				continue;
			} else {
				this.calculScore();

			}
		}

		try

		{
			if (!result.isEmpty()) {
				this.calculScore();
			}
			TotalQueue.put("*");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void calculScore() {
		for (int i = 0; i < result.size(); i++) { // Pour chaque post
			int score = 0;
			int patchScore = 10;
			long tpsEcoule = ActualTime.getTime() - result.get(i).getTemps().getTime();
			long diff = tpsEcoule / 1000;

			while (diff >= 86400 && patchScore > 0) { // On recalcul el score total
				diff -= 86400;
				patchScore -= 1;

			}

			Long IDPost = result.get(i).getPostID();
			if (IDPost2Com.get(IDPost) != null) {
				for (int j = 0; j < IDPost2Com.get(IDPost).size(); j++) { // Pour chaque comment
					int patchScoreCom = 10;
					tpsEcoule = ActualTime.getTime() - IDPost2Com.get(IDPost).get(j).getTemps().getTime();
					diff = tpsEcoule / 1000;
					while (diff >= 86400 && patchScoreCom > 0) {
						diff -= 86400;
						patchScoreCom -= 1;

					}
					score += patchScoreCom;
				}
			}
			if (score + patchScore <= 0) {
				result.remove(i);
			} else {
				result.get(i).setTotalScore(score + patchScore);
			}
		}

	}

	@Override
	public String toString() {
		return "Transformer [PostsQueue=" + PostsQueue + ", CommentsQueue=" + CommentsQueue + ", TotalQueue="
				+ TotalQueue + "]";
	}

}
