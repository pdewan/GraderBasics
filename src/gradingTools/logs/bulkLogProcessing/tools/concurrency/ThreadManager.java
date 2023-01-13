package gradingTools.logs.bulkLogProcessing.tools.concurrency;

import java.util.concurrent.ArrayBlockingQueue;

public class ThreadManager {

	private ArrayBlockingQueue<Task> tasks = new ArrayBlockingQueue<>(1000);
	private TaskRunnable [] runnables = new TaskRunnable[8];
	private Thread [] threads = new Thread[runnables.length];
	
	public ThreadManager() {
		for(int i=0;i<runnables.length;i++) {
			runnables[i]=new TaskRunnable(this);
			threads[i]=new Thread(runnables[i]);
			threads[i].setName("Thread: "+i);
			threads[i].start();
		}
	}
	
	public boolean addTask(Task t) {
		return tasks.offer(t);
	}
	
	public Task getTask() throws InterruptedException {
		return tasks.take();
	}
	
	public void end() {
		for(Thread t:threads)
			t.interrupt();
	}
	
	
}
