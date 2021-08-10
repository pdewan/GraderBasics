package gradingTools.shared.testcases.concurrency.propertyChanges;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sun.jmx.snmp.tasks.ThreadService;

import net.sf.saxon.expr.LastItemExpression;
import util.trace.Tracer;
import util.trace.WaitingForClearance;

public abstract class AbstractConcurrentEventSupport<EventType, ObservableType>
		implements ConcurrentEventSupport<EventType, ObservableType> {
	protected List<ConcurrentEvent<EventType>> concurrentEvents = new ArrayList();
	protected int nextSequenceNumber = 0;
	protected Selector<ConcurrentEventSupport<EventType, ObservableType>> selector;
	protected long resetTime;
	protected boolean eventsFrozen;
	protected boolean waitSelectorSuccessful = false;
	protected Thread creatingThread = null;
	protected boolean ignorePreviousThreadEvents = false;


	protected List<ObservableType> observables = new ArrayList();
	protected List<Selector<EventType>> selectors = new ArrayList();
	protected Set<Thread> previousThreads = new HashSet();
	protected Set<Thread> lateThreads = new HashSet();
	
	protected Set<Thread> threads = new HashSet();

	public AbstractConcurrentEventSupport() {
		resetConcurrentEvents();
		creatingThread = Thread.currentThread();
	}
	@Override
	public synchronized Set<Thread> getLateThreads() {
		return lateThreads;
	}

	@Override
	public synchronized ConcurrentEvent<EventType>[] getConcurrentEvents() {
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

//	ObservableType[] emptyObservableArray = {};
	@Override
	public ObservableType[] getObservables() {
		return (ObservableType[]) observables.toArray();
	}

	@Override
	public synchronized void resetConcurrentEvents() {
		concurrentEvents.clear();
		previousThreads.addAll(threads);
		threads.clear();
		resetTime = System.currentTimeMillis();
		setEventsFrozen(false);
		waitSelectorSuccessful = false;
		lateThreads.clear();

	}

	protected synchronized boolean ignoreEvent(EventType anEvent) {
		for (Selector<EventType> aSelector : selectors) {
			if (aSelector.selects(anEvent)) {
				return true;
			}
		}
		return false;
	}
	boolean eventsReceivedWhenFrozen = false;
	protected synchronized void addEvent(EventType anEvent) {
//		System.out.println("received event:" + anEvent);
		if (isEventsFrozen()) {
			eventsReceivedWhenFrozen = true;
			Tracer.info(this, "Event " + anEvent + "received when events were frozen");
			return;
		}
//		if (isEventsFrozen() || ignoreEvent(anEvent)) {
		if (ignoreEvent(anEvent)) {

			return;
		}
		int aSequenceNumber = nextSequenceNumber;
		ConcurrentEvent<EventType> aConcurrentOrderedEvent = new BasicConcurrentEvent<EventType>(resetTime,
				aSequenceNumber, anEvent);
		Thread anEventThread = aConcurrentOrderedEvent.getThread();
		if (isIgnorePreviousThreadEvents()  && previousThreads.contains(anEventThread)) {
			Tracer.info(this, "ignoring event from previous thread " + anEventThread);
			lateThreads.add(anEventThread);
			return;
		}
		if (!threads.contains(anEventThread)) {
			Tracer.info(this, " Added new thread " + anEventThread + " for event " + aConcurrentOrderedEvent );
			threads.add(aConcurrentOrderedEvent.getThread());


		}
//		threads.add(aConcurrentOrderedEvent.getThread());
//		System.out.println("added event:" + anEvent);

		concurrentEvents.add(aConcurrentOrderedEvent);
		nextSequenceNumber++;
	}

	public synchronized ConcurrentEvent<EventType> getLastEvent() {
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
		if (selector != null && selector.selects(this)) {
			notify();
			setEventsFrozen(true);
		}
	}

	@Override
	public int getNextSequenceNumber() {
		return nextSequenceNumber;
	}

	public synchronized void wait(long aTimeOut,
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

	static Thread[] emptyThreads = {};

	@Override
	public Thread[] getThreads() {
		return threads.toArray(emptyThreads);
	}
	@Override
	public boolean isEventsFrozen() {
		return eventsFrozen;
	}
	@Override
	public void setEventsFrozen(boolean newValue) {
		this.eventsFrozen = newValue;
	}
	@Override
	public boolean isIgnorePreviousThreadEvents() {
		return ignorePreviousThreadEvents;
	}
	@Override
	public void setIgnorePreviousThreadEvents(boolean ignorePreviousThreadEvents) {
		this.ignorePreviousThreadEvents = ignorePreviousThreadEvents;
	}
}
