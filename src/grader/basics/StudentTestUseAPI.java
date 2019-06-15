package grader.basics;

import java.io.File;

import grader.basics.execution.BasicExecutionSpecification;
import grader.basics.execution.BasicExecutionSpecificationSelector;
import grader.basics.execution.BasicProcessRunner;
import grader.basics.execution.NotRunnableException;
import grader.basics.execution.Runner;
import grader.basics.execution.RunningProject;
import trace.grader.basics.GraderBasicsTraceUtility;
import util.pipe.InputGenerator;
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
