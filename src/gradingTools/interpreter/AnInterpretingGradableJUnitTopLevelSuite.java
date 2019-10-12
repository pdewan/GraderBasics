package gradingTools.interpreter;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bus.uigen.ObjectEditor;
import grader.basics.assignment.AssignmentDataHolder;
import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.file.FileProxy;
import grader.basics.file.RootFolderProxy;
import grader.basics.file.filesystem.AFileSystemRootFolderProxy;
import grader.basics.file.zipfile.AZippedRootFolderProxy;
import grader.basics.junit.AGradableJUnitTopLevelSuite;
import grader.basics.junit.GradableJUnitSuite;
import grader.basics.junit.GradableJUnitTest;
import grader.basics.requirements.interpreter.specification.ACSVRequirementsSpecification;
import grader.basics.requirements.interpreter.specification.CSVRequirementsSpecification;
import grader.basics.testcase.PassFailJUnitTestCase;
import util.annotations.Visible;

public class AnInterpretingGradableJUnitTopLevelSuite extends AGradableJUnitTopLevelSuite{
	protected Map<String, GradableJUnitSuite> stringToGradableSuite = new HashMap<>();
	public static final String emptyString = "";
	static protected RootFolderProxy assignmentsDataFolderProxy;

	public AnInterpretingGradableJUnitTopLevelSuite(RootFolderProxy aParentFolder, CSVRequirementsSpecification aCSVRequirementsSpecification) {
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
	protected void addLeaf(
//			FileProxy aParentFolder,
			GradableJUnitSuite aParentSuite,
			CSVRequirementsSpecification aCSVRequirementsSpecification, 			
			int aRequirementNumber,
			String aSimpleDescription) {
//		String aDescription = aCSVRequirementsSpecification.getDescription(aRequirementNumber);
//		if (aDescription == null) {
//			return;
//		}
			
//		String[] aDescriptionNames = aDescription.split("\\.");
//		String aSimpleDescription = aDescriptionNames[aDescriptionNames.length - 1];
		PassFailJUnitTestCase aPassFailJUnitTestCase = new AnInterpretingJUnitTestCase(aSimpleDescription, aCSVRequirementsSpecification, aRequirementNumber);
		GradableJUnitTest aGradableJUnitTest = 
				new AnInterpretingGradableJUnitTest(AnInterpretingJUnitTestCase.class, aPassFailJUnitTestCase,aSimpleDescription, aCSVRequirementsSpecification, aRequirementNumber );
		aParentSuite.add(aGradableJUnitTest);
	}
	protected void addDescendents(
//			FileProxy aParentFolder,
			CSVRequirementsSpecification aCSVRequirementsSpecification, 
			int aRequirementNumber, 
			String[] aDescriptionComponents, 
			String aParentString, 
			int anIndex) {
		GradableJUnitSuite aParentSuite = stringToGradableSuite.get(aParentString);
		
		
		if (anIndex == aDescriptionComponents.length - 1 ) {
			
			addLeaf(aParentSuite, aCSVRequirementsSpecification, aRequirementNumber, aDescriptionComponents[anIndex]);	
			return;
		} else {
			String aNodeFullName = aParentString + "_" + aDescriptionComponents[anIndex];
			GradableJUnitSuite anInternalNode = stringToGradableSuite.get(aNodeFullName);
			if (anInternalNode == null) {
				anInternalNode = new AnInterpretingGradableJUnitSuite(aDescriptionComponents[anIndex]); // may need special node
				stringToGradableSuite.put(aNodeFullName, anInternalNode);
				aParentSuite.add(anInternalNode);
			}
			addDescendents(aCSVRequirementsSpecification, aRequirementNumber, aDescriptionComponents, aNodeFullName, anIndex + 1);			
		}
		
	}
	protected void addDescendents(CSVRequirementsSpecification aCSVRequirementsSpecification) {
		int aNumRequirements = aCSVRequirementsSpecification.getNumberOfRequirements();
		stringToGradableSuite.put(emptyString, this);			

		for (int aRequirementNumber = 0; aRequirementNumber <  aNumRequirements; aRequirementNumber++) {
			String aDescription = aCSVRequirementsSpecification.getDescription(aRequirementNumber);
			if (aDescription == null) {
				continue;
			}
				
			String[] aDescriptionNames = aDescription.split("\\.");
			addDescendents(aCSVRequirementsSpecification, aRequirementNumber, aDescriptionNames, emptyString, 0);

//			String aSimpleDescription = aDescriptionNames[aDescriptionNames.length - 1];
//			PassFailJUnitTestCase aPassFailJUnitTestCase = new AnInterpretingJUnitTestCase(aSimpleDescription, aCSVRequirementsSpecification, aRequirementNumber);
//			GradableJUnitTest aGradableJUnitTest = 
//					new AnInterpretingGradableJUnitTest(AnInterpretingJUnitTestCase.class, aPassFailJUnitTestCase,aSimpleDescription, aCSVRequirementsSpecification, aRequirementNumber );
//			add(aGradableJUnitTest);
		}
	}
	@Visible(false)
	public static RootFolderProxy getAssignmentDataProxy() {
		if (assignmentsDataFolderProxy == null) {
			assignmentsDataFolderProxy = searchForAssignmentDataProxy(new File("."));
		}
		return assignmentsDataFolderProxy;
	}
	@Visible(false)
	public static RootFolderProxy searchForAssignmentDataProxy(File aParentFolder) {
		RootFolderProxy retVal = AssignmentDataHolder.getAssignmentDataFileProxy();
		if (retVal != null) {
			return retVal;
		}
		File aFile = searchForAssignmentDataFolder(aParentFolder);		
//		if (aFile == null || !aFile.exists()) {
//			String aParentFolderName = aParentFolder.getName();
//			URL aURL = AnInterpretingGradableJUnitSuite.class.getResource(".");
//			
//			try {
//				File aURLFile = new File(aURL.toURI());
//				aFile = searchForAssignmentDataFolder(aURLFile);
//			} catch (URISyntaxException e) {
//				e.printStackTrace();
//				return null;
//
//			}
//						
//		}
		
//		if (aFile == null || !aFile.exists())
//			return null;
		
		if (aFile == null || !aFile.exists())
			return null;
		if (aFile.isDirectory()) {
			AssignmentDataHolder.setAssignmentDataFile(aFile);
			retVal = new AFileSystemRootFolderProxy(aFile.getAbsolutePath());
		} else {
		    retVal = new AZippedRootFolderProxy(aFile.getAbsolutePath());
		}
		AssignmentDataHolder.setAssignmentDataFileProxy(retVal);
		return retVal;
	}
	@Visible(false)
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
	@Visible(false)
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
	@Visible(false)
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
	@Visible(false)
	public static FileProxy getUnspecifiedRequirementsFile (RootFolderProxy aCurrentDirFile) {
		if (aCurrentDirFile == null) {
			return null;
		}
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
	@Visible(false)
	public static File getRequirementsFile() {
		String aConfigurationFileName = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getRequirementsLocation();
		File aCurrentDir = new File(".");
		if (aConfigurationFileName != null) {
			return getSpecifiedRequirementsFile(aCurrentDir, aConfigurationFileName);
		} else {
			return getUnspecifiedRequirementsFile(aCurrentDir);
		}
	}
	@Visible(false)
	public static void runSpecifiedTests( ) {
//		RootFolderProxy anAssignmentData = searchForAssignmentDataProxy(new File("."));
		RootFolderProxy anAssignmentData = getAssignmentDataProxy();
		runSpecifiedTests(anAssignmentData);

//		FileProxy aRequirementsFile = getUnspecifiedRequirementsFile(anAssignmentData);
//		if (aRequirementsFile == null) {
//			System.err.println("Could not find requirements file");
//			return;
//		}
//		String aFileName = aRequirementsFile.getMixedCaseLocalName();
//		String[] aFileNameComponents = aFileName.split("_|-");
//		if (aFileNameComponents.length < 3) {
//			System.err.println(aFileName + " not of the form <Course>_<Problem>_Requirements.csv");
//		} else {
//			BasicStaticConfigurationUtils.setModule(aFileNameComponents[0]);
//			BasicStaticConfigurationUtils.setProblem(aFileNameComponents[1]);
//		}
//		
////		System.out.println(aRequirementsFile);
//		CSVRequirementsSpecification aSpecification = new ACSVRequirementsSpecification(aRequirementsFile);
//		GradableJUnitSuite aTopLevelSuite = new AnInterpretingGradableJUnitTopLevelSuite(assignmentsDataFolderProxy, aSpecification);
//		ObjectEditor.treeEdit(aTopLevelSuite);
	}
	public static void runSpecifiedTests(RootFolderProxy anAssignmentData  ) {
//		RootFolderProxy anAssignmentData = searchForAssignmentDataProxy(new File("."));
//		RootFolderProxy anAssignmentData = getAssignmentDataProxy();

		FileProxy aRequirementsFile = getUnspecifiedRequirementsFile(anAssignmentData);
		if (aRequirementsFile == null) {
			System.err.println("Could not find requirements file");
			return;
		}
		String aFileName = aRequirementsFile.getMixedCaseLocalName();
		String[] aFileNameComponents = aFileName.split("_|-");
		if (aFileNameComponents.length < 3) {
			System.err.println(aFileName + " not of the form <Course>_<Problem>_Requirements.csv");
		} else {
			BasicStaticConfigurationUtils.setModule(aFileNameComponents[0]);
			BasicStaticConfigurationUtils.setProblem(aFileNameComponents[1]);
		}
		
//		System.out.println(aRequirementsFile);
		CSVRequirementsSpecification aSpecification = new ACSVRequirementsSpecification(aRequirementsFile);
		GradableJUnitSuite aTopLevelSuite = new AnInterpretingGradableJUnitTopLevelSuite(assignmentsDataFolderProxy, aSpecification);
		ObjectEditor.treeEdit(aTopLevelSuite);
	}
	@Visible(false)
	public static void runSpecifiedTests(URL anAssignmentDataFolderName ) {
//		RootFolderProxy anAssignmentData = searchForAssignmentDataProxy(new File("."));
		File aFile;
		try {
			aFile = new File(anAssignmentDataFolderName.toURI());
			AssignmentDataHolder.setAssignmentDataFile(aFile);
			RootFolderProxy aProxy = new AFileSystemRootFolderProxy(aFile);
			AssignmentDataHolder.setAssignmentDataFileProxy(aProxy);


			runSpecifiedTests(aProxy);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return;
		}
//		RootFolderProxy aProxy = new AFileSystemRootFolderProxy(aFile);
//		RootFolderProxy anAssignmentData = getAssignmentDataProxy();
//
//		FileProxy aRequirementsFile = getUnspecifiedRequirementsFile(anAssignmentData);
//		if (aRequirementsFile == null) {
//			System.err.println("Could not find requirements file");
//			return;
//		}
//		String aFileName = aRequirementsFile.getMixedCaseLocalName();
//		String[] aFileNameComponents = aFileName.split("_|-");
//		if (aFileNameComponents.length < 3) {
//			System.err.println(aFileName + " not of the form <Course>_<Problem>_Requirements.csv");
//		} else {
//			BasicStaticConfigurationUtils.setModule(aFileNameComponents[0]);
//			BasicStaticConfigurationUtils.setProblem(aFileNameComponents[1]);
//		}
//		
////		System.out.println(aRequirementsFile);
//		CSVRequirementsSpecification aSpecification = new ACSVRequirementsSpecification(aRequirementsFile);
//		GradableJUnitSuite aTopLevelSuite = new AnInterpretingGradableJUnitTopLevelSuite(assignmentsDataFolderProxy, aSpecification);
//		ObjectEditor.treeEdit(aTopLevelSuite);
	}
	public static void main (String[] args) {
		runSpecifiedTests();
//		RootFolderProxy anAssignmentData = searchForAssignmentDataProxy(new File("."));
//		FileProxy aRequirementsFile = getUnspecifiedRequirementsFile(anAssignmentData);
//		if (aRequirementsFile == null) {
//			System.err.println("Could not find requirements file");
//			return;
//		}
//		String aFileName = aRequirementsFile.getMixedCaseLocalName();
//		String[] aFileNameComponents = aFileName.split("_|-");
//		if (aFileNameComponents.length < 3) {
//			System.err.println(aFileName + " not of the form <Course>_<Problem>_Requirements.csv");
//		} else {
//			BasicStaticConfigurationUtils.setModule(aFileNameComponents[0]);
//			BasicStaticConfigurationUtils.setProblem(aFileNameComponents[1]);
//		}
//		
////		System.out.println(aRequirementsFile);
//		CSVRequirementsSpecification aSpecification = new ACSVRequirementsSpecification(aRequirementsFile);
//		GradableJUnitSuite aTopLevelSuite = new AnInterpretingGradableJUnitTopLevelSuite(aSpecification);
//		ObjectEditor.treeEdit(aTopLevelSuite);
////		int aNumRequirements = aSpecification.getNumberOfRequirements();
////		for (int aRequirementNumber = 0; aRequirementNumber <=  aNumRequirements; aRequirementNumber++) {
////			String aDescription = aSpecification.getDescription(aRequirementNumber);
////			String[] aDescriptionNames = aDescription.split("\\.");
////		}
			
	}

}
