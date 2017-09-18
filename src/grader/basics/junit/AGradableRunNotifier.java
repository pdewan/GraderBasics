package grader.basics.junit;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunNotifier;

import util.trace.Tracer;
// we do not really need this clsas
public class AGradableRunNotifier extends RunNotifier{
	@Override
	public void fireTestFinished(Description aDescription) {
		Tracer.info(this, aDescription.getClassName());
		Tracer.info(this, aDescription.getMethodName());		
		Tracer.info(this, aDescription.getDisplayName());
		
		Tracer.info(this, aDescription.getClass().toString());
		
	}
	public void fireTestStarted(Description aDescription) {
		Tracer.info(this, aDescription.getClassName());
		Tracer.info(this, aDescription.getMethodName());		
		Tracer.info(this, aDescription.getDisplayName());
		
		Tracer.info(this, aDescription.getClass().toString());

	}
	public void fireTestRunStarted(Description aDescription) {
		Tracer.info(this, aDescription.getClassName());
		Tracer.info(this, aDescription.getMethodName());		
		Tracer.info(this, aDescription.getDisplayName());
		
		Tracer.info(this, aDescription.getClass().toString());
	}
	public void fireTestRunFinished(Result aResult) {
		super.fireTestRunFinished(aResult);
	}

}
