package grader.basics;

import java.io.File;
import java.util.Map;

import grader.basics.execution.BasicExecutionSpecification;
import grader.basics.execution.BasicExecutionSpecificationSelector;
import grader.basics.execution.BasicProcessRunner;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.BasicRunningProject;
import grader.basics.execution.NotRunnableException;
import grader.basics.execution.Runner;
import grader.basics.execution.RunningProject;
import trace.grader.basics.GraderBasicsTraceUtility;
import util.pipe.InputGenerator;
import util.trace.Tracer;
import grader.basics.project.Project;

public class TestImplementationAPI {
	public static RunningProject createRunningProject(File aWorkingDirectory, InputGenerator anOutputBasedInputGenerator, String[] command, int timeout, String[] inputs,
			String[] args) throws NotRunnableException {
		 String input = (inputs == null || inputs.length == 0)?null:BasicProjectExecution.toInputString(inputs);
		 Runner processRunner = new BasicProcessRunner(aWorkingDirectory);
		 RunningProject aRunningProject =  
				 
					 new BasicRunningProject(null, anOutputBasedInputGenerator, input);

//				 BasicProcessRunner.createRunningProject(null, anOutputBasedInputGenerator, input);
//	     return processRunner.run(null, command, "", args, 3000);
	    processRunner.run(aRunningProject, anOutputBasedInputGenerator, command, input, args, timeout, "main", true);
	    return aRunningProject;
	}
	public static RunningProject createRunningProject(File aWorkingDirectory, InputGenerator anOutputBasedInputGenerator, String[] command, String... inputs) throws NotRunnableException {
		 return createRunningProject(aWorkingDirectory, anOutputBasedInputGenerator, command, getProcessTimeout(), inputs, null);
	}
	public static void setGraderProcessTimeout(int aTimeout) {
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setGraderProcessTimeOut(aTimeout);
	}
	public static int getProcessTimeout() {
		return BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getProcessTimeOut();
	}
	public static Map<String, String> getEntryPoints(Project project, String aSpecifiedMainClass) {
		return BasicLanguageDependencyManager.getMainClassFinder().getEntryPoints(project, aSpecifiedMainClass);	
	}
	public static String getMainEntryPoint(Project project, String aSpecifiedMainClass) {
		return BasicProcessRunner.getMainEntryPoint(project, aSpecifiedMainClass);
	}
	
	
}
