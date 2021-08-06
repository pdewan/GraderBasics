package gradingTools.shared.testcases.concurrency.propertyChanges;

import java.beans.PropertyChangeEvent;

public interface ConcurrentEvent<EventType> {
	 long getTime();
	 long getRelativeTime();
	 long getStartTime();
	 EventType getEvent();
	 int getSequenceNumber();
	 Thread getThread();
	

}
