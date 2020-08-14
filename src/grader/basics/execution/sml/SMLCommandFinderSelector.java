package grader.basics.execution.sml;

import grader.basics.execution.MainClassFinder;

public class SMLCommandFinderSelector {
	static MainClassFinder mainClassFinder = new SMLCommandFinder();

	public static MainClassFinder getMainClassFinder() {
		return mainClassFinder;
	}

	public static void setMainClassFinder(MainClassFinder mainClassFinder) {
		SMLCommandFinderSelector.mainClassFinder = mainClassFinder;
	}

}
