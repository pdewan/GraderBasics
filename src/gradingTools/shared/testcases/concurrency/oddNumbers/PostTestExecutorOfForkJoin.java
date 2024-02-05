package gradingTools.shared.testcases.concurrency.oddNumbers;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.shared.testcases.concurrency.outputObserver.AbstractForkJoinChecker;

public class PostTestExecutorOfForkJoin 
	extends 
	AbstractForkJoinChecker 
//	PassFailJUnitTestCase
	{

	@Override
	protected int numExpectedForkedThreads() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	protected String mainClassIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected boolean nonPassTestVetoes (Class aClass) {
		Class[] aPrecedingTests = precedingTests();
		if (aPrecedingTests == null || aPrecedingTests.length == 0) {
			return false;
		}
		return ((aPrecedingTests[0] != aClass)); // the first test does not veto
		
	}
	@Override
	protected boolean failedTestVetoes(Class aClass) {
		return nonPassTestVetoes(aClass);
	}
	@Override
	protected boolean partialPassTestVetoes(Class aClass) {
		return nonPassTestVetoes(aClass);
	}

	String[] emptyStringArray = {};
	protected  String[] relevantCheckNames(  ) {
		return emptyStringArray;
	}
	protected TestCaseResult testCaseResult() {
		return combineNormalizedResults(relevantCheckNames());
	}
	
	protected TestCaseResult[] maybeAddToResults (TestCaseResult[] aResults) {
		return aResults;
	}
	
	public TestCaseResult computeFromPreTest(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {

		AbstractForkJoinChecker preExecution =
				(AbstractForkJoinChecker) getPrecedingTestInstances().get(0);
		String[] aTestNames = relevantCheckNames();
		TestCaseResult[] aResults = preExecution.toTestResults(relevantCheckNames());
		aResults = maybeAddToResults(aResults);
		for (int anIndex = 0; anIndex < aTestNames.length; anIndex++)  {
			nameToResult.put(aTestNames[anIndex], aResults[anIndex]);
		}
		return combineResults(aResults);

//		return preExecution.combineNormalizedResults(relevantCheckNames());
		
	}
	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {
		return computeFromPreTest(project, autoGrade);
	}

}
