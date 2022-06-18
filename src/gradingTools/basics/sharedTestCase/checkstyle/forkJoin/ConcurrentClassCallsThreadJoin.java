package gradingTools.basics.sharedTestCase.checkstyle.forkJoin;

import gradingTools.basics.sharedTestCase.checkstyle.predefined.RootClassCallsThreadJoin;

public class ConcurrentClassCallsThreadJoin extends RootClassCallsThreadJoin {

	public ConcurrentClassCallsThreadJoin() {
//		super(ConcurrentPrimesSuite.ROOT_CLASS);
		super("Concurrent.*");

	}
	

}
