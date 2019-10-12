package grader.basics;

import grader.basics.config.BasicExecutionSpecificationSelector;
import trace.grader.basics.GraderBasicsTraceUtility;
import util.trace.Tracer;

public class StudentTestUseAPI {
//	public static RunningProject create(File aWorkingDirectory, InputGenerator anOutputBasedInputGenerator, String[] command, String input,
//			String[] args, int timeout) throws NotRunnableException {
//		 Runner processRunner = new BasicProcessRunner(aWorkingDirectory);
//	     return processRunner.run(null, command, "", args, 3000);
//	}
	public static void tracerShowInfo(boolean newVal) {
		Tracer.showInfo(newVal);
	}
	public static void setBufferTracedMessages(boolean newVal) {
		GraderBasicsTraceUtility.setBufferTracedMessages(false);
	}
	public static void setStudentProcessTimeout(int aTimeout) {
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setStudentProcessTimeOut(aTimeout);
	}
	
}
