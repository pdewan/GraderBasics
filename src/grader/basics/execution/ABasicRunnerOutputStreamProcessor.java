package grader.basics.execution;

import java.io.InputStream;

import util.trace.Tracer;

public class ABasicRunnerOutputStreamProcessor extends ARunnerErrorOrOutStreamProcessor implements Runnable {
	// protected Scanner scanner ;
	// protected InputStream out;
	// protected RunningProject runner;
	public ABasicRunnerOutputStreamProcessor(InputStream aProcessErrorOut, RunningProject aRunner,
			/* Semaphore aSemaphore, */ String aProcessName, Boolean anOnlyProcess) {
		super(aProcessErrorOut, aRunner, /* aSemaphore, */ aProcessName, anOnlyProcess);

	}

	protected void maybeTraceLine(String s) {
		// if (StaticConfigurationUtils.getTrace()) {
		//
		// if (Tracer.isInfo(s))
		// return; // do not create console output info for an info event
		// ConsoleOutput consoleOutput = ConsoleOutput.newCase(s, this);
		// String infoString = Tracer.toInfo(consoleOutput, consoleOutput.getMessage());
		// if (infoString != null)
		// runner.appendProcessOutput(processName, infoString);
		// }
	}
	
	public static  int maxOutputLines = Tracer.getMaxTraces();
	 public static int getMaxOutputLines() {
		return maxOutputLines;
	}

	public static void setMaxOutputLines(int maxOutputLines) {
		ABasicRunnerOutputStreamProcessor.maxOutputLines = maxOutputLines;
	}

	int numLinesOutput = 0;
	boolean linesErrorGiven = false;

	@Override
	public synchronized void processLine(String s) {
		if (numLinesOutput > getMaxOutputLines()) {
			if (!linesErrorGiven) {
				linesErrorGiven = true;
				Tracer.error("Num output lines exceeded " + getMaxOutputLines());
							}
			return;
		}
		numLinesOutput++;
		// Printing moved to BasicRunningProject when signaled instead of when received
		// Tracer.info(this,"Process line:" + s);
	    String aNewOutputLine = s + "\n";
	    
//		runner.appendCumulativeOutput(s + "\n"); // append cumulative output
		runner.appendCumulativeOutput(aNewOutputLine ); // append cumulative output

		// if (processName != null) {

		// Printing moved to BasicRunningProject when signaled instead of when received
		// Tracer.info(this,outPrefix + s);

		// runner.appendErrorOutput(processName, s + "\n");
//		runner.appendProcessOutput(processName, s + "\n"); // append this process output
		runner.appendProcessOutput(processName, s); // append this process output
		// }
//		Tracer.info(this, "Appended output:" + s);
		maybeTraceLine(s);
		// if (StaticConfigurationUtils.getTrace()) {
		//
		// if (Tracer.isInfo(s))
		// return; // do not create console output info for an info event
		// ConsoleOutput consoleOutput = ConsoleOutput.newCase(s, this);
		// String infoString = Tracer.toInfo(consoleOutput, consoleOutput.getMessage());
		// if (infoString != null)
		// runner.appendProcessOutput(processName, infoString);
		// }
	}

	@Override
	protected void handleEndOfOutput() {
		processLine(END_OF_OUPUT);
	}

	// @Override
	// public void run() {
	// while (scanner.hasNextLine()) {
	// String line = scanner.nextLine();
	// System.err.println(line);
	// runner.appendErrorOutput(line + "\n");
	// }
	// scanner.close();
	// }

}
