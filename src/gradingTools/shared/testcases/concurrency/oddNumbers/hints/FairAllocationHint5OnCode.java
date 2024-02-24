package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

import java.util.List;

import grader.basics.junit.JUnitTestsEnvironment;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.observers.AnAbstractTestLogFileWriter;
import grader.basics.observers.TestLogFileWriterFactory;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.shared.testcases.concurrency.oddNumbers.FairAllocationErrorInference;
import gradingTools.shared.testcases.concurrency.oddNumbers.FairAllocationProblem;
import util.annotations.Explanation;
import util.annotations.MaxValue;
@MaxValue(0)
@Explanation("This hint tries to identify parts of the code in fairThreadRemainderSize() that are leading to teh error message from FairAllocationSmallProblem")
public class FairAllocationHint5OnCode extends FairAllocationHint {
	static Class[] PREVIOUS_HINTS = {
			FairAllocationHint3OnError.class
	};
//	static Class[] PRECEDING_TESTS = {
//			FairAllocationSmallProblem.class
//	};
//	
//	protected Class[] precedingTests() {
//		return PRECEDING_TESTS;
//	}
	
	@Override
	protected Class[] previousHints() {
		// TODO Auto-generated method stub
		return PREVIOUS_HINTS;
	}
	
	@Override
	protected String hint() {
		List<PassFailJUnitTestCase> aPrecedingTestInstances = 
				getPrecedingTestInstances();
		if (aPrecedingTestInstances.size() == 0) {
			return "Internal Error, at least one preceding test does not exist";
		}
		FairAllocationProblem aPrecedingInstance =
				(FairAllocationProblem) aPrecedingTestInstances.get(0);
		FairAllocationErrorInference aFairAllocationErrorInference = aPrecedingInstance.getFairAllocationErrorInference();
		String aPrefix = "In method fairThreadRemainderSize ";
		switch (aFairAllocationErrorInference) {
		case MOD_OPERATOR:
			return  aPrefix + "check your mod operator in fair";
		case MULTIPLE_IFS:
			return aPrefix + "you should have a single if with a comparision operator in the checked expression rather than multiple ifs";
		case NONE:
			return aPrefix + "you do not need a hint";
		case OFF_BY_ONE:
			return aPrefix + "your need a different comparison operator to address the off by one problem in allocating to threads";
		case RETURN_TOO_BIG:
			return aPrefix + "the value you return should be zero or 1, as this method id allocating the remainder after allocating the minimum work to each thread";
		case UNKNOWN:
			return aPrefix + " cannot determine the exact problem";
		default:
			return aPrefix + " cannot determine the exact problem";

		
		
		}
		
//		// TODO Auto-generated method stub
//		String aLine1 = "Look at the error message produced by FairAllocationSmallProblem and FairAllocationKLargerProblem";
//		String aLine2 = "There are two possible errors reported";
//		String aLine3 = "The sum of the total items allocated to each thread runnable is not the same as the total number of items in the list";
//		String aLine4 = "So the difference between the min and max allocations is not <= 1";
//		String aLine5 = "You need to fix one or both errors";
//
//		return "\n" + aLine1 + "\n" + aLine2 + "\n" + aLine3 + "\n" + aLine4 + "\n" + aLine5;		   
	}
	
	
//	@Override
//	public TestCaseResult test(Project project, boolean autoGrade)
//			throws NotAutomatableException, NotGradableException {
//		AnAbstractTestLogFileWriter[] aFileWriters =
//				TestLogFileWriterFactory.getFileWriter();
//		AnAbstractTestLogFileWriter aFineGrainedWriter = aFileWriters[1];
//		
//		String aPreviousContents = aFineGrainedWriter.getPreviousContents();
//		String aName = FairAllocationSmallProblem.class.getSimpleName();
//		boolean hasBeenRun = aPreviousContents.contains(aName);
//		
////		PassFailJUnitTestCase aTestCase = JUnitTestsEnvironment
////		.getAndPossiblyRunGradableJUnitTest(FairAllocationSmallProblem.class);
//		return null;
//	}

}
