package gradingTools.logs.models;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.collections.map.HashedMap;

import bus.uigen.ObjectEditor;
import grader.basics.junit.BasicJUnitUtils;
import grader.basics.junit.GradableJUnitTest;
import grader.basics.project.CurrentProjectHolder;
import gradingTools.logs.LocalChecksLogData;
import gradingTools.logs.localChecksStatistics.collectors.Collector;
import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.AttemptsCollector;
import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.AttemptsCollectorV2;
import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.BreakTimeCollector;
import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.DateFirstTestedCollector;
import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.DateLastTestedCollector;
import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.DecreasingAttemptsCollector;
import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.DecreasingAttemptsCollectorV2;
import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.TotalTimeCollector;
import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.WorkTimeCollector;

public class MetricsUtils {
	public static final int BREAK_TIME = 300;
	static Collector dateFirstTested = new DateFirstTestedCollector();
	static Collector dateLastTested = new DateLastTestedCollector();
	static Collector totalTime = new TotalTimeCollector();
	static Collector breakTime = new BreakTimeCollector(BREAK_TIME);
	static Collector workTimeCollector = new WorkTimeCollector(BREAK_TIME);
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
	static Collector[] allCollectors = { dateFirstTested, dateLastTested, totalTime, breakTime, workTimeCollector, totalAttempts,
			totalAttemptsV2, };

	static Collector[] testingPeriodCollectors = { dateFirstTested, dateLastTested,

	};
	static Collector[] workTimeCollectors = { workTimeCollector };

	static Collector[] attemptsCollectors = { totalAttempts, totalAttemptsV2 };
	
	static Collector[] decreasingAttemptsCollectors = { decreasingAttempts, decreasingAttemptsV2 };

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

			aTestMetrics.setAttempts(Double.parseDouble(aNameAndMetric[1]));

		}
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
			int aSeconds = (int) Double.parseDouble(aNameAndMetric[1]);
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
			aTestMetrics.setRegressions(Double.parseDouble(aNameAndMetric[1]));

		}
	}

	public static void fillTestMetricsMap(Map<String, TestMetrics> aTestMetricsMap) {
		File directory = CurrentProjectHolder.getProjectLocation();
		if (!directory.exists()) {
			return;
		}
		int assignmentNumber = BasicJUnitUtils.getLastAssignmentNumber();
		List<String> anAttemptsList = LocalChecksLogData.getData(directory, assignmentNumber, attemptsCollectors);
		fillAttemptsInMap(aTestMetricsMap, anAttemptsList);
		
		List<String> aWorkTimeList = LocalChecksLogData.getData(directory, assignmentNumber,
				workTimeCollectors);
		fillWorktimeInMap(aTestMetricsMap, aWorkTimeList);
		
		List<String> aRegressionsList = LocalChecksLogData.getData(directory, assignmentNumber,
				decreasingAttemptsCollectors);
		fillRegressionsInMap(aTestMetricsMap, anAttemptsList);

	}

	static String noOutput = "Could not find project directory";
	static String noOutputArray[] = { "Could not find project directory" };

	static List<String> noOutputList = Arrays.asList(noOutputArray);

	static StringBuffer stringBuffer = new StringBuffer();
	
	public static TestMetrics progressMetrics(GradableJUnitTest aJUnitTest) {
		return null;
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

	public static List<String> testingPeriodMetrics() {
		File directory = CurrentProjectHolder.getProjectLocation();
		if (!directory.exists()) {
			return noOutputList;
		}
		int assignmentNumber = BasicJUnitUtils.getLastAssignmentNumber();
		List<String> aData = LocalChecksLogData.getData(directory, assignmentNumber, testingPeriodCollectors);

//		stringBuffer.setLength(0);
//		for (String aString:aData) {
//			stringBuffer.append(aString + "\n");
//		}
//		return stringBuffer.toString();
		return aData;
	}
}
