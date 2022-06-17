package grader.basics.output.observer;

import gradingTools.shared.testcases.SubstringSequenceChecker;

public interface PropertyBasedStringChecker extends SubstringSequenceChecker{
	public String[][] getProperties();
}
