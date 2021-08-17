package gradingTools.shared.testcases.concurrency.propertyChanges;

import java.util.Arrays;

public class ConcurrentPropertyChangeMatchesSelector implements Selector<ConcurrentPropertyChangeSupport> {
	Object[] parameters;
	int numMatches;
	int matchesSoFar= 0;
	long lastEventTime = 0;
	long minDelay;
	int minThreads;
	public ConcurrentPropertyChangeMatchesSelector (Object[] aParameters, int aMinMatches, long aMinDelay, int aMinThreads) {
		parameters = aParameters;
		numMatches = aMinMatches;
		minDelay = aMinDelay;
		minThreads = aMinThreads;
		
	}
	protected boolean checkEventSeperation(ConcurrentPropertyChange aNewEvent) {
		long aNewEventTime = aNewEvent.getRelativeTime();
		return (numMatches == 0  
				|| (aNewEventTime - lastEventTime) >= minDelay);
			
	}
	@Override
	public synchronized boolean selects(ConcurrentPropertyChangeSupport aConcurrentPropertyChangeSupport) {
		ConcurrentPropertyChange aNewEvent = aConcurrentPropertyChangeSupport.getLastConcurrentPropertyChange();
//		System.out.println("new event:" + aNewEvent);
		long aNewEventTime = aNewEvent.getRelativeTime();
//		if (numMatches > 0  // not first event
//				&& (aNewEventTime - lastEventTime) < minDelay) {
//			lastEventTime = System.currentTimeMillis();
//			return false;
//		}
//		long aNewEventTime = aNewEvent.getRelativeTime();
		boolean anEventsSeparated = checkEventSeperation(aNewEvent);
		lastEventTime = aNewEvent.getRelativeTime();
		if (!anEventsSeparated) {
			return false;
		}

		if (ConcurrentEventUtility.matches(aNewEvent, parameters)) {
			matchesSoFar++;
//			System.out.println("new event matched:" + Arrays.toString(parameters));

		}
		if (matchesSoFar >= numMatches && 
				aConcurrentPropertyChangeSupport.getNotifyingThreads().length >= minThreads) {
//			System.out.println("Found required matches:" + numMatches);

			return true;
		}
		return false;
	}

}
