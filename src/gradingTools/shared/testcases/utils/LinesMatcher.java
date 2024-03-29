package gradingTools.shared.testcases.utils;

import java.util.regex.Pattern;

public interface LinesMatcher {

	public abstract void init(String[] aLines);

	public abstract boolean match(String[] aRegexes,
			LinesMatchKind aMatchKind, int aFlags);

	String getLastUnmatchedRegex();

	boolean match(Pattern[] aPatterns, LinesMatchKind aMatchKind, int aFlags);
	
	public int getStartLineNumber() ;
	public void setStartLineNumber(int startLineNumber) ;
	public String[] getLines() ;
	public boolean[] getLinesUsageStatus() ;
	public int getMaxMatchedLineNumber() ;
}