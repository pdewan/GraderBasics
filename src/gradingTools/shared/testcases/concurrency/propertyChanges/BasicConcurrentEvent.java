package gradingTools.shared.testcases.concurrency.propertyChanges;

import java.beans.PropertyChangeEvent;

public class BasicConcurrentEvent<EventType> implements ConcurrentEvent<EventType> {
	int sequenceNumber; 
	Thread thread;
	EventType event;
	long time;
	long relativeTime;
	long startTime;
	
	public BasicConcurrentEvent (
			long aStartTime,
			int aSequenceNumber, 
			EventType anEvent) {
		sequenceNumber = aSequenceNumber;
		thread = Thread.currentThread();
		event = anEvent;
		time = System.currentTimeMillis();
		relativeTime = time -aStartTime;
		startTime = aStartTime;
				
	}
	
	public BasicConcurrentEvent (
			ConcurrentEvent<EventType> anOriginalEvent) {
		sequenceNumber = anOriginalEvent.getSequenceNumber();
		thread = anOriginalEvent.getThread();
		event = anOriginalEvent.getEvent();
		time = anOriginalEvent.getTime();
		startTime = anOriginalEvent.getStartTime();
		relativeTime = anOriginalEvent.getRelativeTime();
				
	}
	
	@Override
	public EventType getEvent() {
		return event;
	}

	@Override
	public int getSequenceNumber() {
		return sequenceNumber;
	}

	@Override
	public Thread getThread() {
		return thread;
	}

	@Override
	public long getTime() {
		return time;
	}

	@Override
	public long getRelativeTime() {
		return relativeTime;
	}

	@Override
	public long getStartTime() {
		return startTime;
	}
	public String toString() {
		return getRelativeTime() + ", " +
		
			getSequenceNumber() + ", " +
			getThread().getName() + "," +
			event;
			
			
	}
	@Override
	public String toMatchableText() {
		// TODO Auto-generated method stub
		return ":" + getThread().getName() + "," +
		event + ":";
	}
}
