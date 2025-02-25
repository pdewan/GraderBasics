package gradingTools.shared.testcases.concurrency.oddNumbers.context;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.shared.testcases.concurrency.oddNumbers.AbstractOddNumbersExecution;
import gradingTools.shared.testcases.concurrency.oddNumbers.SynchronizationSmallProblemInRepository;
import util.annotations.Explanation;

@Explanation("Run this \"test\" to get Piazza hints to make the SynchronizationSmallProblem and SynchornizationLargeProblem tests succeed")
public class SynchronizationProblemPiazzaMessage extends AbstractOddNumberProblemContext{
	private static Class synchronizationTestClass = SynchronizationSmallProblemInRepository.class;
//	private static Class[] PRECEDING_TESTS = {
//			SynchronizationSmallProblemInRepository.class
//	};
	
	public static Class getSynchronizationTestClass() {
		return synchronizationTestClass;
	}
	public static void setSynchronizationTestClass(Class newVal) {
		SynchronizationProblemPiazzaMessage.synchronizationTestClass = newVal;
		PRECEDING_TESTS[0] = newVal;
	}

	private static Class[] PRECEDING_TESTS = {
			 synchronizationTestClass
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
	 public void passfailDefaultTest() {
			AbstractOddNumbersExecution.setTracSynchronization(true);
			super.passfailDefaultTest();
	}
	 
	 
	 @Override
	public TestCaseResult test(Project project, boolean autoGrade)
				throws NotAutomatableException, NotGradableException {
	    	return super.test(project, autoGrade);

//		    Boolean isSynchronizationTraced = AbstractOddNumbersExecution.isTraceSynchronization();
//		    if (isSynchronizationTraced == null || isSynchronizationTraced) {
//		    	return super.test(project, autoGrade);
//		    }
//		    String aMessage = AbstractOddNumbersExecution.composNotEnabledMessage("Synchronization");		    		
//		    return fail(aMessage);
	}

}
