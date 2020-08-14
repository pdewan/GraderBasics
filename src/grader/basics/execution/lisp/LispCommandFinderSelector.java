package grader.basics.execution.lisp;

import grader.basics.execution.MainClassFinder;

public class LispCommandFinderSelector {
	static MainClassFinder mainClassFinder = new LispCommandFinder();

	public static MainClassFinder getMainClassFinder() {
		return mainClassFinder;
	}

	public static void setMainClassFinder(MainClassFinder mainClassFinder) {
		LispCommandFinderSelector.mainClassFinder = mainClassFinder;
	}

}
