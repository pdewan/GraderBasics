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
	
	private final String EXTENDED_HEADER = HEADER+",SessionNumber,SessionRunNumber,IsSuite,SuiteTests,PrerequisiteTests,ExtraCreditTests,TestScores,FailFromPreReq,";
	private final String FILENAME_MODIFIER="FineGrained";
	
	public static final int SESSION_NUMBER_INDEX = 9;
	public static final int SESSION_RUN_NUMBER_INDEX = 10;
	public static final int IS_SUITE_INDEX = 11;
	public static final int SUITE_TESTS_INDEX = 12;
	public static final int PREREQUISITE_TESTS_INDEX = 13;
	public static final int EXTRA_CREDIT_TESTS_INDEX = 14;
	public static final int TEST_SCORES_INDEX = 15;
	public static final int FailFromPreReq_INDEX=16;
	
	List<String> unsortedPrereqTests=null;
	List<String> unsortedExtraCreditTests=null;
	List<String> unsortedSuiteTests=null;
	List<String> unsortedTestScores=null;
	List<String> unsortedFailFromPreReq=null;
	
	int sessionNumber=0;
	
	File sessionDataFile;
	public static final int SESSION_DATA_SESSION_NUMBER_INDEX=0;
	public static final int SESSION_DATA_TOTAL_RUN_INDEX=1;
	
	private boolean testIsSuite;
	private GradableJUnitTest testedSuiteOrTest;
	private String topLevelInfo;
	
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

			testedSuiteOrTest=aTestOrSuiteSelected;
			topLevelInfo = toFileName(aTopLevelSuite);
			
			unsortedExtraCreditTests = new ArrayList<String>();
			
			testIsSuite = aTestOrSuiteSelected instanceof GradableJUnitSuite;
			if(testIsSuite) {
				unsortedSuiteTests = new ArrayList<String>();
				determineSuiteTests((GradableJUnitSuite)aTestOrSuiteSelected);
			}else {
				unsortedSuiteTests=null;
				GradableJUnitTest selectedTest = (GradableJUnitTest)aTestOrSuiteSelected;
				if(selectedTest.isExtra()) 
					unsortedExtraCreditTests.add(selectedTest.getSimpleName());
				
			}
				
			
			if (numRuns == 0) {
				totalTests = aTopLevelSuite.getLeafClasses().size();
				String fileLoc = AConsentFormVetoer.LOG_DIRECTORY + "/";
				logFileName = fileLoc + toFileName(aTopLevelSuite) + FILENAME_MODIFIER + LOG_SUFFIX;
				sessionDataFile = new File(fileLoc + toFileName(aTopLevelSuite) + FILENAME_MODIFIER + "_data.txt");
				
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
	
	private void determineSuiteTests(GradableJUnitSuite aSuite) {
		boolean suiteIsExtra = aSuite.isExtra();
		for(GradableJUnitTest test : aSuite.children()) {
			if(test instanceof GradableJUnitSuite) { //used in instances where there are nested Suites such as running the top assignment suite
				determineSuiteTests((GradableJUnitSuite)test);
				continue;
			}
			if(suiteIsExtra || test.isExtra())
				unsortedExtraCreditTests.add(test.getSimpleName());
			
			unsortedSuiteTests.add(test.getSimpleName());
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
			
			int determinedSessionNumber = 0;
			int determinedTotalRuns = 0;
			
			if(sessionDataFile.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(sessionDataFile));
				String [] firstLine = br.readLine().split(NAME_SEPARATOR);
				br.close();
				determinedSessionNumber = Integer.parseInt(firstLine[SESSION_DATA_SESSION_NUMBER_INDEX]);
				determinedTotalRuns = Integer.parseInt(firstLine[SESSION_DATA_TOTAL_RUN_INDEX]);
			}else {
				sessionDataFile.createNewFile();
			}
			
			determinedSessionNumber = determinedSessionNumber > sessionNumber ? determinedSessionNumber : sessionNumber;
			determinedTotalRuns = determinedTotalRuns > numTotalRuns ? determinedTotalRuns : numTotalRuns;
			

			FileWriter fw = new FileWriter(sessionDataFile);
			fw.write(determinedSessionNumber+NAME_SEPARATOR+determinedTotalRuns);
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
			determinePostRunData();
			correctUntested();
			setPassPercentage();
			checkTotalRunNumber();
			composeTrace();
			appendLine(fullTrace.toString());	
			unsortedPrereqTests=null;
			unsortedExtraCreditTests=null;
			unsortedTestScores=null;
			numRuns++;
			numTotalRuns++;
			writeToSessionDataFile();
			
			try {
				LogSender.sendToServer(fullTrace.toString(), topLevelInfo, numTotalRuns);
			}catch(Exception e) {
				System.err.println("Error resolving local checks server sending");
				System.err.println("Thrown message:\n"+e.getMessage());
			}
			
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
		fullTrace.append(composeString(unsortedSuiteTests)+",");
		fullTrace.append(composeString(unsortedPrereqTests)+",");
		fullTrace.append(composeString(unsortedExtraCreditTests)+",");
		fullTrace.append(composeString(unsortedTestScores)+",");
		fullTrace.append(composeString(unsortedFailFromPreReq)+",");
	}
	
	private StringBuilder composeString(List<String> dataSrc) {
		StringBuilder string = new StringBuilder();
		if(dataSrc==null||dataSrc.size()==0) {
			string.append("");
			return string;
		}
		Collections.sort(dataSrc);
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
			testIsPrereq=!unsortedSuiteTests.contains(fixedClassName);
		else
			testIsPrereq=!currentTest.equals(fixedClassName);
		
		if(testIsPrereq) {
			if(unsortedPrereqTests==null) 
				unsortedPrereqTests=new ArrayList<String>();
			unsortedPrereqTests.add(fixedClassName);	
		}
		
    }
	
	private void determinePostRunData() {
		unsortedTestScores = new ArrayList<String>();
		unsortedFailFromPreReq = new ArrayList<String>();
		
		if(testedSuiteOrTest instanceof GradableJUnitSuite)
			determineForEachTest((GradableJUnitSuite)testedSuiteOrTest);
		else {
			unsortedTestScores.add(composeTestScore(testedSuiteOrTest));
			checkForFailureCausedByPreRequisite(testedSuiteOrTest);
		}
			
	}
	
	private void determineForEachTest(GradableJUnitSuite aSuite) {
		for(GradableJUnitTest test : aSuite.children()) {
			if(test instanceof GradableJUnitSuite) { //used in instances where there are nested Suites such as running the top assignment suite
				determineForEachTest((GradableJUnitSuite)test);
				continue;
			}
			unsortedTestScores.add(composeTestScore(test));
			checkForFailureCausedByPreRequisite(test);
		}
	}
	
	private String [] preReqErrMsgs= {
		"correct test"
	};
	private void checkForFailureCausedByPreRequisite(GradableJUnitTest aTest) {
		if(aTest.getDisplayedScore()!=0) return;
		String message = aTest.getMessage();
		for(String errMsg:preReqErrMsgs)
			if(message.contains(errMsg)) 
				unsortedFailFromPreReq.add(aTest.getSimpleName());
			
	}
	
	private String composeTestScore(GradableJUnitTest aTest) {
		String name = aTest.getSimpleName();
		String score = aTest.getDisplayedScore()+"";
		String maxScore = aTest.getComputedMaxScore()+"";
		return name+"-("+score+"/"+maxScore+")";
	}
	
}