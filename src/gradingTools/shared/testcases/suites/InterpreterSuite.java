package gradingTools.shared.testcases.suites;

import grader.basics.config.BasicExecutionSpecificationSelector;

public class InterpreterSuite {
public static void setProjectLocation(String aFile) {
	
	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().
	setStudentGradableProjectLocation(aFile);
}
public static void setUserPath(String newValue) {
	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().
	setUserPath(newValue);
}
//public static void smlIsBatFile(boolean newValue) {
//	if (!newValue) {
//		
//	}
//}
}
