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
	
	protected boolean missingCompleteCode = false;
	protected boolean missingCompleteCode() {
		return missingCompleteCode;
	}	
	
	 public static String findInPackageCompleteCode(Map<String, String> aMap, String aFileName) {
			for (String aKey: aMap.keySet()) {
				if (aKey.endsWith("/" + aFileName)) {
					String aPrefixString = "// Source code should be in file " + aFileName + " but was found in file "  + aKey + "\n" +
					"// Please put the class in the default package after removing the package declaration\n" +
							"// Delete any moduleinfo file in your project\n";
					
					return aPrefixString + aMap.get(aKey);
				}
			}
			return null;
		}
	protected String getCompleteCode() {

		Map<String, String> aMap = getOrCreateCurrentSourcesMap();
		oddNumbersCode = aMap.get(oddNumbersFileName);
		if (oddNumbersCode == null) {
			oddNumbersCode = findInPackageCompleteCode(aMap, oddNumbersFileName);
			if (oddNumbersCode == null) {
			missingCompleteCode = true;
			oddNumbersCode = "Could not find code for file:"  + oddNumbersFileName + " in project directory \n" +
					"Found the following files:" + aMap.keySet() + "\n";
			}
			
		}
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
