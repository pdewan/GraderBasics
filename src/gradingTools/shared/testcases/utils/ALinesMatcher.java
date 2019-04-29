package gradingTools.shared.testcases.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import util.trace.Tracer;

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
    protected void resetLineStatus() {
    	for (int i = 0; i < linesUsageStatus.length; i++) {
    		linesUsageStatus[i] = false;
    	}
    }
    @Override
	public boolean match(String[] aRegexes, LinesMatchKind aMatchKind, int aFlags ) {
//    	Pattern[] aPatterns = new Pattern[aRegexes.length];
    	resetLineStatus();
    	int aLineNumber = startLineNumber;
    	Set<Integer> aMatchedLines = new HashSet<>();
//    	init(lines);
    	for (int aRegexIndex = 0; aRegexIndex < aRegexes.length; aRegexIndex++) {
    		Pattern aPattern = Pattern.compile(aRegexes[aRegexIndex], Pattern.DOTALL);  
    		Tracer.info(this, "Matching pattern starting at line:" + startLineNumber);
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
    		System.out.println("Matched Line " + lines[aMatchedLine] + " for pattern " + Arrays.toString(aRegexes));
//    		linesUsageStatus[aLineNumber] = true;
    		linesUsageStatus[aMatchedLine] = true; 

    	}
    	return true;
    	
    }
    @Override
   	public boolean match(Pattern[] aPatterns, LinesMatchKind aMatchKind, int aFlags ) {
//       	Pattern[] aPatterns = new Pattern[aRegexes.length];
    	resetLineStatus();
       	int aLineNumber = startLineNumber;
       	Set<Integer> aMatchedLines = new HashSet<>();
       	for (int aRegexIndex = 0; aRegexIndex < aPatterns.length; aRegexIndex++) {
       		Pattern aPattern = aPatterns[aRegexIndex]; 
           	Tracer.info(this, "Matching pattern:" + aPattern + "starting at:" + aLineNumber);

       		boolean aMatch = false;
//       		int aMatchedLineNumber = 0;
       		for (; aLineNumber < lines.length; aLineNumber++) {
       			if (linesUsageStatus[aLineNumber]) {
       				Tracer.info(this, "Skipping line:" + aLineNumber);
       				continue; 
       			}
       			String aLine =  lines[aLineNumber];
       			Tracer.info(this, "Checking: " + aLine);
       			aMatch = aPattern.matcher(aLine).matches();
       			if (aMatch) {
       				if (aMatchKind == LinesMatchKind.ONE_TIME_LINE) {
//       					if (aLineNumber == 35) {
//       						String aTrueLine = aLine;
//       					}
       	    			aMatchedLines.add(aLineNumber);
       	    		}
       				aLineNumber++;
       				break;
       			}
       		};
       		if (!aMatch) {
       			lastUnmatchedRegex = aPatterns[aRegexIndex].toString();
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
       		System.out.println("Matched Line " + lines[aMatchedLine] + " for pattern " + Arrays.toString(aPatterns));
//       		linesUsageStatus[aLineNumber] = true;
       		linesUsageStatus[aMatchedLine] = true; 

       	}
       	return true;
       	
       }
    @Override
	public String getLastUnmatchedRegex() {
		return lastUnmatchedRegex;
	}
}
