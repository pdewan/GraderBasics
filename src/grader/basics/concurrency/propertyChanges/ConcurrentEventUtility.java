package grader.basics.concurrency.propertyChanges;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import util.trace.Tracer;

public class ConcurrentEventUtility {

	public static Set<Thread> newThreads(Set<Thread> aPreviousThreads) {
		Set<Thread> retVal = new HashSet(getCurrentThreads());
		retVal.removeAll(aPreviousThreads);
		return retVal;
	}
	public static Set<Thread> newThreads(Thread[] aPreviousThreads) {
		Set<Thread> aPreviousThreadsSet = new HashSet(Arrays.asList(aPreviousThreads));
		Set<Thread> retVal = new HashSet(getCurrentThreads());
		retVal.removeAll(aPreviousThreadsSet);
		return retVal;
	}
	public static Thread getFirstNewThread(Set<Thread> aPreviousThreads) {
		Set<Thread> aNewThreads = ConcurrentEventUtility.newThreads(aPreviousThreads);
		if (aNewThreads.size() > 0) {
		return aNewThreads.iterator().next();
		} else {
			return null;
		}
	}
	public static Set<Thread> getCurrentThreads() {
		return new HashSet<Thread>(Thread.getAllStackTraces().keySet());
	}
	
	public static boolean newThreadsCreated(Set<Thread> aPreviousThreads, int aNumThreads) {
		Set<Thread> aNewThreads = ConcurrentEventUtility.newThreads(aPreviousThreads);
		return (aNewThreads.size() != aNumThreads) ;
		
	}

	public static <EventType> ConcurrentEvent<EventType>[] selectEvents(
			ConcurrentEvent<EventType>[] anOriginalEventList,

			Selector<ConcurrentEvent<EventType>> aSelector) {
		List<ConcurrentEvent<EventType>> retValList = new ArrayList<ConcurrentEvent<EventType>>();
		for (ConcurrentEvent<EventType> anEvent : anOriginalEventList) {
//			int aSequenceNumber =
			if (aSelector.selects(anEvent)) {
				retValList.add(anEvent);
			}
		}
		return (ConcurrentEvent<EventType>[]) retValList.toArray();
	}

	public static ConcurrentPropertyChange[] selectEvents(ConcurrentPropertyChange[] anOriginalEventList,

			Selector<ConcurrentPropertyChange> aSelector) {
		List<ConcurrentPropertyChange> retValList = new ArrayList<ConcurrentPropertyChange>();
		for (ConcurrentPropertyChange anEvent : anOriginalEventList) {
//			int aSequenceNumber =
			if (aSelector.selects(anEvent)) {
				retValList.add(anEvent);
			}
		}
		return (ConcurrentPropertyChange[]) retValList.toArray();
	}

	public static boolean matches(ConcurrentPropertyChange aConcurrentPropertyChange, Object[] aParameters

	) {
		if (aParameters == null) {
			return true;
		}
		Integer startSequenceNumber = null;
		Integer stopSequenceNumber = null;
		int aThreadIndex = 0;
		String threadSelector;
		String sourceSelector;
		String propertySelector;
		String oldValueSelector;
		String newValueSelector;
		int aParametersLength = aParameters.length;
		if (aParametersLength != 7 && aParametersLength != 5) {
			System.out.println(Arrays.toString(aParameters) + "should have five or seven elements instead of "
					+ aParametersLength + " elements");
			return false;
		}
		if (aParametersLength == 7) {
			aThreadIndex = 2;

			if (aParameters[0] != null) {
				startSequenceNumber = (Integer) aParameters[0];
			}
			if (aParameters[1] != null) {

				stopSequenceNumber = (Integer) aParameters[1];
			}
		}
		threadSelector = (String) aParameters[aThreadIndex];
		sourceSelector = (String) aParameters[aThreadIndex + 1];
		propertySelector = (String) aParameters[aThreadIndex + 2];
		oldValueSelector = (String) aParameters[aThreadIndex + 3];
		newValueSelector = (String) aParameters[aThreadIndex + 4];
		int aSequenceNumber = aConcurrentPropertyChange.getSequenceNumber();
		String aThreadName = aConcurrentPropertyChange.getThread().getName();
		String aSource = aConcurrentPropertyChange.getEvent().getSource().toString();

		String aPropertyName = aConcurrentPropertyChange.getEvent().getPropertyName().toString();
		String anOldValue = aConcurrentPropertyChange.getEvent().getOldValue().toString();
		String aNewValue = aConcurrentPropertyChange.getEvent().getNewValue().toString();
		boolean retVal = (startSequenceNumber == null
				|| aConcurrentPropertyChange.getSequenceNumber() >= startSequenceNumber)
				&& (stopSequenceNumber == null || aSequenceNumber <= stopSequenceNumber)
				&& (threadSelector == null || aThreadName.matches(threadSelector))
				&& (sourceSelector == null || aSource.matches(sourceSelector))
				&& (propertySelector == null || aPropertyName.toString().matches(propertySelector))
				&& (oldValueSelector == null || anOldValue.toString().matches(oldValueSelector))
				&& (newValueSelector == null || aNewValue.toString().matches(newValueSelector));
		return retVal;

//		return (startSequenceNumber == null || aConcurrentPropertyChange.getSequenceNumber() >= startSequenceNumber)
//				&& (stopSequenceNumber == null || aConcurrentPropertyChange.getSequenceNumber() <= stopSequenceNumber)
//				&& (threadSelector == null || aConcurrentPropertyChange.getThread().getName().matches(threadSelector))
//				&& (sourceSelector == null
//						|| aConcurrentPropertyChange.getEvent().getSource().toString().matches(sourceSelector))
//				&&  (propertySelector == null
//						|| aConcurrentPropertyChange.getEvent().getPropertyName().toString().matches(propertySelector))
//				&& (oldValueSelector == null
//						|| aConcurrentPropertyChange.getEvent().getOldValue().toString().matches(oldValueSelector))
//				&& (newValueSelector == null
//						|| aConcurrentPropertyChange.getEvent().getNewValue().toString().matches(oldValueSelector));
	}

	public static ConcurrentPropertyChange[] selectEvents(ConcurrentPropertyChange[] anOriginalEventList,

			Object[] aMatchedComponents) {
		if (aMatchedComponents == null) {
			return anOriginalEventList;
		}
		Selector<ConcurrentPropertyChange> aSelector = new ParameterizedPropertyChangeSelector(aMatchedComponents);
//		List<ConcurrentPropertyChange> retValList = new ArrayList<ConcurrentPropertyChange>();
		return selectEvents(anOriginalEventList, aSelector);
	}

