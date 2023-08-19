package gradingTools.logs.models;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grader.basics.junit.BasicJUnitUtils;
import grader.basics.junit.GradableJUnitTest;
import grader.basics.project.CurrentProjectHolder;
import gradingTools.logs.bulkLogProcessing.collectors.Collector;
import gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.Edits.EditsIRCollector;
import gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.Runs.RunsIRCollector;
import gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.Timing.ContextBasedWorkTimeIRCollector;
import gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.Timing.FixedWorkTimeIRCollector;
import gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.Timing.TestFocusedContextBasedWorkTimeIRCollector;
import gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.Timing.TestFocusedFixedWorkTimeIRCollector;
import gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors.AttemptsCollector;
import gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors.AttemptsCollectorV2;
import gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors.BreakTimeCollector;
import gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors.DateFirstTestedCollector;
import gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors.DateLastTestedCollector;
import gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors.DecreasingAttemptsCollector;
import gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors.DecreasingAttemptsCollectorV2;
import gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors.TotalTimeCollector;
import gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors.WorkTimeCollector;
import gradingTools.logs.bulkLogProcessing.compiledLogGenerator.LocalChecksLogData;
import gradingTools.logs.bulkLogProcessing.compiledLogGenerator.SemesterLogGenerator;

public class MetricsUtils {
	public static final int BREAK_TIME = 300;
	
	static Collector dateFirstTested = new DateFirstTestedCollector();
	static Collector dateLastTested = new DateLastTestedCollector();
	static Collector totalTime = new TotalTimeCollector();
	static Collector breakTime = new BreakTimeCollector(BREAK_TIME);
	static Collector workTimeCollector = new WorkTimeCollector(BREAK_TIME);
//	static Collector fixedTestTimeCollector = new TestFocusedFixedWorkTimeIRCollector();
//	static Collector fixedWorkTimeCollector = new FixedWorkTimeIRCollector();
//
//	static Collector contextWorkTimeCollector = new ContextBasedWorkTimeIRCollector();
//	static Collector contextTestTimeCollector = new TestFocusedContextBasedWorkTimeIRCollector();
//	static Collector editsColector = new EditsIRCollector();
//	static Collector runsCollector = new RunsIRCollector();

	static Collector totalAttempts = new AttemptsCollector();
	static Collector totalAttemptsV2 = new AttemptsCollectorV2();
	static Collector decreasingAttempts = new DecreasingAttemptsCollector();
	static Collector decreasingAttemptsV2 = new DecreasingAttemptsCollectorV2();

//	static Collector [] collectors = {
//			new DateFirstTestedCollector(),
//			new DateLastTestedCollector(),
//			new TotalTimeCollector(),
//			new BreakTimeCollector(BREAK_TIME),
//			new WorkTimeCollector(BREAK_TIME),
//			new TotalAttemptsCollector(),
//			new AttemptsCollectorV2(),
//	};
	static Collector[] allCollectors = { dateFirstTested, dateLastTested, totalTime, 
			breakTime, workTimeCollector, totalAttempts,
			totalAttemptsV2, };

//	static Collector[] testingPeriodCollectors = { dateFirstTested, dateLastTested, fixedWorkTimeCollector, contextWorkTimeCollector
//
//	};
//	static Collector[] testingPeriodCollectors = { dateFirstTested, dateLastTested 
//
//	};
	
//	static Collector[] testingPeriodCollectors = { 
//			dateFirstTested, dateLastTested, workTimeCollector, totalTime, fixedWorkTimeCollector, fixedTestTimeCollector,
//			contextTestTimeCollector,  contextWorkTimeCollector
//
//	};
	static Collector[] workTimeCollectors = { workTimeCollector };
//	static Collector[] workTimeCollectors = { contextTestTimeCollector };

//	static Collector[] attemptsCollectors = { totalAttempts, totalAttemptsV2 };
	static Collector[] attemptsCollectors = {totalAttemptsV2 };

	
//	static Collector[] decreasingAttemptsCollectors = { decreasingAttempts, decreasingAttemptsV2 };
	static Collector[] decreasingAttemptsCollectors = {decreasingAttemptsV2 };

	public static void extractNameAndMetric(String aLine, String[] aNameAndMetric) {

		try {
//		String[] retVal = new String[2];
			int aSpaceIndex = aLine.indexOf(" ");
			int aCommaIndex = aLine.indexOf(":");
			String aTestName = aLine.substring(0, aSpaceIndex);
			String aMetric = aLine.substring(aCommaIndex + 1);
			aNameAndMetric[0] = aTestName;
			aNameAndMetric[1] = aMetric;
		} catch (Exception e) {
			aNameAndMetric[0] = null;
			aNameAndMetric[1] = null;
		}
	}

