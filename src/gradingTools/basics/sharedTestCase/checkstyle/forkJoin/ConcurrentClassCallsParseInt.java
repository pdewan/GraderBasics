package gradingTools.basics.sharedTestCase.checkstyle.forkJoin;

import gradingTools.basics.sharedTestCase.checkstyle.predefined.ClassCallsParseInt;

public class ConcurrentClassCallsParseInt extends ClassCallsParseInt {

	public ConcurrentClassCallsParseInt() {
//		super(ConcurrentPrimesSuite.ROOT_CLASS);
		super("Concurrent.*");

	}

}
