package gradingTools.shared.testcases.valgrindTestCases;

public class ConcurrentCounterCreatedThreadCount extends ExpectedCreatedThreadCount{
	@Override
	protected Class precedingTest() {
		return ConcurrentCounterTracesProduced.class;
	}
	@Override
	int expectedNumThreadsCreated() {
		return 2; // 2 threads created
	}

}
