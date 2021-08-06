package gradingTools.shared.testcases.concurrency.propertyChanges;

import java.util.List;
import java.util.Set;

public interface ConcurrentEventSupport<EventType, ObservableType>  {
	ConcurrentEvent<EventType>[] getConcurrentEvents();
	void addIgnoreEventSelector(Selector<EventType> aSelector);
	void removeIgnoreEventSelector(Selector<EventType> aSelector);	
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
	Set<Thread> getThreads();
}
