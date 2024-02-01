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
import gradingTools.shared.testcases.concurrency.oddNumbers.FairAllocationLargerProblem;
import util.annotations.MaxValue;
@MaxValue(0)
public class FairAllocationHint5OnCode extends FairAllocationHint4OnCode {
	static Class[] PRECEDING_TESTS = {
//			FairAllocationSmallProblem.class,
			FairAllocationLargerProblem.class
	};
	
	protected Class[] precedingTests() {
		return PRECEDING_TESTS;
	}
	static Class[] PREVIOUS_HINTS = {
			FairAllocationHint4OnCode.class
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
}
