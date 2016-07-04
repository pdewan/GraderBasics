package grader.basics.junit;

import org.junit.runner.notification.Failure;

public interface JUnitTestResult {
	public TestCaseResult getTestCaseResult() ;
	public Failure getFailure();

}
