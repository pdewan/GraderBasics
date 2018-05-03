package gradingTools.shared.testcases.utils;

public interface LinesMatcher {

	public abstract void init(String[] aLines);

	public abstract boolean match(String[] aRegexes,
			LinesMatchKind aMatchKind, int aFlags);

	String getLastUnmatchedRegex();

}