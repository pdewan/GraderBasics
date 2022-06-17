package grader.basics.output.observer;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObservablePrintStreamUtility {
	
	static String sourceSuffix = BasicObjectToPropertyChange.SOURCE_SUFFIX;
	static String propertySuffix = BasicObjectToPropertyChange.PROPERTY_SUFFIX; ;
	
	
	public static String getSourceSuffix() {
		return sourceSuffix;
	}

	public static void setSourceSuffix(String sourceSuffix) {
		ObservablePrintStreamUtility.sourceSuffix = sourceSuffix;
	}

	public static String getPropertySuffix() {
		return propertySuffix;
	}

	public static void setPropertySuffix(String propertySuffix) {
		ObservablePrintStreamUtility.propertySuffix = propertySuffix;
	}

	
	public static PropertyChangeEvent toDefaultPropertyChange(Object anObject, String anAnonymousSource) {
		return new PropertyChangeEvent(anAnonymousSource, anObject.getClass().toString(), null, anObject);
	}
	
	public static String toPropertyChangeRegex (String[] aThreeTuple, String aSourceSuffix, String aPropertySuffix) {
		return toPropertyChangeRegex(aThreeTuple[0], aThreeTuple[1], aThreeTuple[2], aSourceSuffix, aPropertySuffix);
	}
	
	public static String toCompletePropertyChangeRegex (String[] aThreeTuple) {
		return toPropertyChangeRegex(aThreeTuple[0], aThreeTuple[1], aThreeTuple[2], sourceSuffix, propertySuffix);
	}
	
	 public static String toPerTreadPropertyChangeRegex (String[] aTwoTuple) {
	    	
			return toPropertyChangeRegex(aTwoTuple[0], aTwoTuple[1], propertySuffix); 
		}
	
    public static String toPerTreadPropertyChangeRegex (String[] aTwoTuple, String aPropertySuffix) {
    	
		return toPropertyChangeRegex(aTwoTuple[0], aTwoTuple[1], aPropertySuffix); 
	}
    
    public static String toRegex (String anOriginal) {
    	if (anOriginal == null) {
			return ".*";
		} else {
			return ".*" + anOriginal + ".*";
		}
    }
    public static String toPropertyChangeRegex (String aPropertyName, String aPropertyValue, String aPropertySuffix) {
		return toRegex(aPropertyName) + aPropertySuffix + toRegex(aPropertyValue);		
	}
    public static String toPropertyChangeRegex (String aSourceName, String aPropertyName, String aPropertyValue, String aSourceSuffix, String aPropertySuffix) {
		return toRegex(aSourceName) + aSourceSuffix + toRegex(aPropertyName) + aPropertySuffix + toRegex(aPropertyValue);		
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
		
		
		return new PropertyChangeEvent(aSource, aProperty, null, aNewValue);
	}
	
	public static String[] parseStringArray(String anArrayToString) {
		String aValuesPart = anArrayToString.substring(1, anArrayToString.length() - 1);
		String[] retVal = aValuesPart.split(", ");
		return retVal;
	}
	
	public static List<String> toPropertyNames(String[][] aTuples) {
		List<String> retVal = new ArrayList();
		
		for (String[] aTuple:aTuples) {
			try {				
			String aPropertyName = aTuple[1];
			if (!retVal.contains(aPropertyName)) {
				retVal.add(aPropertyName);
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retVal;
	}
	
	
	public static void main (String[] args) {
		Object[] anArray = {1, 2, "345 ", 4.5};
		String anArrayToString = Arrays.toString(anArray);
		String[] aParsedArray = parseStringArray(anArrayToString);
	}

}
