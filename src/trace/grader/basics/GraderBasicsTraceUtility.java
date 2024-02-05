package trace.grader.basics;

import grader.basics.concurrency.propertyChanges.ConcurrentEventUtility;
import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.file.zipfile.AZippedRootFolderProxy;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectClassesManager;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.testcase.PassFailJUnitTestCase;
import grader.basics.util.DirectoryUtils;
import grader.basics.util.TimedProcess;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleWarningsRatioTestCase;
import gradingTools.shared.testcases.MethodExecutionTest;
import gradingTools.shared.testcases.concurrency.AbstractBarrier;
import gradingTools.shared.testcases.concurrency.AbstractEarlyJoinBasicJoiner;
import gradingTools.shared.testcases.shapes.LocatableTest;
import gradingTools.shared.testcases.shapes.interfaces.TestBoundedShape;
import gradingTools.shared.testcases.shapes.rotate.detached.DetachedRotatingLineFortyFiveDegreeTest;
import gradingTools.shared.testcases.shapes.rotate.fixed.RotatingFixedLineRotateTest;
import gradingTools.shared.testcases.shapes.rotate.moving.MovingRotatingLineFortyFiveDegreeTest;
import gradingTools.shared.testcases.utils.ALinesMatcher;
import gradingTools.shared.testcases.utils.MethodPropertyChecker;
import util.misc.ThreadSupport;
import util.trace.ImplicitKeywordKind;
import util.trace.TraceableWarning;
import util.trace.Tracer;
import util.trace.uigen.UnknownPropertyNotification;

public class GraderBasicsTraceUtility {
	static boolean turnOn = true;

//	static boolean bufferTracedMessages = true;
	@Deprecated
	public static boolean isTurnOn() {
		return turnOn;
	}

	public static int getMaxTraces() {
		return BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getMaxTraces();
	}

	public static void setMaxTraces(int newVal) {
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setMaxTraces(newVal);
	}

	public static int getMaxPrintedTraces() {
		return BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getMaxPrintedTraces();
	}

	public static void setMaxPrintedTraces(int newVal) {
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setMaxPrintedTraces(newVal);
	}

	public static void setBufferTracedMessages(boolean newVal) {
//		bufferTracedMessages  = newVal;
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setBufferTracedMessages(newVal);
	}

	public static boolean getBufferTracedMessages() {
//		return bufferTracedMessages;
		return BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getBufferTracedMessages();
	}

	public static boolean getTracerShowInfo() {
//		return bufferTracedMessages;
		return BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getTracing();
	}

	public static void setTracerShowInfo(boolean newVal) {
//		bufferTracedMessages  = newVal;
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setTracing(newVal);
	}

	public static boolean getHideRedirectedPrints() {
		return BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getHideRedirectedOutput();
	}

	public static void setHideRedirectedPrints(boolean newVal) {
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setHideRedirectedOuput(newVal);
	}

	@Deprecated
	public static void setTurnOn(boolean turnOn) {
		GraderBasicsTraceUtility.turnOn = turnOn;
	}

	public static void setTracing() {
//		Tracer.setBufferTracedMessages(bufferTracedMessages);
		Tracer.setBufferTracedMessages(getBufferTracedMessages());
		Tracer.setDisplayThreadName(true);
		Tracer.showInfo(GraderBasicsTraceUtility.getTracerShowInfo());

//		Tracer.showInfo(GraderBasicsTraceUtility.getTracerShowInfo());
		Tracer.setMaxTraces(GraderBasicsTraceUtility.getMaxTraces());
		Tracer.setMaxPrintedTraces(GraderBasicsTraceUtility.getMaxPrintedTraces());
		TraceableWarning.doNotWarn(UnknownPropertyNotification.class);

		Tracer.setImplicitPrintKeywordKind(ImplicitKeywordKind.OBJECT_PACKAGE_NAME);
//		if (isTurnOn()) {
		Tracer.setKeywordPrintStatus(PassFailJUnitTestCase.class, true);

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
		Tracer.setKeywordPrintStatus(ALinesMatcher.class, true);
		Tracer.setKeywordPrintStatus(AZippedRootFolderProxy.class, true);
		Tracer.setKeywordPrintStatus(BasicProjectClassesManager.class, true);
		Tracer.setKeywordPrintStatus(CheckStyleTestCase.class, true);
		Tracer.setKeywordPrintStatus(CheckStyleWarningsRatioTestCase.class, true);
		Tracer.setKeywordPrintStatus(ConcurrentEventUtility.class, true);

//		}

	}

	// This method must be reused directly in all testable code and should
	// be given to the student
	public static String threadPrefix() {
//		return "Thread " + Thread.currentThread().getId() + "->";
		return Thread.currentThread().getName() + "->";
	}

	// This method should be used directly in all testable programs
	// it does the print only if the problem size is small - implying testing or
	// debugging. The performance benefits of concurrency are demonstrated only
	// when problems are large, when we do not want prints to clutter the display
	// and slow computation
	public static void printProperty(String aPropertyName, Object aPropertyValue) {
		String aComposition = threadPrefix() + aPropertyName + ":" + aPropertyValue;
//			System.out.println(threadPrefix() + aPropertyName + ":" + aPropertyValue);
		System.out.println(aComposition);
//		ThreadSupport.sleep(1);// force context switch
	}
}
