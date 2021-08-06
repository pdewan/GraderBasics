package gradingTools.shared.testcases.concurrency.propertyChanges;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import util.models.PropertyListenerRegisterer;

public interface ConcurrentPropertyChangeSupport extends PropertyChangeListener, ConcurrentEventSupport<PropertyChangeEvent, PropertyListenerRegisterer> {

	ConcurrentPropertyChange[] getConcurrentPropertyChanges();

	ConcurrentPropertyChange getLastConcurrentPropertyChange();
	void wait (Selector<ConcurrentPropertyChangeSupport> aSelector, long aTimeOut);


}
