package gradingTools.basics.sharedTestCase.checkstyle.forkJoin;

import gradingTools.basics.sharedTestCase.checkstyle.predefined.ClassISARunnable;
import util.annotations.MaxValue;
@MaxValue(2)
public class WorkerISARunnable extends ClassISARunnable {
	
	public WorkerISARunnable() {
//		super(ConcurrentPrimesSuite.WORKER_CLASS);
		super(".*Worker");

	}
	

}
