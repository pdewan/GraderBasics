package gradingTools.logs.bulkLogProcessing.tools.concurrency;

public class Joiner {

	private final int numThreads;
	private int currentThreads;
	private boolean threadHadException=false;
	
	public Joiner(int numThreads) {
		this.numThreads=numThreads;
		currentThreads=0;
	}
	
	public synchronized void join(boolean hadException) {
		currentThreads++;
		if(hadException)
			threadHadException=true;
		if(numThreads==currentThreads)
			notify();
	}
	
	public synchronized void finish() throws Exception {
		if(currentThreads!=numThreads)
			wait();
		if(threadHadException)
			throw new Exception("error when parsing logs");
	}
	
}
