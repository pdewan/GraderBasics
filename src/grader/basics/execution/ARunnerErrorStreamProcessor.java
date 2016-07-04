package grader.basics.execution;

import java.io.InputStream;

public class ARunnerErrorStreamProcessor extends ARunnerErrorOrOutStreamProcessor implements Runnable {
//	protected Scanner scanner ;
//	protected InputStream out;
//	protected RunningProject runner;
	public ARunnerErrorStreamProcessor(InputStream aProcessErrorOut, 
			RunningProject aRunner, 
			/*Semaphore aSemaphore, */
			String aProcessName,
			Boolean anOnlyProcess) {
		super(aProcessErrorOut, aRunner, /*aSemaphore,*/ aProcessName, anOnlyProcess);
	}

@Override
public void processLine(String s) {
	runner.appendErrorOutput(s + "\n"); // cumulative
	if (processName != null) {
		System.err.println(outPrefix + s);

		runner.appendErrorOutput(processName, s + "\n"); // per process
	}
	
}
		

//			@Override
//			public void run() {
//				while (scanner.hasNextLine()) {
//					String line = scanner.nextLine();
//					System.err.println(line);
//					runner.appendErrorOutput(line + "\n");
//				}
//				scanner.close();
//			}
		
	}
