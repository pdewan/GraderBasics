package gradingTools.shared.testcases.concurrency.odddNumbers;

import java.util.Map;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodCalledTestCase;
import gradingTools.shared.testcases.concurrency.outputObserver.AbstractForkJoinChecker;

public class ForkJoinSmallNumberOfRandoms extends PassFailJUnitTestCase {
	Class[] PRECEDING_TESTS = {SmallNumberOfRandoms.class};
	@Override
	protected Class[] precedingTests() {
		return PRECEDING_TESTS;
	}
	
	protected AbstractForkJoinChecker oddNumbersExecution() {
		
//			List<PassFailJUnitTestCase> aPrecedingInstances =
			return (AbstractOddNumbersExecution) getPrecedingTestInstances().get(0);
		
	}
	
	@Override
	protected boolean shouldScaleResult() {
		return false;
	}

	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {
		AbstractForkJoinChecker oddNumbersExecution = oddNumbersExecution();
		Map<String, TestCaseResult> nameToResult = oddNumbersExecution.getNameToResult();
		System.out.print(nameToResult);
		return oddNumbersExecution().getLastResult();
	}
	

}
