package grader.basics.testcase;

import grader.basics.execution.RunningProject;
import grader.basics.junit.TestCaseResult;
import gradingTools.shared.testcases.utils.ABufferingTestInputGenerator;

public interface JUnitTestCase {
	TestCaseResult getLastResult();

	void setLastResult(TestCaseResult lastResult);

	ABufferingTestInputGenerator getOutputBasedInputGenerator();

	RunningProject getInteractiveInputProject();
    /**
   * @return A name or short description about the test case.
   */
	String getName();

	void setName(String aName);

	void setOutputBasedInputGenerator(ABufferingTestInputGenerator newVal);

	void setInteractiveInputProject(RunningProject aProject);

}
