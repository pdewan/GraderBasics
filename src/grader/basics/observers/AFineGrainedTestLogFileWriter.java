package grader.basics.observers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.junit.runner.Description;
import org.junit.runner.Result;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.junit.GradableJUnitSuite;
import grader.basics.junit.GradableJUnitTest;
import grader.basics.observers.logSending.ALogSendingRunnable;
import grader.basics.observers.logSending.JSONObject;
import grader.basics.observers.logSending.LocalChecksLogSender;
import grader.basics.vetoers.AConsentFormVetoer;
import util.misc.Common;

public class AFineGrainedTestLogFileWriter extends AnAbstractTestLogFileWriter {

	private final String EXTENDED_HEADER = HEADER
			+ ",SessionNumber,SessionRunNumber,IsSuite,SuiteTests,PrerequisiteTests,ExtraCreditTests,TestScores,FailFromPreReq,";
	private final String FILENAME_MODIFIER = "FineGrained";

	public static final int SESSION_NUMBER_INDEX = 9;
	public static final int SESSION_RUN_NUMBER_INDEX = 10;
	public static final int IS_SUITE_INDEX = 11;
	public static final int SUITE_TESTS_INDEX = 12;
	public static final int PREREQUISITE_TESTS_INDEX = 13;
	public static final int EXTRA_CREDIT_TESTS_INDEX = 14;
	public static final int TEST_SCORES_INDEX = 15;
	public static final int FailFromPreReq_INDEX = 16;

	List<String> unsortedPrereqTests = null;
	List<String> unsortedExtraCreditTests = null;
	List<String> unsortedSuiteTests = null;
	List<String> unsortedTestScores = null;
	List<String> unsortedFailFromPreReq = null;

	private int sessionNumber = 0;

	File sessionDataFile;
	File schemaFile;
//	File receivedHelpFile;
//	File readHelpFile;
	public static final int SESSION_DATA_SESSION_NUMBER_INDEX = 0;
	public static final int SESSION_DATA_TOTAL_RUN_INDEX = 1;

	private boolean testIsSuite;
	private GradableJUnitTest testedSuiteOrTest;
	private String topLevelInfo;
	private ALogSendingRunnable logSender;

	GradableJUnitSuite topLevelSuite;

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
			AbstractMap.SimpleEntry<GradableJUnitSuite, GradableJUnitTest> anEntry = (AbstractMap.SimpleEntry<GradableJUnitSuite, GradableJUnitTest>) idField
					.get(description);

			GradableJUnitSuite aTopLevelSuite = anEntry.getKey();
			topLevelSuite = aTopLevelSuite;
			Class aTopLevelSuiteClass = aTopLevelSuite.getJUnitClass();
			GradableJUnitTest aTestOrSuiteSelected = anEntry.getValue();
			Class aTestOrSuiteSelectedClass = aTestOrSuiteSelected.getJUnitClass();

			testedSuiteOrTest = aTestOrSuiteSelected;
			setTopLevelInfo(toFileName(aTopLevelSuite));

			unsortedExtraCreditTests = new ArrayList<String>();

			testIsSuite = aTestOrSuiteSelected instanceof GradableJUnitSuite;
			if (testIsSuite) {
				unsortedSuiteTests = new ArrayList<String>();
				determineSuiteTests((GradableJUnitSuite) aTestOrSuiteSelected);
			} else {
				unsortedSuiteTests = null;
				GradableJUnitTest selectedTest = (GradableJUnitTest) aTestOrSuiteSelected;
				if (selectedTest.isExtra())
					unsortedExtraCreditTests.add(selectedTest.getSimpleName());

			}

