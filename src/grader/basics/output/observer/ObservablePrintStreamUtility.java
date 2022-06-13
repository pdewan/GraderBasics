package grader.basics.output.observer;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;

public class ObservablePrintStreamUtility {
	
	public static PropertyChangeEvent toDefaultPropertyChange(Object anObject, String anAnonymousSource) {
		return new PropertyChangeEvent(anAnonymousSource, anObject.getClass().toString(), null, anObject);
	}
	
	public static PropertyChangeEvent toPropertyChange(Object anObject, String aSourceSuffix, String aPropertySuffix, String anAnonymousSource) {
		
		String aString = anObject.toString();
		int aSourceSuffixIndex = aString.indexOf(aSourceSuffix);
		int aPropertySuffixIndex = aString.indexOf(aPropertySuffix);
		if (!(anObject instanceof String) || aSourceSuffixIndex < 0 || aPropertySuffixIndex < 0 ) {
			return toDefaultPropertyChange(anObject, anAnonymousSource);
		}
		String aSource = aString.substring(0, aSourceSuffixIndex).trim();
		String aProperty =  aString.substring(aSourceSuffixIndex + aSourceSuffix.length(), aPropertySuffixIndex).trim();
		String aNewValue = aString.substring(aPropertySuffixIndex + aPropertySuffix.length(), aString.length()) ;
		
		
		return new PropertyChangeEvent(aSource, aProperty, null, anObject);
	}
	
	public static String[] parseStringArray(String anArrayToString) {
		String aValuesPart = anArrayToString.substring(1, anArrayToString.length() - 1);
		String[] retVal = aValuesPart.split(", ");
		return retVal;
	}
	
	
	public static void main (String[] args) {
		Object[] anArray = {1, 2, "345 ", 4.5};
		String anArrayToString = Arrays.toString(anArray);
		String[] aParsedArray = parseStringArray(anArrayToString);
	}

}
