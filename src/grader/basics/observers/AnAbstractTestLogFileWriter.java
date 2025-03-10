package grader.basics.observers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.WritableToken;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.junit.GradableJUnitSuite;
import grader.basics.junit.GradableJUnitTest;
import grader.basics.observers.logSending.JSONObject;
import grader.basics.project.CurrentProjectHolder;
import grader.basics.project.Project;
import grader.basics.trace.CheckersLogFolderCreated;
import grader.basics.trace.JUnitLogFileCreatedOrLoaded;
import grader.basics.vetoers.AConsentFormVetoer;
import util.misc.Common;
import util.trace.Tracer;


public abstract class AnAbstractTestLogFileWriter extends RunListener {
	public static final String NAME_SEPARATOR = " ";
	public static final String LOG_SUFFIX = ".csv";
	// must be consecutine indices
	public static final int RUN_INDEX = 0; 
	public static final int DATE_INDEX = 1;
	public static final int SCORE_INDEX = 2;
	public static final int SCORE_INCREMENT_INDEX = 3;
	public static final int TEST_NAME_INDEX = 4;
	public static final int PASS_INDEX = 5;
	public static final int PARTIAL_PASS_INDEX = 6;
	public static final int FAIL_INDEX = 7;
	public static final int UNTESTED_INDEX = 8;
	public static final String HEADER= "#,Time,%Passes,Change,Test,Pass,Partial,Fail,Untested";
	int numRuns = 0;
	int numTotalRuns = 0;
	Integer totalTests = null;
	File receivedHelpFile;
	File readHelpFile;
	
	protected  PrintWriter out = null, out_altLocation = null;
	protected  BufferedWriter bufWriter, bufWriter_altLocation;	
	
	protected String logFilePath, logFileName;
	protected String previousContents;
	Field idField;
	List<String[]> previousRunData = new ArrayList();
	String[] normalizedLastLines = null;
	Set<String> previousPasses = new HashSet();
	Set<String> previousPartialPasses = new HashSet();
	Set<String> previousFails = new HashSet();
	Set<String> previousUntested = new HashSet();
	double previousScore = 0.0;
	int previousPassPercentage = 0;
	int currentPassPercentage = 0;
	
	// may not really use them
	Set<String> currentPasses = new HashSet();
	Set<String> currentPartialPasses = new HashSet();
	Set<String> currentFails = new HashSet();
	Set<String> currentUntested = new HashSet();
	double currentScore = 0.0;
	
	// sorted lists
	List<String> sortedPasses = new ArrayList();
	List<String> sortedPartialPasses = new ArrayList();
	List<String> sortedFails = new ArrayList();
	List<String> sortedUntested = new ArrayList();

	
	GradableJUnitSuite currentTopSuite;
	String currentTest;
	String lastLine, lastLineNormalized;
	

	public AnAbstractTestLogFileWriter() {
		maybeCreateChecksFolder();
	}
	
	//Override the following methods to change output format
	public String getLogFileName() {
		return logFileName;
	}
	public String getLogFilePath() {
		return logFilePath;
	}
	@Override
	public void testRunFinished(Result aResult) throws Exception {
		super.testRunFinished(aResult);
	}
	
	@Override
	public void testRunStarted(Description description) throws Exception {
		super.testRunStarted(description);
    }

	@Override
	public void testStarted(Description description) throws Exception {
		super.testStarted(description);	
    }
	@Override
	public void testAssumptionFailure(Failure aFailure) {
		super.testAssumptionFailure(aFailure);
	}
	
	@Override
	public void testFailure(Failure aFailure) throws Exception {
		super.testFailure(aFailure);
	}
	
