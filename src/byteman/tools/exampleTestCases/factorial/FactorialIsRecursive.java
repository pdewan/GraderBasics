package byteman.tools.exampleTestCases.factorial;

import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodCalledTestCase;

public class FactorialIsRecursive extends CheckstyleMethodCalledTestCase {

	public FactorialIsRecursive() {
		super("@Factorial",
				"factorial:Integer->Integer",
				"factorial:Integer->Integer"
				);
		// TODO Auto-generated constructor stub
	}

}
