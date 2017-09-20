package trace.grader.basics;

import grader.basics.project.BasicProjectIntrospection;
import grader.basics.util.DirectoryUtils;
import gradingTools.shared.testcases.MethodExecutionTest;
import gradingTools.shared.testcases.shapes.LocatableTest;
import gradingTools.shared.testcases.shapes.interfaces.TestBoundedShape;
import gradingTools.shared.testcases.shapes.rotate.detached.DetachedRotatingLineFortyFiveDegreeTest;
import gradingTools.shared.testcases.shapes.rotate.fixed.RotatingFixedLineRotateTest;
import gradingTools.shared.testcases.shapes.rotate.moving.MovingRotatingLineFortyFiveDegreeTest;
import gradingTools.shared.testcases.utils.MethodPropertyChecker;
import util.trace.ImplicitKeywordKind;
import util.trace.Tracer;

public class GraderBasicsTraceUtility {
	public static void setTracing() {
		Tracer.setImplicitPrintKeywordKind(ImplicitKeywordKind.OBJECT_PACKAGE_NAME);		
		Tracer.setKeywordPrintStatus(MethodExecutionTest.class, true);
		Tracer.setKeywordPrintStatus(LocatableTest.class, true);
		Tracer.setKeywordPrintStatus(TestBoundedShape.class, true);
		Tracer.setKeywordPrintStatus(DetachedRotatingLineFortyFiveDegreeTest.class, true);
		Tracer.setKeywordPrintStatus(RotatingFixedLineRotateTest.class, true);
		Tracer.setKeywordPrintStatus(MovingRotatingLineFortyFiveDegreeTest.class, true);
		Tracer.setKeywordPrintStatus(MethodPropertyChecker.class, true);
		Tracer.setKeywordPrintStatus(BasicProjectIntrospection.class, true);
		Tracer.setKeywordPrintStatus(DirectoryUtils.class, true);

	}

}
