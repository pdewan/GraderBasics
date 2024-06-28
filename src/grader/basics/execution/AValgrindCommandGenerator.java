package grader.basics.execution;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import grader.basics.file.RootFolderProxy;
import grader.basics.file.filesystem.AFileSystemRootFolderProxy;
import grader.basics.project.Project;
//import sun.java2d.pipe.hw.AccelGraphicsConfig;
import util.pipe.InputGenerator;
import util.trace.Tracer;
import valgrindpp.codegen.Parser;
import valgrindpp.codegen.Wrapper;
import valgrindpp.grader.Grader;
import valgrindpp.grader.SimpleGrader;
import valgrindpp.helpers.CommandLineHelper;
import valgrindpp.helpers.CompilerHelper;
import valgrindpp.helpers.DockerHelper;

public abstract class AValgrindCommandGenerator extends AnExecutableFinder implements CommandGenerator {
	private CompilerHelper ch;
	private String traceExecutionFileName;
//	File traceFile;
	private String relativeTraceFileName;
	private File traceExecutionFile;
	private File traceExecutionDirectory;
//	private String traceExecutionFolderName;
	private String studentDirectory;
	private String projectDirectory;
	private String executionDirectory;
	

	private String src;
	private String bin;
//	private String executionDrectoryPath;
	boolean copiedExecutionDirectory = false;
	private static final String relativeDockerDirName = "copyForDocker";
	private boolean createDockerContainer;
	

	boolean instrumentUsingValgrind;
	protected boolean isCreateDockerContainer() {
		return createDockerContainer;
	}
	protected String getProjectDirectory() {
		return projectDirectory;
	}
	public static String getTraceFileRelativeName() {
		String aValgrindTraceDirectory = BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
				.getValgrindTraceDirectory();
		String aTraceShortFileName = BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
				.getValgrindTraceFile();
		return aValgrindTraceDirectory + "/" + aTraceShortFileName;
	}

	public static File getTraceFile(Project aProject) {
		File aProjectDirectory = aProject.getProjectFolder();
		return new File(aProjectDirectory + "/" + getTraceFileRelativeName());
	}

//	public static File getTraceFile(Project aProject) {		
//		File aProjectDirectory = aProject.getProjectFolder();
//		
//		String aTraceFileName = aProjectDirectory + "/"  +
//				getTraceFileRelativeName();
////		String aTraceShortFileName = 			
////				BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getValgrindTraceFile();
////		String aValgrindTraceDirectory = aProjectDirectory + "/"  +
////				BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getValgrindTraceDirectory();
////		String aTraceShortFileName = 			
////				BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getValgrindTraceFile();
////		if (aTraceShortFileName == null) {
////			aTraceShortFileName = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getValgrindConfiguration() + "Traces" + ".txt";
////		}
////		traceFile = aTraceShortFileName;
//
////		String aTraceFileName = aValgrindTraceDirectory + "/" + aTraceShortFileName;
//		return new File(aTraceFileName);
//	}
	public static String redirection() {
//		String aRedirection = 
//				Tracer.showInfo()?
//						"| tee":
//							">";
		String aRedirection = BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
				.getOutputValgrindTrace() ?

						"| tee" : ">";
		
		return aRedirection;
	}

	protected abstract String[] getBasicTraceCommand();

