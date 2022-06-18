package gradingTools.basics.sharedTestCase.checkstyle.forkJoin;

import gradingTools.basics.sharedTestCase.checkstyle.predefined.RootClassCallsThreadStart;

public class ConcurrentClassCallsThreadStart extends RootClassCallsThreadStart {

	public ConcurrentClassCallsThreadStart() {
		super("Concurrent.*");
	}

}
