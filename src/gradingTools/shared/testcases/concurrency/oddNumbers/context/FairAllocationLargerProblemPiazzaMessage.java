package gradingTools.shared.testcases.concurrency.oddNumbers.context;

import java.util.Map;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.observers.ATraceSourceAndTestLogWriter;
import grader.basics.observers.TestLogFileWriterFactory;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.shared.testcases.concurrency.oddNumbers.AbstractOddNumbersExecution;
import gradingTools.shared.testcases.concurrency.oddNumbers.FairAllocationLargerProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.FairAllocationSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.ForkJoinSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.hints.ForkJoinHint;
import util.annotations.Explanation;

@Explanation("Run this \"test\" to get Piazza hints to make the FairAllocationLargerProblem test succeed after you have made the FailAllocationSmallProblem succeed")
public class FairAllocationLargerProblemPiazzaMessage extends AbstractOddNumberProblemContext{
	
	private static Class[] PRECEDING_TESTS = {
			FairAllocationLargerProblem.class,
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
		return "// Start of fairThreadRemainderSize (DO NOT EDIT THIS LINE)" ;
	}
	
	 protected String getRelevantCodeEnd() {
			return "// End fairThreadRemainderSize (DO NOT EDIT THIS LINE)" ;
	 }
	 public void passfailDefaultTest() {
			AbstractOddNumbersExecution.setTraceFairAllocation(true);
			super.passfailDefaultTest();
	}
	 @Override
		public TestCaseResult test(Project project, boolean autoGrade)
					throws NotAutomatableException, NotGradableException {
		 		return super.test(project, autoGrade);
//			    Boolean isFairAllocationTraced = AbstractOddNumbersExecution.isTraceFairAllocation();
//			    if (isFairAllocationTraced == null || isFairAllocationTraced) {
//			    	return super.test(project, autoGrade);
//			    }
//			    String aMessage = AbstractOddNumbersExecution.composNotEnabledMessage("Fair Allocation");		    		
//			    return fail(aMessage);
		}

}
