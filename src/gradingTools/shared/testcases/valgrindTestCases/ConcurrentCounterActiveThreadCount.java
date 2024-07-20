package gradingTools.shared.testcases.valgrindTestCases;

public class ConcurrentCounterActiveThreadCount extends ExpectedThreadCount{
	@Override
	protected Class precedingTest() {
		return ConcurrentCounterTracesProduced.class;
	}
	@Override
	int expectedNumThreadsTraced() {
		return 3;
	}
}
