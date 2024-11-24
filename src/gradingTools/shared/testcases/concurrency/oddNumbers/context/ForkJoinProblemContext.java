package gradingTools.shared.testcases.concurrency.oddNumbers.context;

import java.util.Map;

import grader.basics.observers.ATraceSourceAndTestLogWriter;
import grader.basics.observers.TestLogFileWriterFactory;
import gradingTools.shared.testcases.concurrency.oddNumbers.FairAllocationSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.ForkJoinSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.hints.ForkJoinHint;
import util.annotations.Explanation;

@Explanation("This is a request to create context to get help for the fork join problem")
public class ForkJoinProblemContext extends AbstractOddNumberProblemContext{
	
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

}
