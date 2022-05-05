package grader.basics.execution;


import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
//import framework.project.ClassDescription;
//import framework.project.ClassesManager;
//import framework.project.Project;
import java.util.Map;

import grader.basics.config.BasicConfigurationManagerSelector;
import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.project.Project;
import sun.java2d.pipe.hw.AccelGraphicsConfig;
import util.pipe.InputGenerator;
import valgrindpp.codegen.Parser;
import valgrindpp.codegen.Wrapper;
import valgrindpp.helpers.CompilerHelper;
import valgrindpp.helpers.DockerHelper;

public class AValgrindCommandGenerator extends AnExecutableFinder  implements CommandGenerator {
	CompilerHelper ch;
	@Override
	public void  runPreIndividualCommand (RunningProject runner, InputGenerator anOutputBasedInputGenerator,
			String[] aCommand, String input, String[] args, int timeout,
			String aProcessName, boolean anOnlyProcess) throws NotRunnableException {
		try {
		String aDockerProgramName = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getDockerProgramName();
		String aValgrindConfigurationFileName = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getValgrindConfiguration();;
		String aTraceFile = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getValgrindConfiguration();;
		String aProjectDirectory = runner.getBasicProject().getProjectFolder().getAbsolutePath();
		DockerHelper.deleteContainer();
		DockerHelper.createContainer(aProjectDirectory);
		
		Parser parser = new Parser(aValgrindConfigurationFileName);
		
		Wrapper wrapper = parser.parse();
		wrapper.toFile(aValgrindConfigurationFileName, aProjectDirectory);
		
	    ch = new CompilerHelper(aValgrindConfigurationFileName, aProjectDirectory, null);
		
		ch.compileWrapper();
		ch.deleteWrapperCFile();
		ch.compileStudentCode();
		ch.deleteWrapperObjFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void  runPostIndividualCommand (RunningProject runner, InputGenerator anOutputBasedInputGenerator,
			String[] aCommand, String input, String[] args, int timeout,
			String aProcessName, boolean anOnlyProcess) throws NotRunnableException {
		try {
			ch.deleteBinary();
			DockerHelper.stopContainer();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}	
}