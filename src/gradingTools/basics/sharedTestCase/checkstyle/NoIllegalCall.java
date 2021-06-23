package gradingTools.basics.sharedTestCase.checkstyle;

import util.annotations.MaxValue;


	@MaxValue(1)
	public class NoIllegalCall extends CheckStyleIllegalCallTestCase {
//		[INFO] D:\dewan_backup\Java\grail13\.\src\greeting\Cls.java:6: Expected signature main:String[]->void in type greeting.Cls:[@Comp301Tags.GREETING_MAIN]. Good! [ExpectedSignatures]
//		[WARN] D:\dewan_backup\Java\grail13\.\src\greeting\Cls.java:6: Missing signature main:String[]->void in type greeting.Cls:[@Comp301Tags.GREETING_MAIN]. [ExpectedSignatures]
		public NoIllegalCall() {
			super(".*");
			// TODO Auto-generated constructor stub
		}
		
		
	
}
