package grader.basics.concurrency.propertyChanges;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.trace.Tracer;

public class AbstractConcurrentEventSupport<EventType, ObservableType>
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
	protected Set<Thread> previousNotifyingThreads = new HashSet();
	protected Set<Thread> lateThreads = new HashSet();
	
	protected List<Thread> notifyingThreads = new ArrayList();
	protected List<Thread> notifyingNewThreads = new ArrayList();

	protected Set<Thread> allKnownThreads = new HashSet();
	protected Set<Thread> threadsAtReset;

	protected long minimumEventDelayPerThread = 0;
	protected Map<Thread, Long> threadToLastEventTimes = new HashMap();
	protected int initialThreadCount;
	protected Thread lastThread;

	public AbstractConcurrentEventSupport() {		
		resetConcurrentEvents();
		creatingThread = Thread.currentThread();
//		initialThreadCount = getNumCurrentThreads();
	}
//	protected synchronized int getNumCurrentThreads() {
//		return Thread.activeCount();
//	}
	
	Thread currentThread() {
		if (ConcurrentEventUtility.getThreadsInDifferentProcess()) {
			return lastThread;
		}
		return Thread.currentThread();
	}
	
	@Override
	public synchronized Set<Thread> getAllKnownThreads() {
		return allKnownThreads;
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
	public boolean isWaitSelectorSuccessful() {
		return waitSelectorSuccessful;
	}
	@Override
	public synchronized void resetConcurrentEvents() {
		resetConcurrentEvents(System.currentTimeMillis());
////		System.out.println("RESET OCCURRED");
//		Tracer.info(this, "reset concurrent events: notifying threads, notifying new threads, wait selectot successfil, lateThreads");
//		
//		allKnownThreads.addAll(ConcurrentEventUtility.getCurrentThreads());
//		threadsAtReset = new HashSet( Thread.getAllStackTraces().keySet());
//		concurrentEvents.clear();
////		System.out.println("Previous notifying threads:	" + notifyingThreads);
//
//		previousNotifyingThreads.addAll(notifyingThreads);
//		allKnownThreads.addAll(notifyingThreads);
//		Tracer.info(this, "All known threads after reset:" + allKnownThreads);
//		Tracer.info(this, "Previous notifying threads after reset:" + previousNotifyingThreads);
////		System.out.println(" THreads at reset " + threadsAtReset);
//		notifyingThreads.clear();
//		notifyingNewThreads.clear();
//		resetTime = System.currentTimeMillis();
//		setEventsFrozen(false);
//		waitSelectorSuccessful = false;
//		lateThreads.clear();
//		threadToLastEventTimes.clear();

	}
	@Override
	public synchronized void resetConcurrentEvents(long aResetTime) {
//		System.out.println("RESET OCCURRED");
		Tracer.info(this, "reset concurrent events: notifying threads, notifying new threads, wait selectot successfil, lateThreads");
		
		allKnownThreads.addAll(ConcurrentEventUtility.getCurrentThreads());
		threadsAtReset = new HashSet( Thread.getAllStackTraces().keySet());
		concurrentEvents.clear();
//		System.out.println("Previous notifying threads:	" + notifyingThreads);

		previousNotifyingThreads.addAll(notifyingThreads);
		allKnownThreads.addAll(notifyingThreads);
		Tracer.info(this, "All known threads after reset:" + allKnownThreads);
		Tracer.info(this, "Previous notifying threads after reset:" + previousNotifyingThreads);
//		System.out.println(" THreads at reset " + threadsAtReset);
		notifyingThreads.clear();
		notifyingNewThreads.clear();
//		resetTime = System.currentTimeMillis();
		resetTime = aResetTime;
		setEventsFrozen(false);
		waitSelectorSuccessful = false;
		lateThreads.clear();
		threadToLastEventTimes.clear();

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
	protected <EventType> boolean checkEventSeparation(Thread aThread, ConcurrentEvent<EventType> aNewEvent) {
		if (minimumEventDelayPerThread == 0) {
			return true; // short circuit
		}
		long aNewEventTime = aNewEvent.getRelativeTime();
		Long lastEventTime = threadToLastEventTimes.get(aThread);
		if (lastEventTime == null) return true;
		long anActualSeparation = aNewEventTime - lastEventTime;
		boolean retVal = (lastEventTime == null  
				|| anActualSeparation >= minimumEventDelayPerThread);
		if (retVal) {
			Tracer.info(this, "Event separation:" + anActualSeparation + " > min separation:" +minimumEventDelayPerThread );
		} else {
			Tracer.info(this, "Event separation:" + anActualSeparation + " < min separation:" +minimumEventDelayPerThread );

		}
		return retVal;
//		return (lastEventTime == null  
//				|| (aNewEventTime - lastEventTime) >= minimumEventDelayPerThread);
//			
	}
	private boolean giveWarning = true;
	protected boolean giveEventsFrozenWarningMessage() {
		return giveWarning;
	}
	@Override
	public void setGiveEventsFrozenWarning(boolean newVal) {
		Tracer.info(this, "Give frozen even warning:" + newVal);
		giveWarning = newVal;
	}
	protected void recordEventThread(EventType anEvent) {
		System.err.println("Event thread not recorded");
	}
	protected synchronized void addEvent(EventType anEvent) {
//		System.out.println("received event:" + anEvent);
		if (ConcurrentEventUtility.getThreadsInDifferentProcess()) {
			recordEventThread(anEvent);
		}
		if (isEventsFrozen()) {
			eventsReceivedWhenFrozen = true;			
//			String aMessage = "Warning:Ignored event:" + Thread.currentThread() + "->" + anEvent ;
			String aMessage = "Warning:Ignored event:" + currentThread() + "->" + anEvent ;

			if (giveEventsFrozenWarningMessage()) {				
		
			System.err.println(">>" + aMessage + "<<");
			
			Tracer.info(this, aMessage );
			}
			
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
		if (minimumEventDelayPerThread != 0) {
//			Thread aThread = Thread.currentThread();
			Thread aThread = currentThread();

			boolean anEventsSeparated = checkEventSeparation(aThread, aConcurrentOrderedEvent);
			threadToLastEventTimes.put(aThread, aConcurrentOrderedEvent.getRelativeTime());
			if (!anEventsSeparated) {
				return ;
			}
		}
		
		if (isIgnorePreviousThreadEvents()  && previousNotifyingThreads.contains(anEventThread)) {
			Tracer.info(this, "ignoring event from previous thread " + anEventThread);
			lateThreads.add(anEventThread);
			return;
		}
		if (!notifyingThreads.contains(anEventThread)) {
//			Tracer.info(this, "Notifying threads before addition:" + notifyingThreads);
			Tracer.info(this, "Added notifying thread " + anEventThread + " for event " + aConcurrentOrderedEvent );
//			notifyingThreads.add(aConcurrentOrderedEvent.getThread());
			notifyingThreads.add(anEventThread);
//			Tracer.info(this, "Notifying threads after addition:" + notifyingThreads);


			if (!allKnownThreads.contains(anEventThread)) {
				Tracer.info(this, "Added new notifying thread " + anEventThread);
				notifyingNewThreads.add(anEventThread);
			} else {
				String aMessage = "If threads created by previous testcases that should have terminatd did not terminate, this test may give erroneous result";
				Tracer.info(this, aMessage);
				System.err.println(aMessage);
//				Tracer.info(this, "all known threads:" + allKnownThreads);
//				Tracer.info(this, "threads at reset:" + threadsAtReset);

			}


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
		if (!isEventsFrozen()) {
			maybeNotify();
		}
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
		setEventsFrozen(true);
	}
	@Override
	public synchronized void timeOutBasedWait(long aTimeOut) {
		
			try {
				wait(aTimeOut);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		setEventsFrozen(true);
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
	public Thread[] getNotifyingThreads() {
		return notifyingThreads.toArray(emptyThreads);
	}
//	@Override
//	public int getNumberOfCreatedThreads() {
//		return getNumCurrentThreads() - initialThreadCount;
//	}
	@Override
	public Thread[] getNotifyingNewThreads() {
		return notifyingNewThreads.toArray(emptyThreads);
	}
	@Override
	public boolean isEventsFrozen() {
		return eventsFrozen;
	}
	@Override
	public void setEventsFrozen(boolean newValue) {
		Tracer.info(this, "Events frozen = " + newValue);
		if (newValue) {
			Tracer.info(this, "Until unfozen events will be ignored");
		} else {
			Tracer.info(this, "Events will now be processed");

		}
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
	@Override
	public void setMinimumEventDelayPerThread(long newVal) {
		minimumEventDelayPerThread = newVal;
	}
	@Override
	public long getMinimumEventDelayPerThread() {
		// TODO Auto-generated method stub
		return 0;
	}
}
