package grader.basics.trace.output;

import gradingTools.shared.testcases.concurrency.propertyChanges.Selector;
import util.trace.Tracer;

public class BasicNegativeOutputSelector implements Selector{

	@Override
	public boolean selects(Object anObject) {
		String aString = anObject.toString();
		if (aString.startsWith("java.beans.PropertyChangeEvent") || Tracer.isTrace(aString)) {
			return true;
		}
		return false;
	}

}
