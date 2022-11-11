package grader.basics.execution;


import java.io.File;
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
	String traceDockerFileName;
//	File traceFile;
	String relativeTraceFileName;
	File traceDockerFile;
	File traceDockerDir;
	String traceDockerFolderName;
	String studentDir;
	String dockerDir;
	String src;
	String bin;
	String dockerPath;
	boolean copiedDockerDirectory = false;
	private static final String relativeDockerDirName = "copyForDocker";
	public static String getTraceFileRelativeName() {
		String aValgrindTraceDirectory = 
				BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getValgrindTraceDirectory();
		String aTraceShortFileName = 			
				BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getValgrindTraceFile();
		return aValgrindTraceDirectory + "/" + aTraceShortFileName;
	}
	public static File getTraceFile(Project aProject) {	
		File aProjectDirectory = aProject.getProjectFolder();
		return new File (aProjectDirectory + "/" + getTraceFileRelativeName());
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
		String aRedirection = 
				BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getOutputValgrindTrace()?
				
						"| tee":
							">";
		
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
	public static void recursiveDelete(File aFile) {
		if (aFile.isDirectory()) {
			File[] aFiles = aFile.listFiles();
			for (File aChildFile:aFiles) {
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
	    	System.out.println ("File:" + aDestFile.getAbsolutePath());
	    	
	        Files.walk(Paths.get(src)).forEach(a -> {
	            Path b = Paths.get(dest, a.toString().substring(src.length()));
//	            File bFile = b.toFile();
//	            bFile.getParentFile().mkdirs();
	            try {
	                if (!a.toString().equals(src))
	                    Files.copy(a, b, overwrite ? new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} : new CopyOption[]{});
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        });
	    } catch (IOException e) {
	        //permission issue
	        e.printStackTrace();
	    }
	}
	public  String[] getExecutionCommand(Project aProject,
			File aBuildFolder, String anEntryPoint, String[] anArgs) {
		try {
			studentDir = aProject.getProjectFolder().getAbsolutePath();
			dockerDir = studentDir;
			if (GradingMode.getGraderRun() || 
					BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getDockerMountIsCopy()) {
				
			
			copyDir(studentDir, relativeDockerDirName, true);
			dockerDir = new File( relativeDockerDirName).getAbsolutePath();
			copiedDockerDirectory = true;
			}
//			dockerDir = studentDir;
			src = CompilerHelper.toRelativePath(studentDir, aProject.getSourceFolder().getAbsolutePath()) ;
			bin = CompilerHelper.toRelativePath(studentDir, aProject.getBuildFolder().getAbsolutePath());
//		File aTraceFile = getTraceFile(aProject);
			
		relativeTraceFileName = getTraceFileRelativeName();
		
		traceDockerFile = new File (dockerDir + "/" + relativeTraceFileName);

		traceDockerDir = traceDockerFile.getParentFile();
		if (!traceDockerDir.exists()) {
			traceDockerDir.mkdirs();
		}
//		String aValgrindTraceDirectory = aProjectDirectory + "/"  +
//				BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getValgrindTraceDirectory();
	
			traceDockerFolderName = traceDockerDir.getCanonicalPath();
		
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
		traceDockerFileName = traceDockerFile.getCanonicalPath();
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
		String aValgrindConfigurationRelativeFileName = 
				aValgrindConfigurationDirectory + "/" +
				aConfiguration;
		String aValgrindConfigurationFileName = aValgrindConfigurationRelativeFileName;
		if (!GradingMode.getGraderRun()) {
			aValgrindConfigurationFileName = dockerDir + "/" + aValgrindConfigurationRelativeFileName;
		}
//		String aValgrindConfigurationFileName = 
//				aProjectDirectory + "/" +
//				aValgrindConfigurationDirectory + "/" +
//				aConfiguration;
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
		DockerHelper.createContainer(dockerDir);
		
//		Parser parser = new Parser(aValgrindConfigurationFileName);
		Parser parser = new Parser(aValgrindConfigurationFileName, aValgrindConfigurationRelativeFileName);

		
		Wrapper wrapper = parser.parse();
//		String aSourceFolder = runner.getBasicProject().getSourceFolder().getAbsolutePath();
//		String aBuildFolder = runner.getBasicProject().getBuildFolder().getAbsolutePath();
		String aSourceFolder = dockerDir + "/" + src;
		String aBuildFolder = dockerDir + "/" + bin;

		wrapper.toFile(aConfiguration, aSourceFolder);
//		wrapper.toFile(aConfiguration, aProjectDirectory);


//		wrapper.toFile(aValgrindConfigurationFileName, aProjectDirectory);
		
//	    ch = new CompilerHelper(aValgrindConfigurationFileName, aProjectDirectory, aTraceFile);
//	    ch = new CompilerHelper(aConfiguration, aSourceFolder, aTraceFile);
	    
	    ch = new CompilerHelper(src, bin, aConfiguration, dockerDir, traceDockerFile.getAbsolutePath());
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
	void maybeCopyTraceFile() throws IOException {
		if (copiedDockerDirectory) {
			String aProjectTraceFileName = studentDir + "/" + relativeTraceFileName;
//			String aDockerCopyTraceFileName = dockerDir + "/" + relativeDockerDirName + "/" + relativeTraceFileName;
			String aDockerCopyTraceFileName = dockerDir + "/" + relativeTraceFileName;
			File aSourceFile = new File (aDockerCopyTraceFileName);
			if (!aSourceFile.exists()) {
				System.out.println(" No file");
			}
			File aDestFile = new File (aProjectTraceFileName);
			aDestFile.getParentFile().mkdirs();

			Files.copy(Paths.get(aDockerCopyTraceFileName), Paths.get(aProjectTraceFileName), 
            		new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
		}
	}
	@Override
	public void  runPostIndividualCommand (RunningProject runner) {
		try {
			
			maybeCopyTraceFile();
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