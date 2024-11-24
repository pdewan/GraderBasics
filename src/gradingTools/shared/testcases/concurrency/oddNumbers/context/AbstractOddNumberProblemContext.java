package gradingTools.shared.testcases.concurrency.oddNumbers.context;

import java.util.Map;

import grader.basics.observers.ATraceSourceAndTestLogWriter;
import grader.basics.observers.TestLogFileWriterFactory;
import gradingTools.shared.testcases.concurrency.oddNumbers.FairAllocationSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.hints.ForkJoinHint;
import util.annotations.Explanation;

@Explanation("This is a request to create context to get help for the fork join problem")
public abstract class AbstractOddNumberProblemContext extends AbstractProblemContext{
	private static String oddNumbersCode;
	private static String oddNumbersFileName = "ConcurrentOddNumbers.java";
	private String currentTrace;
//	private static Class[] PRECEDING_TESTS = {
//			FairAllocationSmallProblem.class,
////			FairAllocationLargerProblem.class
//	};
	
//	protected Class[] precedingTests() {
//		return PRECEDING_TESTS;
//	}
	
	
	
	protected String getCompleteCode() {
		Map<String, String> aMap = getOrCreateCurrentSourcesMap();
		oddNumbersCode = aMap.get(oddNumbersFileName);
		return oddNumbersCode;
	}
	
	protected String getOrCreateOddNumbersCode() {
		Map<String, String> aMap = getOrCreateCurrentSourcesMap();
		oddNumbersCode = aMap.get(oddNumbersFileName);
		return oddNumbersCode;
	}
	
	@Override
	protected Class[] previousHints() {
		// TODO Auto-generated method stub
		return noPreviousHints();
	}
	
	

//	@Override
//	protected String hint() {
//		return get
//
//	}

}