			if (numRuns == 0) {
				totalTests = aTopLevelSuite.getLeafClasses().size();
				String fileLoc = AConsentFormVetoer.LOG_DIRECTORY + "/";
				logFileName = toFileName(aTopLevelSuite) + FILENAME_MODIFIER + LOG_SUFFIX;
				logFilePath = fileLoc + logFileName;
				sessionDataFile = new File(fileLoc + toFileName(aTopLevelSuite) + FILENAME_MODIFIER + "_data.txt");
				String aSchemaFileName = sessionDataFile.getCanonicalPath().replace("data", "schema");
				schemaFile = new File(aSchemaFileName);	
				String aReceivedHelpFileName = sessionDataFile.getCanonicalPath().replace("data", "received_help");
				receivedHelpFile = new File (aReceivedHelpFileName);
				String aReadHelpFileName = sessionDataFile.getCanonicalPath().replace("data", "read_help");
				readHelpFile = new File (aReadHelpFileName);

				if (BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getLogTestData()) {
					if (getLogSender() == null) {
						setLogSender(ALogSendingRunnable.getInstance());

//						setLogSender(new ALogSendingRunnable());
						Thread logSendingThread = new Thread(getLogSender());
						logSendingThread.setName("Log Sending");
						logSendingThread.start();
					}

					if (maybeReadLastLineOfLogFile(logFilePath)) {
						maybeLoadSavedSets();
						maybeCreateOrLoadAppendableFile(logFilePath);
						maybeDetermineConsecutiveTestRunNumber(aTopLevelSuite);
					}
				}

			}
			maybeWriteToSchemaFile();
			maybeCreateFile(receivedHelpFile);
			maybeCreateFile(readHelpFile);
//			if (sessionNumber == 0) {
//				writeToSchemaFile();
//			}
			currentTopSuite = aTopLevelSuite;
			currentTest = description.getClassName();
			saveState();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void determineSuiteTests(GradableJUnitSuite aSuite) {
		boolean suiteIsExtra = aSuite.isExtra();
		for (GradableJUnitTest test : aSuite.children()) {
			if (test instanceof GradableJUnitSuite) { // used in instances where there are nested Suites such as running
														// the top assignment suite
				determineSuiteTests((GradableJUnitSuite) test);
				continue;
			}
			if (suiteIsExtra || test.isExtra())
				unsortedExtraCreditTests.add(test.getSimpleName());

			unsortedSuiteTests.add(test.getSimpleName());
		}
	}

