package gradingTools.shared.testcases.concurrency.oddNumbers.hanging;

import gradingTools.shared.testcases.concurrency.oddNumbers.ForkJoinLargerProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.LargerNumberOfRandoms;
import gradingTools.shared.testcases.concurrency.oddNumbers.LargerNumberTests;
import gradingTools.shared.testcases.concurrency.oddNumbers.SynchronizationProblem;
import util.annotations.Explanation;
import util.annotations.MaxValue;
@MaxValue(10)
@Explanation(SynchronizationProblem.EXPLANATION + LargerNumberTests.LARGER_EXPLANATION)
public class SynchronizationLargerProblemInDemo extends SynchronizationProblem {
	
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
	public SynchronizationLargerProblemInDemo() {
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
