package grader.basics.concurrency.propertyChanges;

public class ParameterizedPropertyChangeSelector implements Selector<ConcurrentPropertyChange> {
	Object[] parameters;	

	public ParameterizedPropertyChangeSelector(Integer aStartSequenceNumber, Integer aStopSequenceNumber,
			String aThreadSelector, String aSourceSelector, String aPropertySelector, String anOldValueSelector,
			String aNewValueSelector) {
		init(aStartSequenceNumber, aStopSequenceNumber, aThreadSelector, aSourceSelector, aPropertySelector,
				anOldValueSelector, aNewValueSelector);
	}
	
	public ParameterizedPropertyChangeSelector(Object[] aParameters) {
		parameters = aParameters;
//		if (aParameters.length != 6) {
//			System.out.println(Arrays.toString(aParameters) + "should have six elements");
//			return;
//		}
//		if (aParameters[0] != null) {
//			startSequenceNumber = (Integer) aParameters[0];
//		}
//		if (aParameters[1] != null) {
//
//			stopSequenceNumber = (Integer) aParameters[1];
//		}
//		threadSelector = (String) aParameters[2];
//		sourceSelector = (String) aParameters[3];
//		oldValueSelector = (String) aParameters[4];
//		newValueSelector = (String) aParameters[5];

	}

	protected void init(Integer aStartSequenceNumber, Integer aStopSequenceNumber, String aThreadSelector,
			String aSourceSelector, String aPropertySelector, String anOldValueSelector, String aNewValueSelector) {
//		startSequenceNumber = aStartSequenceNumber;
//		stopSequenceNumber = aStopSequenceNumber;
//		threadSelector = aThreadSelector;
//		sourceSelector = aSourceSelector;
//		oldValueSelector = anOldValueSelector;
//		newValueSelector = aNewValueSelector;
		parameters = new Object[] {
				aStartSequenceNumber,
				aStopSequenceNumber,
				aThreadSelector,
				aSourceSelector,
				aPropertySelector,
				anOldValueSelector,
				anOldValueSelector
				};
		
				
		
	}

	@Override
	public boolean selects(ConcurrentPropertyChange aConcurrentPropertyChange) {
//		return (startSequenceNumber == null || aConcurrentOrderedEvent.getSequenceNumber() >= startSequenceNumber)
//				&& (stopSequenceNumber == null || aConcurrentOrderedEvent.getSequenceNumber() <= stopSequenceNumber)
//				&& (threadSelector == null || aConcurrentOrderedEvent.getThread().getName().matches(threadSelector))
//				&& (sourceSelector == null
//						|| aConcurrentOrderedEvent.getEvent().getSource().toString().matches(sourceSelector))
//				&& (oldValueSelector == null
//						|| aConcurrentOrderedEvent.getEvent().getOldValue().toString().matches(oldValueSelector))
//				&& (newValueSelector == null
//						|| aConcurrentOrderedEvent.getEvent().getNewValue().toString().matches(oldValueSelector));
		return ConcurrentEventUtility.matches(aConcurrentPropertyChange, parameters);
	}

	

}
