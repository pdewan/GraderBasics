package grader.basics.concurrency.propertyChanges;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import util.trace.Tracer;

public class ConcurrentPropertyChangeThreadMatchesSelector implements Selector<ConcurrentPropertyChangeSupport> {
	Object[] parameters;
	int numMatches;
	int numThreads;
	String threadPattern;
	Map<Thread, Integer> threadToMatches = new HashMap();
	Map<Thread, Long> threadToLastEventTimes = new HashMap();

	long minimumEventDelayPerThread;
	
//	long lastEventTime = 0;
//	int minThreads = 0;


	public ConcurrentPropertyChangeThreadMatchesSelector (Object[] aParameters,  int aNumThreads, int aNumMatches, String aThreadPattern, long aMinDelay) {
		parameters = aParameters;
		numMatches = aNumMatches;
		numThreads = aNumThreads;
		threadPattern = aThreadPattern;
		minimumEventDelayPerThread = aMinDelay;
		
		Tracer.info(this, "Num threads to be matched:" + numThreads + " " +
		"minimumEventDelayPerThread:" +  minimumEventDelayPerThread + " " +
		"numMatches:" + numMatches + " " + 
		"threadPattern:" + aThreadPattern + " " +
		"parameters:" + Arrays.toString(parameters) + " " +
		"threadPattern:" + aThreadPattern);
		
//		minThreads = aMinThreads;
		
		
	}
	protected boolean checkEventSeparation(Thread aThread, ConcurrentPropertyChange aNewEvent) {
		long aNewEventTime = aNewEvent.getRelativeTime();
		Long lastEventTime = threadToLastEventTimes.get(aThread);
		return (lastEventTime == null  
				|| (aNewEventTime - lastEventTime) >= minimumEventDelayPerThread);
			
	}
	protected boolean maybeIncrementThreadMatches (ConcurrentPropertyChange anEvent) {
		Thread aThread = anEvent.getThread();
		if (threadPattern != null && !aThread.getName().matches(threadPattern)) {
			
			return false;
		}
		Integer aNumMatches = threadToMatches.get(aThread);
		
		if (aNumMatches == null) {
			aNumMatches = 1;			
		} else {
			aNumMatches++;
		}
		threadToMatches.put(aThread, aNumMatches);
//		System.out.println("Incremented matches for thread" + aThread);

		return true;
	}
	
	protected boolean checkThreadMatches() {
		if (threadToMatches.size() < numThreads) {
			return false;
		}
		for (Thread aThread: threadToMatches.keySet()) {
			int anActualMatches = threadToMatches.get(aThread);
			if (anActualMatches < numMatches) {
//				System.out.println("Num matches " + anActualMatches + " for thread:" +  aThread + " < " + numMatches);

				return false;
			}
			Tracer.info(this, "Num matches " + anActualMatches + " for thread:" +  aThread + " == " + numMatches);

		}
		Tracer.info("Num matches for all threads == " + numMatches);

		return true;
	}
	
	@Override
	public boolean selects(ConcurrentPropertyChangeSupport aConcurrentPropertyChangeSupport) {
		ConcurrentPropertyChange aNewEvent = aConcurrentPropertyChangeSupport.getLastConcurrentPropertyChange();
		Thread aThread = aNewEvent.getThread();
		boolean anEventsSeparated = checkEventSeparation(aThread, aNewEvent);
		threadToLastEventTimes.put(aThread, aNewEvent.getRelativeTime());
		if (!anEventsSeparated) {
			return false;
		}
//		System.out.println("Events separated by delay for thread:" + aThread);
		
		//		System.out.println("new event:" + aNewEvent);
		if (ConcurrentEventUtility.matches(aNewEvent, parameters)) {
			if (maybeIncrementThreadMatches(aNewEvent) )
			return checkThreadMatches();

//			System.out.println("new event matched:" + Arrays.toString(parameters));

		}
		return  false;
	}

}
