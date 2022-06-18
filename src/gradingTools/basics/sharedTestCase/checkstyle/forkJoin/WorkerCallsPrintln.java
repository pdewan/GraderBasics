package gradingTools.basics.sharedTestCase.checkstyle.forkJoin;

import gradingTools.basics.sharedTestCase.checkstyle.predefined.ClassCallesPrintln;

public class WorkerCallsPrintln extends ClassCallesPrintln {

	public WorkerCallsPrintln() {
		//super(ConcurrentPrimesSuite.WORKER_CLASS);
		super(".*Worker");

	}

}
