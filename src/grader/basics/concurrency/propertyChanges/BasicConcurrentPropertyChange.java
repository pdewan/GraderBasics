package grader.basics.concurrency.propertyChanges;

import java.beans.PropertyChangeEvent;

public class BasicConcurrentPropertyChange extends BasicConcurrentEvent<PropertyChangeEvent> 
			implements ConcurrentPropertyChange{
	
	public BasicConcurrentPropertyChange (
			long aStartTime,
			int aSequenceNumber, 
			long aTime,
			Thread aThread,			
			PropertyChangeEvent anEvent) {
		super(aStartTime, aSequenceNumber, aTime, aThread,  anEvent);
		
				
	}
	public BasicConcurrentPropertyChange (
			
			PropertyChangeEvent anEvent) {
		super(-1, -1, System.currentTimeMillis(),Thread.currentThread(), anEvent);
		
				
	}
	public BasicConcurrentPropertyChange(Object[] aSpecification) {
		this(toPropertyChangeEvent(aSpecification));
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
	
	public static PropertyChangeEvent toPropertyChangeEvent(Object[] aSpecification) {
		if (aSpecification.length != 5) {
			System.out.println("Illegal concurrent event specification:" + aSpecification);
			return new PropertyChangeEvent(null, null, null, null);
		}
		return new PropertyChangeEvent(
				aSpecification[1], 
				aSpecification[2].toString(),
				aSpecification[3],
				aSpecification[4]);
	}
	
//	public static String toMatchableText(Object[] aSpecification) {
////		if (aSpecification.length() != )
//		// TODO Auto-generated method stub
//		return ":" + 
//		getThread().getName() + "," +
//		event.getSource() + "," +
//		event.getPropertyName() + "," +
//		event.getOldValue() + "," +
//		event.getNewValue() +
//		":";
//	}
//	public static String toMatchableText(Object[] 
	
	
}
