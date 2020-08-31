package grader.basics.checkstyle;

import java.io.File;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.project.CurrentProjectHolder;
import grader.basics.project.Project;
import unc.checks.STBuilderCheck;
import unc.checks.UNCCheck;
import unc.symbolTable.SymbolTable;
import unc.symbolTable.SymbolTableFactory;
import unc.tools.checkstyle.PostProcessingCustomMain;
import unc.tools.checkstyle.PostProcessingMain;
import unc.tools.checkstyle.ProjectDirectoryHolder;

public class CheckStyleForker {
	static final String SOURCE = "D:/dewan_backup/Java/PLProjs/PLProjsJava/src/greeting/Hello.java";

	static final String CHECKSTYLE_CONFIGURATION = "D:/dewan_backup/Java/UNC_Checkstyle_8/unc_checks.xml";
	static final String[] ARGS = {"-c", null,  null};
	public static SymbolTable runCheckstyle(Project aProject, File aCheckstyleConfigurationFile, File aCheckstyleOutFile, File aCheckstyleGeneratedFile) {
		if (!aCheckstyleConfigurationFile.exists()) {
			System.err.println("Checkstyle configuration does not exist:" + aCheckstyleConfigurationFile);
			return null;
		}
		
		
		UNCCheck.setManualProjectDirectory(true);
		STBuilderCheck.setDoAutoPassChange(false);
        ProjectDirectoryHolder.setCurrentProjectDirectory(aProject.getProjectFolder().getAbsolutePath());
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

		ARGS[2] = aProject.getSourceFolder().getAbsolutePath();
		PostProcessingMain.main(ARGS);
		return SymbolTableFactory.getCurrentSymbolTable();
		
		
	}
	public static SymbolTable runCheckstyle(Project aProject,  String aCheckstyleConfigurationFileName, String aCheckstyleOutFileName, String aCheckstyleGeneratedFileName) {
		return runCheckstyle(aProject, new File(aCheckstyleConfigurationFileName), new File(aCheckstyleOutFileName), new File( aCheckstyleGeneratedFileName));
		
	}
public static void main (String[] args) {
	Project aProject = CurrentProjectHolder.getCurrentProject();
	if (aProject == null) {
		System.err.println("No project loaded as no suite run?");
		System.exit(-1);
	}
	File aFile = aProject.getProjectFolder();
	
	File anEclipseFolder = new File (aFile.getAbsoluteFile()+"/Logs/Eclipse");
	File aCheckStyleAllFile = new File (aFile.getAbsoluteFile()+"/Logs/LocalChecks/CheckStyle_All.csv");
	if (!anEclipseFolder.exists()) {
		System.err.println("File does not exist:" + anEclipseFolder);
	}
	if (!aCheckStyleAllFile.exists()) {
		System.err.println("File does not exist:" + aCheckStyleAllFile);
	}
	PostProcessingMain.setSecondPassFile(new File("mysecondpass.txt"));
	PostProcessingMain.setGeneratedChecksFile(new File("mygenerated.xml"));

	
	PostProcessingMain.setPrintOnlyTaggedClasses(true);
	PostProcessingMain.setRedirectSecondPassOutput(true);
	PostProcessingMain.setGenerateChecks(true);
	ARGS[1] = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getCheckStyleConfiguration();
	PostProcessingMain.main(ARGS);

}
}