	private void maybeDetermineConsecutiveTestRunNumber(GradableJUnitSuite aTopLevelSuite) {
		if (sessionDataFile.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(sessionDataFile));
//				System.out.println("abs path" + sessionDataFile.getAbsolutePath());
				String[] firstLine = br.readLine().split(NAME_SEPARATOR);
				br.close();
				sessionNumber = Integer.parseInt(firstLine[SESSION_DATA_SESSION_NUMBER_INDEX]) + 1;
//				numTotalRuns = Integer.parseInt(firstLine[SESSION_DATA_TOTAL_RUN_INDEX]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void checkTotalRunNumber() {
		if (sessionDataFile.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(sessionDataFile));
				String[] firstLine = br.readLine().split(NAME_SEPARATOR);
				br.close();
				numTotalRuns = Integer.parseInt(firstLine[SESSION_DATA_TOTAL_RUN_INDEX]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
//public static void maybeCreateFile(File aFile) {
//	try {
//		if (aFile == null) {
//			throw new Exception("Null file passed to maybeCreateFile");
//		}
//		if (aFile.exists()) {
//			return;
//		}
//		aFile.createNewFile();
//		
//	} catch (Exception e) {
//		System.out.println(e);
//	}
//}

//public static void appendToFile (File aFile, String aTextToAppend) {
//	try (FileWriter fileWriter = new FileWriter(aFile, true)) {
//        fileWriter.write(aTextToAppend);
//    } catch (IOException e) {
//        System.out.println("An error occurred: " + e.getMessage());
//    }
//}
//
//public static String readFile (File aFile) {
//	return Common.toText(aFile);
//}

	
//	private void maybeCreateReceivedHelpFile() {
//	
//		try {
//			if (receivedHelpFile == null) {
//				throw new Exception("Received Data File Never Set");
//			}
//			if (schemaFile.exists()) {
//				return;
//			}
//			schemaFile.createNewFile();
//			FileWriter fw = new FileWriter(schemaFile);
//			JSONObject aTopJSONObject = toJSONObject(topLevelSuite);
//			String aSchema = aTopJSONObject.toString();
//			fw.write(aSchema);
//			fw.close();
//			getLogSender().addToQueue(LogEntryKind.SCHEMA, schemaFile.getName(), aSchema, getTopLevelInfo(), numTotalRuns);
//
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//	}
	private void maybeWriteToSchemaFile() {
		try {
			if (schemaFile == null) {
				throw new Exception("Session Data File Never Set");
			}
			if (schemaFile.exists()) {
				return;
			}
			schemaFile.createNewFile();
			FileWriter fw = new FileWriter(schemaFile);
			JSONObject aTopJSONObject = toJSONObject(topLevelSuite);
			String aSchema = aTopJSONObject.toString();
			fw.write(aSchema);
			fw.close();
			getLogSender().addToQueue(LogEntryKind.SCHEMA, schemaFile.getName(), aSchema, getTopLevelInfo(), numTotalRuns);

		} catch (Exception e) {
			System.out.println(e);
		}
	}

//	public static JSONObject toJSONObject(GradableJUnitSuite aSuite) {
//		JSONObject retVal = new JSONObject();
//		retVal.put("name", aSuite.getSimpleName());
//		JSONObject aChildrenJSON = new JSONObject();
//		retVal.put("children", aChildrenJSON);
//		List<GradableJUnitTest> aChildrenList = aSuite.getChildren();
//		for (int anIndex = 0; anIndex < aChildrenList.size(); anIndex++) {
//			GradableJUnitTest aChildTest = aChildrenList.get(anIndex);
//			String anIndexString = Integer.toString(anIndex);
//			if (aChildTest instanceof GradableJUnitSuite) {
//				GradableJUnitSuite aChildSuite = (GradableJUnitSuite) aChildTest;
//				JSONObject aChildJSONObject = toJSONObject(aChildSuite);
//				aChildrenJSON.put(anIndexString, aChildJSONObject);
//			} else {
//				aChildrenJSON.put(anIndexString, aChildTest.getSimpleName());
//			}
//		}
//		return retVal;
//
//	}

	private void writeToSessionDataFile() {
		try {
			if (sessionDataFile == null)
				throw new Exception("Session Data File Never Set");

			int determinedSessionNumber = 0;
			int determinedTotalRuns = 0;

			if (sessionDataFile.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(sessionDataFile));
				String[] firstLine = br.readLine().split(NAME_SEPARATOR);
				br.close();
				determinedSessionNumber = Integer.parseInt(firstLine[SESSION_DATA_SESSION_NUMBER_INDEX]);
				determinedTotalRuns = Integer.parseInt(firstLine[SESSION_DATA_TOTAL_RUN_INDEX]);
			} else {
				sessionDataFile.createNewFile();
			}

			determinedSessionNumber = determinedSessionNumber > sessionNumber ? determinedSessionNumber : sessionNumber;
			determinedTotalRuns = determinedTotalRuns > numTotalRuns ? determinedTotalRuns : numTotalRuns;

			FileWriter fw = new FileWriter(sessionDataFile);
			fw.write(determinedSessionNumber + NAME_SEPARATOR + determinedTotalRuns);
			fw.close();

		} catch (Exception e) {
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
			unsortedPrereqTests = null;
			unsortedExtraCreditTests = null;
			unsortedTestScores = null;
			numRuns++;
			numTotalRuns++;

			try {
//				String aLogFileName = getLogFileName();
				String aLogFileName = getLogFilePath();
//				LocalChecksLogSender.sendToServer(fullTrace.toString(), topLevelInfo, numTotalRuns);
				getLogSender().addToQueue(LogEntryKind.TEST, aLogFileName, fullTrace.toString(), getTopLevelInfo(),
						numTotalRuns);

//				getLogSender().addToQueue(true, aLogFileName, fullTrace.toString(), getTopLevelInfo(), numTotalRuns);
			} catch (Exception e) {
//				System.err.println("Error resolving local checks server sending");
//				System.err.println("Thrown message:\n"+e.getMessage());
			}
			writeToSessionDataFile();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void composeTrace() {
		super.composeTrace();
		fullTrace.append(sessionNumber + ",");
		fullTrace.append(numRuns + ",");
		fullTrace.append(testIsSuite + ",");
		fullTrace.append(composeString(unsortedSuiteTests) + ",");
		fullTrace.append(composeString(unsortedPrereqTests) + ",");
		fullTrace.append(composeString(unsortedExtraCreditTests) + ",");
		fullTrace.append(composeString(unsortedTestScores) + ",");
		fullTrace.append(composeString(unsortedFailFromPreReq) + ",");
	}

	private StringBuilder composeString(List<String> dataSrc) {
		StringBuilder string = new StringBuilder();
		if (dataSrc == null || dataSrc.size() == 0) {
			string.append(" ");
			return string;
		}
		Collections.sort(dataSrc);
		for (String data : dataSrc) {
			string.append(data);
			string.append(NAME_SEPARATOR);
		}
		return string;
	}

	@Override
	public void testStarted(Description description) throws Exception {
		super.testStarted(description);

		boolean testIsPrereq;

		String fixedClassName = description.getClassName().replaceAll(".*\\.", "");
//		String test = description.getClassName();

		if (testIsSuite)
			testIsPrereq = !unsortedSuiteTests.contains(fixedClassName);
		else
			testIsPrereq = !currentTest.equals(fixedClassName);

		if (testIsPrereq) {
			if (unsortedPrereqTests == null)
				unsortedPrereqTests = new ArrayList<String>();
			unsortedPrereqTests.add(fixedClassName);
		}

	}

	private void determinePostRunData() {
		unsortedTestScores = new ArrayList<String>();
		unsortedFailFromPreReq = new ArrayList<String>();

		if (testedSuiteOrTest instanceof GradableJUnitSuite)
			determineForEachTest((GradableJUnitSuite) testedSuiteOrTest);
		else {
			unsortedTestScores.add(composeTestScore(testedSuiteOrTest));
			checkForFailureCausedByPreRequisite(testedSuiteOrTest);
		}

	}

	private void determineForEachTest(GradableJUnitSuite aSuite) {
		for (GradableJUnitTest test : aSuite.children()) {
			if (test instanceof GradableJUnitSuite) { // used in instances where there are nested Suites such as running
														// the top assignment suite
				determineForEachTest((GradableJUnitSuite) test);
				continue;
			}
			unsortedTestScores.add(composeTestScore(test));
			checkForFailureCausedByPreRequisite(test);
		}
	}

	private String[] preReqErrMsgs = { "correct test" };

	private void checkForFailureCausedByPreRequisite(GradableJUnitTest aTest) {
		if (aTest.getDisplayedScore() != 0)
			return;
		String message = aTest.getMessage();
		for (String errMsg : preReqErrMsgs)
			if (message.contains(errMsg))
				unsortedFailFromPreReq.add(aTest.getSimpleName());

	}

	private String composeTestScore(GradableJUnitTest aTest) {
		String name = aTest.getSimpleName();
		String score = aTest.getDisplayedScore() + "";
		String maxScore = aTest.getComputedMaxScore() + "";
		return name + "-(" + score + "/" + maxScore + ")";
	}

	protected int getSessionNumber() {
		return sessionNumber;
	}

	public ALogSendingRunnable getLogSender() {
		return logSender;
	}

	public void setLogSender(ALogSendingRunnable logSender) {
		this.logSender = logSender;
	}

	public String getTopLevelInfo() {
		return topLevelInfo;
	}

	public void setTopLevelInfo(String topLevelInfo) {
		this.topLevelInfo = topLevelInfo;
	}

}