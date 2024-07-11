package gradingTools.shared.testcases.valgrindTestCases;

public class ConcurrentCounterTracesProduced extends ValgrindOutputProduced{
	protected int maxOutputLines() {
		return 20000;
	}
	protected int maxTraceRepetitions() {
		return 10;
	}

}
