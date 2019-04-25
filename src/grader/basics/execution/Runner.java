package grader.basics.execution;

import grader.basics.util.TimedProcess;
import util.pipe.InputGenerator;

/**
 * The interface for different runners to use
 */
public interface Runner {
	

    public RunningProject run(String input) throws NotRunnableException;

    public RunningProject run(String input, int timeout) throws NotRunnableException;
    
    public RunningProject run(InputGenerator anOutputBasedInputGenerator, String input, int timeout) throws NotRunnableException;


    public RunningProject run(String input, String[] args, int timeout) throws NotRunnableException;

    RunningProject run(InputGenerator anOutputBasedInputGenerator, String[] command, String input,
			String[] args, int timeout) throws NotRunnableException;

    RunningProject run(InputGenerator aDynamicInputProvider, String anEntryPoint, String input,
			String[] args, int timeout) throws NotRunnableException;

	TimedProcess run(RunningProject aRunner, InputGenerator anOutputBasedInputGenerator, String[] command, String input,
			String[] args, int timeout, String aProcess, boolean wait) throws NotRunnableException;

	RunningProject run(InputGenerator aDynamicInputProvider,
			String input,
			String[] args, int timeout) throws NotRunnableException;

	void terminateProcess();

	String getSpecifiedMainClass();

	void setSpecifiedMainClass(String specifiedMainClass);

//	BasicRunningProject runMainClass(Class aClass, String input, String[] args,
//			int timeout) throws NotRunnableException;

}
