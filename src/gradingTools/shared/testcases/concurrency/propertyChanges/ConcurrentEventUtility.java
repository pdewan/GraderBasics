package gradingTools.shared.testcases.concurrency.propertyChanges;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.models.PropertyListenerRegisterer;
import util.trace.Tracer;

public class ConcurrentEventUtility {
//	static ConcurrentPropertyChangeSupport concurrentPropertyChhangeSupport = new BasicConcurrentPropertyChangeSupport();
//	static ConcurrentEvent<PropertyChangeEvent>[] getConcurrentOrderedEvents() {
//		
//	}
//	static void addIgnoreEventSelector(Selector<PropertyChangeEvent> aSelector) {
//		
//	}
//	static void removeIgnoreEventSelector(Selector<PropertyChangeEvent> aSelector) {
//		
//	}
//	static Selector<PropertyChangeEvent>[] getSelectors() {
//		
//	}
//	static void addObservable(PropertyListenerRegisterer anObservable) {
//		
//	}
//	static void removeObservable(PropertyListenerRegisterer anObservable) {
//		
//	}
//	static ObservableType[] getObservables() {
//		
//	}
//	static void resetConcurrentOrderedEvents() {
//		
//	}
//	static int getNextSequenceNumber() {
//		
//	}
	public static Set<Thread> getCurrentThreads() {
		return new HashSet<Thread>(Thread.getAllStackTraces().keySet());
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
	
	public static boolean matches(ConcurrentPropertyChange aConcurrentPropertyChange,
			Object[] aParameters
			
			) {
		Integer startSequenceNumber = null;
		Integer stopSequenceNumber = null;
		String threadSelector;
		String sourceSelector;
		String propertySelector;
		String oldValueSelector;
		String newValueSelector;
		
		if (aParameters.length != 7) {
			System.out.println(Arrays.toString(aParameters) + "should have six elements");
			return false;
		}
		if (aParameters[0] != null) {
			startSequenceNumber = (Integer) aParameters[0];
		}
		if (aParameters[1] != null) {

			stopSequenceNumber = (Integer) aParameters[1];
		}
		threadSelector = (String) aParameters[2];
		sourceSelector = (String) aParameters[3];
		propertySelector = (String) aParameters[4];
		oldValueSelector = (String) aParameters[5];
		newValueSelector = (String) aParameters[6];
		int aSequenceNumber = aConcurrentPropertyChange.getSequenceNumber();
		String aThreadName = aConcurrentPropertyChange.getThread().getName();
		String aSource = aConcurrentPropertyChange.getEvent().getSource().toString();

		String aPropertyName = aConcurrentPropertyChange.getEvent().getPropertyName().toString();
		String anOldValue = aConcurrentPropertyChange.getEvent().getOldValue().toString();
		String aNewValue = aConcurrentPropertyChange.getEvent().getNewValue().toString();
		boolean retVal = (startSequenceNumber == null || aConcurrentPropertyChange.getSequenceNumber() >= startSequenceNumber)
				&& (stopSequenceNumber == null || aSequenceNumber <= stopSequenceNumber)
				&& (threadSelector == null || aThreadName.matches(threadSelector))
				&& (sourceSelector == null
						|| aSource.matches(sourceSelector))
				&&  (propertySelector == null
						|| aPropertyName.toString().matches(propertySelector))
				&& (oldValueSelector == null
						|| anOldValue.toString().matches(oldValueSelector))
				&& (newValueSelector == null
						|| aNewValue.toString().matches(newValueSelector));
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
		Selector<ConcurrentPropertyChange> aSelector = new ParameterizedPropertyChangeSelector(aMatchedComponents);
		List<ConcurrentPropertyChange> retValList = new ArrayList<ConcurrentPropertyChange>();
		return selectEvents(anOriginalEventList, aSelector);
	}

	public static Selector<ConcurrentPropertyChange> createSelector(Object[][] aMatchedComponentsList, int anIndex) {
		if (anIndex >= aMatchedComponentsList.length) {
			return null;
		}
		return new ParameterizedPropertyChangeSelector(aMatchedComponentsList[anIndex]);
	}

	public static boolean matches(ConcurrentPropertyChange[] anOriginalEvents,

			Object[][] aMatchedComponentsList) {
		List<Integer> anIndices = indicesOf(anOriginalEvents, aMatchedComponentsList);
		if (anIndices == null || 
				anIndices.size() != aMatchedComponentsList.length ) {
			System.err.println(
					Arrays.toString(anOriginalEvents) + " does not match " + Arrays.toString(aMatchedComponentsList));
			return false;
		}
		return true;
		

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
	public static List<Integer> indicesOf(ConcurrentPropertyChange[] anOriginalEvents,

			Object[][] aMatchedComponentsList) {
		List<Integer> retVal = new ArrayList();

		if (anOriginalEvents == null) {
			System.err.println("Null event array passed to matches");
			return null;
		}

		int aMatchIndex = 0;
		Selector aSelector = createSelector(aMatchedComponentsList, aMatchIndex);

//		for (ConcurrentPropertyChange anEvent : anOriginalEvents) {
		for (int anEventIndex = 0; anEventIndex < anOriginalEvents.length; anEventIndex++) {
			ConcurrentPropertyChange anEvent = anOriginalEvents[anEventIndex];
			if (aSelector == null) {
				return retVal; // we have gone through all of components to be matched
			}
			if (aSelector.selects(anEvent)) {
				retVal.add(anEventIndex);
				aMatchIndex++;
				aSelector = createSelector(aMatchedComponentsList, aMatchIndex);
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
			if (!matches(aThreadToEvents.get(aThread), aMatchedComponentsList)) {
				return false;
			}
		}
		return true;

	}
	public static boolean matchesEachThread(ConcurrentPropertyChange[] anOriginalEvents, 
			Object[][] aMatchedComponentsList) {

		Map<Thread, ConcurrentPropertyChange[]> aThreadToEvents = getConcurrentPropertyChangesByThread(
				anOriginalEvents);
		for (Thread aThread : aThreadToEvents.keySet()) {
			if (!matches(aThreadToEvents.get(aThread), aMatchedComponentsList)) {
				return false;
			}
		}
		return true;

	}

	public static boolean processInterleaving (
			ConcurrentPropertyChange[]  aThreadEvents,
			List<Integer> aStartIndices,
			List<Integer> aStopIndices
			) {
		int aStartIndex = aThreadEvents[0].getSequenceNumber();
		int aStopIndex = aThreadEvents[aThreadEvents.length - 1].getSequenceNumber();
		for (int aStartIndicesIndex = 0; aStartIndicesIndex < aStartIndices.size(); aStartIndicesIndex++ ) {
			int aPreviousStartIndex = aStartIndices.get(aStartIndicesIndex);
			int aPreviousStopIndex = aStopIndices.get(aStartIndicesIndex);
			boolean aNonOverlapping =
					aPreviousStartIndex > aStopIndex ||
					aStartIndex > aPreviousStopIndex;
			if (aNonOverlapping) {
				return false;
			}
		}
		aStartIndices.add(aStartIndex);
		aStopIndices.add(aStopIndex);
		return true;
	}

	public static boolean nonInterleaved(ConcurrentPropertyChange[] anOriginalEvents, Thread[] aThreads,
			Object[] aMatchedComponents) {

		Map<Thread, ConcurrentPropertyChange[]> aThreadToEvents = getConcurrentPropertyChangesByThread(
				anOriginalEvents);
		List<Integer> aStartIndices = new ArrayList();
		List<Integer> aStopIndices = new ArrayList();
		for (Thread aThread : aThreads) {
			ConcurrentPropertyChange[] aThreadEvents = aThreadToEvents.get(aThread);
			ConcurrentPropertyChange[] aSelectedEvents = selectEvents(aThreadEvents, aMatchedComponents);
			if (!processInterleaving(aThreadEvents, aStartIndices, aStopIndices)) {
				Tracer.info(ConcurrentEventUtility.class,"Events of thread:" +
							aThread + ":" + 
							Arrays.toString(aSelectedEvents) +
							" overlap with those of matched threads");
				Tracer.info (ConcurrentEventUtility.class, "Start indices of matched threads:" + aStartIndices);
				Tracer.info(ConcurrentEventUtility.class,"Stop indices of matched threads:" + aStopIndices);
				return false;
			}
		}
		return true;

	}
	
	public static int getMaxIndex(
			ConcurrentPropertyChange[] anOriginalEvents, 
			Thread[] aThreads,
			Object[] aMatchedComponents
			) {

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
	
	public static int getMinIndex(
			ConcurrentPropertyChange[] anOriginalEvents, 
			Thread[] aThreads,
			Object[] aMatchedComponents
			) {

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
	
	
	
	public static boolean occurAfter(
			ConcurrentPropertyChange[] anOriginalEvents, 
			Thread[] aBeforeThreads,
			Object[] aBeforeThreadMatchedComponents,
			Thread[] anAfterThreads,
			Object[] anAfterThreadMatchedComponents) {

		Map<Thread, ConcurrentPropertyChange[]> aThreadToEvents = getConcurrentPropertyChangesByThread(
				anOriginalEvents);
		
		int aBeforeThreadMaxIndex = 
				getMaxIndex(anOriginalEvents, aBeforeThreads, aBeforeThreadMatchedComponents);
		int anAfterThreadMinIndex =
				getMinIndex(anOriginalEvents, anAfterThreads, anAfterThreadMatchedComponents);
		boolean retVal = anAfterThreadMinIndex > aBeforeThreadMaxIndex;
		if (!retVal) {
			System.err.println(
					"Max sequence number of threads:  " +
					 Arrays.toString(aBeforeThreads) + 
					 " = " +
					 aBeforeThreadMaxIndex +
					 " > " +
					 "Min sequence number of threads: " +
					 Arrays.toString(anAfterThreads) + 
					 " = " +
					 anAfterThreadMinIndex
					);
		}
		return retVal ;

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
	public static <EventType> Map<Thread, List<ConcurrentPropertyChange>> getConcurrentPropertyChangeListByThread(
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
	public static <EventType> Map<Thread, ConcurrentPropertyChange[]> getConcurrentPropertyChangesByThread(
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

	public static Map<Object, ConcurrentPropertyChange[]> getConcurrentPropertyChangesBySource(
			ConcurrentPropertyChange[] anOriginalEvents) {
		Map<Object, List<ConcurrentPropertyChange>> anEventListByThread = getConcurrentEventListBySource(
				anOriginalEvents);
		Map<Object, ConcurrentPropertyChange[]> retVal = new HashMap<Object, ConcurrentPropertyChange[]>();
		for (Object aSource : anEventListByThread.keySet()) {
			retVal.put(aSource, (ConcurrentPropertyChange[]) anEventListByThread.get(aSource).toArray());
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

	public static Map<Object, List<ConcurrentEvent<PropertyChangeEvent>>> getEventsBySource() {
		return null;

	}
//	public static  Map<Object,List<PropertyChangeEvent>> getPropertyChangeEventsBySource() {
//		return (Map<Object,List<PropertyChangeEvent>>) getEventsBySource();
//		
//	}
}
