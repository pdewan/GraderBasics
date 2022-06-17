package gradingTools.shared.testcases.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.collections.map.HashedMap;

import util.trace.Tracer;

public class ALinesMatcher implements LinesMatcher {
	protected String[] lines;
	protected boolean[] linesUsageStatus;
	protected int startLineNumber;
	protected String lastUnmatchedRegex;
	protected Map<Object, Integer> patternToLineNumber = new HashedMap();
	protected int maxMatchedLineNumber = -1;

	public ALinesMatcher(String aStringWithLines) {
		lines = aStringWithLines.split("\n");
		
		init(lines);

	}
	
	protected void toLowerCase(String[] aLines) {
		for (int index = 0; index < lines.length; index++) {
			lines[index] = lines[index].toLowerCase().trim();
		}
	}

	public ALinesMatcher(String[] aLines) {
		lines = aLines;
		init(lines);

	}

	@Override
	public void init(String[] aLines) {
		linesUsageStatus = new boolean[aLines.length];
		toLowerCase(aLines);

	}

	protected void resetLineStatus() {
		for (int i = 0; i < linesUsageStatus.length; i++) {
			linesUsageStatus[i] = false;
		}
		patternToLineNumber.clear();
		maxMatchedLineNumber = 0;
	}

	@Override
	public boolean match(String[] aRegexes, LinesMatchKind aMatchKind, int aFlags) {
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
				String aLine = lines[aLineNumber];
				aMatch = aPattern.matcher(aLine).matches();
				if (aMatch) {
//    				if (aMatchKind == LinesMatchKind.ONE_TIME_LINE) {
					if (aMatchKind == LinesMatchKind.ONE_TIME_LINE || aMatchKind == LinesMatchKind.ONE_TIME_UNORDERED) {

//    					if (aLineNumber == 35) {
//    						String aTrueLine = aLine;
//    					}
						aMatchedLines.add(aLineNumber);
					}
					if (aMatchKind == LinesMatchKind.ONE_TIME_UNORDERED) { // important for fork-join
						linesUsageStatus[aLineNumber] = true;
					}
//    	    		System.out.println(aLineNumber+ ":Matched  pattern " + Arrays.toString(aRegexes));
					Tracer.info(this, aLineNumber + ":Matched  pattern " + Arrays.toString(aRegexes));

					break;
				}
			}
			;
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
		for (Integer aMatchedLine : aMatchedLines) {
//    		System.out.println("Matched Line " + lines[aMatchedLine] + " for pattern " + Arrays.toString(aRegexes));
//    		linesUsageStatus[aLineNumber] = true;
			linesUsageStatus[aMatchedLine] = true;

		}
		return true;

	}

	@Override
	public boolean match(Pattern[] aPatterns, LinesMatchKind aMatchKind, int aFlags) {
//       	Pattern[] aPatterns = new Pattern[aRegexes.length];
		resetLineStatus();
		int aLineNumber = startLineNumber;
		int aNumMatchableLines = lines.length - startLineNumber;
		if ((aNumMatchableLines < aPatterns.length) 
				&& (aMatchKind == LinesMatchKind.ONE_TIME_UNORDERED||
				aMatchKind == LinesMatchKind.ONE_TIME_UNORDERED
				)) {
			Tracer.info(this, "Number of matchable lines " + aNumMatchableLines +
					" <  number of patterns " + aPatterns.length);
			return false;
		}
		Set<Integer> aMatchedLines = new HashSet<>();
		for (int aRegexIndex = 0; aRegexIndex < aPatterns.length; aRegexIndex++) {
			Pattern aPattern = aPatterns[aRegexIndex];
			if (aMatchKind == LinesMatchKind.ONE_TIME_UNORDERED) {
				aLineNumber = 0;
			}
			Tracer.info(this, "Matching pattern " + aRegexIndex + " " + aPattern + "starting at:" + aLineNumber);

			boolean aMatch = false;
//       		int aMatchedLineNumber = 0;
			
			for (; aLineNumber < lines.length; aLineNumber++) {
				if (linesUsageStatus[aLineNumber]) {
//					Tracer.info(this, "Skipping matched line:" + aLineNumber);
					continue;
				}
				String aLine = lines[aLineNumber];
				Tracer.info(this, aLineNumber + ": Checking: " + aLine);
				aMatch = aPattern.matcher(aLine).matches();
				if (aMatch) {
					if (aMatchKind == LinesMatchKind.ONE_TIME_LINE || aMatchKind == LinesMatchKind.ONE_TIME_UNORDERED) {
//       					if (aLineNumber == 35) {
//       						String aTrueLine = aLine;
//       					}
						aMatchedLines.add(aLineNumber);
					}
					if (aMatchKind == LinesMatchKind.ONE_TIME_UNORDERED) { // important for fork-join
						linesUsageStatus[aLineNumber] = true;
					}
//    	    		System.out.println(aLineNumber+ ": Matched  pattern " + aPattern);
					Tracer.info(this, aLineNumber + ": Matched  pattern " + aRegexIndex + ":" + aPattern);
					maxMatchedLineNumber = Math.max(maxMatchedLineNumber, aLineNumber);
					patternToLineNumber.put(aPattern, aLineNumber);
					aLineNumber++;
					break;
				}
			}
			;
			if (!aMatch) {
				lastUnmatchedRegex = aPatterns[aRegexIndex].toString();
				Tracer.info(this, aLineNumber + ": Did not match  pattern " + aRegexIndex + ":" + aPattern);

				return false;
			}

		}
		if (aMatchKind == LinesMatchKind.MULTIPLE) {
			return true;
		}
		if (aMatchKind == LinesMatchKind.ONE_TIME_SUBSEQUENCE) {
			startLineNumber = aLineNumber + 1;// this is for the next match?
			return true;
		}
		for (Integer aMatchedLine : aMatchedLines) {
//       		System.out.println("Matched Line " + lines[aMatchedLine] + " for pattern " + Arrays.toString(aPatterns));
//			Tracer.info(this, "Matched Line " + lines[aMatchedLine] + " for pattern " + Arrays.toString(aPatterns));

			// linesUsageStatus[aLineNumber] = true;
			linesUsageStatus[aMatchedLine] = true; // so this is for the next match?

		}
		return true;

	}

	@Override
	public String getLastUnmatchedRegex() {
		return lastUnmatchedRegex;
	}

	public int getStartLineNumber() {
		return startLineNumber;
	}

	public void setStartLineNumber(int startLineNumber) {
		this.startLineNumber = startLineNumber;
	}

	public String[] getLines() {
		return lines;
	}

	public boolean[] getLinesUsageStatus() {
		return linesUsageStatus;
	}

	public int getMaxMatchedLineNumber() {
		return maxMatchedLineNumber;
	}
}
