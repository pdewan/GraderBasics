package trace.grader.basics;

import grader.basics.execution.BasicProjectExecution;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.util.DirectoryUtils;
import grader.basics.util.TimedProcess;
import gradingTools.shared.testcases.MethodExecutionTest;
import gradingTools.shared.testcases.concurrency.AbstractBarrier;
import gradingTools.shared.testcases.concurrency.AbstractEarlyJoinBasicJoiner;
import gradingTools.shared.testcases.shapes.LocatableTest;
import gradingTools.shared.testcases.shapes.interfaces.TestBoundedShape;
import gradingTools.shared.testcases.shapes.rotate.detached.DetachedRotatingLineFortyFiveDegreeTest;
import gradingTools.shared.testcases.shapes.rotate.fixed.RotatingFixedLineRotateTest;
import gradingTools.shared.testcases.shapes.rotate.moving.MovingRotatingLineFortyFiveDegreeTest;
import gradingTools.shared.testcases.utils.MethodPropertyChecker;
import util.trace.ImplicitKeywordKind;
import util.trace.Tracer;

public class GraderBasicsTraceUtility {
	static boolean turnOn = true;
	static boolean bufferTracedMessages = true;
	@Deprecated
	public static boolean isTurnOn() {
		return turnOn;
	}
	public static void setBufferTracedMessages(boolean newVal) {
		bufferTracedMessages  = newVal;
	}
	
	public static boolean getBufferTracedMessages() {
		return bufferTracedMessages;
	}

	@Deprecated
	public static void setTurnOn(boolean turnOn) {
		GraderBasicsTraceUtility.turnOn = turnOn;
	}
	public static void setTracing() {
		Tracer.setBufferTracedMessages(bufferTracedMessages);

		Tracer.setImplicitPrintKeywordKind(ImplicitKeywordKind.OBJECT_PACKAGE_NAME);	
//		if (isTurnOn()) {
			Tracer.setKeywordPrintStatus(MethodExecutionTest.class, true);
			Tracer.setKeywordPrintStatus(LocatableTest.class, true);
			Tracer.setKeywordPrintStatus(TestBoundedShape.class, true);
			Tracer.setKeywordPrintStatus(DetachedRotatingLineFortyFiveDegreeTest.class, true);
			Tracer.setKeywordPrintStatus(RotatingFixedLineRotateTest.class, true);
			Tracer.setKeywordPrintStatus(MovingRotatingLineFortyFiveDegreeTest.class, true);
			Tracer.setKeywordPrintStatus(MethodPropertyChecker.class, true);
			Tracer.setKeywordPrintStatus(BasicProjectIntrospection.class, true);
			Tracer.setKeywordPrintStatus(BasicProjectExecution.class, true);
			Tracer.setKeywordPrintStatus(DirectoryUtils.class, true);
			Tracer.setKeywordPrintStatus(TestCaseResult.class, true);
			Tracer.setKeywordPrintStatus(AbstractEarlyJoinBasicJoiner.class, true);
			Tracer.setKeywordPrintStatus(AbstractBarrier.class, true);
			Tracer.setKeywordPrintStatus(TimedProcess.class, true);


//		}

	}

}
