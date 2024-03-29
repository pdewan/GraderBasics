package gradingTools.shared.testcases.concurrency.oddNumbers;

import java.util.Map;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodCalledTestCase;
import gradingTools.shared.testcases.concurrency.outputObserver.AbstractForkJoinChecker;
import util.annotations.Explanation;
import util.annotations.MaxValue;
@MaxValue(2)
public class BasicsProblem extends PostTestExecutorOfForkJoin {
//	public static final String EXPLANATION = "This test checks for the correct thread count, number of loop iterations performed to process the input random numbers, correct implementation of each iteration, and pre-fork behavior. This test should work correctly with the given code. However changes to fairRemainderSize can cause this test to fail."; 
	public static final String EXPLANATION = "This test checks for the correct thread count, number of loop iterations performed to process the input random numbers, correct implementation of each iteration, and pre-fork behavior for the "; 

//	Class[] PRECEDING_TESTS = {SmallNumberOfRandoms.class};
	String[] relevantCheckNames = {
			THREAD_COUNT,
			PRE_FORK_OUTPUT,
			PRE_FORK_EVENTS,
			ITERATION_EVENTS,
			TOTAL_ITERATION_COUNT
			};
//	@Override
//	protected Class[] precedingTests() {
//		return PRECEDING_TESTS;
//	}
	
	protected  String[] relevantCheckNames(  ) {
		return relevantCheckNames;
	}
//	protected AbstractForkJoinChecker oddNumbersExecution() {
//		
////			List<PassFailJUnitTestCase> aPrecedingInstances =
//			return (AbstractOddNumbersExecution) getPrecedingTestInstances().get(0);
//		
//	}
	
//	protected boolean shouldScaleResult() {
//		return false;
//	}

//	@Override
//	public TestCaseResult test(Project project, boolean autoGrade)
//			throws NotAutomatableException, NotGradableException {
//		AbstractForkJoinChecker oddNumbersExecution = oddNumbersExecution();
//		Map<String, TestCaseResult> nameToResult = oddNumbersExecution.getNameToResult();
//		
//		System.out.print(nameToResult);
//		return oddNumbersExecution().getLastResult();
//	}
	

}
