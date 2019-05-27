package consommation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import templatePC.*;

public class Sorter implements Runnable {

	Date ActualTime;
	Map<Long, ArrayList<Comment>> IDPost2Com = new HashMap<Long, ArrayList<Comment>>(1000000);
	Map<Long, Post> ID2Post = new HashMap<Long, Post>(1000000);
	Map<Long, Long> IDCom2IDPost = new HashMap<Long, Long>(1000000);
	BlockingQueue<Object> TotalQueue = new ArrayBlockingQueue<Object>(1000000);
	List<Post> result;

	public Sorter(Map<Long, ArrayList<Comment>> iDPost2Com, Map<Long, Post> iD2Post, Map<Long, Long> iDCom2IDPost,
			BlockingQueue<Object> totalQueue, Date ActualTime, List<Post> result) {
		super();
		this.IDPost2Com = iDPost2Com;
		this.ID2Post = iD2Post;
		this.IDCom2IDPost = iDCom2IDPost;
		this.TotalQueue = totalQueue;
		this.ActualTime = ActualTime;
		this.result = result;
	}

	public void run() {
		try {
			Thread.sleep(180);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		while (TotalQueue.peek() != "*") {
			if (TotalQueue.isEmpty()) {
				continue;
			}

			if (TotalQueue.peek().getClass().equals(Post.class)) {
				try {
					Post patchPost = (Post) TotalQueue.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// Si c'est un com
			} else {

					try {
						Comment patchComment = (Comment) TotalQueue.take();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				
			}
			if(!result.isEmpty()) {this.sort();}
			
		}
	}

	private void sort() {
		if (result.size() > 1) {
			
			Collections.sort(result, new Comparator<Post>() {

				public int compare(Post o1, Post o2) {
					if (o1.getTotalScore() != o2.getTotalScore()) {
						return o2.getTotalScore().compareTo(o1.getTotalScore());
					}
					if (o1.getTemps() != o2.getTemps()) {
						return o2.getTemps().compareTo(o1.getTemps());
					}
					return o2.getLastCom().getTemps().compareTo(o1.getLastCom().getTemps());
				}
			});
		}

	}

	public Map<Long, ArrayList<Comment>> getIDPost2Com() {
		return IDPost2Com;
	}

	public void setIDPost2Com(Map<Long, ArrayList<Comment>> iDPost2Com) {
		IDPost2Com = iDPost2Com;
	}

	public Map<Long, Post> getID2Post() {
		return ID2Post;
	}

	public void setID2Post(Map<Long, Post> iD2Post) {
		ID2Post = iD2Post;
	}

	public Map<Long, Long> getIDCom2IDPost() {
		return IDCom2IDPost;
	}

	public void setIDCom2IDPost(Map<Long, Long> iDCom2IDPost) {
		IDCom2IDPost = iDCom2IDPost;
	}

	public BlockingQueue<Object> getTotalQueue() {
		return TotalQueue;
	}

	public void setTotalQueue(BlockingQueue<Object> totalQueue) {
		TotalQueue = totalQueue;
	}

	public List<Post> getResult() {
		return result;
	}

	public void setResult(List<Post> result) {
		this.result = result;
	}

}
