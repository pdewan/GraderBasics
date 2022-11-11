package grader.basics.junit;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runners.Suite;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.execution.GradingMode;
import grader.basics.observers.TestLogFileWriterFactory;
import grader.basics.project.BasicProjectIntrospection;
//import grader.junit.GraderTestCase;
import grader.basics.project.CurrentProjectHolder;
import grader.basics.vetoers.AConsentFormVetoer;
import gradingTools.logs.LocalChecksLogData;
import gradingTools.logs.localChecksStatistics.collectors.Collector;
import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.AttemptsCollectorV2;
import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.BreakTimeCollector;
import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.DateFirstTestedCollector;
import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.DateLastTestedCollector;
import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.TotalAttemptsCollector;
import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.TotalTimeCollector;
import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.WorkTimeCollector;
import gradingTools.logs.models.ATestMetricsList;
import gradingTools.logs.models.TestMetrics;
import trace.grader.basics.GraderBasicsTraceUtility;
import util.annotations.PreferredWidgetClass;
import util.trace.Tracer;

public class BasicJUnitUtils {

	public static void assertTrue(Throwable e, double aFractionComplete) {
		if (e instanceof AssertionError) {
			if (e.getMessage().contains("" + NotesAndScore.PERCENTAGE_MARKER))
				Assert.assertTrue(e.getMessage(), false);
			else
				Assert.assertTrue(e.getMessage() + NotesAndScore.PERCENTAGE_MARKER + aFractionComplete, false);

		} else {
			Assert.assertTrue(
					e.getClass().getName() + " " + e.getMessage() + NotesAndScore.PERCENTAGE_MARKER + aFractionComplete,
					false);
		}
	}

	protected static boolean giveAssertionErrorStackTrace = false;

	public static boolean isGiveAssertionErrorStackTrace() {
		return giveAssertionErrorStackTrace;
	}

	public static void setGiveAssertionErrorStackTrace(boolean throwAssertionError) {
		BasicJUnitUtils.giveAssertionErrorStackTrace = throwAssertionError;
	}

	public static void assertTrue(String aMessage, double aFractionComplete, boolean aCondition) {
//		if (aCondition || isThrowAssertionError()) {

		Assert.assertTrue(aMessage + NotesAndScore.PERCENTAGE_MARKER + aFractionComplete, aCondition);
//		} else {
//			System.err.println(aMessage + NotesAndScore.PERCENTAGE_MARKER + aFractionComplete);
//		}

	}

	public static Set<GradableJUnitSuite> findGradableTrees(Collection<Class> aClasses) {
		Set<Class> aSuiteClasses = findTopLevelSuites(aClasses);
		return toGradableTrees(aSuiteClasses);
	}

	public static Set<GradableJUnitSuite> toGradableTrees(Collection<Class> aJUnitClasses) {
		Set<GradableJUnitSuite> result = new HashSet();
		for (Class aClass : aJUnitClasses) {
			GradableJUnitSuite aTree = toGradableTree(aClass).rootNode;
			if (aTree != null) {
				result.add(aTree);
			}
		}
		return result;
	}

	public static GradableJUnitTest toGradable(Class<?> aSuiteOrTestClass) {
		if (aSuiteOrTestClass == null) {
			return null;
		}

		if (!isJUnitSuite(aSuiteOrTestClass)) {

			return new AGradableJUnitTest(aSuiteOrTestClass);
		}
		return toGradableTree(aSuiteOrTestClass).rootNode;
	}

	public static GradableJUnitTest createGradable(String aKeyWord) {
		Class aClass = BasicProjectIntrospection.findClassByKeyword(aKeyWord);
		return toGradable(aClass);
	}

	public static void interactiveTestAll(Class<?> aJUnitSuiteClass) {
		GraderBasicsTraceUtility.setTracing();
		GradableJUnitSuite aGradable = BasicJUnitUtils.toGradableTree(aJUnitSuiteClass).rootNode;
		RunVetoerFactory.getOrCreateRunVetoer().addVetoableChangeListener(new AConsentFormVetoer());
		for (RunListener listener : TestLogFileWriterFactory.getFileWriter())
			RunNotifierFactory.getOrCreateRunNotifier().addListener(listener);
		aGradable.testAll();
		ObjectEditor.treeEdit(aGradable);
	}

	static String lastAssignmentNumber = "-1";

	public static String getLastAssignmentNumber() {
		return lastAssignmentNumber;
	}

