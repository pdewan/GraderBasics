package grader.basics.execution;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
//import framework.project.ClassDescription;
//import framework.project.ClassesManager;
//import framework.project.Project;
import java.util.Map;

import grader.basics.config.BasicConfigurationManagerSelector;
import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.project.Project;
import sun.java2d.pipe.hw.AccelGraphicsConfig;
import util.pipe.InputGenerator;
import util.trace.Tracer;
import valgrindpp.codegen.Parser;
import valgrindpp.codegen.Wrapper;
import valgrindpp.grader.Grader;
import valgrindpp.grader.SimpleGrader;
import valgrindpp.helpers.CommandLineHelper;
import valgrindpp.helpers.CompilerHelper;
import valgrindpp.helpers.DockerHelper;

public abstract class AValgrindCommandGenerator extends AnExecutableFinder  implements CommandGenerator {
	CompilerHelper ch;
	String traceFileName;
	File traceFile;
	String traceFolder;
	String studentDir;
	String src;
	String bin;
	String dockerPath;
	public static File getTraceFile(Project aProject) {
		File aProjectDirectory = aProject.getProjectFolder();
		String aValgrindTraceDirectory = aProjectDirectory + "/"  +
				BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getValgrindTraceDirectory();
		String aTraceShortFileName = 			
				BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getValgrindTraceFile();
//		if (aTraceShortFileName == null) {
//			aTraceShortFileName = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getValgrindConfiguration() + "Traces" + ".txt";
//		}
//		traceFile = aTraceShortFileName;

		String aTraceFileName = aValgrindTraceDirectory + "/" + aTraceShortFileName;
		return new File(aTraceFileName);
	}
	public static String redirection() {
//		String aRedirection = 
//				Tracer.showInfo()?
//						"| tee":
//							">";
		String aRedirection = 
				
						"| tee";
		
		return aRedirection;
	}
	protected abstract String[] getBasicTraceCommand();
	
	public String[] getTraceCommand( ) {
		
//		String aRelativeTraceFile = CompilerHelper.toRelativePath(studentDir, traceFileName);
//		String fullExecName = bin + CompilerHelper.EXEC_NAME;
//		String aRedirection = 
//				Tracer.showInfo()?
//						"| tee":
//							">";
		String[] command = getBasicTraceCommand();
		try {
			return CommandLineHelper.getDockerCommand(command);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public  String[] getExecutionCommand(Project aProject,
			File aBuildFolder, String anEntryPoint, String[] anArgs) {
		try {
			studentDir = aProject.getProjectFolder().getAbsolutePath();
			src = CompilerHelper.toRelativePath(studentDir, aProject.getSourceFolder().getAbsolutePath()) ;
			bin = CompilerHelper.toRelativePath(studentDir, aProject.getBuildFolder().getAbsolutePath());
		File aTraceFile = getTraceFile(aProject);
		File aTraceFolder = aTraceFile.getParentFile();
		if (!aTraceFolder.exists()) {
			aTraceFolder.mkdirs();
		}
//		String aValgrindTraceDirectory = aProjectDirectory + "/"  +
//				BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getValgrindTraceDirectory();
	
			traceFolder = aTraceFolder.getCanonicalPath();
		
//		File aFolder = new File(aValgrindTraceDirectory);
//		if (!aFolder.exists()) {
//			aFolder.mkdirs();
//		}
//		String aTraceShortFileName = 			
//				BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getValgrindTraceFile();
//		if (aTraceShortFileName == null) {
//			aTraceShortFileName = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getValgrindConfiguration() + "Traces" + ".txt";
//		}
//		traceFile = aTraceShortFileName;

//		String aTraceFile = aValgrindTraceDirectory + "/" + aTraceShortFileName;
		traceFileName = aTraceFile.getCanonicalPath();
		traceFile = aTraceFile;
		return getTraceCommand();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	

	}
	protected abstract void compileStudentCode() throws Exception;
	@Override
	public void  runPreIndividualCommand (RunningProject runner, InputGenerator anOutputBasedInputGenerator,
			String[] aCommand, String input, String[] args, int timeout,
			String aProcessName, boolean anOnlyProcess) throws NotRunnableException {
		try {
		String aDockerProgramName = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getDockerPath();
		String aValgrindConfigurationDirectory = 
				BasicExecutionSpecificationSelector.
				getBasicExecutionSpecification().getValgrindConfigurationDirectory();
		String aProjectDirectory = runner.getBasicProject().getProjectFolder().getAbsolutePath();
		String aConfiguration = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getValgrindConfiguration();
;
		String aValgrindConfigurationFileName = 
				aProjectDirectory + "/" +
				aValgrindConfigurationDirectory + "/" +
				aConfiguration;
//				BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getValgrindConfiguration();
//		File aTraceFile = getTraceFile(runner.getBasicProject());
//		File aTraceFolder = aTraceFile.getParentFile();
//		if (!aTraceFolder.exists()) {
//			aTraceFolder.mkdirs();
//		}
////		String aValgrindTraceDirectory = aProjectDirectory + "/"  +
////				BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getValgrindTraceDirectory();
//		traceFolder = aTraceFolder.getCanonicalPath();
////		File aFolder = new File(aValgrindTraceDirectory);
////		if (!aFolder.exists()) {
////			aFolder.mkdirs();
////		}
////		String aTraceShortFileName = 			
////				BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getValgrindTraceFile();
////		if (aTraceShortFileName == null) {
////			aTraceShortFileName = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getValgrindConfiguration() + "Traces" + ".txt";
////		}
////		traceFile = aTraceShortFileName;
//
////		String aTraceFile = aValgrindTraceDirectory + "/" + aTraceShortFileName;
//		traceFile = aTraceFile.getCanonicalPath();
	
//		String aProjectDirectory = runner.getBasicProject().getProjectFolder().getAbsolutePath();
		DockerHelper.deleteContainer();
		DockerHelper.createContainer(aProjectDirectory);
		
		Parser parser = new Parser(aValgrindConfigurationFileName);
		
		Wrapper wrapper = parser.parse();
		String aSourceFolder = runner.getBasicProject().getSourceFolder().getAbsolutePath();
		String aBuildFolder = runner.getBasicProject().getBuildFolder().getAbsolutePath();

		wrapper.toFile(aConfiguration, aSourceFolder);
//		wrapper.toFile(aConfiguration, aProjectDirectory);


//		wrapper.toFile(aValgrindConfigurationFileName, aProjectDirectory);
		
//	    ch = new CompilerHelper(aValgrindConfigurationFileName, aProjectDirectory, aTraceFile);
//	    ch = new CompilerHelper(aConfiguration, aSourceFolder, aTraceFile);
	    
	    ch = new CompilerHelper(aSourceFolder, aBuildFolder, aConfiguration, aProjectDirectory, traceFile.getAbsolutePath());
		ch.compileWrapper();
		ch.deleteWrapperCFile();
		compileStudentCode();
//		ch.compileStudentCode();
		ch.deleteWrapperObjFile();
//		ch.trace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String toSingleSlash(String aFileName) {
		return aFileName.replaceAll("\\\\", "/");

	}
	abstract void deleteBinary() throws Exception;
	@Override
	public void  runPostIndividualCommand (RunningProject runner) {
		try {
			
			deleteBinary();
			DockerHelper.stopContainer();
//			Grader grader = new SimpleGrader(traceFolder, traceFileName);
//			ch.deleteTraces();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}	
}