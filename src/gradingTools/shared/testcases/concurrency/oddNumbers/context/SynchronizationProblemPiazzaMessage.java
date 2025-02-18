package gradingTools.shared.testcases.concurrency.oddNumbers.context;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.shared.testcases.concurrency.oddNumbers.AbstractOddNumbersExecution;
import gradingTools.shared.testcases.concurrency.oddNumbers.SynchronizationSmallProblemInRepository;
import util.annotations.Explanation;

@Explanation("This is a request to create context to get Piazza help for the synchronization problem")
public class SynchronizationProblemPiazzaMessage extends AbstractOddNumberProblemContext{
	
	private static Class[] PRECEDING_TESTS = {
			SynchronizationSmallProblemInRepository.class
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
	 
	 @Override
	public TestCaseResult test(Project project, boolean autoGrade)
				throws NotAutomatableException, NotGradableException {
		    Boolean isSynchronizationTraced = AbstractOddNumbersExecution.isTraceSynchronization();
		    if (isSynchronizationTraced == null || isSynchronizationTraced) {
		    	return super.test(project, autoGrade);
		    }
		    String aMessage = AbstractOddNumbersExecution.composNotEnabledMessage("Synchronization");		    		
		    return fail(aMessage);
	}

}
