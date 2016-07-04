package grader.basics.execution;


public class JavaMainClassFinderSelector {
	static MainClassFinder mainClassFinder = new ABasicMainClassFinder();

	public static MainClassFinder getMainClassFinder() {
		return mainClassFinder;
	}

	public static void setMainClassFinder(MainClassFinder mainClassFinder) {
		JavaMainClassFinderSelector.mainClassFinder = mainClassFinder;
	}

}