	public static Selector<ConcurrentPropertyChange> createSelector(Object[][] aMatchedComponentsList, int anIndex) {
		if (anIndex >= aMatchedComponentsList.length) {
			return null;
		}
		return new ParameterizedPropertyChangeSelector(aMatchedComponentsList[anIndex]);
	}
	
	public static void setMatchEveryThreadWaitSelector(ConcurrentPropertyChangeSupport aConcurrentPropertyChangeSupport,
			Object[] aParameters, int aNumThreads,  int aNumMatches, String aThreadPattern) {
		Selector<ConcurrentPropertyChangeSupport> aSelector = 
				new ConcurrentPropertyChangeThreadMatchesSelector(aParameters, aNumThreads, aNumMatches, null, 0);
		aConcurrentPropertyChangeSupport.addtWaitSelector(aSelector);
	}
	public static void setWaitSelector(ConcurrentPropertyChangeSupport aConcurrentPropertyChangeSupport,
			Selector<ConcurrentPropertyChangeSupport> aSelector) {		
		aConcurrentPropertyChangeSupport.addtWaitSelector(aSelector);
	}
	public static void setWaitSelector(ConcurrentPropertyChangeSupport aConcurrentPropertyChangeSupport,
			Object[][] aParameters, int aNumMatches, long aTimeOut) {
		Selector<ConcurrentPropertyChangeSupport> aSelector = 
				new ConcurrentPropertyChangeHistoryMatchesSelector(aParameters, aNumMatches);
		aConcurrentPropertyChangeSupport.addtWaitSelector(aSelector);
	}
	
    public static void trace(ConcurrentPropertyChange[] anOriginalEvents) {
    	Tracer.info(ConcurrentEventUtility.class, "Tracing events start");
    	for (ConcurrentPropertyChange aConcurrentChange : anOriginalEvents) {			
			Tracer.info(ConcurrentEventUtility.class, aConcurrentChange.toString());
		}
    	Tracer.info(ConcurrentEventUtility.class, "Tracing events end");
    }  
    
    public static void traceAll(ConcurrentPropertyChange[] anOriginalEvents) {
    	trace(anOriginalEvents);
    	Map<Thread, ConcurrentPropertyChange[]> aThreadToEvents = getConcurrentPropertyChangesByThread(anOriginalEvents);
    	trace(aThreadToEvents);
    	Map<Object, ConcurrentPropertyChange[]> aThreadToSources = getConcurrentPropertyChangesBySource(anOriginalEvents);
    	trace(aThreadToSources);
    	Map<Long, ConcurrentPropertyChange[]> aThreadToTimes = getConcurrentPropertyChangesByTime(anOriginalEvents);
    	trace(aThreadToTimes);
    }
	
    public static <KeyType> void trace(Map<KeyType, ConcurrentPropertyChange[]> aMap) {
    	Tracer.info(ConcurrentEventUtility.class, "Tracing events by key start");
    	for (KeyType aKey:aMap.keySet()) {
    		if (aKey instanceof Thread) {
    			Tracer.info(ConcurrentEventUtility.class, ((Thread) aKey).getName()); // yuk instance of!
    		} else {
    			Tracer.info(ConcurrentEventUtility.class, aKey.toString());
    		}
    		trace(aMap.get(aKey));    		
    	}
    	Tracer.info(ConcurrentEventUtility.class, "Tracing events by key end");

    }
    
    public static void trace(Object[][] aMatchedComponentsList) {
    	for (Object[] aMatchedComponents : aMatchedComponentsList) {
			Tracer.info(ConcurrentEventUtility.class, Arrays.toString(aMatchedComponents));
		}
    }
    
    public static String toMatchableText(Object[] aSpecification) {
    	return new BasicConcurrentPropertyChange(aSpecification).toMatchableText();
    }
    static StringBuffer stringBuffer = new StringBuffer();

    
    public static String toMatchableText(Object[][] aSpecifications, int aStartIndex) {

    	stringBuffer.setLength(0);
    	for (int i = aStartIndex; i < aSpecifications.length; i++) {
    		stringBuffer.append(toMatchableText(aSpecifications[i]));
    	}
    	return stringBuffer.toString();
    }
//    public static String toMatchableText(ConcurrentPropertyChange[] anOriginalEvents, int aStartIndex) {
//
//    	stringBuffer.setLength(0);
//    	
//    		stringBuffer.append(anEvent.toMatchableText());
//    	}
//    	return stringBuffer.toString();
//    }

	
    public static void trace(ConcurrentPropertyChange[] anOriginalEvents, Object[][] aMatchedComponentsList) {
    	Tracer.info(ConcurrentEventUtility.class, "Actual events:");
		trace(anOriginalEvents);
		Tracer.info(ConcurrentEventUtility.class, "Expected event specifications:");
		trace(aMatchedComponentsList);
    }


	public static boolean matches(ConcurrentPropertyChange[] anOriginalEvents,

			Object[][] aMatchedComponentsList, int aNumMatches) {
		int anActualMatches = numMatches(anOriginalEvents, aMatchedComponentsList);
		return anActualMatches == aNumMatches;
//		int aStartIndex = 0;
//		for (int aMatchNumber = 0; aMatchNumber < aNumMatches; aMatchNumber++) {
//			List<Integer> anIndices = indicesOf(anOriginalEvents, aMatchedComponentsList, aStartIndex);
//			if (anIndices == null || anIndices.size() != aMatchedComponentsList.length) {
//				System.err.println("Match of actual events failed against expected events");
//				System.err.println("Actual events:");
//				for (ConcurrentPropertyChange aConcurrentChange : anOriginalEvents) {
//					System.out.println(aConcurrentChange);
//				}
//				System.err.println("Expected events:");
//				for (Object[] aMatchedComponents : aMatchedComponentsList) {
//					System.out.println(Arrays.toString(aMatchedComponents));
//				}
//				return false;
//			}
//			aStartIndex = anIndices.get(anIndices.size() - 1);
//		}
//		return true;
//
////		if (anOriginalEvents == null) {
////			System.err.println("Null event array passed to matches");
////			return false;
////		}
////
////		int aMatchIndex = 0;
////		Selector aSelector = createSelector(aMatchedComponentsList, aMatchIndex);
////
////		for (ConcurrentPropertyChange anEvent : anOriginalEvents) {
////			if (aSelector == null) {
////				return true; // we have gone through all of components to be matched
////			}
////			if (aSelector.selects(anEvent)) {
////				aMatchIndex++;
////				aSelector = createSelector(aMatchedComponentsList, aMatchIndex);
////			}
////		}
////		System.err.println(
////				Arrays.toString(anOriginalEvents) + " does not match " + Arrays.toString(aMatchedComponentsList));
////		return false;
	}
	
