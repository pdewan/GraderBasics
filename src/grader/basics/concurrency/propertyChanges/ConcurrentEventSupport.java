package grader.basics.concurrency.propertyChanges;

import java.util.Set;

public interface ConcurrentEventSupport<EventType, ObservableType>  {
	ConcurrentEvent<EventType>[] getConcurrentEvents();
	void addIgnoreEventSelector(Selector<EventType> aSelector);
	void removeIgnoreEventSelector(Selector<EventType> aSelector);	
	void setMinimumEventDelayPerThread(long newVal);
	long getMinimumEventDelayPerThread();	
	Selector<EventType>[] getSelectors();
	void addObservable(ObservableType anObservable);
	void removeObservable(ObservableType anObservable);
	ObservableType[] getObservables();
	void resetConcurrentEvents();
	int getNextSequenceNumber();
	ConcurrentEvent<EventType>  getLastEvent();
	void wait (long aTimeOut, Selector<ConcurrentEventSupport<EventType, ObservableType>> aSelector);

	int size();
	long getResetTime();
	Thread[] getNotifyingThreads();
	boolean isEventsFrozen();
	void setEventsFrozen(boolean eventsFrozen);
	Set<Thread> getLateThreads();
	boolean isIgnorePreviousThreadEvents();
	void setIgnorePreviousThreadEvents(boolean ignorePreviousThreadEvents);
	boolean isWaitSelectorSuccessful();
	Thread[] getNotifyingNewThreads();
	void timeOutBasedWait(long aTimeOut);
//	int getNumberOfCreatedThreads();
	void resetConcurrentEvents(long aResetTime);
	Set<Thread> getAllKnownThreads();
	void setGiveEventsFrozenWarning(boolean s);
}
