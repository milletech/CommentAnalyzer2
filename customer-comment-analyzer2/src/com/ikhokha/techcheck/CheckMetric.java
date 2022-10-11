package com.ikhokha.techcheck;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckMetric {
	
//	Variables
	String line;
	Map<String, Integer> resultsMap;
	
	
//	Class Constructor
	CheckMetric(Map<String, Integer> resultsMap,String line){
		this.resultsMap=(Map<String, Integer>) resultsMap;
		this.line=line;
	}
	
//	Methods
	public boolean checkIfURL(String str) {
		
		String target=str;
		String http = "((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)";
		Pattern pattern=Pattern.compile(http);
		Matcher matcher=pattern.matcher(target);
		
		if(matcher.find()) {
			return true;
		}else {
			return false;
		}
	}
	
	void shorterThan15() {
		if (line.length() < 15) {
			incOccurrence(resultsMap, "SHORTER_THAN_15");
		};
	}
	
	void moverComments() {
		if (line.contains("mover")) {
			incOccurrence(resultsMap, "MOVER_MENTIONS");
		};
	}
	
	void shakerComments() {
		if (line.contains("shaker")) {
			incOccurrence(resultsMap, "SHAKER_MENTIONS");
		};
	}
	
	void questionComments() {
		if (line.contains("?")) {
			incOccurrence(resultsMap, "QUESTION_MENTIONS");
		};
	}
	
	void spamComments() {
		if(checkIfURL(line)) {
			incOccurrence(resultsMap, "SPAM");
		}
	}
	
	/**
	 * This method increments a counter by 1 for a match type on the countMap. Uninitialized keys will be set to 1
	 * @param countMap the map that keeps track of counts
	 * @param key the key for the value to increment
	 */
	private void incOccurrence(Map<String, Integer> countMap, String key) {
		countMap.putIfAbsent(key, 0);
		countMap.put(key, countMap.get(key) + 1);
	}
	
}
