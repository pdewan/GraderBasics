package grader.basics.output.observer;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import valgrindpp.grader.ValgrindTrace;
// maybe this should go in the converter
public class ValgrindRepetitionBasedPropertyChangeSelector implements PropertyOutputSelector{
	Map<String, ValgrindTrace> stringToLatestTrace = new HashMap();
	protected static final int MAX_REPETITIONS = 10;
	protected int maxRepetitions = MAX_REPETITIONS;
	public ValgrindRepetitionBasedPropertyChangeSelector(int aMaxRepetitions) {
		maxRepetitions = aMaxRepetitions;
	}
	public ValgrindRepetitionBasedPropertyChangeSelector() {
		this(MAX_REPETITIONS);
	}
	@Override
	public boolean selectOutput(Object anOutput, PropertyChangeEvent aPropertyChangeEvent) {
		if (aPropertyChangeEvent == null) {
			return false;
		}
		Object anOldValue = aPropertyChangeEvent.getOldValue();
		if (anOldValue == null || !(anOldValue instanceof ValgrindTrace)) {
			return false;
		}
		ValgrindTrace aValgrindTrace = (ValgrindTrace) anOldValue;
		String aTraceText = aValgrindTrace.toStringWithoutTimestamp();
//		System.out.println("Trace text" + aTraceText);
		ValgrindTrace aPreviousTrace = stringToLatestTrace.get(aTraceText);
		if (aPreviousTrace == null) {
			stringToLatestTrace.put(aTraceText, aValgrindTrace);
			return true;
		}
		if (aPreviousTrace.repetitons > maxRepetitions()) {
			aPreviousTrace.repetitons++;
			return false;			
		}
		aValgrindTrace.repetitons = aPreviousTrace.repetitons+ 1;
		stringToLatestTrace.put(aTraceText, aValgrindTrace);
		return true;
		
		
	}
	protected int maxRepetitions() {
		return MAX_REPETITIONS;
	}
	
	

}
