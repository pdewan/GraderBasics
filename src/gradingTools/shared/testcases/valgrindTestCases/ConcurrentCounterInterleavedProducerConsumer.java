package gradingTools.shared.testcases.valgrindTestCases;

public class ConcurrentCounterInterleavedProducerConsumer extends InterleavedThreads{
	@Override
	protected Thread[] interleavedThreads() {
		Thread[] anAllThreads = allThreads();
		Thread[] retVal = new Thread[] {
				anAllThreads[1],
				anAllThreads[2]
		};
		return retVal;
		
	}
	
	@Override
	protected Class[] precedingTests() {
		return new Class[] {
				ConcurrentCounterTracesProduced.class,
				ConcurrentCounterActiveThreadCount.class
		};
	}
	
}
