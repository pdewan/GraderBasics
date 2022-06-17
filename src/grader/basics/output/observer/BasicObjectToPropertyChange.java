package grader.basics.output.observer;

import java.beans.PropertyChangeEvent;

public class BasicObjectToPropertyChange implements ObjectToPropertyChange {
	public static final String ANONYMOUS_SOURCE = "Program";
	public static final String ANONYMOUS_PROPERTY = "OutputLine";
	public static final String SOURCE_SUFFIX = "->";
	public static final String PROPERTY_SUFFIX = ":";
	
	public BasicObjectToPropertyChange() {
		ObservablePrintStreamUtility.setSourceSuffix(sourceSuffix());
		ObservablePrintStreamUtility.setPropertySuffix(propertySuffix());

	}

	protected String anonymousSource() {
		return ANONYMOUS_SOURCE;
	}
	
	protected String sourceSuffix() {
		return SOURCE_SUFFIX;
	}
	
	protected String propertySuffix() {
		return PROPERTY_SUFFIX;
	}
	

	@Override
	public PropertyChangeEvent toPropertyChange(Object anObject) {
		return ObservablePrintStreamUtility.toPropertyChange(anObject, sourceSuffix(), propertySuffix(), anonymousSource());
	}
	
	

}
