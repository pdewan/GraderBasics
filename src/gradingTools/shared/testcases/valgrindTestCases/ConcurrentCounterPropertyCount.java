package gradingTools.shared.testcases.valgrindTestCases;

public class ConcurrentCounterPropertyCount extends ExpectedPropertyCount{
	Object[][] propertyCounts = { 
			{"lock", 40, 50},
			{"init", 2, 2}				
	
	};
	@Override
	protected Object[][] propertyCounts() {
		return propertyCounts;
	}
	@Override
	protected Class precedingTest() {
		return ConcurrentCounterTracesProduced.class;
	}
	
}
