package gradingTools.interpreter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.sun.xml.internal.ws.encoding.RootOnlyCodec;

import bus.uigen.ObjectEditor;
import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.file.FileProxy;
import grader.basics.file.RootFolderProxy;
import grader.basics.file.filesystem.AFileSystemRootFolderProxy;
import grader.basics.file.zipfile.AZippedRootFolderProxy;
import grader.basics.junit.AGradableJUnitTopLevelSuite;
import grader.basics.junit.GradableJUnitSuite;
import grader.basics.junit.GradableJUnitTest;
import grader.basics.project.BasicProject;
import grader.basics.requirements.interpreter.specification.ACSVRequirementsSpecification;
import grader.basics.requirements.interpreter.specification.CSVRequirementsSpecification;
import grader.basics.testcase.PassFailJUnitTestCase;
import util.annotations.Visible;

public class AnInterpretingGradableJUnitTopLevelSuite extends AGradableJUnitTopLevelSuite{

	public AnInterpretingGradableJUnitTopLevelSuite(CSVRequirementsSpecification aCSVRequirementsSpecification) {
		super(null);
		addDescendents(aCSVRequirementsSpecification);
		
	}
	@Override
	/**
	 * do nothing as JUnit class is null
	 */
	public void setJUnitClass(Class aJUnitClass) {
	}
	@Visible(false)
	public String getSimpleName() {
		return "root";
	}
	protected void addDescendents(CSVRequirementsSpecification aCSVRequirementsSpecification) {
		int aNumRequirements = aCSVRequirementsSpecification.getNumberOfRequirements();

		for (int aRequirementNumber = 0; aRequirementNumber <=  aNumRequirements; aRequirementNumber++) {
			String aDescription = aCSVRequirementsSpecification.getDescription(aRequirementNumber);
			if (aDescription == null) {
				continue;
			}
				
			String[] aDescriptionNames = aDescription.split("\\.");
			String aSimpleDescription = aDescriptionNames[aDescriptionNames.length - 1];
			PassFailJUnitTestCase aPassFailJUnitTestCase = new AnInterpretingJUnitTestCase(aSimpleDescription, aCSVRequirementsSpecification, aRequirementNumber);
			GradableJUnitTest aGradableJUnitTest = 
					new AnInterpretingGradableJUnitTest(AnInterpretingJUnitTestCase.class, aPassFailJUnitTestCase,aSimpleDescription, aCSVRequirementsSpecification, aRequirementNumber );
			add(aGradableJUnitTest);
		}
	}
	public static RootFolderProxy searchForAssignmentDataProxy(File aParentFolder) {
		File aFile = searchForAssignmentDataFolder(aParentFolder);
		
		if (aFile == null || !aFile.exists())
			return null;
		if (aFile.isDirectory()) {
			return new AFileSystemRootFolderProxy(aFile.getAbsolutePath());
		}
		return new AZippedRootFolderProxy(aFile.getAbsolutePath());
	}
	public static File searchForAssignmentDataFolder(File aParentFolder) {
		File[] aFiles = aParentFolder.listFiles();
//		System.out.println(aParentFolder.getAbsolutePath());
		for (File aFile:aFiles) {
			String aFileName = aFile.getName();
//			if (
//					aFileName.equals(aProject.getBuildFolder().getName()) ||
//					aFileName.equals(aProject.getSourceFolder().getName())
//				) {
//				continue;
//			}
			
			if (aFileName.startsWith("."))
				continue;
			String[] aFileNames = aFileName.split("\\.");
			int aLastIndex = aFileNames[0].length() - 1;
			char aLastChar = aFileNames[0].charAt(aLastIndex);
			if (Character.isDigit(aLastChar)) { // "recitation1"
				return aFile;
			}
			if (aFileName.contains("ssignment")) {
				return aFile;
			}
			
			
		}
		return null;		
	}
	public static File getSpecifiedRequirementsFile (File aCurrentDirFile, String aRelativeLocation) {
		try {
			String aCurrentDir = aCurrentDirFile.getCanonicalPath();
			File aFile = new File (aCurrentDir + "/" + aRelativeLocation);
			return aFile;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static File getUnspecifiedRequirementsFile (File aCurrentDirFile) {
		File anAssignmentsDataFolder = searchForAssignmentDataFolder(aCurrentDirFile) ;
		
		if (!anAssignmentsDataFolder.exists()) {
			return null;
		}
		else {
			File aFile = null;
			try {
				aFile = new File (aCurrentDirFile.getCanonicalPath() + "/" + CSVRequirementsSpecification.DEFAULT_REQUIREMENTS_SPREADHEET_NAME);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return aFile;
		}
		
	}
	public static FileProxy getUnspecifiedRequirementsFile (RootFolderProxy aCurrentDirFile) {
//		Set<String> aFiles = aCurrentDirFile.getChildrenNames();
		List<FileProxy> aFiles = aCurrentDirFile.getFileEntries();
//		String aFileName = null;
		for (FileProxy aFile:aFiles) {
			String aFileName = aFile.getLocalName();
//			if (aFileName.equals(CSVRequirementsSpecification.DEFAULT_REQUIREMENTS_SPREADHEET_NAME)) {
//				return aFile;
//			}
			if (aFileName.endsWith(".csv") && (aFileName.contains("equirements"))) {
				return aFile;
			}
		}
		
		return null;
		
	}
	public static File getRequirementsFile() {
		String aConfigurationFileName = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getRequirementsLocation();
		File aCurrentDir = new File(".");
		if (aConfigurationFileName != null) {
			return getSpecifiedRequirementsFile(aCurrentDir, aConfigurationFileName);
		} else {
			return getUnspecifiedRequirementsFile(aCurrentDir);
		}
	}
	
	public static void main (String[] args) {
		RootFolderProxy anAssignmentData = searchForAssignmentDataProxy(new File("."));
		FileProxy aRequirementsFile = getUnspecifiedRequirementsFile(anAssignmentData);
		
		System.out.println(aRequirementsFile);
		CSVRequirementsSpecification aSpecification = new ACSVRequirementsSpecification(aRequirementsFile);
		GradableJUnitSuite aTopLevelSuite = new AnInterpretingGradableJUnitTopLevelSuite(aSpecification);
		ObjectEditor.treeEdit(aTopLevelSuite);
//		int aNumRequirements = aSpecification.getNumberOfRequirements();
//		for (int aRequirementNumber = 0; aRequirementNumber <=  aNumRequirements; aRequirementNumber++) {
//			String aDescription = aSpecification.getDescription(aRequirementNumber);
//			String[] aDescriptionNames = aDescription.split("\\.");
//		}
			
	}

}
