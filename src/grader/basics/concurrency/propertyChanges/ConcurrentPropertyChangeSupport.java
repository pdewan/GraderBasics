package grader.basics.concurrency.propertyChanges;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import util.models.PropertyListenerRegisterer;

public interface ConcurrentPropertyChangeSupport extends PropertyChangeListener, ConcurrentEventSupport<PropertyChangeEvent, PropertyListenerRegisterer> {
	ConcurrentPropertyChange[] getConcurrentPropertyChanges();
	ConcurrentPropertyChange getLastConcurrentPropertyChange();
	void addtWaitSelector(Selector<ConcurrentPropertyChangeSupport> aSelector);
	Selector<ConcurrentPropertyChangeSupport> getWaitSelector();
	void selectorBasedWait (long aTimeOut);
	List<Object> getNotifyingSources();
	List<String> getNotifiedProperties();
	List<Object> getNotifiedNewValues();
}