	public static void setLastAssignmentNumber(String lastAssignmentNumber) {
		BasicJUnitUtils.lastAssignmentNumber = lastAssignmentNumber;
	}
//	public static final int BREAK_TIME = 300;
//	static Collector dateFirstTested = new DateFirstTestedCollector();
//	static Collector dateLastTested = new DateLastTestedCollector();
//	static Collector totalTime = new TotalTimeCollector();
//	static Collector breakTime = new BreakTimeCollector(BREAK_TIME);
//	static Collector workTime = new WorkTimeCollector(BREAK_TIME);
//	static Collector totalAttempts = new WorkTimeCollector(BREAK_TIME);
//	static Collector totalAttemptsV2 = new  AttemptsCollectorV2();
//
//
//
//
////	static Collector [] collectors = {
////			new DateFirstTestedCollector(),
////			new DateLastTestedCollector(),
////			new TotalTimeCollector(),
////			new BreakTimeCollector(BREAK_TIME),
////			new WorkTimeCollector(BREAK_TIME),
////			new TotalAttemptsCollector(),
////			new AttemptsCollectorV2(),
////	};
//	static Collector [] allCollectors = {
//			dateFirstTested,
//			dateLastTested,
//			totalTime,
//			breakTime,
//			workTime,
//			totalAttempts,
//			totalAttemptsV2,
//	};
//	
//	static Collector [] testingPeriodCollectors = {
//			dateFirstTested,
//			dateLastTested,
//			
//	};
//	
//	static Collector[] attemptsCollectors = {
//			totalAttempts,
//			totalAttemptsV2
//	};
//	public static void fillAttemptsInMap(
//			Map<GradableJUnitTest,TestMetrics> aTestMetricsMap, 
//			List<String> anAttemptsList) {
//		
//	}
//
//	
//	public static void fillTestMetricsMap(Map<GradableJUnitTest,TestMetrics> aTestMetricsMap) {
//		File directory = CurrentProjectHolder.getProjectLocation();
//		if (!directory.exists()) {
//			return ;
//		}
//		int assignmentNumber= BasicJUnitUtils.getLastAssignmentNumber();
//		List<String> aData = LocalChecksLogData.getData(directory,assignmentNumber,attemptsCollectors);
//
//	}
//	
//	
//	static String noOutput = "Could not find project directory";
//	static String noOutputArray[] = {"Could not find project directory"};
//
//	 static List<String> noOutputList = Arrays.asList(noOutputArray);
//	 
//	 static StringBuffer stringBuffer = new StringBuffer();
//	public static List<String>  allProgressMetrics() {
//		File directory = CurrentProjectHolder.getProjectLocation();
//		if (!directory.exists()) {
//			return noOutputList;
//		}
//		int assignmentNumber= BasicJUnitUtils.getLastAssignmentNumber();
//		List<String> aData = LocalChecksLogData.getData(directory,assignmentNumber,allCollectors);
////		stringBuffer.setLength(0);
////		for (String aString:aData) {
////			stringBuffer.append(aString + "\n");
////		}
////		return stringBuffer.toString();
//		return aData;
//	}
//	public static List<String>  testingPeriodMetrics() {
//		File directory = CurrentProjectHolder.getProjectLocation();
//		if (!directory.exists()) {
//			return noOutputList;
//		}
//		int assignmentNumber= BasicJUnitUtils.getLastAssignmentNumber();
//		List<String> aData = LocalChecksLogData.getData(directory,assignmentNumber,testingPeriodCollectors);
//		
////		stringBuffer.setLength(0);
////		for (String aString:aData) {
////			stringBuffer.append(aString + "\n");
////		}
////		return stringBuffer.toString();
//		return aData;
//	}

