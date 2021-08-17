package gradingTools.shared.testcases.concurrency.propertyChanges;

import java.beans.PropertyChangeEvent;

import util.models.PropertyListenerRegisterer;
import util.trace.Tracer;

public class BasicConcurrentPropertyChangeSupport 
	extends AbstractConcurrentEventSupport<PropertyChangeEvent, PropertyListenerRegisterer>
	implements ConcurrentPropertyChangeSupport
{
	public synchronized void addObservable(PropertyListenerRegisterer anObservable) {
		super.addObservable(anObservable);	
		anObservable.addPropertyChangeListener(this);
	}
	@Override
	public synchronized void propertyChange(PropertyChangeEvent anEvent) {
//		System.out.println("Received property change:" + anEvent);
		addEventAndMabeNotify(anEvent);
	}
	@Override
	public synchronized ConcurrentPropertyChange[] getConcurrentPropertyChanges() {
		return ConcurrentEventUtility.toConcurrentPropertyChanges (concurrentEvents);	
	}
	@Override
	public synchronized ConcurrentPropertyChange getLastConcurrentPropertyChange () {
		ConcurrentEvent<PropertyChangeEvent> aLastEvent =  super.getLastEvent();
		if (aLastEvent == null) {
			return null;
		}
		return new BasicConcurrentPropertyChange(aLastEvent);
	}
	

	protected Selector<ConcurrentPropertyChangeSupport> propertyChangeSelector;
//	public synchronized void wait (Selector<ConcurrentPropertyChangeSupport> aSelector, 
//			long aTimeOut) {
//		System.out.println("property change selector:" + aSelector);
//		propertyChangeSelector = aSelector;
//		if (!waitSelectorSuccessful) {
//			try {
//				System.out.println("Waiting for time:" + aTimeOut);
//				wait(aTimeOut);
//				System.out.println("Finished Waiting for time:" + aTimeOut);
//
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	protected synchronized void maybeNotify() {
		if (propertyChangeSelector != null && propertyChangeSelector.selects(this)) {
			waitSelectorSuccessful = true;
			notify();
//			propertyChangeSelector = null;
		}
	}
	@Override
	public synchronized void addtWaitSelector(Selector<ConcurrentPropertyChangeSupport> aSelector) {
		propertyChangeSelector = aSelector;
		
	}
	@Override
	public Selector<ConcurrentPropertyChangeSupport> getWaitSelector() {
		return propertyChangeSelector;
	}
	@Override
	public synchronized void selectorBasedWait( long aTimeOut) {
		if (!waitSelectorSuccessful) {
			try {
//				System.out.println("Waiting for max time:" + aTimeOut);
				wait(aTimeOut);
				if (!waitSelectorSuccessful) {
					Tracer.info(this, "Timed out after ms:" + aTimeOut);
				}
//				System.out.println("Finished Waiting for time:" + aTimeOut);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		setEventsFrozen(true);
	}

}
