package grader.basics.concurrency.propertyChanges;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.models.PropertyListenerRegisterer;
import util.trace.Tracer;

public class BasicConcurrentPropertyChangeSupport
		extends AbstractConcurrentEventSupport<PropertyChangeEvent, PropertyListenerRegisterer>
		implements ConcurrentPropertyChangeSupport {
	List<Object> notifyingSources = new ArrayList();

	List<String> notifiedProperties =new ArrayList();
	List<Object> notifiedNewValues = new ArrayList();

	public synchronized void addObservable(PropertyListenerRegisterer anObservable) {
		super.addObservable(anObservable);
		anObservable.addPropertyChangeListener(this);
	}

	@Override
	public synchronized void propertyChange(PropertyChangeEvent anEvent) {
//		System.out.println("Received property change:" + anEvent);
		Object aSource = anEvent.getSource();
		addEventAndMabeNotify(anEvent);
	}

	@Override
	protected void addEventAndMabeNotify(PropertyChangeEvent anEvent) {
		super.addEventAndMabeNotify(anEvent);
		setAdd (notifyingSources, anEvent.getSource());
		setAdd (notifiedProperties, anEvent.getPropertyName());
		setAdd (notifiedNewValues, anEvent.getNewValue());
	}
	
	protected <T> void setAdd(List<T> aList, T anObject) {
		if (anObject == null  || aList.contains(anObject)) {
			return;
		}		
		aList.add(anObject);
	}

	@Override
	public synchronized void resetConcurrentEvents() {
		super.resetConcurrentEvents();
		if (notifyingSources != null) {
			notifyingSources.clear();
			notifiedProperties.clear();
			notifiedNewValues.clear();
		}
	}

	@Override
	public synchronized ConcurrentPropertyChange[] getConcurrentPropertyChanges() {
		return ConcurrentEventUtility.toConcurrentPropertyChanges(concurrentEvents);
	}

	@Override
	public synchronized ConcurrentPropertyChange getLastConcurrentPropertyChange() {
		ConcurrentEvent<PropertyChangeEvent> aLastEvent = super.getLastEvent();
		if (aLastEvent == null) {
			return null;
		}
		return new BasicConcurrentPropertyChange(aLastEvent);
	}
//@Override	
//protected void recordEventThread(PropertyChangeEvent anEvent) {
//		Object anOldValue = anEvent.getOldValue();
//		if (anOldValue != null && anOldValue instanceof Thread) {
//			lastThread = (Thread) anOldValue;
//		}
//}

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
	protected boolean giveEventsFrozenWarningMessage() {
		return !waitSelectorSuccessful && super.giveEventsFrozenWarningMessage();
	}

	@Override
	public synchronized void selectorBasedWait(long aTimeOut) {
		if (!waitSelectorSuccessful) {
			try {
				Tracer.info(this, "Selector waiting for notofying condition");

//				System.out.println("Waiting for max time:" + aTimeOut);
				wait(aTimeOut);
				if (!waitSelectorSuccessful) {
					String aMessage = "Warning: Selector timed out after ms:" + aTimeOut
							+ " notifying condition did not occur";
					System.err.println(">>" + aMessage + "<<");
					Tracer.info(this, aMessage);
				}
//				System.out.println("Finished Waiting for time:" + aTimeOut);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (waitSelectorSuccessful) {
			String aMessage = "Selector based wait finished successfully, it is ok if some later events are ignored";
			Tracer.info(this, aMessage);

		}
		setEventsFrozen(true);
	}
	@Override
	public List<Object> getNotifyingSources() {
		return notifyingSources;
	}
	@Override
	public List<String> getNotifiedProperties() {
		return notifiedProperties;
	}
	@Override
	public List<Object> getNotifiedNewValues() {
		return notifiedNewValues;
	}
}
