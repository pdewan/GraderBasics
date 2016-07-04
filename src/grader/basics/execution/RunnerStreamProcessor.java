package grader.basics.execution;

import java.util.concurrent.Semaphore;

public interface RunnerStreamProcessor {
	

	public RunningProject getRunner() ;


	public Semaphore getSemaphore() ;


	public String getProcessName() ;

	public Boolean getOnlyProcess() ;

}
