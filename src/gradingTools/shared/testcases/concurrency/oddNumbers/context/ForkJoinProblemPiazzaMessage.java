package gradingTools.shared.testcases.concurrency.oddNumbers.context;

import java.util.Map;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.observers.ATraceSourceAndTestLogWriter;
import grader.basics.observers.TestLogFileWriterFactory;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.shared.testcases.concurrency.oddNumbers.AbstractOddNumbersExecution;
import gradingTools.shared.testcases.concurrency.oddNumbers.FairAllocationSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.ForkJoinSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.hints.ForkJoinHint;
import util.annotations.Explanation;

@Explanation("This is a request to create context to get Piazza help for the fork join problem")
public class ForkJoinProblemPiazzaMessage extends AbstractOddNumberProblemContext{
	
	private static Class[] PRECEDING_TESTS = {
			ForkJoinSmallProblem.class,
//			FairAllocationLargerProblem.class
	};
	
	protected Class[] precedingTests() {
		return PRECEDING_TESTS;
	}
	@Override
	protected Class[] previousHints() {
		// TODO Auto-generated method stub
		return noPreviousHints();
	}
	protected String getRelevantCodeStart() {
		return "// Start of forkAndJoinThreads (DO NOT EDIT THIS LINE)" ;
	}
	
	 protected String getRelevantCodeEnd() {
			return "// End of forkAndJoinThreads (DO NOT EDIT THIS LINE)" ;
	 }
	 @Override
	public TestCaseResult test(Project project, boolean autoGrade)
				throws NotAutomatableException, NotGradableException {
		    Boolean isForkJoinTraced = AbstractOddNumbersExecution.isTraceForkJoin();
		    if (isForkJoinTraced == null || isForkJoinTraced) {
		    	return super.test(project, autoGrade);
		    }
		    String aMessage = AbstractOddNumbersExecution.composNotEnabledMessage("Fork Join");		    		
		    return fail(aMessage);
	}

}
