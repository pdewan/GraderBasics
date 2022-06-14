package grader.basics.concurrency.propertyChanges;

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
