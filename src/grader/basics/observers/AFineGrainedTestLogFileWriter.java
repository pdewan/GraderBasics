package grader.basics.observers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.runner.Description;
import org.junit.runner.Result;

import grader.basics.junit.GradableJUnitSuite;
import grader.basics.junit.GradableJUnitTest;
import grader.basics.vetoers.AConsentFormVetoer;

public class AFineGrainedTestLogFileWriter extends AnAbstractTestLogFileWriter{
	
	private final String EXTENDED_HEADER = HEADER+",SessionNumber,SessionRunNumber,IsSuite,SuiteTests,PrerequisiteTests,";
	private final String FILENAME_MODIFIER="FineGrained";
	
	public static final int SESSION_NUMBER_INDEX = 9;
	public static final int SESSION_RUN_NUMBER_INDEX = 10;
	public static final int IS_SUITE_INDEX = 11;
	public static final int SUITE_TESTS_INDEX = 12;
	public static final int PREREQUISITE_TESTS_INDEX = 13;
	
	List<String> sortedPrereqTests=null;
	List<String> sortedSuiteTests=null;
	
	int sessionNumber=0;
	
	File sessionDataFile;
	public static final int SESSION_DATA_SESSION_NUMBER_INDEX=0;
	public static final int SESSION_DATA_TOTAL_RUN_INDEX=1;
	
	private boolean testIsSuite;
	
	
	public AFineGrainedTestLogFileWriter() {
		super();
	}
	
	@Override
	protected String getHeader() {
		return EXTENDED_HEADER;
	}
	
	@Override
	public void testRunStarted(Description description) throws Exception {
		try {
			super.testRunStarted(description);
			if (idField == null) {
		    idField = Description.class.getDeclaredField("fUniqueId"); // ugh but why not give us the id?
			idField.setAccessible(true);
			}
			AbstractMap.SimpleEntry<GradableJUnitSuite, GradableJUnitTest> anEntry = 
				(AbstractMap.SimpleEntry<GradableJUnitSuite, GradableJUnitTest>) idField.get(description);
				
			GradableJUnitSuite aTopLevelSuite = anEntry.getKey();
			Class aTopLevelSuiteClass = aTopLevelSuite.getJUnitClass();
			GradableJUnitTest aTestOrSuiteSelected = anEntry.getValue();
			Class aTestOrSuiteSelectedClass = aTestOrSuiteSelected.getJUnitClass();

			
			testIsSuite = aTestOrSuiteSelected instanceof GradableJUnitSuite;
			if(testIsSuite) {
				sortedSuiteTests = new ArrayList();
				for(GradableJUnitTest test : ((GradableJUnitSuite)aTestOrSuiteSelected).children()) {
					sortedSuiteTests.add(test.getSimpleName());
				}
				Collections.sort(sortedSuiteTests);
			}else
				sortedSuiteTests=null;
			
			if (numRuns == 0) {
				totalTests = aTopLevelSuite.getLeafClasses().size();				
				logFileName = AConsentFormVetoer.LOG_DIRECTORY + "/" + toFileName(aTopLevelSuite) + FILENAME_MODIFIER + LOG_SUFFIX;
				sessionDataFile = new File(AConsentFormVetoer.LOG_DIRECTORY + "/" + toFileName(aTopLevelSuite) + FILENAME_MODIFIER + "_data.txt");
				
				if (maybeReadLastLineOfLogFile(logFileName)) {
					maybeLoadSavedSets();
					maybeCreateOrLoadAppendableFile(logFileName);
					maybeDetermineConsecutiveTestRunNumber(aTopLevelSuite);
				}

			}
			currentTopSuite = aTopLevelSuite;
			currentTest = description.getClassName();
			saveState();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
			
    }
	
	private void maybeDetermineConsecutiveTestRunNumber(GradableJUnitSuite aTopLevelSuite) {
		if(sessionDataFile.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(sessionDataFile));
				String [] firstLine = br.readLine().split(NAME_SEPARATOR);
				br.close();
				sessionNumber = Integer.parseInt(firstLine[SESSION_DATA_SESSION_NUMBER_INDEX])+1;
//				numTotalRuns = Integer.parseInt(firstLine[SESSION_DATA_TOTAL_RUN_INDEX]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void checkTotalRunNumber() {
		if(sessionDataFile.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(sessionDataFile));
				String [] firstLine = br.readLine().split(NAME_SEPARATOR);
				br.close();
				numTotalRuns = Integer.parseInt(firstLine[SESSION_DATA_TOTAL_RUN_INDEX]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void writeToSessionDataFile() {
		try {
			if(sessionDataFile==null) 
				throw new Exception("Session Data File Never Set");
			
			BufferedReader br = new BufferedReader(new FileReader(sessionDataFile));
			String [] firstLine = br.readLine().split(NAME_SEPARATOR);
			br.close();
			
			int determinedSessionNumber = Integer.parseInt(firstLine[SESSION_DATA_SESSION_NUMBER_INDEX]);
			int determinedTotalRuns = Integer.parseInt(firstLine[SESSION_DATA_TOTAL_RUN_INDEX]);
			
			determinedSessionNumber = determinedSessionNumber > sessionNumber ? determinedSessionNumber : sessionNumber;
			determinedTotalRuns = determinedTotalRuns > numTotalRuns ? determinedTotalRuns : numTotalRuns;
			
			sessionDataFile.createNewFile();
			FileWriter fw = new FileWriter(sessionDataFile);
			fw.append(determinedSessionNumber+NAME_SEPARATOR+determinedTotalRuns);
			fw.close();
		}catch(Exception e) {
			System.err.println(e);
		}
	}
	
	
	@Override
	public void testRunFinished(Result aResult) throws Exception {
		try {
		super.testRunFinished(aResult);
		loadCurrentSets(currentTopSuite);
		correctUntested();
		setPassPercentage();
		checkTotalRunNumber();
		composeTrace();
		appendLine(fullTrace.toString());		
		sortedPrereqTests=null;
		numRuns++;
		numTotalRuns++;
		writeToSessionDataFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void composeTrace() {
		super.composeTrace();
		fullTrace.append(sessionNumber+",");
		fullTrace.append(numRuns+",");
		fullTrace.append(testIsSuite+",");
		fullTrace.append(composeString(sortedSuiteTests)+",");
		fullTrace.append(composeString(sortedPrereqTests)+",");
	}
	
	private StringBuilder composeString(List<String> dataSrc) {
		StringBuilder string = new StringBuilder();
		if(dataSrc==null) {
			string.append("");
			return string;
		}
		for(String data:dataSrc) {
			string.append(data);
			string.append(NAME_SEPARATOR);
		}
		return string;
	}
	
	@Override
	public void testStarted(Description description) throws Exception {
		super.testStarted(description);	
		
		boolean testIsPrereq;
		
		String fixedClassName=description.getClassName().replaceAll(".*\\.", "");
//		String test = description.getClassName();
		
		if(testIsSuite) 
			testIsPrereq=!sortedSuiteTests.contains(fixedClassName);
		else
			testIsPrereq=!currentTest.equals(fixedClassName);
		
		if(testIsPrereq) {
			if(sortedPrereqTests==null) 
				sortedPrereqTests=new ArrayList<String>();
			sortedPrereqTests.add(fixedClassName);
		}
		
    }
	
	
}