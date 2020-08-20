package grader.basics.execution;


public class JavaMainClassFinderSelector {
	static CommandGenerator mainClassFinder = new ABasicMainClassFinder();

	public static CommandGenerator getMainClassFinder() {
		return mainClassFinder;
	}

	public static void setMainClassFinder(CommandGenerator mainClassFinder) {
		JavaMainClassFinderSelector.mainClassFinder = mainClassFinder;
	}

}
