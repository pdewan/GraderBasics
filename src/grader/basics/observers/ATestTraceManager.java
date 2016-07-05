package grader.basics.observers;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class ATestTraceManager extends RunListener {
	@Override
	public void testRunStarted(Description description) throws Exception {
			super.testRunStarted(description);
			
    }
	@Override
	public void testStarted(Description description) throws Exception {
		super.testStarted(description);

			
    }
	@Override
	public void testAssumptionFailure(Failure aFailure) {
		super.testAssumptionFailure(aFailure);

	}


	@Override
	public void testFailure(Failure aFailure) throws Exception {
	  
			super.testFailure(aFailure);
			
	}
	@Override
	public void testFinished(Description description) {
		try {
			super.testFinished(description);
			
//			System.out.println ("Test finished:"+ description);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		

	}
	public void testIgnored(Description description) throws Exception {
		super.testIgnored(description);

	}
}
