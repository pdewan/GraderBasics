package gradingTools.shared.testcases.concurrency.oddNumbers;

import util.annotations.MaxValue;
@MaxValue(5)
public class ForkJoinLargerProblem extends ForkJoinProblem {
	Class[] PRECEDING_TESTS = {LargerNumberOfRandoms.class};
	
//	String[] relevantCheckNames = {this.HAS_INTERLEAVING};

	@Override
	protected Class[] precedingTests() {
		return PRECEDING_TESTS;
	}
//	@Override
//	protected String[] relevantCheckNames() {
//		return relevantCheckNames;
//	}
	
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
