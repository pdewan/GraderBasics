package gradingTools.basics.sharedTestCase.checkstyle.predefined;

import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodCalledTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodDefinedTestCase;

public class RootClassCallsThreadJoin extends CheckstyleMethodCalledTestCase {
//	[INFO] D:\dewan_backup\Java\grail13\.\src\greeting\Cls.java:6: Expected signature main:String[]->void in type greeting.Cls:[@Comp301Tags.GREETING_MAIN]. Good! [ExpectedSignatures]
//	[WARN] D:\dewan_backup\Java\grail13\.\src\greeting\Cls.java:6: Missing signature main:String[]->void in type greeting.Cls:[@Comp301Tags.GREETING_MAIN]. [ExpectedSignatures]
	public static final String JOIN_CALL = "java.lang.Thread!join:->void";
	public RootClassCallsThreadJoin(String aClassName) {
		super(aClassName, JOIN_CALL);
	}
	
	public RootClassCallsThreadJoin(String aClassName, String aMethod) {
		super(aClassName, aMethod, JOIN_CALL);
	}
}
