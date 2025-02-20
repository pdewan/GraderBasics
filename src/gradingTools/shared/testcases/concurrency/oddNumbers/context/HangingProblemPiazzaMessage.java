package gradingTools.shared.testcases.concurrency.oddNumbers.context;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.shared.testcases.concurrency.oddNumbers.AbstractOddNumbersExecution;
import gradingTools.shared.testcases.concurrency.oddNumbers.SmallNumberOfRandoms;
import gradingTools.shared.testcases.concurrency.oddNumbers.SynchronizationSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.SynchronizationSmallProblemInRepository;
import util.annotations.Explanation;

@Explanation("This is a request to create context to get Piazza help for the hanging problem")
public class HangingProblemPiazzaMessage extends AbstractOddNumberProblemContext{
	
	private static Class[] PRECEDING_TESTS = {			
			SmallNumberOfRandoms.class,
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
		return "//Start OddNumbersRepository (DO NOT EDIT THIS LINE)" ;
	}
	
	 protected String getRelevantCodeEnd() {
			return "//End OddNumbersRepository (DO NOT EDIT THIS LINE)";
	 }
	 protected boolean failedTestVetoes(Class aClass) {
			return false; // the test will fail if it hangs
	}
	 
	 @Override
	public TestCaseResult test(Project project, boolean autoGrade)
				throws NotAutomatableException, NotGradableException {
		    Boolean isCallsTraced = AbstractOddNumbersExecution.isTraceCalls();
		    PassFailJUnitTestCase aPrecedingTest = getFirstPrecedingTestInstance();
		    Exception aTimeoutException = aPrecedingTest.getTimeoutException();
		    if (aTimeoutException == null) {
		    	String aMessage = "Program did not timeout, does not seem to be hanging.\nPlease follow instructions to make it hang.";
		    	return fail(aMessage);
		    }
		    if (isCallsTraced != null && !isCallsTraced) {
		    	 String aMessage = AbstractOddNumbersExecution.composNotEnabledMessage("Hanging");		    		
				    return fail(aMessage);
		    }		    
		    return super.test(project, autoGrade);		   
	}

}
