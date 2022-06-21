package gradingTools.basics.sharedTestCase.checkstyle.forkJoin;

import gradingTools.basics.sharedTestCase.checkstyle.predefined.ClassCallsPrintln;

public class WorkerCallsPrintln extends ClassCallsPrintln {

	public WorkerCallsPrintln() {
		//super(ConcurrentPrimesSuite.WORKER_CLASS);
		super(".*Worker");

	}

}
