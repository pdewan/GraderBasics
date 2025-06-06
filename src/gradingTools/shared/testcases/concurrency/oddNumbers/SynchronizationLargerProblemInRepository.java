package gradingTools.shared.testcases.concurrency.oddNumbers;

import java.util.Map;

import org.eclipse.jdt.core.dom.ThisExpression;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodCalledTestCase;
import gradingTools.shared.testcases.concurrency.outputObserver.AbstractForkJoinChecker;
import util.annotations.Explanation;
import util.annotations.MaxValue;
@MaxValue(10)
@Explanation(SynchronizationProblem.EXPLANATION + LargerNumberTests.LARGER_EXPLANATION)
public class SynchronizationLargerProblemInRepository extends SynchronizationProblem {
	
	Class[] PRECEDING_TESTS = {
			LargerNumberOfRandoms.class,
			ForkJoinLargerProblem.class
			};
//	String[] relevantCheckNames = {
//			this.POST_JOIN_EVENTS
//			};
	@Override
	protected Class[] precedingTests() {
		return PRECEDING_TESTS;
	}
	public SynchronizationLargerProblemInRepository() {
		super("OddNumbersRepository");
	}
	
//	protected  String[] relevantCheckNames(  ) {
//		return relevantCheckNames;
//	}
////	protected AbstractForkJoinChecker oddNumbersExecution() {
////		
//////			List<PassFailJUnitTestCase> aPrecedingInstances =
////			return (AbstractOddNumbersExecution) getPrecedingTestInstances().get(0);
////		
////	}
//	
////	protected boolean shouldScaleResult() {
////		return false;
////	}
//
////	@Override
////	public TestCaseResult test(Project project, boolean autoGrade)
////			throws NotAutomatableException, NotGradableException {
////		AbstractForkJoinChecker oddNumbersExecution = oddNumbersExecution();
////		Map<String, TestCaseResult> nameToResult = oddNumbersExecution.getNameToResult();
////		
////		System.out.print(nameToResult);
////		return oddNumbersExecution().getLastResult();
////	}
//	

}
