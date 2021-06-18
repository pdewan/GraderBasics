package grader.basics.observers.logSending;

import java.util.concurrent.ArrayBlockingQueue;

public class ALogSendingRunnable implements Runnable {

	ArrayBlockingQueue<SendingData> logQueue = new ArrayBlockingQueue<>(25);
	
	public void addToQueue(SendingData log) {
		logQueue.add(log);
	}
	
	public void addToQueue(String log, String assignment, int intr) {
		logQueue.add(new SendingData(log,assignment,intr));
	}
	
	@Override
	public void run() {
			for(;;) 
				try {
					SendingData log = logQueue.take();
//					System.out.println("Log Taken " + System.currentTimeMillis());
					LogSender.sendToServer(log);
//					System.out.println("Log Sent " + System.currentTimeMillis());
				} catch (InterruptedException e) {
					break;
				} catch (Exception e) {
					  System.err.println("Error sending log: "+e.getMessage());
				}
	}

}
