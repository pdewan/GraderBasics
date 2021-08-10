package gradingTools.shared.testcases.concurrency.propertyChanges;

public class ConcurrentPropertyChangeHistoryMatchesSelector implements Selector<ConcurrentPropertyChangeSupport> {
	Object[][] matchedComponentsList;
	int numMatches;
	public ConcurrentPropertyChangeHistoryMatchesSelector (Object[][] aMatchedComponentsList, int aNumMatches) {
		matchedComponentsList = aMatchedComponentsList;
		numMatches = aNumMatches;
	}
	@Override
	public boolean selects(ConcurrentPropertyChangeSupport aConcurrentPropertyChangeSupport) {
		ConcurrentPropertyChange aNewEvent = aConcurrentPropertyChangeSupport.getLastConcurrentPropertyChange();
		return ConcurrentEventUtility.matches(aConcurrentPropertyChangeSupport.getConcurrentPropertyChanges(), matchedComponentsList, numMatches);
	}
}
