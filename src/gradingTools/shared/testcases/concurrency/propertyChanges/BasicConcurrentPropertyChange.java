package gradingTools.shared.testcases.concurrency.propertyChanges;

import java.beans.PropertyChangeEvent;

public class BasicConcurrentPropertyChange extends BasicConcurrentEvent<PropertyChangeEvent> 
			implements ConcurrentPropertyChange{
	
	public BasicConcurrentPropertyChange (
			long aStartTime,
			int aSequenceNumber, 
			PropertyChangeEvent anEvent) {
		super(aStartTime, aSequenceNumber, anEvent);
		
				
	}
	public BasicConcurrentPropertyChange (
			ConcurrentEvent<PropertyChangeEvent> anOriginalEvent) {
		super(anOriginalEvent);
				
	}
	@Override
	public String toString() {
		return getRelativeTime() + ":" +
			"[" +
			getSequenceNumber() + "," +
			getThread().getName() + "," +
			event.getSource() + "," +
			event.getPropertyName() + "," +
			event.getOldValue() + "," +
			event.getNewValue() +
			"]"
			;
			
			
	}
	
	
}
