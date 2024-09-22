package gradingTools.shared.testcases.valgrindTestCases;

public class ConcurrentCounterPropertyValueCount extends ExpectedPropertyValueCount{
	Object[][] propertyValueCounts = { 
			{true, 24, 24}, // join lock + initial lock and and a final lock that does not get unlocked
			{false, 22, 22}	 // join unlock, extra unlock of first value			
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
