package gradingTools.shared.testcases.concurrency.propertyChanges;

import java.beans.PropertyChangeEvent;

import util.models.PropertyListenerRegisterer;

public class BasicConcurrentPropertyChangeSupport 
	extends AbstractConcurrentEventSupport<PropertyChangeEvent, PropertyListenerRegisterer>
	implements ConcurrentPropertyChangeSupport
{
	public void addObservable(PropertyListenerRegisterer anObservable) {
		super.addObservable(anObservable);	
		anObservable.addPropertyChangeListener(this);
	}
	@Override
	public synchronized void propertyChange(PropertyChangeEvent anEvent) {
//		System.out.println("Received property change:" + anEvent);
		addEvent(anEvent);
	}
	@Override
	public ConcurrentPropertyChange[] getConcurrentPropertyChanges() {
		return ConcurrentEventUtility.toConcurrentPropertyChanges (concurrentEvents);	
	}
	@Override
	public ConcurrentPropertyChange getLastConcurrentPropertyChange () {
		ConcurrentEvent<PropertyChangeEvent> aLastEvent =  super.getLastEvent();
		return new BasicConcurrentPropertyChange(aLastEvent);
	}
	protected Selector<ConcurrentPropertyChangeSupport> propertyChangeSelector;
	public synchronized void wait (Selector<ConcurrentPropertyChangeSupport> aSelector, 
			long aTimeOut) {
		propertyChangeSelector = aSelector;
		while (!propertyChangeSelector.selects(this)) {
			try {
				wait(aTimeOut);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	protected synchronized void maybeNotify() {
		if (propertyChangeSelector.selects(this)) {
			notify();
		}
	}

}
