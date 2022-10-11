package com.ikhokha.techcheck;
//import java.util.HashMap;
//import java.util.Hash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class CommentAnalyze implements Callable<Map> {
//	Variables
	private File file;
	Map<String, Integer> resultsMap = new HashMap<>();
//	Class Constructor
	public CommentAnalyze(File file) {
		this.file = file;
	};
	
//	Method
	public String convertToLower(String str) {
		str=str.toLowerCase();
		return str;
	}
	
	@Override
	public Map<String, Integer> call() throws Exception {
		// TODO Auto-generated method stub
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				line=convertToLower(line);
				CheckMetric checkMetric=new CheckMetric(resultsMap,line);
				checkMetric.shorterThan15();
				checkMetric.moverComments();
				checkMetric.shakerComments();
				checkMetric.questionComments();
				checkMetric.spamComments();
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + file.getAbsolutePath());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Error processing file: " + file.getAbsolutePath());
			e.printStackTrace();
		}
		
		return resultsMap;
	}

}
