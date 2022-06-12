package grader.basics.trace.output;

import java.beans.PropertyChangeListener;

import gradingTools.shared.testcases.concurrency.propertyChanges.Selector;
import util.models.PropertyListenerRegisterer;

public interface ObservablePrintStream extends PropertyListenerRegisterer{
	void addPositiveSelector(Selector aSelector);
	void addNegativeSelector(Selector aSelector);
	void registerPropertyChangeConverter(ObjectToPropertyChange aConverter);
	void removePropertyChangeListener(PropertyChangeListener aListener);
	void setRedirectionFrozen(boolean newVal);

}