	public static int numMatches(ConcurrentPropertyChange[] anOriginalEvents,

			Object[][] anOredMatchedComponents) {
		Tracer.info(ConcurrentEventUtility.class, "Matching following events:");
		trace(anOriginalEvents);

		int aNumMatches = 0;
		int aStartIndex = 0;
		for (; true; aNumMatches++) {
			List<Integer> anIndices = indicesOfOredSelectors(anOriginalEvents, anOredMatchedComponents, aStartIndex);
			int aNumMatchedComponents = anIndices.size();
			if (anIndices == null || aNumMatchedComponents != anOredMatchedComponents.length) {
				if (aNumMatchedComponents > 0) {
					Tracer.info(ConcurrentEventUtility.class, "Unmatched specification at index " + aNumMatchedComponents + " = " +
							Arrays.toString(anOredMatchedComponents[anIndices.size()]));;
				}
				break;
			} 
//			else {
				aStartIndex = anIndices.get(anIndices.size() - 1) + 1;
//				aNumMatches++;
//			}
		}
		return aNumMatches;

//		if (anOriginalEvents == null) {
//			System.err.println("Null event array passed to matches");
//			return false;
//		}
//
//		int aMatchIndex = 0;
//		Selector aSelector = createSelector(aMatchedComponentsList, aMatchIndex);
//
//		for (ConcurrentPropertyChange anEvent : anOriginalEvents) {
//			if (aSelector == null) {
//				return true; // we have gone through all of components to be matched
//			}
//			if (aSelector.selects(anEvent)) {
//				aMatchIndex++;
//				aSelector = createSelector(aMatchedComponentsList, aMatchIndex);
//			}
//		}
//		System.err.println(
//				Arrays.toString(anOriginalEvents) + " does not match " + Arrays.toString(aMatchedComponentsList));
//		return false;
	}
	public static int indexOf(ConcurrentPropertyChange[] anOriginalEvents, 
			Selector<ConcurrentPropertyChange>[] aSelectorSequence, int aStartIndex) {
		
		for (int aPropertyChangeIndex = aStartIndex; 
				aPropertyChangeIndex < anOriginalEvents.length; 
				aPropertyChangeIndex++) {
			if (startsWith(anOriginalEvents, aSelectorSequence, aPropertyChangeIndex)) {
				return aPropertyChangeIndex;
			}
			 
		}
		return -1;
	}
	public static int indexOfMatch(List<String> aStringList, 
			String aRegularExpression, int aStartIndex, int aStopIdex) {
		
		for (int anIndex = aStartIndex; 
				anIndex < aStopIdex; 
				anIndex++) {
			if (aStringList.get(anIndex).matches(aRegularExpression)) {
				return anIndex;
			}
			 
		}
		return -1;
	}
	public static int indexOfPrefix(List<String> aStringList, 
			String aSubString, int aStartIndex, int aStopIndex) {
		
		for (int anIndex = aStartIndex; 
				anIndex < aStopIndex; 
				anIndex++) {
			if (aStringList.get(anIndex).startsWith(aSubString)) {
				return anIndex;
			}
			 
		}
		return -1;
	}
	
	public static List<Integer> indicesOf(ConcurrentPropertyChange[] anOriginalEvents, 
			Selector<ConcurrentPropertyChange>[] aSelectorSequence, int aStartIndex) {
		List<Integer> retVal = new ArrayList();
		int aNextStartIndex = aStartIndex;
		while (true) {
			aNextStartIndex = indexOf(anOriginalEvents, aSelectorSequence, aNextStartIndex);
			if (aNextStartIndex < 0) {
				return retVal;
			}
			aNextStartIndex += aSelectorSequence.length;
		}
	}
	public static List<Integer> indicesOf(ConcurrentPropertyChange[] anOriginalEvents, 
			Object[][] aSelectorSequence, int aStartIndex) {
		return indicesOf(anOriginalEvents, createSelectors(aSelectorSequence), aStartIndex);
	}
	public static List<Long> interEventTimeDifferences(ConcurrentPropertyChange[] anOriginalEvents, int from) {
		List<Long> retVal = new ArrayList();
		for (int aStartIndex = from + 1; aStartIndex < anOriginalEvents.length; aStartIndex++) {
			long aTimeDifference = anOriginalEvents[aStartIndex].getStartTime() - 
					anOriginalEvents[aStartIndex -1].getStartTime();
			retVal.add(aTimeDifference);
		}
		return retVal;
	}
	public static List<Long> interEventPairTimeDifferences(ConcurrentPropertyChange[] anOriginalEvents, int from) {
		List<Long> retVal = new ArrayList();
		for (int aStartIndex = from + 4; aStartIndex < anOriginalEvents.length; aStartIndex = aStartIndex + 4) {
			long aTimeDifference = anOriginalEvents[aStartIndex -1].getStartTime() - 
					anOriginalEvents[aStartIndex -3].getStartTime();
			retVal.add(aTimeDifference);
		}
		
		return retVal;
	}
	public static List<Long> intraEventPairTimeDifferences(ConcurrentPropertyChange[] anOriginalEvents, int from) {
		List<Long> retVal = new ArrayList();
		for (int aStartIndex = from + 1; aStartIndex < anOriginalEvents.length; aStartIndex = aStartIndex + 2) {
			long aTimeDifference = anOriginalEvents[aStartIndex].getStartTime() - 
					anOriginalEvents[aStartIndex-1].getStartTime();
			retVal.add(aTimeDifference);
		}
		return retVal;
	}
	
	public static DescriptiveStatistics computeDescriptiveStatistics(List<Long> aValues) {
		
		// Get a DescriptiveStatistics instance
	DescriptiveStatistics stats = new DescriptiveStatistics();

	// Add the data from the array
	for( int i = 0; i < aValues.size(); i++) {
	        stats.addValue(aValues.get(i));
	}
	return stats;
	}
	public static boolean startsWith(ConcurrentPropertyChange[] anOriginalEvents, 
			Selector<ConcurrentPropertyChange>[] aSelectorSequence, int aStartIndex) {
		
		if (anOriginalEvents.length - aStartIndex < aSelectorSequence.length) {
			return false;
		}
		for (int aSelectorIndex = 0; aSelectorIndex < aSelectorSequence.length; aSelectorIndex++) {
			Selector<ConcurrentPropertyChange> aSelector = aSelectorSequence[aSelectorIndex];
			if (!aSelector.selects(anOriginalEvents[aStartIndex + aSelectorIndex])) return false;
		}
		return true;
	}
	
