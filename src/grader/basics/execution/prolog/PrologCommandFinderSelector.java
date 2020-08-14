package grader.basics.execution.prolog;

import grader.basics.execution.MainClassFinder;

public class PrologCommandFinderSelector {
	static MainClassFinder mainClassFinder = new PrologCommandFinder();

	public static MainClassFinder getMainClassFinder() {
		return mainClassFinder;
	}

	public static void setMainClassFinder(MainClassFinder mainClassFinder) {
		PrologCommandFinderSelector.mainClassFinder = mainClassFinder;
	}

}
