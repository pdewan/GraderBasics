package gradingTools.basics.sharedTestCase.checkstyle.predefined;

import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleInterfaceDefinedTestCase;
import util.annotations.MaxValue;
@MaxValue(2)
public class ClassISARunnable extends CheckstyleInterfaceDefinedTestCase {

	

	public ClassISARunnable(String aClassName) {
		super(aClassName, "java.lang.Runnable");
		// TODO Auto-generated constructor stub
	}
	

}
