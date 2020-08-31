package grader.basics.execution;

import util.trace.Tracer;

public class GradingMode {
	protected static boolean graderRun = false;
	
	public static boolean getGraderRun() {
		return graderRun;
	}
	public static void setGraderRun(boolean newVal) {
		graderRun = newVal;
//		if (newVal) {
//			Tracer.showInfo(true);
//		}
	}

}
