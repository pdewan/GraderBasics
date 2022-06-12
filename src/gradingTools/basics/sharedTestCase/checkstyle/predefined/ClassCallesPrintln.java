package gradingTools.basics.sharedTestCase.checkstyle.predefined;

import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodCalledTestCase;

public class ClassCallesPrintln extends CheckstyleMethodCalledTestCase {

	public ClassCallesPrintln(String aClassName) {
		super(aClassName, "java\\.io\\.PrintStream!println:\\*->\\.\\*");
		// TODO Auto-generated constructor stub
	}

}
