package gradingTools.shared.testcases.valgrindTestCases;

public class ConcurrentCounterPropertyValueCount extends ExpectedPropertyValueCount{
	Object[][] propertyValueCounts = { 
			{true, 20, 25},
			{false, 20, 20}				
			};
	@Override
	protected Object[][] propertyValueCounts() {
		return propertyValueCounts;
	}
	@Override
	protected Class precedingTest() {
		return ConcurrentCounterTracesProduced.class;
	}
	
}
