package gradingTools.shared.testcases.concurrency.oddNumbers;

import util.annotations.MaxValue;
@MaxValue(10)
public class ForkJoinProblem extends PostTestExecutorOfForkJoin {
	public static final String EXPLANATION = "This test checks if the fork-join bug in the method forkAndJoinThreads() has been fixed for the ";
//	Class[] PRECEDING_TESTS = {SmallNumberOfRandoms.class};
	
	String[] relevantCheckNames = {this.HAS_INTERLEAVING};

//	@Override
//	protected Class[] precedingTests() {
//		return PRECEDING_TESTS;
//	}
	@Override
	protected String[] relevantCheckNames() {
		return relevantCheckNames;
	}
	
//	protected AbstractForkJoinChecker oddNumbersExecution() {
//		
////			List<PassFailJUnitTestCase> aPrecedingInstances =
//			return (AbstractOddNumbersExecution) getPrecedingTestInstances().get(0);
//		
//	}
	
//	@Override
//	protected boolean shouldScaleResult() {
//		return false;
//	}

//	@Override
//	public TestCaseResult test(Project project, boolean autoGrade)
//			throws NotAutomatableException, NotGradableException {
////		
//		return computeFromPreTest(project, autoGrade);
//	}
	

}
