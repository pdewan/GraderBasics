package gradingTools.shared.testcases.concurrency.oddNumbers;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.shared.testcases.concurrency.outputObserver.AbstractForkJoinChecker;

public class PreTestExecutorOfForkJoin extends AbstractForkJoinChecker {

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
	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {
//		
		return computeFromPreTest(project, autoGrade);
	}

}