	public static TestMetrics getAndPossiblyCreateTestMetrics(Map<String, TestMetrics> aTestMetricsMap,
			String aTestName) {
		TestMetrics aTestMetrics = aTestMetricsMap.get(aTestName);
		if (aTestMetrics == null) {
			aTestMetrics = new ATestMetrics();
			aTestMetrics.setTestName(aTestName);
			aTestMetricsMap.put(aTestName, aTestMetrics);
		}
		return aTestMetrics;

	}

	public static void fillAttemptsInMap(Map<String, TestMetrics> aTestMetricsMap, List<String> anAttemptsList) {
		String[] aNameAndMetric = new String[2];
		for (String anAttemptsLine : anAttemptsList) {
			extractNameAndMetric(anAttemptsLine, aNameAndMetric);
			if (aNameAndMetric[0] == null) {
				continue;
			}
			TestMetrics aTestMetrics = getAndPossiblyCreateTestMetrics(aTestMetricsMap, aNameAndMetric[0]);
			double anAttempts = twoDecimalPlaces(Double.parseDouble(aNameAndMetric[1]));
			if (anAttempts < 0) {
				anAttempts = - anAttempts;
			}
			aTestMetrics.setAttempts(anAttempts);

		}
	}
	
	public static void fillAttemptsInMapDoubleList(Map<String, TestMetrics> aTestMetricsMap, List<List<String>> anAttemptsList) {
		for (int i=0;i<anAttemptsList.size();i++) {
			try {
				String [] aNameAndMetric = {anAttemptsList.get(0).get(i),anAttemptsList.get(1).get(i)};
				TestMetrics aTestMetrics = getAndPossiblyCreateTestMetrics(aTestMetricsMap, aNameAndMetric[0]);
				double anAttempts = twoDecimalPlaces(Double.parseDouble(aNameAndMetric[1]));
				if (anAttempts < 0) {
					anAttempts = - anAttempts;
				}
				aTestMetrics.setAttempts(anAttempts);	
			}catch(Exception e) {
				continue;
			}
		}
	}
	
	public static double twoDecimalPlaces (Double aDouble) {
		return Math.round(aDouble * 100.0) / 100.0;
	}
	public static void fillBreaksInMap(Map<String, TestMetrics> aTestMetricsMap, List<String> aBreaksList) {
		String[] aNameAndMetric = new String[2];
		for (String anAttemptsLine : aBreaksList) {
			extractNameAndMetric(anAttemptsLine, aNameAndMetric);
			if (aNameAndMetric[0] == null) {
				continue;
			}
			TestMetrics aTestMetrics = getAndPossiblyCreateTestMetrics(aTestMetricsMap, aNameAndMetric[0]);

			aTestMetrics.setBreaks(Double.parseDouble(aNameAndMetric[1]));

		}
	}

	public static void fillWorktimeInMap(Map<String, TestMetrics> aTestMetricsMap, List<String> anAttemptsList) {
		String[] aNameAndMetric = new String[2];
		for (String aWorkTimeList : anAttemptsList) {
			extractNameAndMetric(aWorkTimeList, aNameAndMetric);
			if (aNameAndMetric[0] == null) {
				continue;
			}
			TestMetrics aTestMetrics = getAndPossiblyCreateTestMetrics(aTestMetricsMap, aNameAndMetric[0]);
			int aMilliSeconds = (int) Double.parseDouble(aNameAndMetric[1]);
			int aSeconds = aMilliSeconds/1000;

			String aFormattedString = formatSecondDateTime(aSeconds);
			aTestMetrics.setTimeWorked(aFormattedString);

		}
	}

	public static String formatSecondDateTime(int aSeconds) {
		if (aSeconds <= 0)
			return "0h:0m:0s";
		int h = aSeconds / 3600;
		int m = aSeconds % 3600 / 60;
		int s = aSeconds % 60; // Less than 60 is the second, enough 60 is the minute
		return h + "h:" + m + "m:" + s + "s";
	}

