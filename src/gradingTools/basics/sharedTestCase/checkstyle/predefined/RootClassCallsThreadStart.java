package gradingTools.basics.sharedTestCase.checkstyle.predefined;

import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodCalledTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodDefinedTestCase;

public class RootClassCallsThreadStart extends CheckstyleMethodCalledTestCase {
	public static final String THREAD_START_CALL = "java.lang.Thread!start:->void";
	public RootClassCallsThreadStart(String aClassName) {
		super(aClassName, THREAD_START_CALL );
		// TODO Auto-generated constructor stub
	}

}
