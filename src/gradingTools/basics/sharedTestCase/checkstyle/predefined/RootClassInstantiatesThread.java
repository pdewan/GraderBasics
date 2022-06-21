package gradingTools.basics.sharedTestCase.checkstyle.predefined;

import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleClassInstantiatedTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodCalledTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodDefinedTestCase;

public class RootClassInstantiatesThread extends CheckstyleClassInstantiatedTestCase {
	public RootClassInstantiatesThread(String aClassName) {
		super(aClassName, "Thread");
		// TODO Auto-generated constructor stub
	}
	public RootClassInstantiatesThread(String aClassName, String aMethod) {
		super(aClassName,  aMethod,  "Thread");
		// TODO Auto-generated constructor stub
	}


}
