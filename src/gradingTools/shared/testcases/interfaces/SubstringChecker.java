package gradingTools.shared.testcases.interfaces;

import gradingTools.shared.testcases.utils.LinesMatchKind;
import gradingTools.shared.testcases.utils.LinesMatcher;

public interface SubstringChecker {
	public abstract boolean check(StringBuffer aStringBuffer);
	public abstract boolean check(String aStringBuffer);
	boolean check(LinesMatcher aLinesMatcher, LinesMatchKind aMatchKind,
			int aFlags);
}
