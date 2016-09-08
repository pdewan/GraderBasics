package grader.basics.execution;

public class GradingMode {
	protected static boolean graderRun = false;
	
	public static boolean getGraderRun() {
		return graderRun;
	}
	public static void setGraderRun(boolean newVal) {
		graderRun = newVal;
	}

}