	static void extractAssignmentNuber(Class aJUnitSuiteClass) {
		String aClassName = aJUnitSuiteClass.getSimpleName();
//		aClassName = "F21Assignmment12";
//		Pattern p = Pattern.compile("\\d+");
		Pattern p = Pattern.compile("\\d+_*\\d*");
		

		Matcher m = p.matcher(aClassName);
		String aDigitString = null;
		while (m.find()) {
//        	if (aDigitString != null) {
//        		aDigitString = null;
//        		break;
//        	}
			aDigitString = m.group();

		}
		lastAssignmentNumber = aDigitString;
	}
//		String aPrefix = "";
//		if (aDigitString != null) {
//			try {
//				int anIndex = aClassName.lastIndexOf(aDigitString);
//				if (anIndex > 0) {
//					int anUnderscoreIndex = anIndex - 1;
//					if (aClassName.charAt(anUnderscoreIndex) == '_') {
//						int aBeginNumberPrefixIndex = anUnderscoreIndex - 1;
//						while (aBeginNumberPrefixIndex >= 0) {
//							if (Character.isDigit(aClassName.charAt(aBeginNumberPrefixIndex))) {
//								aBeginNumberPrefixIndex--;
//							} else {
//								break;
//							}
//
//						}
//						aPrefix = aClassName.substring(aBeginNumberPrefixIndex, anIndex);
//
//					}
//				}
////        		lastAssignmentNumber = Integer.parseInt(aDigitString);
//				lastAssignmentNumber = aPrefix + aDigitString;
			
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}

	public static GradableJUnitSuite interactiveTest(Class<?> aJUnitSuiteClass) {
		extractAssignmentNuber(aJUnitSuiteClass);
		if (!GradingMode.getGraderRun()) {
//			ObjectEditor.setAttribute(ArrayList.class, AttributeNames.PREFERRED_WIDGET, javax.swing.JTextArea.class.getName());
			ObjectEditor.setAttribute(ArrayList.class, AttributeNames.PREFERRED_WIDGET,
					javax.swing.JTable.class.getName());
//			ObjectEditor.setAttribute(ATestMetricsList.class, AttributeNames.PREFERRED_WIDGET, javax.swing.JTable.class.getName());

		}
		setGiveAssertionErrorStackTrace(false);
		BasicStaticConfigurationUtils.setModuleProblemAndSuite(aJUnitSuiteClass);
		GraderBasicsTraceUtility.setTracing();
		GradableJUnitSuite aGradable = BasicJUnitUtils.toGradableTree(aJUnitSuiteClass).rootNode;
//		RunVetoerFactory.getOrCreateRunVetoer().addVetoableChangeListener(new AnAlwaysNaySayer());
		RunVetoerFactory.getOrCreateRunVetoer().addVetoableChangeListener(new AConsentFormVetoer());
		for (RunListener listener : TestLogFileWriterFactory.getFileWriter())
			RunNotifierFactory.getOrCreateRunNotifier().addListener(listener);
		ObjectEditor.treeEdit(aGradable);
		return aGradable;
	}

	public static void interactiveTestAll(String aSourceFilePattern, Class<?> aJUnitSuiteClass) {
		CurrentProjectHolder.setProject(aSourceFilePattern);
		interactiveTestAll(aJUnitSuiteClass);
	}

	public static void interactiveTest(String aSourceFilePattern, Class<?> aJUnitSuiteClass) {
		CurrentProjectHolder.setProject(aSourceFilePattern);
		interactiveTest(aJUnitSuiteClass);
	}

	public static void jUnitCoreTestAll(Class<?> aJUnitSuiteClass) {
		GraderBasicsTraceUtility.setTracing();
		Result aResult = JUnitCore.runClasses(aJUnitSuiteClass);
		for (Failure failure : aResult.getFailures()) {
			Tracer.info(BasicJUnitUtils.class, "To string" + failure.toString());
			Tracer.info(BasicJUnitUtils.class, "Header" + failure.getTestHeader());
			Tracer.info(BasicJUnitUtils.class, "ValgrindTrace:" + failure.getTrace());
			Tracer.info(BasicJUnitUtils.class, "Description:" + failure.getDescription());
		}
		Tracer.info(BasicJUnitUtils.class, "" + aResult.wasSuccessful());

	}

	public static GradableTreeConstructionResult toGradableTree(Class<?> aJUnitSuiteClass) {
		GradableTreeConstructionResult aResult = new GradableTreeConstructionResult();
		if (aJUnitSuiteClass == null) {
			return aResult;
		}
		GradableJUnitSuite rootNode = new AGradableJUnitTopLevelSuite(aJUnitSuiteClass);
		aResult.rootNode = rootNode;

		Map<String, List<GradableJUnitTest>> aGradableJUnitTestCaseMap = BasicJUnitUtils.toGroupedGradables(rootNode,
				aJUnitSuiteClass);
//		GradableJUnitSuite retVal = new AGradableJUnitTopLevelSuite(aJUnitSuiteClass);
		aResult.groupedGradables = aGradableJUnitTestCaseMap;
		for (String aGroup : aGradableJUnitTestCaseMap.keySet()) {
			List<GradableJUnitTest> aGradables = aGradableJUnitTestCaseMap.get(aGroup);
			if (aGradables.size() == 2 && aGradables.get(0) instanceof GradableJUnitSuite
					&& ((GradableJUnitSuite) aGradables.get(0)).isImplicit()) { // an ungrouped test
				rootNode.add(aGradables.get(1));
			} else {
				rootNode.add(aGradables.get(0)); // will also have the children
			}
		}
		rootNode.assignMaxScores();
		return aResult;
	}
