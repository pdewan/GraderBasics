package gradingTools.basics.sharedTestCase.checkstyle.forkJoin;

import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleClassInstantiatedTestCase;

public class ConcurrentClassInstantiatesWorker extends CheckstyleClassInstantiatedTestCase {
	public ConcurrentClassInstantiatesWorker() {
//		super(ConcurrentPrimesSuite.ROOT_CLASS, "default." +ConcurrentPrimesSuite.WORKER_CLASS);
		super("Concurrent.*", "default." +".*Worker");

	}

}
