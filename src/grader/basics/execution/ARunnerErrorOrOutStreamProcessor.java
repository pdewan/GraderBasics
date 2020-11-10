package grader.basics.execution;

import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import util.trace.Tracer;

public abstract class ARunnerErrorOrOutStreamProcessor implements RunnerErrorOrOutStreamProcessor {
	protected Scanner scanner;
	protected InputStream errorOrOut;
	protected RunningProject runner;
	protected Semaphore semaphore;
	protected String processName;
	protected Boolean onlyProcess;
	protected String outPrefix = "";
//	public static final String MAIN_PROCESS_NAME = "";
	
	abstract protected void handleEndOfOutput();

	public ARunnerErrorOrOutStreamProcessor(InputStream aProcessErrorOrOut, RunningProject aRunner,
			/* Semaphore aSemaphore, */ String aProcessName, Boolean anOnlyProcess) {
		// Print error output to the console
		errorOrOut = aProcessErrorOrOut;
		scanner = new Scanner(errorOrOut);
		runner = aRunner;
		semaphore = new Semaphore(1);
		
		processName = aProcessName == null?BasicProcessRunner.MAIN_ENTRY_POINT:aProcessName;
//		processName = aProcessName == null?MAIN_PROCESS_NAME:aProcessName;

		onlyProcess = anOnlyProcess;
		if (!onlyProcess)
			outPrefix = "(" + aProcessName + ")";
	}

	@Override
	public void run() {
		try {
			if (onlyProcess)
				semaphore.acquire();
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				// System.err.println(line);
				// runner.appendErrorOutput(line + "\n");
				processLine(line);
			}
//			Tracer.info (this, "No more output");
//			processLine(END_OF_OUPUT);
			handleEndOfOutput();
			scanner.close();
			semaphore.release();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// abstract void processLine(String s);

	public Scanner getScanner() {
		return scanner;
	}

	public InputStream getErrorOrOut() {
		return errorOrOut;
	}

	public RunningProject getRunner() {
		return runner;
	}

	public Semaphore getSemaphore() {
		return semaphore;
	}

	public String getProcessName() {
		return processName;
	}

	public Boolean getOnlyProcess() {
		return onlyProcess;
	}

}