	public static boolean startsWith(ConcurrentPropertyChange[] anOriginalEvents, 
			Object[][] aSelectorSequence, int aStartIndex) {
		
		return startsWith(anOriginalEvents, createSelectors(aSelectorSequence), aStartIndex);
	}
	
	public static Selector<ConcurrentPropertyChange>[] createSelectors( 
			Object[][] aSelectorSequence) {
		ParameterizedPropertyChangeSelector[] aSelectors = new ParameterizedPropertyChangeSelector[aSelectorSequence.length];
		for (int i = 0; i < aSelectors.length; i++ ) {
			aSelectors[i] = new ParameterizedPropertyChangeSelector(aSelectorSequence[i]);
		}
		return aSelectors;
	}

	public static List<Integer> indicesOfOredSelectors(ConcurrentPropertyChange[] anOriginalEvents,

			Object[][] anOredMatchedComponents, int aStartIndex) {
		List<Integer> retVal = new ArrayList();

		if (anOriginalEvents == null) {
			System.err.println("Null event array passed to matches");
			return null;
		}

		int aMatchIndex = 0;
		Selector aSelector = createSelector(anOredMatchedComponents, aMatchIndex);

//		for (ConcurrentPropertyChange anEvent : anOriginalEvents) {
		for (int anEventIndex = aStartIndex; anEventIndex < anOriginalEvents.length; anEventIndex++) {
			ConcurrentPropertyChange anEvent = anOriginalEvents[anEventIndex];
			if (aSelector == null) {
				return retVal; // we have gone through all of components to be matched
			}
			if (aSelector.selects(anEvent)) {
				retVal.add(anEventIndex);
				aMatchIndex++;
				aSelector = createSelector(anOredMatchedComponents, aMatchIndex);
			}
		}
//		System.err.println(
//				Arrays.toString(anOriginalEvents) + " does not completely match " + Arrays.toString(aMatchedComponentsList));
		return retVal;
	}

	public static boolean matchesEachThread(ConcurrentPropertyChange[] anOriginalEvents, Thread[] aThreads,
			Object[][] aMatchedComponentsList) {

		Map<Thread, ConcurrentPropertyChange[]> aThreadToEvents = getConcurrentPropertyChangesByThread(
				anOriginalEvents);
		for (Thread aThread : aThreads) {
			if (!matches(aThreadToEvents.get(aThread), aMatchedComponentsList, 1)) {
				return false;
			}
		}
		return true;

	}
	
	
//	
//	public static <KeyType> boolean matchesEachKey(Map<KeyType, ConcurrentPropertyChange[]> aKeyToEvents,
//			Object[][] aMatchedComponentsList, int aMinExpectedMatches, int aMaxExppectedMatches, String aMatchPattern) {
//		
//		Map<KeyType, Integer> aKeyToMatch = numMatchesOfEachKey(aKeyToEvents, aMatchedComponentsList, aMatchPattern);
//		for (KeyType aKey:aKeyToMatch.keySet()) {
//			int aNumberOfMatches = aKeyToMatch.get(aKey);
//			if (aNumberOfMatches < aMinExpectedMatches || aNumberOfMatches > aMaxExppectedMatches) {
//				Tracer.info(ConcurrentEventUtility.class, "Number of matches for " + aKey + ": "  + aNumberOfMatches + " is not between " +   aMinExpectedMatches + " and " + aMaxExppectedMatches);
//				return false;
//				
//			} else {
//				Tracer.info(ConcurrentEventUtility.class, "Number of matches for " + aKey + ": "  + aNumberOfMatches + " is between " +   aMinExpectedMatches + " and " + aMaxExppectedMatches);
//
//			}
//		}

//		return true;
//
//	}
	public static <KeyType> boolean matchesEachKey(Map<KeyType, ConcurrentPropertyChange[]> aKeyToEvents,
			Object[][] aMatchedComponentsList, int aMinExpectedMatches, int aMaxExppectedMatches, Selector<KeyType> aSelector) {
		
		Map<KeyType, Integer> aKeyToMatch = numMatchesOfEachKey(aKeyToEvents, aMatchedComponentsList, aSelector);
		for (KeyType aKey:aKeyToMatch.keySet()) {
			int aNumberOfMatches = aKeyToMatch.get(aKey);
			if (aNumberOfMatches < aMinExpectedMatches || aNumberOfMatches > aMaxExppectedMatches) {
				Tracer.info(ConcurrentEventUtility.class, "Number of matches for " + aKey + ": "  + aNumberOfMatches + " is not between " +   aMinExpectedMatches + " and " + aMaxExppectedMatches);
				return false;
				
			} else {
				Tracer.info(ConcurrentEventUtility.class, "Number of matches for " + aKey + ": "  + aNumberOfMatches + " is between " +   aMinExpectedMatches + " and " + aMaxExppectedMatches);

			}
		}

		return true;

	}
	
	public static boolean matchesEachThread(ConcurrentPropertyChange[] anOriginalEvents,
			Object[][] aMatchedComponentsList, int aMinExpectedMatches, int aMaxExpectedMatches) {
		return matchesEachThread(anOriginalEvents, aMatchedComponentsList, aMinExpectedMatches, aMaxExpectedMatches, null);
	}
	public static boolean matchesEachSource(ConcurrentPropertyChange[] anOriginalEvents,
			Object[][] aMatchedComponentsList, int aMinExpectedMatches, int aMaxExpectedMatches) {
		return matchesEachSource(anOriginalEvents, aMatchedComponentsList, aMinExpectedMatches, aMaxExpectedMatches, (Selector) null);
	}
	
	public static boolean matchesEachSource(ConcurrentPropertyChange[] anOriginalEvents,
			Object[][] aMatchedComponentsList, int aMinExpectedMatches, int aMaxExpectedMatches, Selector aSelector) {
		Map<Object, ConcurrentPropertyChange[]> aSourceToEvents = getConcurrentPropertyChangesBySource(
		anOriginalEvents);
		return matchesEachKey(aSourceToEvents, aMatchedComponentsList, aMinExpectedMatches, aMaxExpectedMatches, aSelector);


	}
	public static Selector toMatchPatternSelector(String aMatchPattern) {
		return new StringMatchSelector(aMatchPattern);
	}
	public static boolean matchesEachSource(ConcurrentPropertyChange[] anOriginalEvents,
			Object[][] aMatchedComponentsList, int aMinExpectedMatches, int aMaxExpectedMatches, String aMatchPattern) {
		
		Selector aSelector = toMatchPatternSelector(aMatchPattern);
		return matchesEachSource(anOriginalEvents, aMatchedComponentsList, aMinExpectedMatches, aMaxExpectedMatches, aSelector);


	}

