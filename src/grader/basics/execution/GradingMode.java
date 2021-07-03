package grader.basics.execution;

import grader.basics.config.BasicExecutionSpecification;
import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.config.BasicStaticConfigurationUtils;
import util.trace.Tracer;

public class GradingMode {
	protected static boolean graderRun = false;
	protected static BasicExecutionSpecification basicExecutionSpecification = BasicExecutionSpecificationSelector.getBasicExecutionSpecification();
	protected static boolean manualGradingOnly = false;
	
	public static boolean isManualGradingOnly() {
//		return basicExecutionSpecification.getNavigationKind().equals(BasicStaticConfigurationUtils.MANUAL_GRADING);
		return manualGradingOnly;
	}
	
	public static void setManualGradingOnly (boolean newVal) {
		manualGradingOnly = newVal;
	}
	
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
