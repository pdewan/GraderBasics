package gradingTools.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.NotRunnableException;
import grader.basics.execution.RunningProject;
import grader.basics.project.Project;
import util.pipe.InputGenerator;

/*
 * Class that contains various static helper methods for running projects and processing their output
 */
public class RunningProjectUtils extends BasicProjectExecution{

	/*
	 * Convenience method for running projects where inputs are separated by the
	 * newline character
	 */
	public static RunningProject runProject(Project project, int timeout, String... inputs)
			throws NotRunnableException {
		return runProject(project, BasicProjectExecution.DEFAULT_INPUT_SEPARATOR, timeout, inputs);
	}
	public static RunningProject runProjectandWithMainFile(Project project, String aLoadFile, int timeout, String... inputs)
			throws NotRunnableException {
		String aSourceFolder = project.getSourceFolder().getAbsolutePath();

		String aTestFile = aSourceFolder + "/" + aLoadFile;
		File aFile = new File(aTestFile);
		if (!aFile.exists()) {
			System.out.println("Did not find file:" + aTestFile );

			 aTestFile = aSourceFolder + "/" + aLoadFile.toLowerCase();
				System.out.println("Trying to fine file:" + aTestFile );

			 aFile = new File(aTestFile);
		}
		if (!aFile.exists()) {
			FileNotFoundException aFileNotFoundException = new FileNotFoundException(aFile + " does not exist.");
			aFileNotFoundException.printStackTrace();
			return null;
		}
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setEntryPoint(aTestFile);

		return runProject(project, BasicProjectExecution.DEFAULT_INPUT_SEPARATOR, timeout, inputs);
	}
	
	public static RunningProject runProject(Project project, int timeout, InputGenerator anOutputBasedInputGenerator,
			String... inputs)
			throws NotRunnableException {
		return runProject(project, BasicProjectExecution.DEFAULT_INPUT_SEPARATOR, timeout, anOutputBasedInputGenerator, inputs);
	}
	public static RunningProject runProject(Project project,  InputGenerator anOutputBasedInputGenerator,
			String... inputs)
			throws NotRunnableException {
		Integer aTimeout = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getProcessTimeOut();
		return runProject(project, BasicProjectExecution.DEFAULT_INPUT_SEPARATOR, aTimeout, anOutputBasedInputGenerator, inputs);
	}
	public static RunningProject runProject(Project project, String inputSeparator, int timeout, String... inputs) throws NotRunnableException {
		return runProject(project, timeout, null, inputs);
	}
	
//	public static String toString(String inputSeparator, String... inputs) {
//		String allInputsStr = "";
//		for (int i = 0; i < inputs.length; i++) {
//			if (i > 0) {
//				allInputsStr += inputSeparator;
//			}
//			allInputsStr += inputs[i];
//		}
//		return allInputsStr;
//	}
	
	/*
	 * Runs the project with the inputs separated by the given separator string
	 * 
	 * The running project also timesout based on the given timeout
	 */
	public static RunningProject runProject(Project project, String inputSeparator, int timeout, InputGenerator anOutputBasedInputGenerator, String... inputs) throws NotRunnableException {
//		String allInputsStr = "";
//		for (int i = 0; i < inputs.length; i++) {
//			if (i > 0) {
//				allInputsStr += inputSeparator;
//			}
//			allInputsStr += inputs[i];
//		}
		return project.launch(anOutputBasedInputGenerator, toString(inputSeparator, inputs), timeout);
	}
	public static RunningProject runProject(Project project,  int timeout, InputGenerator anOutputBasedInputGenerator, Map<String, String> aProcessToInput) throws NotRunnableException {
//		String allInputsStr = "";
//		for (int i = 0; i < inputs.length; i++) {
//			if (i > 0) {
//				allInputsStr += inputSeparator;
//			}
//			allInputsStr += inputs[i];
//		}
		return project.launch(anOutputBasedInputGenerator, aProcessToInput, timeout);
	}
	public static RunningProject runProject(Project project, int timeout,  Map<String, String> aProcessToInput) throws NotRunnableException {
//		String allInputsStr = "";
//		for (int i = 0; i < inputs.length; i++) {
//			if (i > 0) {
//				allInputsStr += inputSeparator;
//			}
//			allInputsStr += inputs[i];
//		}
		return project.launch(null, aProcessToInput, timeout);
	}

}
