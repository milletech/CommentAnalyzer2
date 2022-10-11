package com.ikhokha.techcheck;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.text.html.HTMLDocument.Iterator;

public class Main {

	@SuppressWarnings({ "unchecked", "deprecation" })
	public static <T> void main(String[] args) throws Exception {
		
		Map<String, Integer> totalResults = new HashMap<>();
		
//		Get the files from docs folder
		File docPath = new File("docs");
		File[] commentFiles = docPath.listFiles((d, n) -> n.endsWith(".txt"));
		
//		Map<String, Integer> fileResults = null;
		
//		We need to create a new thread for each file
//		The number of threads 
		int nOfThreads=commentFiles.length<10 ? commentFiles.length :10;
		
		
		ThreadPoolExecutor executor=(ThreadPoolExecutor) Executors.newFixedThreadPool(nOfThreads);
		List<Callable<Map>> callables=new ArrayList<Callable<Map>>();
		
		for (File commentFile : commentFiles) {
			CommentAnalyze commentAnalyzer = new CommentAnalyze(commentFile);
			callables.add(commentAnalyzer); 
		};
		
		try {
			List<Future<Map>> futures=executor.invokeAll(callables);
			System.out.println(futures);
			System.out.println(Thread.activeCount()+" Threads running concurrently."+(Thread.activeCount()-1)+" Threads for the files and the main Thread.");
			
				for(Future<Map> result: futures) {
					//System.out.println(result.get());
					addReportResults((Map<String, Integer>) result.get(), totalResults);
				}
				
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		
//		Shut down thread when result have come back;
		executor.shutdown();
		
		
		System.out.println();
		System.out.println("RESULTS\n=======");
		totalResults.forEach((k,v) -> System.out.println(k + " : " + v));
		
	}
	
	/**
	 * This method adds the result counts from a source map to the target map 
	 * @param source the source map
	 * @param target the target map
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	private static void addReportResults(Map<String, Integer> fileResult, Map<String, Integer> totalResults){
			
		for (Map.Entry<String, Integer> entry : ((Map<String, Integer>) fileResult).entrySet()) {
			int newValue=entry.getValue();
			if(totalResults.get(entry.getKey()) != null) {
				newValue+=totalResults.get(entry.getKey());
			};
			totalResults.put(entry.getKey(), newValue);
		}
		
	};
	
}
