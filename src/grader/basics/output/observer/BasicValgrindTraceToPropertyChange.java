package grader.basics.output.observer;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import valgrindpp.grader.ValgrindTrace;

public class BasicValgrindTraceToPropertyChange extends BasicObjectToPropertyChange   implements ValgrindTraceToPropertyChange {
	
//	private Map<String, Thread> threadIdToProxyThread = new HashMap();
	private long lastTime = 0;
	private Thread lastThread = null;
	private long lastThreadId = -1;
	public static final String PRINT_NAME = "printf";
	private static String[] printArgs = {"placeholder"};
	Object[][] conversionSpecifications = {
			//fnname, source arg, propery, new value
			{"pthread_mutex_init", 0, "init", 2},
			{"pthread_mutex_lock", 0, "lock", true},
			{"pthread_mutex_unlock", 0, "lock", false},
			{"pthread_cond_wait", 0, "wait", 1},
			{"pthread_cond_broadcast", 0, "broadcast", null},
			{"pthread_cond_signal", 0, "signal", null},
			{"pthread_create", 0, "threadCreated", -2} // result is the value of prpperty
//			{".*", -1, 0, 1}
	};
	
	String getArg(String[] anArgs, int anIndex) {
		if (anIndex < anArgs.length ) {
			return anArgs[anIndex];
		}
		return null;
	}
	Object getValue (String aFunctionName, ValgrindTrace aValgrindTrace, Object aSpecificationElement) {

//	Object getValue (String aFunctionName, String[] anArgs, Object aSpecificationElement) {
		String[] anArgs = aValgrindTrace.arguments;
		String aResult = aValgrindTrace.result;
		String retVal = null;
		
		if (aSpecificationElement instanceof Integer) {
			Integer anIndex = (Integer) aSpecificationElement;
			if (anIndex == -1) {
				return aFunctionName;
			} else if (anIndex == -2) {
				return aResult;
			}
			return getArg(anArgs, anIndex);
		}
		return aSpecificationElement;
		
		
	}
	
	protected Object[][] conversionSpecifications() {
		return conversionSpecifications;
	}
	
	
	public PropertyChangeEvent toPropertyChange (ValgrindTrace aValgrindTrace) {
		String aFunctionName = aValgrindTrace.fnname;
//		String[] anArgs = aValgrindTrace.arguments;
//		String aResult = aValgrindTrace.result;
		Object[][] aConversionSpecifications = conversionSpecifications();

		for (int i = 0; i < aConversionSpecifications.length; i++) {
			Object[] aSpecification = aConversionSpecifications[i] ;
			String aMatchingString = (String) aSpecification[0];
			if (aFunctionName.matches(aMatchingString)) {
				Object aSource = getValue(aFunctionName, aValgrindTrace, aSpecification[1]);
				String aProperty = getValue(aFunctionName, aValgrindTrace, aSpecification[2]).toString();
				Object aNewValue = getValue(aFunctionName, aValgrindTrace, aSpecification[3]);
				Object anOldValue = aValgrindTrace;
				return new PropertyChangeEvent(aSource, aProperty, anOldValue, aNewValue);
			};			
			
		}
		return null;
	}
	private List<ValgrindTrace>  valgrindTraces = new ArrayList();
	@Override
	public List<ValgrindTrace> getValgrindTraces() {
		return valgrindTraces;
	}

	@Override
	public PropertyChangeEvent toPropertyChange(Object anObject) {	
		
		if (!(anObject instanceof String)) {
			return null;
		}
		String aTraceLine = (String) anObject;
		ValgrindTrace aValgrindTrace = toValgrindTrace(aTraceLine);
		
		
//		ValgrindTrace aValgrindTrace = null;
////		long aTime = lastTime;
////		long aTime = System.currentTimeMillis();;
//		long aTime = 0;
//
//		Thread aThread = lastThread;
//		long aThreadId = lastThreadId;
//
//		String aProperty = ANONYMOUS_PROPERTY;
//		
//
//		try {
//		    aValgrindTrace = new ValgrindTrace(aTraceLine);
//		    
//		} catch (Exception e) {
//			printArgs[0] = aTraceLine;
//			aValgrindTrace = new ValgrindTrace(aTime, aThreadId, PRINT_NAME, null, printArgs);
//			
////			return new PropertyChangeEvent(anonymousSource(), ANONYMOUS_PROPERTY, null, aTraceLine);			
//		}
		return toPropertyChange(aValgrindTrace);
		
		
		
//		return ObservablePrintStreamUtility.toPropertyChange(anObject, sourceSuffix(), propertySuffix(), anonymousSource());
	}
	
	ValgrindTrace toValgrindTrace (String aTraceLine) {

		ValgrindTrace aValgrindTrace = null;

		long aTime = lastTime;
		Thread aThread = lastThread;
		long aThreadId = lastThreadId;
		String aProperty = ANONYMOUS_PROPERTY;
		

		try {
		    aValgrindTrace = new ValgrindTrace(aTraceLine);
		    
		} catch (Exception e) {
			printArgs[0] = aTraceLine;
			aValgrindTrace = new ValgrindTrace(aTime, aThreadId, PRINT_NAME, null, printArgs);
			
//			return new PropertyChangeEvent(anonymousSource(), ANONYMOUS_PROPERTY, null, aTraceLine);			
		}
		valgrindTraces.add(aValgrindTrace);
		
		return aValgrindTrace;
	}
	
	
	
	

}
