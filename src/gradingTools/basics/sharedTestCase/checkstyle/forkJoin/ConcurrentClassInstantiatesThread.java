package gradingTools.basics.sharedTestCase.checkstyle.forkJoin;

import gradingTools.basics.sharedTestCase.checkstyle.predefined.RootClassInstantiatesThread;

public class ConcurrentClassInstantiatesThread extends RootClassInstantiatesThread {
	public ConcurrentClassInstantiatesThread() {
//		super(ConcurrentPrimesSuite.ROOT_CLASS);
		super("Concurrent.*");

	}

}