//	public static GradableJUnitSuite toGradableTree (Class<?> aJUnitSuiteClass) {
//		GradableTreeConstructionResult aResult = new GradableTreeConstructionResult();
//
//		if (aJUnitSuiteClass == null) {
//			return aResult;
//		}
//
//		GradableJUnitSuite rootNode = new AGradableJUnitTopLevelSuite(aJUnitSuiteClass);
//
//		Map<String,List<GradableJUnitTest>> aGradableJUnitTestCaseMap = BasicJUnitUtils.toGroupedGradables(rootNode, aJUnitSuiteClass);
////		GradableJUnitSuite retVal = new AGradableJUnitTopLevelSuite(aJUnitSuiteClass);
//		for (String aGroup:aGradableJUnitTestCaseMap.keySet()) {
//			List<GradableJUnitTest> aGradables = aGradableJUnitTestCaseMap.get(aGroup);
//			if (aGradables.size() == 2) { // an ungrouped test
//				rootNode.add(aGradables.get(1));
//			} else {
//				rootNode.add(aGradables.get(0)); // will also have the children
//			}
//		}
//		return rootNode;
//	}

	public static List<Class> selectJUnitSuites(List<Class> aJUnitSuiteAndTestCases) {
		List<Class> retVal = new ArrayList();
		for (Class aJUnitTestCase : aJUnitSuiteAndTestCases) {
			if (isJUnitSuite(aJUnitTestCase)) {
				retVal.add(aJUnitTestCase);
			}
		}
		return retVal;
	}

	/*
	 * Creates a displayable Suite OE node from a list of classes and assign them
	 * scores. This method does both. Going to separate them out.
	 */
	public static Map<String, List<GradableJUnitTest>> toSuiteGradables(List<Class> aSuiteClasses) {
		Map<String, List<GradableJUnitTest>> aResult = new HashMap();
		for (Class aSuiteClass : aSuiteClasses) {
			GradableJUnitSuite aSuiteGradable = new AGradableJUnitSuite(aSuiteClass);
//			String aFeatureName =  aProperties.getExplanation();
			Double aFeatureScore = aSuiteGradable.getMaxScore();
			List<Class> aLeafTestCases = getJUnitTestClassesDeep(aSuiteClass);
			Double aTestMaxScore = null;
//			if (aSuiteGradable.getMaxScore() != null) {
//				aTestMaxScore = aSuiteGradable.getMaxScore()/
//						aLeafTestCases.size();
//			}

			List<GradableJUnitTest> aGradables = new ArrayList();
			for (Class aLeafTestCase : aLeafTestCases) {
				GradableJUnitTest aGradable = new AGradableJUnitTest(aLeafTestCase);
				if (aTestMaxScore != null) {
//					aGradable.setMaxScore(aTestMaxScore);	
					aGradable.setGroup(aSuiteGradable.getExplanation());
					aGradable.setRestriction(aSuiteGradable.isRestriction());
					aGradable.setExtra(aSuiteGradable.isExtra());
				}

				aGradables.add(aGradable);
			}
			aSuiteGradable.addAll(aGradables);
//			if (aGradables.size() > 1) { // they need a parent
			aGradables.add(0, aSuiteGradable); // for consistency add it always
//			}
			aResult.put(aSuiteGradable.getExplanation(), aGradables);
			// we no longer need this as we are
//			maybeAssignMaxScores(aSuiteGradable, aGradables);

		}
		return aResult;
	}

	public static void maybeAssignMaxScores(GradableJUnitTest aSuiteGradable,
			Collection<GradableJUnitTest> aGradables) {

		Double aTestMaxScore = null;
		if (aSuiteGradable.getMaxScore() != null) {
			aTestMaxScore = aSuiteGradable.getMaxScore() / aGradables.size();
		}

//			List<GradableJUnitTest> aGradables = new ArrayList();
		for (GradableJUnitTest aGradable : aGradables) {
			if (aTestMaxScore != null) {
				aGradable.setMaxScore(aTestMaxScore);

			}

		}

	}
