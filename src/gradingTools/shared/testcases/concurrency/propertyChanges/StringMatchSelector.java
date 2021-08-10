package gradingTools.shared.testcases.concurrency.propertyChanges;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;

public class StringMatchSelector implements Selector {
//	Integer startSequenceNumber;
//	Integer stopSequenceNumber;
//	String threadSelector;
//	String sourceSelector;
//	String propertySelector;
//	String oldValueSelector;
//	String newValueSelector;
	String pattern;
	

	public StringMatchSelector(String aPattern) {
		pattern = aPattern;
	}
	
	
	@Override
	public boolean selects(Object anObject) {
		return anObject != null && anObject.toString().matches(pattern);
	}

	

}
