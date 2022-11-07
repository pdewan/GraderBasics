package valgrindpp.graderTestCases;

import grader.basics.config.BasicExecutionSpecificationSelector;
import trace.grader.basics.GraderBasicsTraceUtility;
import util.trace.Tracer;

public class RunValgrindBoundedBufferTest {
	public static void main (String[] args) {
		Tracer.showInfo(true);
		GraderBasicsTraceUtility.setBufferTracedMessages(false);
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().
		setStudentGradableProjectLocation("D:\\dewan_backup\\Java\\eclipse\\test-c");
//		Assignment1Suite.main(args);
//		AddMultiplySuite.main(args);
	}
}