//	public static  Map<String, List<GradableJUnitTest>> toSuiteGradables(List<Class> aSuiteClasses) {
//		Map<String, List<GradableJUnitTest>> aResult = new HashMap();
//		for (Class aSuiteClass:aSuiteClasses) {
//			GradableJUnitSuite aSuiteGradable = new AGradableJUnitSuite(aSuiteClass);
////			String aFeatureName =  aProperties.getExplanation();
//			Double aFeatureScore = aSuiteGradable.getMaxScore();
//			List<Class> aLeafTestCases = getJUnitTestClassesDeep(aSuiteClass);
//			Double aTestMaxScore = null;
//			if (aSuiteGradable.getMaxScore() != null) {
//				aTestMaxScore = aSuiteGradable.getMaxScore()/
//						aLeafTestCases.size();
//			}
//
//			List<GradableJUnitTest> aGradables = new ArrayList();
//			for (Class aLeafTestCase:aLeafTestCases) {
//				GradableJUnitTest aGradable = new AGradableJUnitTest(aLeafTestCase); 
//				if (aTestMaxScore != null) {
//					aGradable.setMaxScore(aTestMaxScore);	
//					aGradable.setGroup(aSuiteGradable.getExplanation());
//					aGradable.setRestriction(aSuiteGradable.isRestriction());
//					aGradable.setExtra(aSuiteGradable.isExtra());
//				}
//				
//				aGradables.add(aGradable);
//			}
//			aSuiteGradable.addAll(aGradables);
////			if (aGradables.size() > 1) { // they need a parent
//				aGradables.add(0, aSuiteGradable); // for consistency add it always
////			}
//			aResult.put(aSuiteGradable.getExplanation(), aGradables);
//			
//		}
//		return aResult;
//	}

	public static Map<String, List<GradableJUnitTest>> toGroupedGradables(GradableJUnitTest aTopLevelSuite,
			Class<?> aJUnitSuiteClass) {
		List<Class> aJUnitOrSuiteClasses = BasicJUnitUtils.getComponentTestsAndSuites(aJUnitSuiteClass);
		List<Class> aSuiteClasses = BasicJUnitUtils.selectJUnitSuites(aJUnitOrSuiteClasses);
		List<Class> aTestCases = new ArrayList(aJUnitOrSuiteClasses);
		aTestCases.removeAll(aSuiteClasses); // so test cases are only leaf nodes
		Map<String, List<GradableJUnitTest>> aGroupedTopLevelGradables = BasicJUnitUtils
				.toTopLevelGradables(aTestCases);
		Map<String, List<GradableJUnitTest>> aGroupedSuiteGradables = BasicJUnitUtils.toSuiteGradables(aSuiteClasses);
		Map<String, List<GradableJUnitTest>> aGroupedGradables = new HashMap();
		aGroupedGradables.putAll(aGroupedTopLevelGradables);
		aGroupedGradables.putAll(aGroupedSuiteGradables);
//		if (aGroupedSuiteGradables.size() == 0) { // no internal nodes
//			maybeAssignMaxScores(aTopLevelSuite, aTopLevelSuite.getL);
//		}
		return aGroupedGradables;
	}

//	public  Map<String, List<GraderTestCase>> createAndCollectTopLevelTestCases(List<Class> aJUnitClasses) {
//		Map<String, List<GraderTestCase>> aResult = new HashMap();
//		for (Class aJUnitClass:aJUnitClasses) {
//			GraderTestCase aJUnitTestToGraderTestCase =
//					 	new AGraderTestCase(new AGradableJUnitTest(aJUnitClass));
//			String aGroup = aJUnitTestToGraderTestCase.getGroup();
//			List<GraderTestCase> aClasses = aResult.get(aGroup);
//			if (aClasses == null) {
//				aClasses = new ArrayList();
//				aResult.put(aGroup, aClasses);
//			}
//			aClasses.add(aJUnitTestToGraderTestCase);			
//		}
//		return aResult;
//	}

	public static Map<String, List<GradableJUnitTest>> toTopLevelGradables(List<Class> aJUnitClasses) {
		Map<String, List<GradableJUnitTest>> aResult = new HashMap();
		for (Class aJUnitClass : aJUnitClasses) {
			GradableJUnitTest aGradableJUnitTest = new AGradableJUnitTest(aJUnitClass);
			String aGroup = aGradableJUnitTest.getGroup();
			List<GradableJUnitTest> aClasses = aResult.get(aGroup);
			GradableJUnitSuite aSuite;
			if (aClasses == null) { // looks as if we create a Suite node even for ;leaf nodes, why?
				aClasses = new ArrayList();
				aResult.put(aGroup, aClasses);
				aSuite = new AGradableJUnitSuite(aJUnitClass, true);// we already created AGradableJUnitTest earlier
				aSuite.setExplanation(aGroup);
				aClasses.add(aSuite);
			}
			aSuite = (GradableJUnitSuite) aClasses.get(0);
			aClasses.add(aGradableJUnitTest);
			aSuite.add(aGradableJUnitTest);
		}
		return aResult;
	}