	public static boolean matchesEachThread(ConcurrentPropertyChange[] anOriginalEvents,
			Object[][] aMatchedComponentsList, int aMinExpectedMatches, int aMaxExpectedMatches, Selector<Thread> aMatchPattern) {
		Map<Thread, ConcurrentPropertyChange[]> aThreadToEvents = getConcurrentPropertyChangesByThread(
		anOriginalEvents);
		return matchesEachKey(aThreadToEvents, aMatchedComponentsList, aMinExpectedMatches, aMaxExpectedMatches, aMatchPattern);
//		Map<Thread, ConcurrentPropertyChange[]> aThreadToEvents = getConcurrentPropertyChangesByThread(
//				anOriginalEvents);
//		for (Thread aThread : aThreadToEvents.keySet()) {
//			ConcurrentPropertyChange[] aThreadEvents = aThreadToEvents.get(aThread);
//			if (!matches(aThreadEvents, aMatchedComponentsList, 1)) {
//				Tracer.info(ConcurrentEventUtility.class, "Events for thread:" + aThread.getName() + " did not match");
//				trace(aThreadEvents, aMatchedComponentsList);
//				return false;
//			}
//		}
//		return true;

	}
	
	public static Map<Thread, Integer> numMatchesOfEachThread(ConcurrentPropertyChange[] anOriginalEvents,
			Object[][] aMatchedComponentsList, Selector<Thread> aSelector) {
//		Map<Thread, Integer> aThreadToMatch = new HashMap();

		Map<Thread, ConcurrentPropertyChange[]> aThreadToEvents = getConcurrentPropertyChangesByThread(
				anOriginalEvents);
		return numMatchesOfEachKey(aThreadToEvents, aMatchedComponentsList, aSelector);
//		for (Thread aThread : aThreadToEvents.keySet()) {
//			ConcurrentPropertyChange[] aThreadEvents = aThreadToEvents.get(aThread);
//			int aNumMatches = numMatches(aThreadEvents, aMatchedComponentsList);
//			aThreadToMatch.put(aThread, aNumMatches);
////			if (!matches(aThreadEvents, aMatchedComponentsList, 1)) {
////				Tracer.info(ConcurrentEventUtility.class, "Events for thread:" + aThread.getName() + " did not match");
////				trace(aThreadEvents, aMatchedComponentsList);
////				return false;
////			}
//		}
//		return aThreadToMatch;
	}
	public static Map<Object, Integer> numMatchesOfEachSource(ConcurrentPropertyChange[] anOriginalEvents,
			Object[][] aMatchedComponentsList, String aSourcePattern) {

		Map<Object, ConcurrentPropertyChange[]> aKeyToEvents = getConcurrentPropertyChangesBySource(
				anOriginalEvents);
		return numMatchesOfEachKey(aKeyToEvents, aMatchedComponentsList);
		
//		
//		Map<Object, ConcurrentPropertyChange[]> aSelectedKeyToEvents = new HashMap();
//		if (aSourcePattern == null) {
//			aSelectedKeyToEvents = aKeyToEvents;
//		} else {
//		for (Object aKey: aKeyToEvents.keySet()) {
//			if (aKey.toString().matches(aSourcePattern)) {
//				aSelectedKeyToEvents.put(aKey, aKeyToEvents.get(aKey));
//			}
//		}
//		}
//		
//		return numMatchesOfEachKey(aSelectedKeyToEvents, aMatchedComponentsList);

	}
	public static <KeyType> Map<KeyType, Integer> numMatchesOfEachKey(Map<KeyType, ConcurrentPropertyChange[]> aKeyToEvents,
			Object[][] aMatchedComponentsList, Selector<KeyType> aSelector) {

	
		
		Map<KeyType, ConcurrentPropertyChange[]> aSelectedKeyToEvents = new HashMap();
		if (aSelector == null) {

//		if (aSourcePattern == null) {
			aSelectedKeyToEvents = aKeyToEvents;
		} else {
		for (KeyType aKey: aKeyToEvents.keySet()) {
//			if (aKey.toString().matches(aSourcePattern)) {
			if (aSelector.selects(aKey)) {

				aSelectedKeyToEvents.put(aKey, aKeyToEvents.get(aKey));
			}
		}
		}
		
		return numMatchesOfEachKey(aSelectedKeyToEvents, aMatchedComponentsList);

	}
	public static <KeyType> Map<KeyType, Integer> numMatchesOfEachKey(Map<KeyType, ConcurrentPropertyChange[]> aKeyToEvents,
			Object[][] aMatchedComponentsList) {
		Map<KeyType, Integer> aKeyToMatch = new HashMap();

		Tracer.info(ConcurrentEventUtility.class, "Matching component patterns:");
		trace(aMatchedComponentsList);

		for (KeyType aKey : aKeyToEvents.keySet()) {
			Tracer.info(ConcurrentEventUtility.class, "Mathcing events associated with " + aKey);

			ConcurrentPropertyChange[] aKeyEvents = aKeyToEvents.get(aKey);
			

			int aNumMatches = numMatches(aKeyEvents, aMatchedComponentsList);
			aKeyToMatch.put(aKey, aNumMatches);
//			if (!matches(aThreadEvents, aMatchedComponentsList, 1)) {
//				Tracer.info(ConcurrentEventUtility.class, "Events for thread:" + aThread.getName() + " did not match");
//				trace(aThreadEvents, aMatchedComponentsList);
//				return false;
//			}
		}
		return aKeyToMatch;
	}

	public static boolean processInterleaving(ConcurrentPropertyChange[] aThreadEvents, List<Integer> aStartIndices,
			List<Integer> aStopIndices) {
		if (aThreadEvents == null || aThreadEvents.length == 0) {
			return false;
		}
		int aStartIndex = aThreadEvents[0].getSequenceNumber();
		int aStopIndex = aThreadEvents[aThreadEvents.length - 1].getSequenceNumber();
		for (int aStartIndicesIndex = 0; aStartIndicesIndex < aStartIndices.size(); aStartIndicesIndex++) {
			int aPreviousStartIndex = aStartIndices.get(aStartIndicesIndex);
			int aPreviousStopIndex = aStopIndices.get(aStartIndicesIndex);
			boolean aNonOverlapping = aPreviousStartIndex > aStopIndex || aStartIndex > aPreviousStopIndex;
			if (!aNonOverlapping) {
				Tracer.info(ConcurrentEventUtility.class, 
						"start index: " + aStartIndex + " stop Index :" + aStopIndex + 
						" overlap with " +
						" previous minimum index " + aPreviousStartIndex + " previous max index " + aPreviousStopIndex);
				return true; // overlapping occurred
			}
		}
		aStartIndices.add(aStartIndex);
		aStopIndices.add(aStopIndex);
		return false; // no overlapping or interleaved
	}
	
