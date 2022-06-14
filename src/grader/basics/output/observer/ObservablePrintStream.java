package grader.basics.output.observer;

import java.beans.PropertyChangeListener;

import grader.basics.concurrency.propertyChanges.Selector;
import util.models.PropertyListenerRegisterer;

public interface ObservablePrintStream extends PropertyListenerRegisterer{
	void addPositiveSelector(Selector aSelector);
	void addNegativeSelector(Selector aSelector);
	void registerPropertyChangeConverter(ObjectToPropertyChange aConverter);
	void removePropertyChangeListener(PropertyChangeListener aListener);
	void setRedirectionFrozen(boolean newVal);

}