	public static void fillRegressionsInMap(Map<String, TestMetrics> aTestMetricsMap, List<String> anAttemptsList) {
		String[] aNameAndMetric = new String[2];
		for (String aRegressionsLine : anAttemptsList) {
			extractNameAndMetric(aRegressionsLine, aNameAndMetric);
			if (aNameAndMetric[0] == null) {
				continue;
			}
			TestMetrics aTestMetrics = getAndPossiblyCreateTestMetrics(aTestMetricsMap, aNameAndMetric[0]);
			aTestMetrics.setRegressions(twoDecimalPlaces(Double.parseDouble(aNameAndMetric[1])));

		}
	}

	public static void fillTestMetricsMap(Map<String, TestMetrics> aTestMetricsMap) {
		File directory = CurrentProjectHolder.getProjectLocation();
		String assignmentNumber = BasicJUnitUtils.getLastAssignmentNumber();
		//File log = SemesterLogGenerator.findAssignment(directory, false, assignmentNumber);
		try {
			List<List<String>> anAttemptsList =  new SemesterLogGenerator(attemptsCollectors, false).generateDataSingleAssignmentWithHeaders(directory, assignmentNumber);
			fillAttemptsInMapDoubleList(aTestMetricsMap, anAttemptsList);
			
			List<List<String>> aWorkTimeList = new SemesterLogGenerator(workTimeCollectors, false).generateDataSingleAssignmentWithHeaders(directory, assignmentNumber);
			fillAttemptsInMapDoubleList(aTestMetricsMap, aWorkTimeList);
			
			List<List<String>> aRegressionsList = new SemesterLogGenerator(decreasingAttemptsCollectors, false).generateDataSingleAssignmentWithHeaders(directory, assignmentNumber);
			fillAttemptsInMapDoubleList(aTestMetricsMap, aRegressionsList);
		}catch(Exception e) {
			
		}
	}

	static String noOutput = "Could not find project directory";
	static String noOutputArray[] = { "Could not find project directory" };

	static List<String> noOutputList = Arrays.asList(noOutputArray);

	static StringBuffer stringBuffer = new StringBuffer();
	
//	public static TestMetrics progressMetrics(GradableJUnitTest aJUnitTest) {
//		return null;
//	}
	public static List<TestMetrics> progressMetrics(String anAssignmentNumber) {
		Map<String, TestMetrics> aTestMetricsMap = new HashMap<>();
		fillTestMetricsMap(aTestMetricsMap);
		List<TestMetrics> aListMetrics = new ArrayList(aTestMetricsMap.values());
		return aListMetrics;
	}

	public static List<TestMetrics> progressMetrics() {
		Map<String, TestMetrics> aTestMetricsMap = new HashMap<>();
		fillTestMetricsMap(aTestMetricsMap);
		List<TestMetrics> aListMetrics = new ArrayList(aTestMetricsMap.values());
		return aListMetrics;
		
//		ObjectEditor.edit(aListMetrics);
//		File directory = CurrentProjectHolder.getProjectLocation();
//		if (!directory.exists()) {
//			return noOutputList;
//		}
//		int assignmentNumber = BasicJUnitUtils.getLastAssignmentNumber();
//		List<String> aData = LocalChecksLogData.getData(directory, assignmentNumber, allCollectors);
////		stringBuffer.setLength(0);
////		for (String aString:aData) {
////			stringBuffer.append(aString + "\n");
////		}
////		return stringBuffer.toString();
//		return aData;
	}
	
	private static final String fineGrainedSuffix = "FineGrained.csv";

	public static List<String> testingPeriodMetrics(GradableJUnitTest aTest, String anAssignmentNumber) {
		try {
		File directory = CurrentProjectHolder.getProjectLocation();
		if (!directory.exists()) {
			return noOutputList;
		}
//		String aFileName = AnAbstractTestLogFileWriter.toFileName(aTest);
//		String aFullFileName = directory.getAbsolutePath() + "/" + 
//		LocalLogDataAnalyzer.getLocalChecksLogsPath() + "/" + aFileName + fineGrainedSuffix;
//		File aLogFile = new File (aFullFileName);
		
//		List<String> aData = LocalLogDataAnalyzer.
//		runEvaluation(aLogFile, directory, new CollectorManager( testingPeriodCollectors), true, null);
//		String anAssignmentNumber = BasicJUnitUtils.getLastAssignmentNumber();
//		String anAssignmentNumber = AnAbstractTestLogFileWriter.toAssignmentNumber(aTopTest)

		//List<String> aData = LocalChecksLogData.getData(directory, anAssignmentNumber, testingPeriodCollectors);

//		stringBuffer.setLength(0);
//		for (String aString:aData) {
//			stringBuffer.append(aString + "\n");
//		}
//		return stringBuffer.toString();
		return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
