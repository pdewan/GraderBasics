package grader.basics.trace.output;

import gradingTools.shared.testcases.concurrency.propertyChanges.Selector;

public class BasicNegativeOutputSelector implements Selector{

	@Override
	public boolean selects(Object anObject) {
		String aString = anObject.toString();
		if (aString.startsWith("java.beans.PropertyChangeEvent")) {
			return true;
		}
		return false;
	}

}
