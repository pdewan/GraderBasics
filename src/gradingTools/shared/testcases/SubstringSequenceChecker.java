package gradingTools.shared.testcases;

import java.util.regex.Pattern;

import gradingTools.shared.testcases.interfaces.SubstringChecker;


public interface SubstringSequenceChecker extends SubstringChecker {

//	public abstract boolean check(StringBuffer aStringBuffer);

	public abstract String[] getSubstrings();

	public abstract void setSubstrings(String[] substrings);

//	public abstract double getMyWeight();

//	public abstract void setMyWeight(double myWeight);

	public abstract String getRegex();

	public abstract void setRegex(String regex);

	public abstract Pattern getPattern();

	public abstract void setPattern(Pattern pattern);

	boolean isSuccess();

}