package gradingTools.shared.testcases.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class ALinesMatcher implements LinesMatcher {
	protected String[] lines;
	protected boolean[] linesUsageStatus;
	protected int startLineNumber;
	protected String lastUnmatchedRegex;
	
	public ALinesMatcher(String aStringWithLines) {
		lines = aStringWithLines.split("\n");
		init(lines);
		
	}
	public ALinesMatcher(String[] aLines) {
		lines =aLines;;
		init(lines);
		
	}
    @Override
	public void init(String[] aLines) {
    	linesUsageStatus = new boolean[aLines.length];
    	
    }
    @Override
	public boolean match(String[] aRegexes, LinesMatchKind aMatchKind, int aFlags ) {
//    	Pattern[] aPatterns = new Pattern[aRegexes.length];
    	int aLineNumber = startLineNumber;
    	Set<Integer> aMatchedLines = new HashSet<>();
    	for (int aRegexIndex = 0; aRegexIndex < aRegexes.length; aRegexIndex++) {
    		Pattern aPattern = Pattern.compile(aRegexes[aRegexIndex], Pattern.DOTALL);  
    		boolean aMatch = false;
//    		int aMatchedLineNumber = 0;
    		for (; aLineNumber < lines.length; aLineNumber++) {
    			if (linesUsageStatus[aLineNumber]) 
    				continue; 
    			String aLine =  lines[aLineNumber];
    			aMatch = aPattern.matcher(aLine).matches();
    			if (aMatch) {
    				if (aMatchKind == LinesMatchKind.ONE_TIME_LINE) {
//    					if (aLineNumber == 35) {
//    						String aTrueLine = aLine;
//    					}
    	    			aMatchedLines.add(aLineNumber);
    	    		}
    				break;
    			}
    		};
    		if (!aMatch) {
    			lastUnmatchedRegex = aRegexes[aRegexIndex];
    			return false;
    		}
    		
    		
		}
    	if (aMatchKind == LinesMatchKind.MULTIPLE) {
			return true;
		}
    	if (aMatchKind == LinesMatchKind.ONE_TIME_SUBSEQUENCE) {
    		startLineNumber = aLineNumber + 1;
    		return true;
    	}
    	for (Integer aMatchedLine:aMatchedLines) {
    		linesUsageStatus[aLineNumber] = true; 
    	}
    	return true;
    	
    }
    @Override
	public String getLastUnmatchedRegex() {
		return lastUnmatchedRegex;
	}
}