	public String[] getTraceCommand() {

//		String aRelativeTraceFile = CompilerHelper.toRelativePath(studentDirectory, traceFileName);
//		String fullExecName = bin + CompilerHelper.EXEC_NAME;
//		String aRedirection = 
//				Tracer.showInfo()?
//						"| tee":
//							">";
		String[] command = getBasicTraceCommand();
//		System.out.println ("Basic command" + Arrays.toString(command));
		try {
			String[] retVal = CommandLineHelper.getExecutionCommand(command, executionDirectory);
			Tracer.info (this, "Tracing execution command " + Arrays.toString(retVal));
			return retVal;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
//	public  String[] getExecutionCommand(String[] command) throws Exception {
//	if (createDockerContainer)	{
//		return CommandLineHelper.getDockerCommand(command);
//	}
//	return CommandLineHelper.getDirectCommand(command, executionDirectory);
//	}

	public static void recursiveDelete(File aFile) {
		if (aFile.isDirectory()) {
			File[] aFiles = aFile.listFiles();
			for (File aChildFile : aFiles) {
				recursiveDelete(aChildFile);
			}
//			return;
		}
		aFile.delete();

	}

	public static void copyDir(String src, String dest, boolean overwrite) {
		try {
			File aDestFile = new File(dest);
			if (aDestFile.exists()) {
				recursiveDelete(aDestFile);
			}
			aDestFile.mkdir();
			System.out.println("File:" + aDestFile.getAbsolutePath());

			Files.walk(Paths.get(src)).forEach(a -> {
				Path b = Paths.get(dest, a.toString().substring(src.length()));
//	            File bFile = b.toFile();
//	            bFile.getParentFile().mkdirs();
				try {
					if (!a.toString().equals(src))
						Files.copy(a, b, overwrite ? new CopyOption[] { StandardCopyOption.REPLACE_EXISTING }
								: new CopyOption[] {});
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			// permission issue
			e.printStackTrace();
		}
	}

	public static File findChildFile(File aFolder, String aChildName) {
//    	File dir = new File(directory);
//    	FilenameFilter foo;
		if (!aFolder.isDirectory()) {
			return null;
		}

		File[] matches = aFolder.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.equals("aChildName");
			}
		});
		if (matches.length == 1) {
			return matches[0];
		}

		return null;

	}

	/*
	 * Project folder:/autograder/source/Assignment5/grade, me(student)/Submission
	 * attachment(s)/submission/autograder
	 * Buildfolder:/autograder/source/Assignment5/grade, me(student)/Submission
	 * attachment(s)/submission/autograder/submission/F22A5Fixed/bin classpath:
	 * .:..::.:/autograder/source/Comp524GraderAll.jar:.:./source/AssignmentSetup.
	 * jar:.:/autograder/source/Comp524GraderAll.jar:.::./source/AssignmentSetup.jar
	 * Project folder:/autograder/source/Assignment5/grade, me(student)/Submission
	 * attachment(s)/submission.zip Project
	 * folder:/autograder/source/Assignment5/grade, me(student)/Submission
	 * attachment(s)/submission/autograder Buffer traced messages =true
	 */
//    private static final String GRADESOPE_SUFFIX = 
	File findChild(String aFileName, String aChild) {
//		System.out.println("Finding " + aChild + " in " + aFileName);		
//		System.out.println ("Child files : " + new File(aFileName).listFiles());
		String aChildFileName = aFileName + "/" + aChild;
		System.out.println("Trying to find file " +aChildFileName);		

		File retVal = new File(aChildFileName);
		if (retVal.exists()) {
			System.out.println("Returning " + aChild);

			return retVal;
		}
		return null;
	}

	File maybeToZippedGradescopeProjectFolder(File aProjectFolder) {
		String aProjectFolderName = aProjectFolder.getAbsolutePath();
		if (!aProjectFolderName.contains("grade, me")) {
			return aProjectFolder;
		}
		System.out.println("Gradescope project folder" + aProjectFolderName);
		File[] aFiles = aProjectFolder.listFiles();
		for (File aFile : aFiles) {
			if (aFile.isDirectory()) {
				System.out.println("Found child folder" + aFile.getAbsolutePath());

				File aZippedChild = findChild(aFile.getAbsolutePath(), aFile.getName()); // zipped folder
				if (aZippedChild != null) {
					return aZippedChild;
				}
				return aFile;
			}
		}
		return aProjectFolder;

//    	File aZippedChild = findChild(aProjectFolder.getAbsolutePath(), 
//    			aProjectFolder.getName()); // zipped folder
//		if (aZippedChild != null) {
//			return aZippedChild;
//		}
//		return aProjectFolder;
//    
//    	File aChild = findChild(aProjectFolderName, "submission");
//    	if (aChild == null) 
//    		aChild = findChild(aProjectFolderName, "autograder/submission"); 
//    	if (aChild != null) {
//    		File[] aFiles = aChild.listFiles();
//    		for (File aFile:aFiles) {
//    			if (aFile.isDirectory()) {
//    				File aZippedChild = findChild(aFile.getAbsolutePath(), aFile.getName()); // zipped folder
//    				if (aZippedChild != null) {
//    					return aZippedChild;
//    				}
//    				return aFile;
//    			}
//    		}
//    	
////    		return aSubmissionChildFolder;
	}

//    	File[] aProjectFiles = aProjectFolderFile.listFiles();
//    	File aSubmissionChildFolder = new File (aSubmissionChildFolderName);
//    	if (aSubmissionChildFolder.exists()) {
//    		File[] aFiles = aSubmissionChildFolder.listFiles();
//    		if (aFiles.length == 0) {
//    			return new AFileSystemFileProxy(aFiles[0]);
//    		}
////    		return aSubmissionChildFolder;
//    	}    	
//    	return aProjectFolder;      	
//    }

	public String[] getExecutionCommand(Project aProject, File aBuildFolder, String anEntryPoint, String[] anArgs) {
		try {
			createDockerContainer = BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
					.getCreateDockerContainer();
			instrumentUsingValgrind = BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
					.getInstrumentUsingValgrind();
			projectDirectory =  aProject.getProjectFolder().getAbsolutePath();
			studentDirectory = projectDirectory;
			Tracer.info(this, "Student directory:" + studentDirectory);

			File aStudentDirectoryFile = new File(studentDirectory);
			File aMaybeChanged = maybeToZippedGradescopeProjectFolder(aStudentDirectoryFile);
			if (aStudentDirectoryFile != aMaybeChanged) {
//				System.out.println("Changed student directory");
				Tracer.info(this, "Original student directory " + studentDirectory);
				studentDirectory = aMaybeChanged.getAbsolutePath();
				Tracer.info(this, "New student directory " + studentDirectory);

			}
			executionDirectory = studentDirectory;
			if (createDockerContainer && (GradingMode.getGraderRun()
					|| BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getDockerMountIsCopy())) {

				copyDir(studentDirectory, relativeDockerDirName, true);
				executionDirectory = new File(relativeDockerDirName).getAbsolutePath();
				copiedExecutionDirectory = true;
			}
//			executionDirectory = studentDirectory;
			src = CompilerHelper.toRelativePath(studentDirectory, aProject.getSourceFolder().getAbsolutePath());
			setBin(CompilerHelper.toRelativePath(studentDirectory, aProject.getBuildFolder().getAbsolutePath()));
//		File aTraceFile = getTraceFile(aProject);

			setRelativeTraceFileName(getTraceFileRelativeName());
// this should be project directory instead of execution directory if no docker container
			traceExecutionFile = new File(executionDirectory + "/" + getRelativeTraceFileName());
			Tracer.info(this, "TraceExecutionFile:" + traceExecutionFile );

			traceExecutionDirectory = traceExecutionFile.getParentFile();
			if (!traceExecutionDirectory.exists()) {
				traceExecutionDirectory.mkdirs();
			}
//		String aValgrindTraceDirectory = aProjectDirectory + "/"  +
//				BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getValgrindTraceDirectory();

//			traceExecutionFolderName = traceExecutionDirectory.getCanonicalPath();

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
			traceExecutionFileName = traceExecutionFile.getCanonicalPath();
			return getTraceCommand();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	protected abstract void compileStudentCode() throws Exception;

	@Override
	public void runPreIndividualCommand(RunningProject runner, InputGenerator anOutputBasedInputGenerator,
			String[] aCommand, String input, String[] args, int timeout, String aProcessName, boolean anOnlyProcess)
			throws NotRunnableException {
		try {
			Tracer.info(this, "Executing pre-execution commands");
//		String aDockerProgramName = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getDockerPath();
			String aValgrindConfigurationDirectory = BasicExecutionSpecificationSelector
					.getBasicExecutionSpecification().getValgrindConfigurationDirectory();
//		String aProjectDirectory = runner.getBasicProject().getProjectFolder().getAbsolutePath();
			String aConfiguration = BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
					.getValgrindConfiguration();
			String aValgrindConfigurationRelativeFileName = aValgrindConfigurationDirectory + "/" + aConfiguration;
			String aValgrindConfigurationFileName = aValgrindConfigurationRelativeFileName;
			if (!GradingMode.getGraderRun()) {
				aValgrindConfigurationFileName = executionDirectory + "/" + aValgrindConfigurationRelativeFileName;
			}
			DockerHelper.deleteContainer();
			DockerHelper.createContainer(executionDirectory);

//		Parser parser = new Parser(aValgrindConfigurationFileName);
			Parser parser = new Parser(aValgrindConfigurationFileName, aValgrindConfigurationRelativeFileName);

			Wrapper wrapper = parser.parse();
//		String aSourceFolder = runner.getBasicProject().getSourceFolder().getAbsolutePath();
//		String aBuildFolder = runner.getBasicProject().getBuildFolder().getAbsolutePath();
			String aSourceFolder = executionDirectory + "/" + src;
			String aBuildFolder = executionDirectory + "/" + getBin();

			wrapper.toFile(aConfiguration, aSourceFolder);
//		wrapper.toFile(aConfiguration, aProjectDirectory);

//		wrapper.toFile(aValgrindConfigurationFileName, aProjectDirectory);

//	    ch = new CompilerHelper(aValgrindConfigurationFileName, aProjectDirectory, aTraceFile);
//	    ch = new CompilerHelper(aConfiguration, aSourceFolder, aTraceFile);

			setCompilerHelper(new CompilerHelper(src, getBin(), aConfiguration, executionDirectory,
					traceExecutionFile.getAbsolutePath()));
			getCompilerHelper().compileWrapper();
			getCompilerHelper().deleteWrapperCFile();
			compileStudentCode();
//		ch.compileStudentCode();
			getCompilerHelper().deleteWrapperObjFile();
//		ch.trace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String toSingleSlash(String aFileName) {
		return aFileName.replaceAll("\\\\", "/");

	}

	abstract void deleteBinary() throws Exception;

	void maybeCopyTraceFile() throws IOException {
		if (copiedExecutionDirectory) {
			String aProjectTraceFileName = studentDirectory + "/" + getRelativeTraceFileName();
//			String aDockerCopyTraceFileName = executionDirectory + "/" + relativeDockerDirName + "/" + relativeTraceFileName;
			String aDockerCopyTraceFileName = executionDirectory + "/" + getRelativeTraceFileName();
			File aSourceFile = new File(aDockerCopyTraceFileName);
			if (!aSourceFile.exists()) {
				System.out.println(" No file");
			}
			File aDestFile = new File(aProjectTraceFileName);
			aDestFile.getParentFile().mkdirs();

			Files.copy(Paths.get(aDockerCopyTraceFileName), Paths.get(aProjectTraceFileName),
					new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
		}
	}

	@Override
	public void runPostIndividualCommand(RunningProject runner) {
		try {
			Tracer.info(this, "Post execution commands");;

			maybeCopyTraceFile();
			Tracer.info(this, "Post execution commands");
			Tracer.info(this, "Deleting binary");
			deleteBinary();
			DockerHelper.stopContainer();
//			Grader grader = new SimpleGrader(traceFolder, traceFileName);
//			ch.deleteTraces();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getRelativeTraceFileName() {
		return relativeTraceFileName;
	}

	public void setRelativeTraceFileName(String relativeTraceFileName) {
		this.relativeTraceFileName = relativeTraceFileName;
	}

	public String getBin() {
		return bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public CompilerHelper getCompilerHelper() {
		return ch;
	}

	public void setCompilerHelper(CompilerHelper ch) {
		this.ch = ch;
	}
}