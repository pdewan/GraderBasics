package grader.basics.output.observer;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ObservablePrintStreamUtility {

	static String sourceSuffix = BasicObjectToPropertyChange.SOURCE_SUFFIX;
	static String propertySuffix = BasicObjectToPropertyChange.PROPERTY_SUFFIX;;

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

	public static String toPropertyChangeRegex(String[] aThreeTuple, String aSourceSuffix, String aPropertySuffix) {
		return toPropertyChangeRegex(aThreeTuple[0], aThreeTuple[1], aThreeTuple[2], aSourceSuffix, aPropertySuffix);
	}

	public static String toCompletePropertyChangeRegex(String[] aThreeTuple) {
		return toPropertyChangeRegex(aThreeTuple[0], aThreeTuple[1], aThreeTuple[2], sourceSuffix, propertySuffix);
	}

	public static String toPerTreadPropertyChangeRegex(String[] aTwoTuple) {

		return toPropertyChangeRegex(aTwoTuple[0], aTwoTuple[1], propertySuffix);
	}

	public static String toPerTreadPropertyChangeRegex(String[] aTwoTuple, String aPropertySuffix) {

		return toPropertyChangeRegex(aTwoTuple[0], aTwoTuple[1], aPropertySuffix);
	}

	public static String toRegex(String anOriginal) {
		if (anOriginal == null) {
			return ".*";
		} else {
			return ".*" + anOriginal + ".*";
		}
	}

	public static String toPropertyChangeRegex(String aPropertyName, String aPropertyValue, String aPropertySuffix) {
		return toRegex(aPropertyName) + aPropertySuffix + toRegex(aPropertyValue);
	}

	public static String toPropertyChangeRegex(String aSourceName, String aPropertyName, String aPropertyValue,
			String aSourceSuffix, String aPropertySuffix) {
//		return toRegex(aSourceName) + aSourceSuffix + toRegex(aPropertyName) + aPropertySuffix + toRegex(aPropertyValue);		
		return toRegex(aSourceName) + aSourceSuffix + aPropertyName + aPropertySuffix + toRegex(aPropertyValue);

	}

	public static PropertyChangeEvent toPropertyChange(Object anObject, String aSourceSuffix, String aPropertySuffix,
			String anAnonymousSource) {
		String aString = anObject.toString();

		try {
//		String aString = anObject.toString();
		int aSourceSuffixIndex = aString.indexOf(aSourceSuffix);
		int aPropertySuffixIndex = aString.indexOf(aPropertySuffix);
		if (!(anObject instanceof String) || aSourceSuffixIndex < 0 || aPropertySuffixIndex < 0) {
			return toDefaultPropertyChange(anObject, anAnonymousSource);
		}
		String aSource = aString.substring(0, aSourceSuffixIndex).trim();
		String aProperty = aString.substring(aSourceSuffixIndex + aSourceSuffix.length(), aPropertySuffixIndex).trim();
		String aNewValue = aString.substring(aPropertySuffixIndex + aPropertySuffix.length(), aString.length());

		return new PropertyChangeEvent(aSource, aProperty, null, aNewValue);
		} catch (Exception e) {
			return toDefaultPropertyChange(anObject, anAnonymousSource);
//			e.printStackTrace();
//			return new PropertyChangeEvent(aString, aSourceSuffix, aPropertySuffix, e.getMessage()); 
		}
	}

	public static Object toTypedObject(String aName, String aValueString, Class aValueClass) {
		Class aClass = aValueClass;
		try {
			if (aClass == Integer.class || aClass == Number.class) {
				return Integer.parseInt(aValueString);
			}
			if (aClass == Double.class) {
				return Double.parseDouble(aValueString);
			}
			if (aClass == Boolean.class) {
				return Boolean.parseBoolean(aValueString);
			}
			if (aClass == Array.class || aClass == Arrays.class) {
				String[] anArray = parseStringArray(aValueString);
				if (aName.contains("Number") || aName.contains("Integer")) {
					Object[] aRetVal = new Object[anArray.length];
					try {
						for (int index = 0; index < anArray.length; index++) {
							String aString = anArray[index];
							aRetVal[index] = Integer.parseInt(aString);
						}
						return aRetVal;
					} catch (Exception e) {
						e.printStackTrace();
						return anArray;
					}
				}
			}
			if (aClass == List.class || aClass == ArrayList.class) {
				List<String> aList = parseStringList(aValueString);
				if (aName.contains("Number") || aName.contains("Integer")) {
					List aRetVal = new ArrayList(aList.size());
					if (aList.size() == 0) {
						return aRetVal;
					}
					try {
						for (int index = 0; index < aList.size(); index++) {
							String aString = aList.get(index);
							aRetVal.add(Integer.parseInt(aString));
						}
						return aRetVal;
					} catch (Exception e) {
						e.printStackTrace();
						return aList;
					}
				}

			}
			if (aClass == Set.class || aClass == HashSet.class) {
				Set<String> aStringSet = parseStringSet(aValueString);
				if (aName.contains("Number") || aName.contains("Integer")) {
					Set aRetVal = new HashSet(aStringSet.size());
					if (aStringSet.size() == 0) {
						return aRetVal;
					}
					try {
						for (String aString : aStringSet) {
							aRetVal.add(Integer.parseInt(aString));
						}
						return aRetVal;
					} catch (Exception e) {
						e.printStackTrace();
						return aStringSet;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
//			return aValueString;
		}
		return aValueString;
	}

	public static String toTypeString(Object[] aNameAndType) {
		String aName = (String) aNameAndType[0];
		Class aClass = (Class) aNameAndType[1];
		if (aClass == Number.class || aClass == Integer.class || aClass == Long.class) {
			return "\\d.*";
		}
		if (aClass == Double.class) {
			return "\\d.*\\.\\d.*";
		}
		if (aClass == Boolean.class) {
			return "(true|false)";
		}
		if (aClass == Arrays.class || 
				aClass == Array.class || 
				aClass == List.class ||
				aClass == ArrayList.class ||
				aClass == Set.class ||
				aClass == HashSet.class) {
			if (aName.contains("Number") || aName.contains("Integer")) {
//				return "\\[\\d.*\\]";
				return "\\[.*\\]"; // may be empty
			}
			return "\\[.*\\]";
		}
		return ".*";

	}

	protected static String[] emptyStringArray = {};

	public static String[] parseStringArray(String anArrayToString) {
		if (anArrayToString.length() <= 2) {
//		if (!anArrayToString.contains(",")) {
			return emptyStringArray;
		}
		String aValuesPart = anArrayToString.substring(1, anArrayToString.length() - 1);
		String[] retVal = aValuesPart.split(", ");
		return retVal;
	}

	public static List<String> parseStringList(String aListToString) {
		String[] anArray = parseStringArray(aListToString);
		return Arrays.asList(anArray);
	}

	public static Set<String> parseStringSet(String aSetToString) {
		List<String> aList = parseStringList(aSetToString);
		return new HashSet(aList);
	}

	public static List<String> toPropertyNames(String[][] aTuples) {
		List<String> retVal = new ArrayList();

		for (String[] aTuple : aTuples) {
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

	public static void main(String[] args) {
		Object[] anArray = { 1, 2, "345 ", 4.5 };
		List<Object> aList = Arrays.asList(anArray);
		System.out.println(aList);
		String anArrayToString = Arrays.toString(anArray);
		Object[] aParsedArray = parseStringArray(anArrayToString);
	}

}
