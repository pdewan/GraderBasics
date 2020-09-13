package grader.basics.checkstyle;

import java.io.File;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.execution.BasicProcessRunner;
import grader.basics.execution.Runner;
import grader.basics.execution.RunningProject;
import grader.basics.project.Project;
import unc.checks.STBuilderCheck;
import unc.checks.UNCCheck;
import unc.symbolTable.SymbolTable;
import unc.symbolTable.SymbolTableFactory;
import unc.tools.checkstyle.PostProcessingMain;
import unc.tools.checkstyle.ProjectDirectoryHolder;

public class CheckStyleInvoker {
	static final String SOURCE = "D:/dewan_backup/Java/PLProjs/PLProjsJava/src/greeting/Hello.java";

	static final String CHECKSTYLE_CONFIGURATION = "D:/dewan_backup/Java/UNC_Checkstyle_8/unc_checks.xml";
	static final String[] ARGS = {"-c", null,  null};
	
	public static SymbolTable runCheckstyle(Project aProject, File aCheckstyleConfigurationFile, File aCheckstyleOutFile, File aCheckstyleGeneratedFile) {
		if (!aCheckstyleConfigurationFile.exists()) {
			System.err.println("Checkstyle configuration does not exist:" + aCheckstyleConfigurationFile);
			return null;
		}
		return runCheckstyle(aProject.getProjectFolder(), aProject.getSourceFolder(), aCheckstyleConfigurationFile, aCheckstyleOutFile, aCheckstyleGeneratedFile);
		
//		
//		UNCCheck.setManualProjectDirectory(true);
//		STBuilderCheck.setDoAutoPassChange(false);
//        ProjectDirectoryHolder.setCurrentProjectDirectory(aProject.getProjectFolder().getAbsolutePath());
//        SymbolTableFactory.setSymbolTable(null);
//		PostProcessingMain.setSecondPassFile(aCheckstyleOutFile);
//		PostProcessingMain.setGeneratedChecksFile(aCheckstyleGeneratedFile);
//		PostProcessingMain.setRedirectFirstPassOutput(true);
//
//		
//		PostProcessingMain.setPrintOnlyTaggedClasses(true);
//		PostProcessingMain.setRedirectSecondPassOutput(true);
//		PostProcessingMain.setGenerateChecks(true);
//		String aCheckstyleFolder = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getCheckStyleConfigurationDirectory();
//		String aCheckStyleFile = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getCheckStyleConfiguration();
//		
////		ARGS[1] = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getCheckStyleConfiguration();
//		ARGS[1] = aCheckstyleConfigurationFile.getAbsolutePath();
//
//		ARGS[2] = aProject.getSourceFolder().getAbsolutePath();
//		PostProcessingMain.main(ARGS);
//		return SymbolTableFactory.getCurrentSymbolTable();
//		
		
	}
	