	protected static Thread[] emptyThreads = {};

	public static boolean someInterleaving(ConcurrentPropertyChange[] anOriginalEvents, Thread[] aThreads,
			Object[] aMatchedComponents) {

		Map<Thread, ConcurrentPropertyChange[]> aThreadToEvents = getConcurrentPropertyChangesByThread(
				anOriginalEvents);
		List<Integer> aStartIndices = new ArrayList();
		List<Integer> aStopIndices = new ArrayList();
//		List<Integer> aThreadsMatched = new ArrayList();
		if (aThreads == null) {
			List<Thread> aList = new ArrayList( aThreadToEvents.keySet());
			aThreads = aList.toArray(emptyThreads);
		}
		for (Thread aThread : aThreads) {			
			ConcurrentPropertyChange[] aThreadEvents = aThreadToEvents.get(aThread);
			ConcurrentPropertyChange[] aSelectedEvents = selectEvents(aThreadEvents, aMatchedComponents);
			if (processInterleaving(aThreadEvents, aStartIndices, aStopIndices)) {
				Tracer.info(ConcurrentEventUtility.class, "Events of thread:" + aThread + 
						" overlap with one or more of matched threads:" + aThreads );
				
				Tracer.info(ConcurrentEventUtility.class, "Events of new thread:");
				trace(aSelectedEvents);
				
				Tracer.info(ConcurrentEventUtility.class, "Start indices of matched threads:" + aStartIndices);
				Tracer.info(ConcurrentEventUtility.class, "Stop indices of matched threads:" + aStopIndices);
				return true;
			}
		}
		Tracer.info(ConcurrentEventUtility.class, "No intervealing occured in matched threads " + aThreads);
		Tracer.info(ConcurrentEventUtility.class, "Start indices " + aStartIndices);
		Tracer.info(ConcurrentEventUtility.class, "Sop indices " + aStopIndices);

		return false;

	}

	public static int getMaxIndex(ConcurrentPropertyChange[] anOriginalEvents, Thread[] aThreads,
			Object[] aMatchedComponents) {

		Map<Thread, ConcurrentPropertyChange[]> aThreadToEvents = getConcurrentPropertyChangesByThread(
				anOriginalEvents);
		int aMaxIndex = 0;
		for (Thread aThread : aThreads) {
			ConcurrentPropertyChange[] aThreadEvents = aThreadToEvents.get(aThread);
			ConcurrentPropertyChange[] aSelectedEvents = selectEvents(aThreadEvents, aMatchedComponents);
			int aThreadStopIndex = aSelectedEvents[aSelectedEvents.length - 1].getSequenceNumber();
			if (aMaxIndex < aThreadStopIndex) {
				aMaxIndex = aThreadStopIndex;
			}

		}
		return aMaxIndex;

	}

	public static int getMinIndex(ConcurrentPropertyChange[] anOriginalEvents, Thread[] aThreads,
			Object[] aMatchedComponents) {

		Map<Thread, ConcurrentPropertyChange[]> aThreadToEvents = getConcurrentPropertyChangesByThread(
				anOriginalEvents);
		int aMinIndex = Integer.MAX_VALUE;
		for (Thread aThread : aThreads) {
			ConcurrentPropertyChange[] aThreadEvents = aThreadToEvents.get(aThread);
			ConcurrentPropertyChange[] aSelectedEvents = selectEvents(aThreadEvents, aMatchedComponents);
			int aThreadStartIndex = aSelectedEvents[0].getSequenceNumber();
			if (aThreadStartIndex < aMinIndex) {
				aMinIndex = aThreadStartIndex;
			}

		}
		return aMinIndex;

	}

	public static boolean occurAfter(ConcurrentPropertyChange[] anOriginalEvents, Thread[] aBeforeThreads,
			Object[] aBeforeThreadMatchedComponents, Thread[] anAfterThreads, Object[] anAfterThreadMatchedComponents) {

		Map<Thread, ConcurrentPropertyChange[]> aThreadToEvents = getConcurrentPropertyChangesByThread(
				anOriginalEvents);

		int aBeforeThreadMaxIndex = getMaxIndex(anOriginalEvents, aBeforeThreads, aBeforeThreadMatchedComponents);
		int anAfterThreadMinIndex = getMinIndex(anOriginalEvents, anAfterThreads, anAfterThreadMatchedComponents);
		boolean retVal = anAfterThreadMinIndex > aBeforeThreadMaxIndex;
		if (!retVal) {
			System.err.println("Max sequence number of threads:  " + Arrays.toString(aBeforeThreads) + " = "
					+ aBeforeThreadMaxIndex + " > " + "Min sequence number of threads: "
					+ Arrays.toString(anAfterThreads) + " = " + anAfterThreadMinIndex);
		}
		return retVal;

	}

	public static <EventType> Map<Thread, List<ConcurrentEvent<EventType>>> getConcurrentEventListByThread(
			ConcurrentEvent<EventType>[] anOriginalEvents) {
		Map<Thread, List<ConcurrentEvent<EventType>>> retVal = new HashMap<Thread, List<ConcurrentEvent<EventType>>>();
		for (ConcurrentEvent<EventType> anEvent : anOriginalEvents) {
			Thread aThread = anEvent.getThread();
			List<ConcurrentEvent<EventType>> anEventList = retVal.get(aThread);
			if (anEventList == null) {
				anEventList = new ArrayList<ConcurrentEvent<EventType>>();
				retVal.put(aThread, anEventList);
			}
			anEventList.add(anEvent);
		}
		return retVal;
	}


