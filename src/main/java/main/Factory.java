package main;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import consommation.*;
import production.Producer;
import templatePC.*;

public class Factory {

	public static void main(String[] args) throws InterruptedException {
		
		Date TotalTime = null;

		BlockingQueue<String> PostsQueue = new ArrayBlockingQueue<String>(50);
		BlockingQueue<String> CommentsQueue = new ArrayBlockingQueue<String>(50);
		BlockingQueue<Object> TotalQueue = new ArrayBlockingQueue<Object>(50);

		Map<Long, ArrayList<Comment>> IDPost2Com = new HashMap<Long, ArrayList<Comment>>();
		Map<Long, Post> ID2Post = new HashMap<Long, Post>();
		Map<Long, Long> IDCom2IDPost = new HashMap<Long, Long>();

		List<Post> result = new ArrayList<Post>(100);

		Producer Lecteur = new Producer(PostsQueue, CommentsQueue);
		Transformer Liaison = new Transformer(PostsQueue, CommentsQueue, TotalQueue,IDPost2Com,ID2Post, IDCom2IDPost, result,TotalTime);
		Sorter Trieur = new Sorter(IDPost2Com, ID2Post, IDCom2IDPost, TotalQueue, TotalTime, result);
		
		
		Thread LecteurThread = new Thread(Lecteur);
		Thread TransformeurThread = new Thread(Liaison);
		Thread TrieurThread = new Thread(Trieur);

		LecteurThread.start();
		TransformeurThread.start();
		TrieurThread.start();
		
		LecteurThread.join();
		TransformeurThread.join();
		TrieurThread.join();

		System.out.println(PostsQueue.toString());
		System.out.println(PostsQueue.size());
		System.out.println(CommentsQueue.toString());
		System.out.println(CommentsQueue.size());
		System.out.println(TotalQueue.toString());
		System.out.println(TotalQueue.size());
		System.out.println(result.toString());
		System.out.println(result.size());


		
	}

}
