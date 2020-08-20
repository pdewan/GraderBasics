package gradingTools.shared.testcases.suites;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.execution.sml.SMLCommandGeneratorSelector;

public class SMLInterpreterSuite extends InterpreterSuite {
public static void setProjectLocation(String aFile) {
	
	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().
	setStudentGradableProjectLocation(aFile);
}
public static void smlIsBatFile(boolean newValue) {
	if (newValue) {
		SMLCommandGeneratorSelector.getCommandGenerator().
		setDefaultCommand(BasicStaticConfigurationUtils.DEFAULT_BASIC_SML_BAT_COMMAND);
	}
	else {
		SMLCommandGeneratorSelector.getCommandGenerator().
		setDefaultCommand(BasicStaticConfigurationUtils.DEFAULT_BASIC_SML_COMMAND);
//		SMLCommandFinderSelector.getMainClassFinder().
//		setDefaultCommand(BasicStaticConfigurationUtils.DEFAULT_BASIC_SML_COMMAND);
	}
}
}