	public static  Map<Thread, List<ConcurrentPropertyChange>> getConcurrentPropertyChangeListByThread(
			ConcurrentPropertyChange[] anOriginalEvents) {
		Map<Thread, List<ConcurrentPropertyChange>> retVal = new HashMap<>();
		for (ConcurrentPropertyChange anEvent : anOriginalEvents) {
			Thread aThread = anEvent.getThread();
			List<ConcurrentPropertyChange> anEventList = retVal.get(aThread);
			if (anEventList == null) {
				anEventList = new ArrayList();
				retVal.put(aThread, anEventList);
			}
			anEventList.add(anEvent);
		}
		return retVal;
	}
	public static  Map<Thread, List<ConcurrentPropertyChange>> getConcurrentPropertyChangeListByThread(
			ConcurrentPropertyChange[] anOriginalEvents, int from, int to) {
		Map<Thread, List<ConcurrentPropertyChange>> retVal = new HashMap<>();
		for (ConcurrentPropertyChange anEvent : anOriginalEvents) {
			Thread aThread = anEvent.getThread();
			List<ConcurrentPropertyChange> anEventList = retVal.get(aThread);
			if (anEventList == null) {
				anEventList = new ArrayList();
				retVal.put(aThread, anEventList);
			}
			anEventList.add(anEvent);
		}
		return retVal;
	}
	public static <EventType> Map<Long, List<ConcurrentPropertyChange>> getConcurrentPropertyChangeListByTime(
			ConcurrentPropertyChange[] anOriginalEvents) {
		Map<Long, List<ConcurrentPropertyChange>> retVal = new TreeMap<>();
		for (ConcurrentPropertyChange anEvent : anOriginalEvents) {
			Long aTime = anEvent.getRelativeTime();
			List<ConcurrentPropertyChange> anEventList = retVal.get(aTime);
			if (anEventList == null) {
				anEventList = new ArrayList();
				retVal.put(aTime, anEventList);
			}
			anEventList.add(anEvent);
		}
		return retVal;
	}
	
	public static <EventType> Map<Long, List<ConcurrentPropertyChange>> getConcurrentPropertyChangeListByTime(
			ConcurrentPropertyChange[] anOriginalEvents, int from, int t) {
		Map<Long, List<ConcurrentPropertyChange>> retVal = new TreeMap<>();
		for (ConcurrentPropertyChange anEvent : anOriginalEvents) {
			Long aTime = anEvent.getRelativeTime();
			List<ConcurrentPropertyChange> anEventList = retVal.get(aTime);
			if (anEventList == null) {
				anEventList = new ArrayList();
				retVal.put(aTime, anEventList);
			}
			anEventList.add(anEvent);
		}
		return retVal;
	}

	public static Map<Object, List<ConcurrentPropertyChange>> getConcurrentEventListBySource(
			ConcurrentPropertyChange[] anOriginalEvents) {
		Map<Object, List<ConcurrentPropertyChange>> retVal = new HashMap<Object, List<ConcurrentPropertyChange>>();
		for (ConcurrentPropertyChange aConcurrentEvent : anOriginalEvents) {
			Object aSource = aConcurrentEvent.getEvent().getSource();
			List<ConcurrentPropertyChange> anEventList = retVal.get(aSource);			
			if (anEventList == null) {
				anEventList = new ArrayList<ConcurrentPropertyChange>();
				retVal.put(aSource, anEventList);
			}
			anEventList.add(aConcurrentEvent);

		}
		return retVal;

	}
	
	public static Map<Object, List<ConcurrentPropertyChange>> getConcurrentEventListByProperty(
			ConcurrentPropertyChange[] anOriginalEvents) {
		Map<Object, List<ConcurrentPropertyChange>> retVal = new HashMap<Object, List<ConcurrentPropertyChange>>();
		for (ConcurrentPropertyChange aConcurrentEvent : anOriginalEvents) {
			Object aPropertyName = aConcurrentEvent.getEvent().getPropertyName();
			List<ConcurrentPropertyChange> anEventList = retVal.get(aPropertyName);			
			if (anEventList == null) {
				anEventList = new ArrayList<ConcurrentPropertyChange>();
				retVal.put(aPropertyName, anEventList);
			}
			anEventList.add(aConcurrentEvent);
		}
		return retVal;
	}

	public static <EventType> Map<Thread, ConcurrentEvent<EventType>[]> getConcurrentEventsByThread(
			ConcurrentEvent<EventType>[] anOriginalEvents) {
		Map<Thread, List<ConcurrentEvent<EventType>>> anEventListByThread = getConcurrentEventListByThread(
				anOriginalEvents);
		Map<Thread, ConcurrentEvent<EventType>[]> retVal = new HashMap<Thread, ConcurrentEvent<EventType>[]>();
		for (Thread aThread : anEventListByThread.keySet()) {
			retVal.put(aThread, (ConcurrentEvent<EventType>[]) anEventListByThread.get(aThread).toArray());

		}
		return retVal;

	}

	static final ConcurrentPropertyChange[] emptyPropertyChange = {};

	public static  Map<Thread, ConcurrentPropertyChange[]> getConcurrentPropertyChangesByThread(
			ConcurrentPropertyChange[] anOriginalEvents) {

		Map<Thread, List<ConcurrentPropertyChange>> anEventListByThread = getConcurrentPropertyChangeListByThread(
				anOriginalEvents);
		Map<Thread, ConcurrentPropertyChange[]> retVal = new HashMap();
		for (Thread aThread : anEventListByThread.keySet()) {
			List<ConcurrentPropertyChange> aList = anEventListByThread.get(aThread);
			ConcurrentPropertyChange[] anArray = aList.toArray(emptyPropertyChange);
			retVal.put(aThread, anArray);

		}
		return retVal;

	}
	public static <EventType> Map<Thread, ConcurrentPropertyChange[]> getConcurrentPropertyChangesByThread(
			ConcurrentPropertyChange[] anOriginalEvents, int from, int to) {

		Map<Thread, List<ConcurrentPropertyChange>> anEventListByThread = getConcurrentPropertyChangeListByThread(
				anOriginalEvents);
		Map<Thread, ConcurrentPropertyChange[]> retVal = new HashMap();
		for (Thread aThread : anEventListByThread.keySet()) {
			List<ConcurrentPropertyChange> aList = anEventListByThread.get(aThread);
			ConcurrentPropertyChange[] anArray = aList.toArray(emptyPropertyChange);
			retVal.put(aThread, anArray);

		}
		return retVal;

	}
	public static ConcurrentPropertyChange[] removeLaterCooccuringEvents(
			ConcurrentPropertyChange[] anOriginalEvents) {

		Map<Long, List<ConcurrentPropertyChange>> anEventListByTime = getConcurrentPropertyChangeListByTime(
				anOriginalEvents);
		ConcurrentPropertyChange[] retVal = new ConcurrentPropertyChange[anEventListByTime.size()];
		int aTimeIndex = 0;
		for (Long aTime : anEventListByTime.keySet()) {
			List<ConcurrentPropertyChange> aList = anEventListByTime.get(aTime);
			retVal[aTimeIndex] = aList.get(0); // take only the first of these events

		}
		return retVal;
	}

