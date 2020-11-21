package grader.basics.execution;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.concurrent.Semaphore;

import grader.basics.config.BasicConfigurationManagerSelector;
import grader.basics.config.BasicExecutionSpecification;
import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.config.BasicStaticConfigurationUtils;
import util.trace.Tracer;

public class ARunnerInputStreamProcessor implements RunnerInputStreamProcessor{
	protected OutputStream input;
	protected RunningProject runner;
	protected Semaphore semaphore; //not sure what we can do with this but symmetry with error and out
	protected String processName;
	protected Boolean onlyProcess;
	OutputStreamWriter inputWriter;
//	public static final String IN_PROMPT = "$";
	public static final String IN_PROMPT = "Provided Input:";

	String inPrefix = IN_PROMPT;
	
	public ARunnerInputStreamProcessor(
			OutputStream anInput, RunningProject aRunner,  String aProcessName, /*Semaphore aSemaphore,*/ Boolean anOnlyProcess
			) {
		input = anInput;
		 inputWriter = new OutputStreamWriter(
					anInput);
		runner = aRunner;
//		semaphore = aSemaphore;
		semaphore = new Semaphore(1);
		processName = aProcessName;
		onlyProcess = anOnlyProcess;
		if (!onlyProcess)
			inPrefix = aProcessName + "$";
	}
	static final int INPUT_INDEX_DIVISION = 1;
	public void newInput(String anInput) {
		if (anInput == null) return;
		
		int aBetweenInputDelay = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getBetweenInputDelay();
		try {
//			Tracer.info(this,inPrefix + anInput); // echo input for display
			String[] inputLines = anInput.split("\n");
			for (int anIndex = 0; anIndex < inputLines.length; anIndex++) {
				String anInputLine = inputLines[anIndex];
				if (anInputLine.isEmpty()) {
					continue;
				}
				Tracer.info(this,inPrefix + anInputLine); // echo input for display
				inputWriter.write(anInputLine + "\n");
				if (anIndex % INPUT_INDEX_DIVISION == 0) {
					Tracer.info(this,"flushing input and sleeping for:" + aBetweenInputDelay); // echo input for display

					inputWriter.flush();				
					Thread.sleep(aBetweenInputDelay);
				}

			}
//			inputWriter.write(anInput);
			inputWriter.flush();
//			ThreadSupport.sleep(200);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void terminateInput() {
		try {
			inputWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public OutputStream getInput() {
		return input;
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
	public OutputStreamWriter getInputWriter() {
		return inputWriter;
	}
	
	


}
