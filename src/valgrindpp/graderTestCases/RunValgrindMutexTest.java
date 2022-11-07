package valgrindpp.graderTestCases;

import grader.basics.config.BasicExecutionSpecificationSelector;
import gradingTools.valgrindTestCases.MutexTestSuite;
import trace.grader.basics.GraderBasicsTraceUtility;
import util.trace.Tracer;

public class RunValgrindMutexTest {
	public static void main (String[] args) {
		Tracer.showInfo(true);
		GraderBasicsTraceUtility.setBufferTracedMessages(false);
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().
//		setStudentGradableProjectLocation(
//		 "D:\\dewan_backup\\Java\\GraderBasics\\src\\valgrindpp\\examples\\mutex");
		setStudentGradableProjectLocation("D:\\dewan_backup\\Java\\eclipse\\test-c");

		
		MutexTestSuite.main(args);
//		AddMultiplySuite.main(args);
	}
}