	public static Map<Long, ConcurrentPropertyChange[]> getConcurrentPropertyChangesByTime(
			ConcurrentPropertyChange[] anOriginalEvents) {

		Map<Long, List<ConcurrentPropertyChange>> anEventListByTime = getConcurrentPropertyChangeListByTime(
				anOriginalEvents);
		Map<Long, ConcurrentPropertyChange[]> retVal = new HashMap();
		for (Long aThread : anEventListByTime.keySet()) {
			List<ConcurrentPropertyChange> aList = anEventListByTime.get(aThread);
			ConcurrentPropertyChange[] anArray = aList.toArray(emptyPropertyChange);
			retVal.put(aThread, anArray);

		}
		return retVal;

	}

	

	public static ConcurrentPropertyChange[] toConcurrentPropertyChanges(
			List<ConcurrentEvent<PropertyChangeEvent>> anOriginalList) {
		ConcurrentPropertyChange[] retVal = new ConcurrentPropertyChange[anOriginalList.size()];
		for (int anIndex = 0; anIndex < anOriginalList.size(); anIndex++) {
			retVal[anIndex] = new BasicConcurrentPropertyChange(anOriginalList.get(anIndex));
//			System.out.println(retVal[anIndex]);
		}
		return retVal;

	}
	// wy not use asList?
	public static ConcurrentPropertyChange[] toConcurrentPropertyChanges2(
			List<ConcurrentPropertyChange> anOriginalList) {
		ConcurrentPropertyChange[] retVal = new ConcurrentPropertyChange[anOriginalList.size()];
		for (int anIndex = 0; anIndex < anOriginalList.size(); anIndex++) {
			retVal[anIndex] = new BasicConcurrentPropertyChange(anOriginalList.get(anIndex));
//			System.out.println(retVal[anIndex]);
		}
		return retVal;

	}

	public static ConcurrentPropertyChange[] toConcurrentPropertyChanges(
			ConcurrentEvent<PropertyChangeEvent>[] anOriginalList) {
		ConcurrentPropertyChange[] retVal = new ConcurrentPropertyChange[anOriginalList.length];
		for (int anIndex = 0; anIndex < anOriginalList.length; anIndex++) {
			retVal[anIndex] = new BasicConcurrentPropertyChange(anOriginalList[anIndex]);
		}
		return retVal;

	}

	public static Map<Thread, ConcurrentPropertyChange[]> getConcurrentPropertyChangesByThread(
			ConcurrentEvent<PropertyChangeEvent>[] anOriginalEvents) {
		Map<Thread, List<ConcurrentEvent<PropertyChangeEvent>>> anEventListByThread = getConcurrentEventListByThread(
				anOriginalEvents);
		Map<Thread, ConcurrentPropertyChange[]> retVal = new HashMap<Thread, ConcurrentPropertyChange[]>();
		for (Thread aThread : anEventListByThread.keySet()) {
			retVal.put(aThread, toConcurrentPropertyChanges(anEventListByThread.get(aThread)));

		}
		return retVal;

	}
	

	public static Map<Object, ConcurrentPropertyChange[]> getConcurrentPropertyChangesBySource(
			ConcurrentPropertyChange[] anOriginalEvents) {
		Map<Object, List<ConcurrentPropertyChange>> anEventListBySource = getConcurrentEventListBySource(
				anOriginalEvents);
		Map<Object, ConcurrentPropertyChange[]> retVal = new HashMap<Object, ConcurrentPropertyChange[]>();
		for (Object aSource : anEventListBySource.keySet()) {
			retVal.put(aSource, toConcurrentPropertyChanges2( anEventListBySource.get(aSource)));
		}
		return retVal;
	}
	
	

	public static Map<Object, List<ConcurrentEvent<PropertyChangeEvent>>> getEventsBySource() {
		return null;

	}
	
	public static void setIgnorePreviousThreadEvents(ConcurrentEventSupport aConcurrentEventSupport, boolean newVal) {
		aConcurrentEventSupport.setIgnorePreviousThreadEvents(newVal);
	}
	static String[] emptyStringArray = {};
	public static String[] toNewValueStrings(ConcurrentPropertyChange[] anOriginal, String aProperty) {
		List<String> retVal = new ArrayList();
		for (int index = 0; index < anOriginal.length; index++) {
			PropertyChangeEvent anEvent = anOriginal[index].getEvent();
			if (aProperty == null || aProperty.equals(anEvent.getPropertyName()))
				retVal.add(anOriginal[index].getEvent().getNewValue().toString());
		}
		return retVal.toArray(emptyStringArray);
	}

	public static <KeyType> Map<KeyType, String[]> toNewValueStrings(Map<KeyType, ConcurrentPropertyChange[]> anOriginal, String aProperty) {
		Map<KeyType, String[]> retVal = new HashMap<>();
		for (KeyType key:anOriginal.keySet()) {
			ConcurrentPropertyChange[] aPropertyChanges = anOriginal.get(key);
			List<String> anOutputs = new ArrayList();
			retVal.put(key, toNewValueStrings(anOriginal.get(key), aProperty));
		}
		return retVal;
		
	}
	public static Map<Thread, String[]> getConcurrentStringsByThread(
			ConcurrentPropertyChange[] anOriginalEvents, String aProperty) {		
		Map<Thread, ConcurrentPropertyChange[]> anOriginal = getConcurrentPropertyChangesByThread(anOriginalEvents);
		Map<Thread, String[]> retVal = toNewValueStrings(anOriginal, aProperty);
		return retVal;	

	}
	public static IndexBasedSortableList toIndexBasedSortableList(List<Object> anOriginal, int aNumIndexBasedComponents) {
		IndexBasedSortableList retVal = new AnIndexBasedSortableList();
		try {
			for (int index = 0; index < anOriginal.size(); index = index + aNumIndexBasedComponents) {
				Object[] aComponents = new Object[aNumIndexBasedComponents];
				aComponents[0] = anOriginal.get(index);
				aComponents[1] = anOriginal.get(index + 1);
				aComponents[2] = anOriginal.get(index + 2);
				retVal.add(aComponents);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return retVal;
		}
		return retVal;
		
	}
	public static IndexBasedSortableList toIndexBasedSortableList(Object[]  anOriginal, int aNumIndexBasedComponents) {
		return toIndexBasedSortableList(Arrays.asList(anOriginal), aNumIndexBasedComponents);
	}

//	public static  Map<Object,List<PropertyChangeEvent>> getPropertyChangeEventsBySource() {
//		return (Map<Object,List<PropertyChangeEvent>>) getEventsBySource();
//		
//	}
}
