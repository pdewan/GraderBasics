package byteman.tools.exampleTestCases.factorial;

import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodCalledTestCase;

public class FactorialIsRecursive extends CheckstyleMethodCalledTestCase {

	public FactorialIsRecursive() {
		super("@Factorial",
				"factorial:int->int",
				"factorial:int->int"
				);
		// TODO Auto-generated constructor stub
	}

}
