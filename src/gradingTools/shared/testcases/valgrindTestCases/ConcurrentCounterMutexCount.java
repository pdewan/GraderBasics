package gradingTools.shared.testcases.valgrindTestCases;

public class ConcurrentCounterMutexCount extends ExpectedSourceCount{
	@Override
	protected Class precedingTest() {
		return ConcurrentCounterTracesProduced.class;
	}
	@Override
	int expectedNumSourcesTraced() {
//		return 3;
		return 5; // 2 mutexes, 2 threads, 1 implicit mutex for join

	}
}
