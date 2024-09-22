package gradingTools.shared.testcases.valgrindTestCases;

public class ConcurrentCounterPropertyCount extends ExpectedPropertyCount{
	Object[][] propertyCounts = { 
			{"lock", 46, 46},
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
