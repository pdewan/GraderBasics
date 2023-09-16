package gradingTools.basics.sharedTestCase.checkstyle.predefined;

import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodCalledTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodDefinedTestCase;

public class ClassCallsSleep extends CheckstyleMethodCalledTestCase {

	private static final String THREAD_SUPPORT_SLEEP_LONG_VOID = "ThreadSupport!sleep:long->void";

	public ClassCallsSleep(String aClassName) {
		super(aClassName, THREAD_SUPPORT_SLEEP_LONG_VOID);
	}

	public ClassCallsSleep(String aClassName, String aCallingMethod) {
		super(aClassName, aCallingMethod, THREAD_SUPPORT_SLEEP_LONG_VOID);
	}
}
