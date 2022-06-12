package gradingTools.basics.sharedTestCase.checkstyle.predefined;

import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodCalledTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodDefinedTestCase;

public class ClassCallsParseInt extends CheckstyleMethodCalledTestCase {

	public ClassCallsParseInt(String aClassName) {
		super(aClassName, "java.lang.Integer!parseInt:String->int");
	}

}
