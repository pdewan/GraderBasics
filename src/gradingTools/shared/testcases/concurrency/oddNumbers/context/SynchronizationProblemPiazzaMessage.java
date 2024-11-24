package gradingTools.shared.testcases.concurrency.oddNumbers.context;

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

}
