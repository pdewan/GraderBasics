package grader.basics.execution;


public class LispMainClassFinderSelector {
	static MainClassFinder mainClassFinder = new LispMainClassFinder();

	public static MainClassFinder getMainClassFinder() {
		return mainClassFinder;
	}

	public static void setMainClassFinder(MainClassFinder mainClassFinder) {
		LispMainClassFinderSelector.mainClassFinder = mainClassFinder;
	}

}
