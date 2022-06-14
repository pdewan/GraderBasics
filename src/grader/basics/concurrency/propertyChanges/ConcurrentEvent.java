package grader.basics.concurrency.propertyChanges;

import java.beans.PropertyChangeEvent;

public interface ConcurrentEvent<EventType> {
	 long getTime();
	 long getRelativeTime();
	 long getStartTime();
	 EventType getEvent();
	 int getSequenceNumber();
	 Thread getThread();
	 String toMatchableText();
	

}