	public static SymbolTable runCheckstyle(String aProjectDirectory, String  aSourceFile, String aCheckstyleConfigurationFile, String aCheckstyleOutFile, String aCheckstyleGeneratedFile) {
		return runCheckstyle(new File(aProjectDirectory), new File(aSourceFile), new File(aCheckstyleConfigurationFile), new File(aCheckstyleOutFile), new File (aCheckstyleGeneratedFile));
	}
	public static void main (final String[] args) {
		if (args.length != 5 ) {
			System.err.println("Expected number of args: 5, actual number: " + args.length);
			System.exit(-1);
		}
		runCheckstyle(args[0], args[1], args[2], args[3], args[4]);
	}
	public static SymbolTable runCheckstyle(File aProjectDirectory, File  aSourceFile, File aCheckstyleConfigurationFile, File aCheckstyleOutFile, File aCheckstyleGeneratedFile) {
		if (!aCheckstyleConfigurationFile.exists()) {
			System.err.println("Checkstyle configuration does not exist:" + aCheckstyleConfigurationFile);
			return null;
		}
		
		
		UNCCheck.setManualProjectDirectory(true);
		STBuilderCheck.setDoAutoPassChange(false);
        ProjectDirectoryHolder.setCurrentProjectDirectory(aProjectDirectory.getAbsolutePath());
        SymbolTableFactory.setSymbolTable(null);
		PostProcessingMain.setSecondPassFile(aCheckstyleOutFile);
		PostProcessingMain.setGeneratedChecksFile(aCheckstyleGeneratedFile);
		PostProcessingMain.setRedirectFirstPassOutput(true);

		
		PostProcessingMain.setPrintOnlyTaggedClasses(true);
		PostProcessingMain.setRedirectSecondPassOutput(true);
		PostProcessingMain.setGenerateChecks(true);
		String aCheckstyleFolder = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getCheckStyleConfigurationDirectory();
		String aCheckStyleFile = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getCheckStyleConfiguration();
		
//		ARGS[1] = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getCheckStyleConfiguration();
		ARGS[1] = aCheckstyleConfigurationFile.getAbsolutePath();

		ARGS[2] = aSourceFile.getAbsolutePath();
		PostProcessingMain.main(ARGS);
//		PostProcessingMain.restoreOut();
		return SymbolTableFactory.getCurrentSymbolTable();
		
		
	}
	public static SymbolTable runCheckstyle(Project aProject,  String aCheckstyleConfigurationFileName, String aCheckstyleOutFileName, String aCheckstyleGeneratedFileName) {
		return runCheckstyle(aProject, new File(aCheckstyleConfigurationFileName), new File(aCheckstyleOutFileName), new File( aCheckstyleGeneratedFileName));
		
	}
	public static RunningProject forkCheckstyle(Project aProject,  String aCheckstyleConfigurationFileName, String aCheckstyleOutFileName, String aCheckstyleGeneratedFileName) {
		String[] anArgs = new String[5];
		anArgs[0] = aProject.getProjectFolder().getAbsolutePath();
		anArgs[1] = aProject.getSourceFolder().getAbsolutePath();
		anArgs[2] = aCheckstyleConfigurationFileName;
		anArgs[3] = aCheckstyleOutFileName;
		anArgs[4] = aCheckstyleGeneratedFileName;
		String aClassPath = System.getProperty("java.class.path");
		String[] command = {"java", 
							"-cp", 
						aClassPath,
						CheckStyleInvoker.class.getName(), 
						anArgs[0],
						anArgs[1],
						anArgs[2],
						anArgs[3],
						anArgs[4]
		};

        Runner processRunner = new BasicProcessRunner(new File(anArgs[0]));
//        RunningProject aReturnValue = processRunner.run(null, command, "", args, 2000);
        RunningProject aReturnValue = processRunner.run(null, command, "", null, BasicStaticConfigurationUtils.DEFAULT_PROCESS_TIME_OUT*10);

        return aReturnValue;

		
	}

//public static void main (String[] args) {
//	Project aProject = CurrentProjectHolder.getCurrentProject();
//	if (aProject == null) {
//		System.err.println("No project loaded as no suite run?");
//		System.exit(-1);
//	}
//	File aFile = aProject.getProjectFolder();
//	
//	File anEclipseFolder = new File (aFile.getAbsoluteFile()+"/Logs/Eclipse");
//	File aCheckStyleAllFile = new File (aFile.getAbsoluteFile()+"/Logs/LocalChecks/CheckStyle_All.csv");
//	if (!anEclipseFolder.exists()) {
//		System.err.println("File does not exist:" + anEclipseFolder);
//	}
//	if (!aCheckStyleAllFile.exists()) {
//		System.err.println("File does not exist:" + aCheckStyleAllFile);
//	}
//	PostProcessingMain.setSecondPassFile(new File("mysecondpass.txt"));
//	PostProcessingMain.setGeneratedChecksFile(new File("mygenerated.xml"));
//
//	
//	PostProcessingMain.setPrintOnlyTaggedClasses(true);
//	PostProcessingMain.setRedirectSecondPassOutput(true);
//	PostProcessingMain.setGenerateChecks(true);
//	ARGS[1] = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getCheckStyleConfiguration();
//	PostProcessingMain.main(ARGS);
//
//}
}