//	public  Map<String, List<JUnitTestToGraderTestCase>> createAndCollectTwoLevelTestCases(List<Class> aJUnitClasses) {
//		Map<String, List<JUnitTestToGraderTestCase>> aResult = new HashMap();
//		for (Class aJUnitClass:aJUnitClasses) {
//			JUnitTestToGraderTestCase aJUnitTestToGraderTestCase =
//					 	new AJUnitTestToGraderTestCase(aJUnitClass, new AJUnitTestToGraderProperties(aJUnitClass));
//			String aGroup = aJUnitTestToGraderTestCase.getGroup();
//			List<JUnitTestToGraderTestCase> aClasses = aResult.get(aGroup);
//			if (aClasses == null) {
//				aClasses = new ArrayList();
//				aResult.put(aGroup, aClasses);
//			}
//			aClasses.add(aJUnitTestToGraderTestCase);			
//		}
//		return aResult;
//	}

	public static List<Class> getJUnitTestClassesDeep(Class<?> aJUnitSuiteClass) {
		Suite.SuiteClasses aSuiteClassAnnotation = aJUnitSuiteClass.getAnnotation(Suite.SuiteClasses.class);
		if (aSuiteClassAnnotation == null)
			return null;
		Class[] aTestClasses = aSuiteClassAnnotation.value();
		List<Class> retVal = new ArrayList();
		for (Class aTestClass : aTestClasses) {
			List<Class> aSubList = getJUnitTestClassesDeep(aTestClass);
			if (aSubList == null) {
				retVal.add(aTestClass);
			} else {
				retVal.addAll(aSubList);
			}
		}
		return retVal;
	}

//	public static List<Class> getTopLevelSuites(List<Class> aSuiteOrTestClasses) {
//		
//	}
	public static boolean isJUnitSuite(Class<?> aClass) {
		return aClass.getAnnotation(Suite.SuiteClasses.class) != null;
	}

	public static List<Class> selectSuites(Collection<Class> aClasses) {
		List<Class> result = new ArrayList();
		for (Class aClass : aClasses) {
			if (isJUnitSuite(aClass)) {
				result.add(aClass);
			}
		}
		return result;
	}

	public static List<Class> getComponentSuites(Class aContainingSuite) {
		List<Class> aTestsAndSuites = getComponentTestsAndSuites(aContainingSuite);
		return selectSuites(aTestsAndSuites);
	}

	public static Set<Class> getAllComponentSuites(Collection<Class> aSuites) {
		Set<Class> result = new HashSet();
		for (Class aSuite : aSuites) {
			result.addAll(getComponentSuites(aSuite));
		}
		return result;

	}

	public static Set<Class> findTopLevelSuites(Collection<Class> aClasses) {
		List<Class> aSuites = selectSuites(aClasses);
		return selectTopLevelSuites(aClasses);
	}

	public static Set<Class> selectTopLevelSuites(Collection<Class> aClasses) {
		Set<Class> aComponentSuites = getAllComponentSuites(aClasses);
		Set<Class> aResult = new HashSet(aClasses);
		aResult.removeAll(aComponentSuites);
		return aResult;

	}

	public static List<Class> getComponentTestsAndSuites(Class<?> aJUnitSuiteClass) {
		Suite.SuiteClasses aSuiteClassAnnotation = aJUnitSuiteClass.getAnnotation(Suite.SuiteClasses.class);
		if (aSuiteClassAnnotation == null)
			return null;
		Class[] aTestClasses = aSuiteClassAnnotation.value();
		return Arrays.asList(aTestClasses);
	}

	static {
//		RunNotifierFactory.getRunNotifier().addListener(FileWriterFactory.getFileWriter());
	}
}
