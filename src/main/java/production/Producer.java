package production;

import java.io.*;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable{
	
	BlockingQueue<String> PQueue = null;
	BlockingQueue<String> CQueue = null;
	
	
	public Producer(BlockingQueue<String> pQueue, BlockingQueue<String> cQueue) {
		super();
		this.PQueue = pQueue;
		this.CQueue = cQueue;
	}



	public void run() {
		File Pfile = new File("../fr.mp/src/main/resources/posts_short.txt");
		File Cfile = new File("../fr.mp/src/main/resources/comments_short.txt");
		
		String Post = null;
		String Comment = null;
		
		
		try {
			BufferedReader brPost = new BufferedReader(new FileReader(Pfile));
			BufferedReader brComment = new BufferedReader(new FileReader(Cfile));
			
			while ((((Post = brPost.readLine()) != null) || (Comment = brComment.readLine()) != null)) {
				
				if (Post != null) {
					PQueue.put(Post);
				}
				if (Comment != null) {
					CQueue.put(Comment);
				}
				
			}
			
			brPost.close();
			brComment.close();
			PQueue.put("*");
			CQueue.put("*");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
