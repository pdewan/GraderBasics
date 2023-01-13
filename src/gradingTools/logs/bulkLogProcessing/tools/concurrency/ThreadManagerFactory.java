package gradingTools.logs.bulkLogProcessing.tools.concurrency;

public class ThreadManagerFactory {

	private static ThreadManager tm;
	
	public static ThreadManager getThreadManager() {
		if(tm==null)
			tm=new ThreadManager();
		return tm;
	}
	
	
	public static void terminateThreadManager() {
		tm.end();
	}
	
}
