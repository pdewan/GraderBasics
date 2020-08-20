package grader.basics.execution;

public class ExecutableFinderSelector {
	static CommandGenerator mainClassFinder = new AnExecutableFinder();

	public static CommandGenerator getMainClassFinder() {
		return mainClassFinder;
	}

	public static void setMainClassFinder(CommandGenerator mainClassFinder) {
		ExecutableFinderSelector.mainClassFinder = mainClassFinder;
	}

}
