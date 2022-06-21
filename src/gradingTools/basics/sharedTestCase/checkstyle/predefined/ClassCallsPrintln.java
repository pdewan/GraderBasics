package gradingTools.basics.sharedTestCase.checkstyle.predefined;

import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodCalledTestCase;

public class ClassCallsPrintln extends CheckstyleMethodCalledTestCase {
	public static final String PRINTLN = "java\\.io\\.PrintStream!println:\\*->\\.\\*";
	public ClassCallsPrintln(String aClassName) {
		super(aClassName, PRINTLN);
		// TODO Auto-generated constructor stub
	}
	public ClassCallsPrintln(String aClassName, String aMethodName) {
		super(aClassName, aMethodName, PRINTLN);
		// TODO Auto-generated constructor stub
	}


}
