package gradingTools.logs.bulkLogProcessing.tools.concurrency;

public class TaskRunnable implements Runnable {

	private final ThreadManager manager;
	
	public TaskRunnable(ThreadManager m) {
		manager=m;
	}
	
	@Override
	public void run() {
		for(;;) {
			try {
				Task t = manager.getTask();
				t.RunTask();
			}catch(InterruptedException e) {
				break;
			}
		}
	}

}
