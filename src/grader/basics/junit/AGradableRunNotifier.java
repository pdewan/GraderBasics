package grader.basics.junit;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunNotifier;
// we do not really need this clsas
public class AGradableRunNotifier extends RunNotifier{
	@Override
	public void fireTestFinished(Description aDescription) {
		System.out.println (aDescription.getClassName());
		System.out.println (aDescription.getMethodName());		
		System.out.println (aDescription.getDisplayName());
		
		System.out.println (aDescription.getClass());
		
	}
	public void fireTestStarted(Description aDescription) {
		System.out.println (aDescription.getClassName());
		System.out.println (aDescription.getMethodName());		
		System.out.println (aDescription.getDisplayName());
		
		System.out.println (aDescription.getClass());

	}
	public void fireTestRunStarted(Description aDescription) {
		System.out.println (aDescription.getClassName());
		System.out.println (aDescription.getMethodName());		
		System.out.println (aDescription.getDisplayName());
		
		System.out.println (aDescription.getClass());
	}
	public void fireTestRunFinished(Result aResult) {
		super.fireTestRunFinished(aResult);
	}

}
