package gradingTools.logs.localChecksStatistics.dataStorage;

public class SuiteTestPairings {

	public final String testName;
	public final String suiteName;
	
	public SuiteTestPairings (String suite,String test) {
		testName=test;
		suiteName=suite;
	}
	
	public boolean equals(SuiteTestPairings stp) {
		return testName.equals(stp.testName) && suiteName.equals(stp.suiteName);
	}
	
}
