package gradingTools.basics.sharedTestCase.checkstyle.predefined;

import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodCalledTestCase;

public class ClassCallsPrintProperty extends CheckstyleMethodCalledTestCase {
	public static final String PRINT_PROPERTY = "printProperty:String;.*";
	public ClassCallsPrintProperty(String aClassName) {
		super(aClassName, PRINT_PROPERTY);
	}
	public ClassCallsPrintProperty(String aClassName, String aMethodName) {
		super(aClassName, aMethodName, PRINT_PROPERTY);
	}


}
