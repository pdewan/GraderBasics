package gradingTools.shared.testcases.valgrindTestCases;

public class ConcurrentCounterMutexCount extends ExpectedSourceCount{
	@Override
	protected Class precedingTest() {
		return ConcurrentCounterTracesProduced.class;
	}
	@Override
	int expectedNumSourcesTraced() {
		return 3;
	}
}
