package gradingTools.shared.testcases.concurrency.propertyChanges;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConcurrentPropertyChangeThreadMatchesSelector implements Selector<ConcurrentPropertyChangeSupport> {
	Object[] parameters;
	int numMatches;
	int numThreads;
	String threadPattern;
	Map<Thread, Integer> threadToMatches = new HashMap();
	public ConcurrentPropertyChangeThreadMatchesSelector (Object[] aParameters,  int aNumThreads, int aNumMatches, String aThreadPattern) {
		parameters = aParameters;
		numMatches = aNumMatches;
		numThreads = aNumThreads;
		threadPattern = aThreadPattern;
		
		
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
		return true;
	}
	
	protected boolean checkThreadMatches() {
		if (threadToMatches.size() < numThreads) {
			return false;
		}
		for (Thread aThread: threadToMatches.keySet()) {
			if (threadToMatches.get(aThread) < numMatches) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean selects(ConcurrentPropertyChangeSupport aConcurrentPropertyChangeSupport) {
		ConcurrentPropertyChange aNewEvent = aConcurrentPropertyChangeSupport.getLastConcurrentPropertyChange();
//		System.out.println("new event:" + aNewEvent);
		if (ConcurrentEventUtility.matches(aNewEvent, parameters)) {
			if (maybeIncrementThreadMatches(aNewEvent) )
			return checkThreadMatches();

//			System.out.println("new event matched:" + Arrays.toString(parameters));

		}
		return  false;
	}

}
