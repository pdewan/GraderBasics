package gradingTools.shared.testcases.concurrency.oddNumbers;

import util.annotations.Explanation;
import util.annotations.MaxValue;
@MaxValue(0)
@Explanation(BasicsProblem.EXPLANATION + SmallNumberTests.SMALL_EXPLANATION)
public class BasicsSmallProblem extends BasicsProblem {	
	Class[] PRECEDING_TESTS = {SmallNumberOfRandoms.class};
//	String[] relevantCheckNames = {
//			THREAD_COUNT,
//			PRE_FORK_OUTPUT,
//			PRE_FORK_EVENTS,
//			ITERATION_EVENTS,
//			TOTAL_ITERATION_COUNT
//			};
	@Override
	protected Class[] precedingTests() {
		return PRECEDING_TESTS;
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
