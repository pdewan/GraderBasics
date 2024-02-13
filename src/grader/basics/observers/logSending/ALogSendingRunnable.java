package grader.basics.observers.logSending;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.ArrayBlockingQueue;

import grader.basics.observers.LogEntryKind;

public class ALogSendingRunnable implements Runnable, WindowListener {

	ArrayBlockingQueue<SendingData> logQueue = new ArrayBlockingQueue<>(100);
	private static ALogSendingRunnable instance;
	public static ALogSendingRunnable getInstance() {
		if (instance == null) {
			instance = new ALogSendingRunnable(); 
		}
		return instance;
	}
	private ALogSendingRunnable() {
//		instance = this;
	}
	public void addToQueue(SendingData log) {
		logQueue.add(log);
	}
	
//	public void addToQueue(boolean isTests, String aLogFileName, String log, String assignment, int intr) {
//		logQueue.add(new SendingData(isTests, aLogFileName, log,assignment,intr));
//	}
	
	public void addToQueue(LogEntryKind aLogEntryKind, String aLogFileName, String log, String assignment, int intr) {
		logQueue.add(new SendingData(aLogEntryKind, aLogFileName, log,assignment,intr));
	}
	
	private boolean end=false;
	public void endProcess(boolean b) {
		end=b;
		if(logQueue.isEmpty())
			logQueue.add(null);
	}
	
	@Override
	public void run() {
			for(;;) 
				try {
					SendingData log = logQueue.take();
//					System.out.println("Log Taken " + System.currentTimeMillis());
					if(end)
						break;
					LocalChecksLogSender.sendToServer(log);
//					System.out.println("Log Sent " + System.currentTimeMillis());
				}  catch (Exception e) {
//					  System.err.println("Error sending log: "+e.getMessage());
				}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		LocalChecksLogSender.appendStatistics();
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {
		LocalChecksLogSender.appendStatistics();

		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
