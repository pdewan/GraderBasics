package gradingTools.shared.testcases.concurrency;

public interface TestJoiner {
	public void finished();
	public void finish();
	public void join();
}