	@Override
	public void testFinished(Description description) {
		try {
			super.testFinished(description);
			
//			System.out.println ("Test finished:"+ description);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	Date lastDate;
	protected Date getLastDate() {
		return lastDate;
	}
	public void composeTrace() {
		fullTrace.setLength(0);
		composePassString();
		composePartialPassString();
		composeFailString();
		composeUntestedString();
		fullTrace.append(""+numTotalRuns);
		Date aDate = new Date(System.currentTimeMillis());
		lastDate = aDate;
		fullTrace.append("," + aDate);
		fullTrace.append("," + currentPassPercentage);
		fullTrace.append("," + (currentPassPercentage - previousPassPercentage));
		fullTrace.append("," + currentTest);
		fullTrace.append("," + passStringBulder + NAME_SEPARATOR);
		fullTrace.append("," + partialPassStringBulder + NAME_SEPARATOR);
		fullTrace.append("," + failStringBulder  + NAME_SEPARATOR);
		fullTrace.append("," + untestedStringBuilder + NAME_SEPARATOR + ",");
	}
	
	protected String getHeader() {
		return HEADER;
	}
	
	// helper methods
	
	StringBuilder passStringBulder = new StringBuilder();
	StringBuilder partialPassStringBulder = new StringBuilder();
	StringBuilder failStringBulder = new StringBuilder();
	StringBuilder untestedStringBuilder = new StringBuilder();
	StringBuilder fullTrace = new StringBuilder();
	
	

	public String getPassMarker(String aPass) {
		if (previousPasses.contains(aPass)) {
			return "";
		}
		return "+"; // things can only improve if put in pass
	}
	public String getPartialPassMarker(String aPartialPass) {
		if (previousPartialPasses.contains(aPartialPass)) {
			return "";
		}
		if (previousPasses.contains(aPartialPass))
			return "-"; 
		return "+"; // otherwise it is an improvement
	}
	public String getFailMarker(String aFail) {
		if (previousFails.contains(aFail)) {
			return "";
		}
		if (numTotalRuns==0||previousUntested.contains(aFail)) //first run edge case
			return "+"; 
		return "-"; // otherwise it is a come down
	}
	public void composePassString() {
		passStringBulder.setLength(0);
		List<String> aPassList = sort(currentPasses);
		boolean aFirstName = true;

		for (String aPass:aPassList) {
			if (!aFirstName) {
				passStringBulder.append(NAME_SEPARATOR);
			} else {
				aFirstName = false;
			}
			passStringBulder.append(aPass);
			passStringBulder.append(getPassMarker(aPass));

		}
	}
	
	public void composePartialPassString() {
		partialPassStringBulder.setLength(0);
		List<String> aParialPassList = sort(currentPartialPasses);
		boolean aFirstName = true;

		for (String aPartialPass:aParialPassList) {
			if (!aFirstName) {
				partialPassStringBulder.append(NAME_SEPARATOR);
			} else {
				aFirstName = false;
			}
			partialPassStringBulder.append(aPartialPass);
			partialPassStringBulder.append(getPartialPassMarker(aPartialPass));

		}
	}
	
	public void composeFailString() {
		failStringBulder.setLength(0);
		List<String> aFailList = sort(currentFails);
		boolean aFirstName = true;
		for (String aFail:aFailList) {
			if (!aFirstName) {
				failStringBulder.append(NAME_SEPARATOR);
			} else {
				aFirstName = false;
			}
			failStringBulder.append(aFail);
			failStringBulder.append(getFailMarker(aFail));
			
		}
	}
	public void composeUntestedString() {
		untestedStringBuilder.setLength(0);
		List<String> anUntestedList = sort(currentUntested);
		boolean aFirstName = true;
		for (String anUntested:anUntestedList) {
			if (!aFirstName) {
				untestedStringBuilder.append(NAME_SEPARATOR);
			} else {
				aFirstName = false;
			}
			untestedStringBuilder.append(anUntested);
		}
	}

	

	protected void closeFile() {
		if (out == null) return;
		out.close();
		bufWriter = null;
		
		if (out_altLocation == null) return;
		out_altLocation.close();
		bufWriter_altLocation = null;
	}
	
	void appendLine(String aLine) {
		if (out == null) return;
		Tracer.info(this, aLine);
		out.println(aLine);
		out.flush();
		
		if (out_altLocation == null) return;
		Tracer.info(this, aLine);
		out_altLocation.println(aLine);
		out_altLocation.flush();
		
	}
	
	 void maybeCreateOrLoadAppendableFile(String aFileName) {
		 if (out != null && bufWriter != null) return;
		 
	        String aFullFileName = aFileName;
	        File aFile = new File(aFullFileName);
	        boolean aNewFile = !aFile.exists();
	        try {
	            bufWriter
	                    = Files.newBufferedWriter(
	                            Paths.get(aFullFileName),
	                            Charset.forName("UTF8"),
	                            StandardOpenOption.WRITE,
	                            StandardOpenOption.APPEND,
	                            StandardOpenOption.CREATE);
	            out = new PrintWriter(bufWriter, true);
	            if (aNewFile) {
	            	aFile.createNewFile();
	            	appendLine(getHeader());
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            //Oh, no! Failed to create PrintWriter
	        }

	        JUnitLogFileCreatedOrLoaded.newCase(aFullFileName, this);

	        maybeCreateOrLoadSecondAppendableFile(aFile);
	        
	        maybeReadPreviousContents();
	    }
	 
	 private void maybeReadPreviousContents() {
		 if (previousContents == null) {
		 String aFilePath = getLogFilePath();
		 previousContents = Common.toText(new File(aFilePath));
		 }
	 }
	 
	 public String getPreviousContents() {
		 return previousContents;
	 }
	 
	
	
	 private void maybeCreateOrLoadSecondAppendableFile(File javaFile) {
		 	File projectLocation =  CurrentProjectHolder.getProjectLocation();
		 
		 	if(!projectLocation.exists()) {
		 		System.err.println("Project Location does not exist");
		 		return;
		 	}
		 	File currentProjectLocation = new File(projectLocation,AConsentFormVetoer.LOG);
		 	if(!currentProjectLocation.exists())
		 		currentProjectLocation.mkdir();
		 	currentProjectLocation=new File(currentProjectLocation,AConsentFormVetoer.LOCALCHECKS); 
		 	if(!currentProjectLocation.exists())
		 		currentProjectLocation.mkdir();
		 	currentProjectLocation = new File(currentProjectLocation,"/"+logFileName);
		 	
		 	if(currentProjectLocation.getAbsolutePath().replace("\\.\\", "\\").equals(javaFile.getAbsolutePath())) return;
		 	
	        File aFile = currentProjectLocation;
	        String aFullFileName = aFile.getAbsolutePath();
	        boolean aNewFile = !aFile.exists();
	        try {
	            bufWriter_altLocation
	                    = Files.newBufferedWriter(
	                            Paths.get(aFullFileName),
	                            Charset.forName("UTF8"),
	                            StandardOpenOption.WRITE,
	                            StandardOpenOption.APPEND,
	                            StandardOpenOption.CREATE);
	            out_altLocation = new PrintWriter(bufWriter_altLocation, true);
	            if (aNewFile) {
	            	aFile.createNewFile();
	            	appendLine(getHeader());
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            //Oh, no! Failed to create PrintWriter
	        }

	        JUnitLogFileCreatedOrLoaded.newCase(aFullFileName, this);
	} 
	 
	public void testIgnored(Description description) throws Exception {
		super.testIgnored(description);

	}
	protected void setPassPercentage() {
		currentPassPercentage = (int) (100 * ((double) currentPasses.size())/totalTests);
	}
	
	public static String toDirectoryName(GradableJUnitTest aTest) {
		return AConsentFormVetoer.LOG_DIRECTORY + "/" + toFileName(aTest);
	}
	public static void maybeCreateChecksFolder() {
       
        File folder = new File(AConsentFormVetoer.LOG_DIRECTORY);
        if (folder.mkdirs()) { // true if dirs made, false otherwise
            CheckersLogFolderCreated.newCase(folder.getAbsolutePath(), AnAbstractTestLogFileWriter.class);
        }

    }
	
	public static int assignmentLength = "Assignment".length();
	public static String toAssignmentNumber(GradableJUnitTest aTopTest) {
		try {
		String anAssignmentName = toAssignmentName(aTopTest);
		if (anAssignmentName == null) {
			return null;
		}
		String anAssignmentNumber = anAssignmentName.
				substring(assignmentLength, anAssignmentName.length());
		return anAssignmentNumber;
		} catch (Exception e) {
			System.out.println("Test:" + aTopTest + "test class " + aTopTest.getSimpleName());
			e.printStackTrace();
			return "0";
			
		}
		
		
	}
	public static String toAssignmentName(GradableJUnitTest aTopTest) {
		String aClassName = aTopTest.getJUnitClass().getName();
		String[] aClassParts = aClassName.split("\\.");
		if (aClassParts.length <= 3) {
			return null;
		}
		String anAssignmentName = aClassParts[2];
		return anAssignmentName;
	}
	public static String composeLogFilePrefix (Project aProject, GradableJUnitSuite aTopLevelSuite ) {
		return aProject.getProjectFolder().getAbsolutePath() +
		"/" + AConsentFormVetoer.LOG_DIRECTORY + "/" + toFileName(aTopLevelSuite);
	}
//	public  String 	composeLogFilePrefix () {
//		return composeLastSourcesFileName(getProject(), aTopLevelSuite);
//
//	}

	
	public static String toFileName (GradableJUnitTest aTest) {
		String aClassName = aTest.getJUnitClass().getName();
		String[] aClassParts = aClassName.split("\\.");
		if (aClassParts.length <= 1) {
			return aClassName;
		}
		if (aClassParts[0].startsWith("grad") ||
				aClassParts[0].startsWith("check") ||
				aClassParts[0].startsWith("junit") ||
				aClassParts[0].startsWith("test") ||
				aClassParts[0].startsWith("suite")) {
			if (aClassParts.length == 2) {
				return aClassParts[1];
			}			
//			if (aClassParts.length == 3) {
//				String aFileName = aClassParts[2];
//				String aDirectoryName = aClassParts[1];
//				return aDirectoryName + "/" + aFileName;
//			}
//			String aDirectoryName = aClassParts[1];
//			String aFileName = aClassParts[2];
			String aFileName = aClassParts[1];
			for (int i = 2; i < aClassParts.length; i++) {
				aFileName += "_" + aClassParts[i];
			}
			return aFileName;
		} else {
			return aClassName.replaceAll(".", "_");
		}
	}
	
	protected boolean maybeReadLastLineOfLogFile(String aLogFileName) {
		if (!BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getLogTestData()) {
			return true;
		}
		try {
		File aFile = new File(aLogFileName);
		if (!aFile.exists()) {
			return true;
		}
		lastLine = tail(aFile, 1).trim();
		if (lastLine.startsWith("#")) {
			System.err.println ("Corrupt log file " + aLogFileName + " has only header");
			System.out.println("Deleting file:" + aLogFileName);
			aFile.delete();
			return false;
		}
//		String lastLineNormalized = lastLine.replaceAll("+|-", ""); // normalize it
		lastLineNormalized = lastLine.replaceAll("\\+|-", ""); // normalize it
		normalizedLastLines = lastLineNormalized.split(",");
		return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static String tail( File file, int lines) {
		if (file == null) {
			System.err.println("Null log file returning empty string");
			return "";
		}
	    java.io.RandomAccessFile fileHandler = null;
	    try {
	        fileHandler = 
	            new java.io.RandomAccessFile( file, "r" );
	        long fileLength = fileHandler.length() - 1;
	        StringBuilder sb = new StringBuilder();
	        int line = 0;

	        for(long filePointer = fileLength; filePointer != -1; filePointer--){
	            fileHandler.seek( filePointer );
	            int readByte = fileHandler.readByte();

	             if( readByte == 0xA ) {
	                if (filePointer < fileLength) {
	                    line = line + 1;
	                }
	            } else if( readByte == 0xD ) {
	                if (filePointer < fileLength-1) {
	                    line = line + 1;
	                }
	            }
	            if (line >= lines) {
	                break;
	            }
	            sb.append( ( char ) readByte );
	        }

	        String lastLine = sb.reverse().toString();
	        return lastLine;
	    } catch( java.io.FileNotFoundException e ) {
	    	System.err.println(e);
//	        e.printStackTrace();
	        return null;
	    } catch( java.io.IOException e ) {
	    	System.err.println(e);
//	        e.printStackTrace();
	        return null;
	    }
	    finally {
	        if (fileHandler != null )
	            try {
	                fileHandler.close();
	            } catch (IOException e) {
	            }
	    }
	}
	public static Set<String> toSet(String[] anArray) {
		return new HashSet(Arrays.asList(anArray));
	}
	
	public static Set<String> toStringSet(Set<Class> aClassSet) {
		Set<String> result = new HashSet();
		for (Class aClass:aClassSet) {
			result.add(aClass.getSimpleName());
		}
		return result;
	}
	
	protected void computePassString() {
		
	}
	
	protected void loadCurrentSets(GradableJUnitSuite aSuite) {
		
		currentScore = aSuite.getUnroundedScore();
		currentPasses = toStringSet(aSuite.getPassClasses());
		currentPartialPasses = toStringSet(aSuite.getPartialPassClasses());
		currentFails = toStringSet(aSuite.getFailClasses());
		currentUntested = toStringSet(aSuite.getUntestedClasses());
		
		
	}
	protected void correctUntested() {
		Set<String> aCorrectUntested = new HashSet();
		for (String aClass:currentUntested) {
			if (previousPasses.contains(aClass)) {
				currentPasses.add(aClass);
			} else if (previousPartialPasses.contains(aClass)) {
				currentPartialPasses.add(aClass);
			} else if (previousFails.contains(aClass)) {
				currentFails.add(aClass);
			} else {
				aCorrectUntested.add(aClass);
			}
		}
		currentUntested = aCorrectUntested;
	}
//	protected void loadCurentSets(GradableJUnitSuite aSuite) {
//		
//		currentScore = aSuite.getPreviousScore();
//		previousPasses = toStringSet(aSuite.getPassClasses());
//		previousPartialPasses = toStringSet(aSuite.getPartialPassClasses());
//		previousFails = toStringSet(aSuite.getFailClasses());
//		previousUntested = toStringSet(aSuite.getFailClasses());
//		
//	}
	
	protected void saveState() {
		previousPasses = currentPasses;
		previousPartialPasses = currentPartialPasses;
		previousFails = currentFails;
		previousUntested = currentUntested;
		previousScore = currentScore;
		previousPassPercentage = currentPassPercentage;
	}
	
	protected List<String> sort(Set<String> aSet) {
		List<String> result = new ArrayList();
		result.addAll(aSet);
		Collections.sort(result);
		return result;
	}
	
	protected void sortCurrentSets() {
		sortedPasses = sort (currentPasses);
		sortedPartialPasses = sort (currentPartialPasses);
		sortedFails = sort (currentFails);
		sortedUntested = sort (currentUntested);	
	}
	
	protected void maybeLoadSavedSets() {
		if (normalizedLastLines == null || numRuns > 0)
			return;
		String aRunsString = normalizedLastLines[RUN_INDEX];
//		String aScore = normalizedLastLines[SCORE_INDEX].trim();
		String aScoreIncrement = normalizedLastLines[SCORE_INCREMENT_INDEX].trim();
		String[] aPasses = normalizedLastLines[PASS_INDEX].split(NAME_SEPARATOR);
		String[] aPartialPasses = normalizedLastLines[PARTIAL_PASS_INDEX].split(NAME_SEPARATOR);
		String[] aFails = normalizedLastLines[FAIL_INDEX].split(NAME_SEPARATOR);
		String[] anUntested = normalizedLastLines[UNTESTED_INDEX].split(NAME_SEPARATOR);
//		currentScore = Double.parseDouble(aScore);
		numTotalRuns = Integer.parseInt(aRunsString) + 1;
		currentPasses = toSet(aPasses);
		currentPartialPasses = toSet(aPartialPasses);
		currentFails = toSet(aFails);
		currentUntested = toSet(anUntested);
		setPassPercentage();
	}
	public static JSONObject toJSONObject(GradableJUnitSuite aSuite) {
		JSONObject retVal = new JSONObject();
		retVal.put("name", aSuite.getSimpleName());
		JSONObject aChildrenJSON = new JSONObject();
		retVal.put("children", aChildrenJSON);
		List<GradableJUnitTest> aChildrenList = aSuite.getChildren();
		for (int anIndex = 0; anIndex < aChildrenList.size(); anIndex++) {
			GradableJUnitTest aChildTest = aChildrenList.get(anIndex);
			String anIndexString = Integer.toString(anIndex);
			if (aChildTest instanceof GradableJUnitSuite) {
				GradableJUnitSuite aChildSuite = (GradableJUnitSuite) aChildTest;
				JSONObject aChildJSONObject = toJSONObject(aChildSuite);
				aChildrenJSON.put(anIndexString, aChildJSONObject);
			} else {
				aChildrenJSON.put(anIndexString, aChildTest.getSimpleName());
			}
		}
		return retVal;

	}
	
	public  void appendToReceivedHelpFile(String aTextToAppend) {
		appendToFile(receivedHelpFile, aTextToAppend);
	}
	
	public void appendToReadHelpFile (String aTextToAppend) {
		appendToFile(readHelpFile, aTextToAppend);
	}
	
	public String readReceivedHelp() {
		return readFile(receivedHelpFile);
	}
	public void clearReceivedHelp() {
		writeToFile(receivedHelpFile, "");
	}
	public String readReadHelp() {
		return readFile(readHelpFile);
	}
	public void clearPastHelp() {
		writeToFile(readHelpFile, "");
	}
	public static void maybeCreateFile(File aFile) {
		try {
			if (aFile == null) {
				throw new Exception("Null file passed to maybeCreateFile");
			}
			if (aFile.exists()) {
				return;
			}
			aFile.createNewFile();
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public static void appendToFile (File aFile, String aTextToAppend) {
		try (FileWriter fileWriter = new FileWriter(aFile, true)) {
	        fileWriter.write(aTextToAppend);
	    } catch (IOException e) {
	        System.out.println("An error occurred: " + e.getMessage());
	    }
	}
	
	public static void writeToFile(File aFile, String aTextToWrite) {
		try {
			Common.writeText(aFile, aTextToWrite);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String readFile (File aFile) {
		return Common.toText(aFile);
	}

//	public static void main (String[] anArgs) {
//		long time = System.currentTimeMillis();
//		Date aDate = new Date(time);
//		System.out.println (aDate);
//
//		Date aDate2 = new Date(aDate.toString());
//		
//		System.out.println (aDate2);
//
//		long time2 = aDate2.getTime();
//		System.out.println ("written time == read time" + (time2 - time));
//		
//		
//	}
}
