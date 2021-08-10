package gradingTools.shared.testcases.concurrency.propertyChanges;

import java.util.Arrays;

public class ConcurrentPropertyChangeMatchesSelector implements Selector<ConcurrentPropertyChangeSupport> {
	Object[] parameters;
	int numMatches;
	int matchesSoFar= 0;
	public ConcurrentPropertyChangeMatchesSelector (Object[] aParameters, int aNumMatches) {
		parameters = aParameters;
		numMatches = aNumMatches;
		
	}
	@Override
	public boolean selects(ConcurrentPropertyChangeSupport aConcurrentPropertyChangeSupport) {
		ConcurrentPropertyChange aNewEvent = aConcurrentPropertyChangeSupport.getLastConcurrentPropertyChange();
//		System.out.println("new event:" + aNewEvent);
		if (ConcurrentEventUtility.matches(aNewEvent, parameters)) {
			matchesSoFar++;
//			System.out.println("new event matched:" + Arrays.toString(parameters));

		}
		if (matchesSoFar >= numMatches) {
//			System.out.println("Found required matches:" + numMatches);

			return true;
		}
		return false;
	}

}
