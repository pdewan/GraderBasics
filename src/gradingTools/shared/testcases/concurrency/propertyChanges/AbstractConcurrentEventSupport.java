package gradingTools.shared.testcases.concurrency.propertyChanges;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sun.jmx.snmp.tasks.ThreadService;

public abstract class AbstractConcurrentEventSupport<EventType, ObservableType> implements ConcurrentEventSupport<EventType, ObservableType>{
    protected List<ConcurrentEvent<EventType>> concurrentEvents = new ArrayList();
    protected int nextSequenceNumber = 0;
    protected Selector<ConcurrentEventSupport<EventType, ObservableType>> selector;
    protected long resetTime;
    

    protected List<ObservableType> observables = new ArrayList();    
    protected List<Selector<EventType>> selectors = new ArrayList();
    protected Set<Thread> threads = new HashSet();
    public AbstractConcurrentEventSupport() {
    	 resetConcurrentEvents();
    }
	@Override
	public ConcurrentEvent<EventType>[] getConcurrentEvents() {
		return (ConcurrentEvent<EventType>[]) concurrentEvents.toArray();	
	}

	@Override
	public void addIgnoreEventSelector(Selector aSelector) {
		selectors.add(aSelector);		
	}

	@Override
	public void removeIgnoreEventSelector(Selector aSelector) {
		selectors.remove(aSelector);
	}
	
	@Override
	public Selector<EventType>[] getSelectors() {
		return (Selector<EventType>[]) selectors.toArray();
	}

	@Override
	public void addObservable(ObservableType anObservable) {
		observables.add(anObservable);		
	}

	@Override
	public void removeObservable(ObservableType anObservable) {
		observables.remove(anObservable);
	}

	@Override
	public ObservableType[] getObservables() {
		return (ObservableType[]) observables.toArray();
	}

	@Override
	public void resetConcurrentEvents() {
		concurrentEvents.clear();
		threads.clear();
		resetTime = System.currentTimeMillis();
		
	}
	
	protected boolean ignoreEvent(EventType anEvent) {
		for (Selector<EventType> aSelector:selectors) {
			if (aSelector.selects(anEvent)) {
				return true;
			}
		}
		return false;
	}
	
	protected void addEvent(EventType anEvent) {
		if (ignoreEvent(anEvent)) {
			return;
		}
		int aSequenceNumber = nextSequenceNumber;
		ConcurrentEvent<EventType> aConcurrentOrderedEvent =
				new BasicConcurrentEvent<EventType>(resetTime, aSequenceNumber, anEvent);
		threads.add(aConcurrentOrderedEvent.getThread());
		concurrentEvents.add(aConcurrentOrderedEvent);
		nextSequenceNumber++;	
	}
	public ConcurrentEvent<EventType> getLastEvent() {
		if (concurrentEvents.size() == 0) {
			return null;
		}
		return concurrentEvents.get(concurrentEvents.size() - 1);
	}

	protected void addEventAndMabeNotify(EventType anEvent) {
		addEvent(anEvent);
		maybeNotify();
	}
	protected synchronized void maybeNotify() {
		if (selector.selects(this)) {
			notify();
		}
	}
	@Override
	public int getNextSequenceNumber() {
			return nextSequenceNumber;
	}
	public synchronized void wait (long aTimeOut, 
			Selector<ConcurrentEventSupport<EventType, ObservableType>> aSelector) {
		selector = aSelector;
		while (!selector.selects(this)) {
			try {
				wait(aTimeOut);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public int size() {
		return concurrentEvents.size();
	}
	public long getResetTime() {
		return resetTime;
	}
	@Override
	public Set<Thread> getThreads() {
		return new HashSet(threads);
	}
}
