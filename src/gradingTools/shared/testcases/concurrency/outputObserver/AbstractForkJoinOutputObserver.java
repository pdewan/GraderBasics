package gradingTools.shared.testcases.concurrency.outputObserver;

import java.beans.PropertyChangeEvent;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.math3.genetics.TournamentSelection;

import grader.basics.concurrency.propertyChanges.AbstractConcurrentEventSupport;
import grader.basics.concurrency.propertyChanges.BasicConcurrentPropertyChangeSupport;
import grader.basics.concurrency.propertyChanges.ConcurrentEvent;
import grader.basics.concurrency.propertyChanges.ConcurrentEventUtility;
import grader.basics.concurrency.propertyChanges.ConcurrentPropertyChange;
import grader.basics.concurrency.propertyChanges.ConcurrentPropertyChangeSupport;
import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.NotRunnableException;
import grader.basics.execution.ResultingOutErr;
import grader.basics.execution.RunningProject;
import grader.basics.junit.JUnitTestsEnvironment;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.output.observer.APropertyBasedStringChecker;
import grader.basics.output.observer.BasicNegativeOutputSelector;
import grader.basics.output.observer.BasicPositiveOutputSelector;
import grader.basics.output.observer.BasicPrintStreamListener;
import grader.basics.output.observer.ObservablePrintStream;
import grader.basics.output.observer.ObservablePrintStreamFactory;
import grader.basics.output.observer.ObservablePrintStreamUtility;
import grader.basics.output.observer.PropertyBasedStringChecker;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.shared.testcases.SubstringSequenceChecker;
import gradingTools.shared.testcases.greeting.AGreetingChecker;
import gradingTools.shared.testcases.greeting.GreetingMainProvided;
import gradingTools.shared.testcases.utils.ALinesMatcher;
import gradingTools.shared.testcases.utils.LinesMatchKind;
import gradingTools.shared.testcases.utils.LinesMatcher;
import gradingTools.utils.RunningProjectUtils;
import junit.framework.TestResult;
import util.annotations.MaxValue;
import util.models.PropertyListenerRegisterer;
import util.trace.Tracer;

@MaxValue(2)
public abstract class AbstractForkJoinOutputObserver extends AbstractOutputObserver {
//	public static final int TIME_OUT_SECS = 1; // secs
	Map<String, Object[]> nameToType = new HashMap();
	private int forkLineNumber = 0;
	private int joinLineNumber = 0;
	private ConcurrentPropertyChange[] propertyChanges;
	protected Map<Thread, ConcurrentPropertyChange[]> emptyMap = new HashMap();
	private Map<Thread, ConcurrentPropertyChange[]> threadToForkEvents = emptyMap;

	private Map<Thread, ConcurrentPropertyChange[]> threadToIterationEvents = emptyMap;
	private Map<Thread, List<Map<String, Object>>> threadToIterationTuples = new HashMap();

	private Map<Thread, ConcurrentPropertyChange[]> threadToPostIterationEvents = emptyMap;

//	private Map<Thread, String[]> threadToStrings;
	private Thread rootThread;
//	private int numOutputtingThreads;

//	private LinesMatcher linesMatcher;

	public AbstractForkJoinOutputObserver() {
//		rootThread = Thread.currentThread();
	}

	public Thread getRootThread() {
		return rootThread;
	}

	protected boolean isRootThread(Thread aThread) {
		return aThread == rootThread;
	}

	public static PropertyBasedStringChecker toChecker(String[][] anOutputProperties) {
		if (anOutputProperties == null) {
			return null;
		}
		return new APropertyBasedStringChecker(anOutputProperties);
	}

//    abstract protected int numExpectedForkedThreads();
	protected PropertyBasedStringChecker preForkChecker;

	protected PropertyBasedStringChecker preForkChecker() {
		if (preForkChecker == null) {
			preForkChecker = toChecker(preForkOutputProperties());
		}
		return preForkChecker;
	}

	protected PropertyBasedStringChecker postForkChecker;

	protected PropertyBasedStringChecker postForkChecker() {
		if (postForkChecker == null) {
			postForkChecker = toChecker(postForkOutputProperties());
		}
		return postForkChecker;
	}

	protected PropertyBasedStringChecker postJoinChecker;

	protected PropertyBasedStringChecker postJoinChecker() {
		if (postJoinChecker == null) {
			postJoinChecker = toChecker(postJoinOutputProperties());
		}
		return postJoinChecker;
	}

//	protected SubstringSequenceChecker forkedThreadChecker() {
//		return null;
//	}
//
//	protected SubstringSequenceChecker rootThreadChecker() {
//		return null;
//	}
//
//	protected String threadCheckerProperty() {
//		return null;
//	}

	protected void invokeMainMethod(Class aMainClass, String[] anArgs, String[] anInputs) throws Throwable {
		super.invokeMainMethod(aMainClass, anArgs, anInputs);
		rootThread = BasicProjectExecution.getLastMainMethodThread();

	}

	protected int getForkLineNumber() {
		return forkLineNumber;
	}

	protected int getJoinLineNumber() {
		return joinLineNumber;
	}

	public ConcurrentPropertyChange[] getPropertyChanges() {
		return propertyChanges;
	}

	public Map<Thread, ConcurrentPropertyChange[]> getThreadToPropertyChanges() {
		return threadToIterationEvents;
	}

//	public Map<Thread, String[]> getThreadToStrings() {
//		return threadToStrings;
//	}

	protected TestCaseResult preForkFailTestResult(String anExpectedLines) {
		return fail("Prefork output did not match:" + anExpectedLines);
	}

//	public int getNumOutputtingThreads() {
//		return numOutputtingThreads;
//	}

	protected double preForkOutputCredit() {
		return 0.1;
	}

	protected double postForkOutputCredit() {
		return 0.1;
	}

	protected double postJoinOutputCredit() {
		return 0.1;
	}

	protected double threadCountCredit() {
		return 0.1;
	}

	protected double totalIterationsCountCredit() {
		return 0.1;
	}
	
	protected double preForkEventCredit() {
		return 0.1;
	}
	protected double interleavingCredit() {
		return 0.1;
	}
	protected double postJoinEventCredit() {
		return 0.1;
	}

	protected double iterationsEventCredit() {
		return 0.1;
	}

	protected double postIterationsEventCredit() {
		return 0.0;
	}
	protected double compareIterationEventsCredit() {
		return 0.1;
	}

	protected double minimumOutputCredit() {
		return preForkOutputCredit() + postForkOutputCredit() + postJoinOutputCredit();
	}

	protected boolean sufficientOutputCredit(TestCaseResult aResult) {
		return aResult.getPercentage() >= minimumOutputCredit();
	};

	

	protected double forkEventCredit() {
		return iterationsEventCredit() + postIterationsEventCredit();
	}
	

//	protected double forkedThreadPartialCredit() {
//		return 0;
//	}
//
//	protected double rootThreadPartialCredit() {
//		return 0;
//	}

	protected TestCaseResult postForkFailTestResult(String anExpectedLines) {
		return partialPass(preForkOutputCredit(), "Post Fork output did not match:" + anExpectedLines);
	}

	protected TestCaseResult postJoinTestResult(String anExpectedLines) {
		return partialPass(postForkOutputCredit(), "Post Join output did not match:" + anExpectedLines);
	}

	protected LinesMatcher getLinesMatcher() {
		return getResultingOutErr().getLinesMatcher();
	}

	protected String[][] preForkProperties = emptyStringMatrix;
//	protected List<String> preForkPropertyNames = emptyList;

	protected abstract Object[][] preForkPropertyNamesAndType();

	protected abstract Object[][] iterationPropertyNamesAndType();

	protected abstract Object[][] postIterationPropertyNamesAndType();

	protected abstract Object[][] postJoinPropertyNamesAndType();

	protected abstract int totalIterations();
	
	
//	public static String toTypeString(Object[] aNameAndType) {
//		String aName = (String) aNameAndType[0];
//		Class aClass = (Class) aNameAndType[1];
//		if (aClass == Number.class || aClass == Integer.class || aClass == Long.class) {
//			return "\\d.*";
//		}
//		if (aClass == Double.class ) {
//			return "\\d.*\\.\\d.*";
//		}
//		if (aClass == Boolean.class) {
//			return "(true|false)";
//		}
//		if (aClass == Arrays.class || aClass == Array.class || aClass == List.class || aClass == ArrayList.class) {
//			if (aName.contains("Number") || aName.contains("Integer")) {
//				return "\\[\\d.*\\]";
//			}
//			return "\\[.*\\]";
//		}
//		return ".*";
//
//	}

	public static String[] toThreeTupleOutputProperty(Object[] aNameAndType) {
		String[] retVal = { 
				"Thread", (String) aNameAndType[0], ObservablePrintStreamUtility.toTypeString(aNameAndType)

		};
		return retVal;
	}

	public static String[][] toOutputPropertiesStatic(Object[][] aNameAndTypes) {
		if (aNameAndTypes == null) {
			return null;
		}
		String[][] retVal = new String[aNameAndTypes.length][];
		for (int index = 0; index < aNameAndTypes.length; index++) {
			retVal[index] = toThreeTupleOutputProperty(aNameAndTypes[index]);
		}
		return retVal;
	}
	public  String[][] toOutputProperties(Object[][] aNameAndTypes) {
		
		String[][] retVal = toOutputPropertiesStatic(aNameAndTypes);
		if (retVal != null) {
			for (Object[] aNameAndType:aNameAndTypes) {
				nameToType.put((String) aNameAndType[0], aNameAndType);
			}
		}
		return retVal;
	}

	public static String[][] repeat(String[][] aThreeTuples, int aNumRepetitions) {
		if (aThreeTuples == null) {
			return null;
		}
		String[][] retVal = new String[aThreeTuples.length * aNumRepetitions][];
		for (int index = 0; index <= retVal.length - aThreeTuples.length;) {
			for (int aTuplesIndex = 0; aTuplesIndex < aThreeTuples.length; aTuplesIndex++) {
				retVal[index] = aThreeTuples[aTuplesIndex];
				index++;

			}
		}
		return retVal;
	}

	protected String[][] postIterationOutputProperties() {
		return repeat(toOutputProperties(postIterationPropertyNamesAndType()), numExpectedForkedThreads());
	}

	protected String[][] perIterationOutputProperties() {
		return toOutputProperties(iterationPropertyNamesAndType());
	}

	protected String[][] allIterationsOutputProperties() {
		return repeat(perIterationOutputProperties(), totalIterations());
	}

	protected String[][] preForkOutputProperties() {
		return toOutputProperties(preForkPropertyNamesAndType());
	}

	protected String[][] postForkOutputProperties() {
		return combine(allIterationsOutputProperties(), postIterationOutputProperties());
	}

	protected String[][] postJoinOutputProperties() {
		return toOutputProperties(postJoinPropertyNamesAndType());
	}

	public static String[][] combine(String[][] operand1, String[][] operand2) {
		if (operand1 == null && operand2 == null) {
			return null;
		}
		if (operand1 == null) {
			return operand2;
		}
		if (operand1 == null) {
			return operand2;
		}
		List<String[]> retVal = new ArrayList();
		List<String[]> list1 = Arrays.asList(operand1);
		retVal.addAll(list1);
		List<String[]> list2 = Arrays.asList(operand2);
		retVal.addAll(list2);
		return retVal.toArray(emptyStringMatrix);
	}

//	public APrimesPostForkPropertyChecker (int aNumItems) {
//		
//		int aNumNotifications = aNumItems*NOTIFICATIONS_PER_ITEM + REMAINING_NOTIFICATIONS;
//		myProperties = new String[aNumNotifications][];
//		for (int i = 0; i < aNumNotifications -REMAINING_NOTIFICATIONS; i = i+NOTIFICATIONS_PER_ITEM) {
////			myPatterns[i] = ".*" + i + ".*ello.*" + ".*";
//			myProperties[i] = new String[] {"Thread", "Index", "\\d.*"};
//			myProperties[i+1] = new String[] {"Thread", "Number", "\\d.*"};
//			myProperties[i+2] = new String[] {"Thread", "Is Prime", "(true|false)"};
//		}
//		myProperties[aNumNotifications -REMAINING_NOTIFICATIONS] = new String[] {"Thread", "Num Primes", "\\d.*"};
//		init ( myProperties);
//			
//		}
//	protected   String[][] MY_PROPERTIES = {
//			{"Root Thread", "Random Numbers", "\\[.*\\d.*\\]"}
//			};
	protected String[] preForkPropertyNames;
	protected String[] iterationPropertyNames;
	protected String[] postIterationPropertyNames;
	protected String[] postJoinPropertyNames;

	protected String[] toNames(Object[][] aNamesAndType) {
		if (aNamesAndType == null) {
			return null;
		}
		String[] retVal = new String[aNamesAndType.length];
		for (int index = 0; index < aNamesAndType.length; index++) {
			retVal[index] = (String) aNamesAndType[index][0];
		}
		return retVal;
	}

	protected String[] preForkPropertyNames() {
		if (preForkPropertyNames == null) {
			preForkPropertyNames = toNames(preForkPropertyNamesAndType());
		}
		return preForkPropertyNames;
	}

	protected String[] iterationPropertyNames() {
		if (iterationPropertyNames == null) {
			iterationPropertyNames = toNames(iterationPropertyNamesAndType());
		}
		return iterationPropertyNames;
	}

	protected String[] postIterationPropertyNames() {
		if (postIterationPropertyNames == null) {
			postIterationPropertyNames = toNames(postIterationPropertyNamesAndType());
		}
		return postIterationPropertyNames;
	}

	protected String[] postJoinPropertyNames() {
		if (postJoinPropertyNames == null) {
			postJoinPropertyNames = toNames(postJoinPropertyNamesAndType());
		}
		return postJoinPropertyNames;

	}

	protected int numPreForkEvents() {
//		PropertyBasedStringChecker aChecker = preForkChecker();
//		if (aChecker == null) {
//			return 0;
//		}
//		return aChecker.getProperties().length;
		String[] aPropertyNames = preForkPropertyNames();
		if (aPropertyNames == null) {
			return 0;
		}
		return aPropertyNames.length;
	}

	protected int numPerIterationEvents() {
		String[] aPropertyNames = iterationPropertyNames();
		if (aPropertyNames == null) {
			return 0;
		}
		return aPropertyNames.length;
	}

	protected int totalIterationEvents() {
		return numPerIterationEvents() * totalIterations();
	}

	protected int numPostIterationEvents() {
		String[] aPropertyNames = postIterationPropertyNames();
		if (aPropertyNames == null) {
			return 0;
		}
		return aPropertyNames.length * numOutputtingForkedThreads;
	}

	protected int numPostJoinEvents() {
		String[] aPropertyNames = postJoinPropertyNames();
		if (aPropertyNames == null) {
			return 0;
		}
		return aPropertyNames.length;
	}

	protected TestCaseResult checkPreForkOutput() {
		LinesMatcher aLinesMatcher = getLinesMatcher();
		boolean aPreForkRetVal = true;
		PropertyBasedStringChecker aPreForkChecker = preForkChecker();
		if (aPreForkChecker != null) {
			aPreForkRetVal = aPreForkChecker.check(aLinesMatcher, LinesMatchKind.ONE_TIME_LINE, Pattern.DOTALL);
			preForkProperties = aPreForkChecker.getProperties();
//			preForkPropertyNames = ObservablePrintStreamUtility.toPropertyNames(preForkProperties);
			if (!aPreForkRetVal) {
				String anExpectedLines = Arrays.toString(aPreForkChecker.getSubstrings());
				return fail("Pre fork output did not match:" + anExpectedLines);
			}
		}
		return partialPass(preForkOutputCredit(), "Pre fork output correct");
	}

	static String[] emptyStringArray = {};
	static String[][] emptyStringMatrix = {};
	static List<String> emptyList = Arrays.asList(emptyStringArray);

	protected String[][] postForkProperties = emptyStringMatrix;
//	protected List<String> postForkPropertyNames = emptyList;
//	protected String[] iterationPropertyNames;
//	protected String[] postIterationPropertyNames;

//	protected String[] postIterationPropertyNames() {
//		return postIterationPropertyNames;
//	}
//
//	protected String[] iterationPropertyNames() {
//		return iterationPropertyNames;
//	}

//	protected String[] postIterationPropertyNames() {
//		return iterationPropertyNames;
//	}

	protected TestCaseResult checkPostForkOutput() {
		LinesMatcher aLinesMatcher = getLinesMatcher();
		forkLineNumber = aLinesMatcher.getMaxMatchedLineNumber() + 1;
		aLinesMatcher.setStartLineNumber(forkLineNumber);
		boolean aPostForkRetVal = true;
		PropertyBasedStringChecker aPostForkChecker = postForkChecker();
		if (postForkChecker == null) {
			return partialPass(postForkOutputCredit(), "Post fork output correct as no fork checker");
		}
		postForkProperties = aPostForkChecker.getProperties();
//		postForkPropertyNames = ObservablePrintStreamUtility.toPropertyNames(postForkProperties);
		int aNumPostIterationEvents = numPostIterationEvents();
//		postIterationPropertyNames = new String[aNumPostIterationEvents];
//        for (int index = 0; index < postIterationPropertyNames.length; index++ ) {
//        	int aForkPropertyIndex = postForkPropertyNames.size()  - aNumPostIterationEvents + index;
//        	postIterationPropertyNames[index] = 
//        			postForkPropertyNames.get(aForkPropertyIndex);
//			
//		}
		int aNumPerIterationEvents = numPerIterationEvents();

//        iterationPropertyNames = new String[aNumPerIterationEvents];
//        for (int index = 0; index < iterationPropertyNames.length; index++ ) {
//        	iterationPropertyNames[index] = 
//        			postForkPropertyNames.get(postForkPropertyNames.size() - aNumPostIterationEvents - aNumPerIterationEvents  + index);
//			
//		}
//	
		if (aPostForkChecker != null) {
			aPostForkRetVal = aPostForkChecker.check(aLinesMatcher, LinesMatchKind.ONE_TIME_UNORDERED, Pattern.DOTALL);

			if (!aPostForkRetVal) {
				String anExpectedLines = Arrays.toString(aPostForkChecker.getSubstrings());
				return fail("Post fork output did not match:" + anExpectedLines);
			}
		}
		return partialPass(postForkOutputCredit(), "Post fork output correct");
	}

	protected String[][] postJoinProperties = emptyStringMatrix;
//	protected List<String> postJoinPropertyNames = emptyList;
//
//	protected String[] postJoinPropertyNames() {
//		return postJoinPropertyNames.toArray(emptyArray);
//	}

	protected TestCaseResult checkPostJoinOutput() {
		LinesMatcher aLinesMatcher = getLinesMatcher();
		joinLineNumber = aLinesMatcher.getMaxMatchedLineNumber() + 1;
		aLinesMatcher.setStartLineNumber(joinLineNumber);

		boolean aPostJoinRetVal = true;
		PropertyBasedStringChecker aPostJoinChecker = postJoinChecker();
		if (aPostJoinChecker != null) {
			postJoinProperties = aPostJoinChecker.getProperties();
//			postJoinPropertyNames = ObservablePrintStreamUtility.toPropertyNames(postJoinProperties);
			aPostJoinRetVal = aPostJoinChecker.check(aLinesMatcher, LinesMatchKind.ONE_TIME_LINE, Pattern.DOTALL);

			if (!aPostJoinRetVal) {
				String anExpectedLines = Arrays.toString(aPostJoinChecker.getSubstrings());

				return fail("Post join output did not match:" + anExpectedLines);
			}
		}
		return partialPass(postJoinOutputCredit(), "Post join output correct");
	}

//	protected TestCaseResult checkRootThreadOutput(String[] aRootThreadOutput) {
//		SubstringSequenceChecker aRootThreadChecker = rootThreadChecker();
//		if (aRootThreadChecker == null) {
//			return pass();
//		}
//		if (aRootThreadOutput == null) {
//			return fail("No root thread output");
//		}
//		LinesMatcher aLinesMatcher = new ALinesMatcher(aRootThreadOutput);
//		boolean retVal = aRootThreadChecker.check(aLinesMatcher, LinesMatchKind.ONE_TIME_LINE, Pattern.DOTALL);
//
//		if (!retVal) {
//			String anExpectedLines = Arrays.toString(aRootThreadChecker.getSubstrings());
//
//			return fail("Root thread output did not match:" + anExpectedLines);
//		}
//
//		return partialPass(rootThreadPartialCredit(), "Root thread output correct");
//	}

//	protected TestCaseResult checkForkedThreadOutput(Thread aThread, String[] aForkedThreadOutput) {
//		SubstringSequenceChecker aForkedThreadChecker = forkedThreadChecker();
//		if (aForkedThreadChecker == null) {
//			return pass();
//		}
//		if (aForkedThreadChecker == null) {
//			return fail("No root thread output");
//		}
//		LinesMatcher aLinesMatcher = new ALinesMatcher(aForkedThreadOutput);
//		boolean retVal = aForkedThreadChecker.check(aLinesMatcher, LinesMatchKind.ONE_TIME_LINE, Pattern.DOTALL);
//
//		if (!retVal) {
//			String anExpectedLines = Arrays.toString(aForkedThreadChecker.getSubstrings());
//
//			return fail("Forked thread " + aThread + " output did not match:" + anExpectedLines);
//		}
//
//		return partialPass(forkedThreadPartialCredit(), "Forked thread " + aThread + " output correct");
//	}

	@Override
	protected TestCaseResult checkOutput(ResultingOutErr anOutput) {
		TestCaseResult aPreForkResult = checkPreForkOutput();
		TestCaseResult aPostForkResult = checkPostForkOutput();
		TestCaseResult aPostJoinResult = checkPostJoinOutput();
		return combineResults(aPreForkResult, combineResults(aPostForkResult, aPostJoinResult));
//		double aTotalCredit = aPreForkResult.getPercentage() + aPostForkResult.getPercentage()
//				+ aPostJoinResult.getPercentage();
//		if (aTotalCredit == 1.0) {
//			return pass();
//		}
//
//		String aTotalNotes = aPreForkResult.getNotes() + "\n" + aPostForkResult.getNotes() + "\n"
//				+ aPostJoinResult.getNotes();
//		return partialPass(aTotalCredit, aTotalNotes);

	}

//	protected TestCaseResult combineResults(TestCaseResult... aResultArray) {
//		boolean anAllPassed = true;
//		double aPercentage = 0;
//		StringBuffer aMessage = new StringBuffer();
//		for (TestCaseResult aTestCaseResult : aResultArray) {
//			if (!aTestCaseResult.isPass() && anAllPassed) {
//				anAllPassed = false;
//			}
//			aPercentage += aTestCaseResult.getPercentage();
//			aMessage.append("\n" + aTestCaseResult.getNotes());
//		}
//		if (anAllPassed) {
//			return pass();
//		}
//		return partialPass(aPercentage, aMessage.toString());
//	
//	}

	protected TestCaseResult combineResults(TestCaseResult aRootTestCaseResult,
			List<TestCaseResult> aForkedThreadResults) {
		int aNumForkedTHreads = aForkedThreadResults.size();

		double aPercentage = aRootTestCaseResult.getPercentage();
		StringBuffer aMessage = new StringBuffer(aRootTestCaseResult.getNotes());
		boolean anAllPassed = aRootTestCaseResult.isPass();
		for (TestCaseResult aTestCaseResult : aForkedThreadResults) {
			if (!aTestCaseResult.isPass() && anAllPassed) {
				anAllPassed = false;
			}
			aPercentage += aTestCaseResult.getPercentage() / numOutputtingForkedThreads;
			aMessage.append("\n" + aTestCaseResult.getNotes());
		}
		if (anAllPassed) {
			return pass();
		}
		return partialPass(aPercentage, aMessage.toString());
	}
//	protected TestCaseResult combineResults(TestCaseResult aResult1, TestCaseResult aResult2) {
//		if (aResult1.isPass() && aResult2.isPass()) {
//			return pass();
//		}
//		return partialPass(
//				aResult1.getPercentage() + aResult2.getPercentage(),
//				aResult1.getNotes() + "\n" + aResult2.getNotes());
//	
//	}

//	protected int numOutputtingForkedThreads;
//	
//	@Override
//	protected  TestCaseResult checkEvents(ConcurrentPropertyChange[] anEvents) {
//		
//
//		propertyChanges = anEvents;
//		threadToPropertyChanges = ConcurrentEventUtility.getConcurrentPropertyChangesByThread(propertyChanges);
////		if (threadCheckerProperty() == null) {
//		threadToStrings = ConcurrentEventUtility.toNewValueStrings(threadToPropertyChanges, threadCheckerProperty());
////		}
////		numOutputtingThreads = threadToPropertyChanges.size();
//		
//		TestCaseResult aRootThreadResult = null;
//		if (rootThreadChecker() == null) {
//			aRootThreadResult = pass();
//		} else {
//			aRootThreadResult = fail ("No root thread output");
//		}
//		List<TestCaseResult> aForkedThreadResults = new ArrayList();
//		for (Thread aThread : threadToStrings.keySet()) {
//			String[] aStrings = threadToStrings.get(aThread);
//			
//			if (aThread == rootThread) {
//				aRootThreadResult = checkRootThreadOutput(aStrings);
//			} else {
//				aForkedThreadResults.add(checkForkedThreadOutput(aThread, aStrings));
//			}
//		}
//		numOutputtingForkedThreads = aForkedThreadResults.size();
//		int anExpectedForkedThreads = numExpectedForkedThreads();
//		if (forkedThreadChecker() != null && numOutputtingForkedThreads != anExpectedForkedThreads) {
//			return partialPass(
//					aRootThreadResult.getPercentage(),
//					aRootThreadResult.getNotes() + "\n" +
//					"# expected forked threads = " + anExpectedForkedThreads + " but # outputting forked threads = " + numOutputtingForkedThreads);
//			
//		}
//		return combineResults(aRootThreadResult, aForkedThreadResults);
//	}

	protected boolean isIndexProperty(String[] aPropertyTuple) {
		return aPropertyTuple[1].toLowerCase().equals("index");
	}

//	protected int numPerIterationEvents() {
//		int retVal = 0;
//		if (postForkProperties.length <= 1) {
//			return retVal;
//		}
//		if (!isIndexProperty(postForkProperties[0])) {
//			return retVal;
//		}
//		for (int index = 1; index < postForkProperties.length; index++) {
//			if (isIndexProperty(postForkProperties[index])) {
//				return index;
//			}
//		}
//		return postForkProperties.length - 1; // Assume one iteration and one post iteration property
//	}
//	protected int numPostIterationEvents() {
//		return 1;
//	}

//	protected int eventsBetweenIterations = 0;
//	protected int eventsAfterIterations = 0;
//	protected int eventsInIterations;

	protected boolean rootThreadIterates() {
		return false;
	}

	@Override
	protected TestCaseResult checkEvents(ConcurrentPropertyChange[] anEvents) {

//		int aPreForkEventsSize = preForkProperties.length;
//		int aPostForkEventsSize = postForkProperties.length;
//		int aPostJoinEventsSize = postJoinProperties.length;
		int aPreForkEventsSize = numPreForkEvents();
//		int aPostForkEventsSize = numPost
		int aPostJoinEventsSize = numPostJoinEvents();
		int aPostIterationEventsSize = numPostIterationEvents();
//		eventsBetweenIterations = numPerIterationEvents();
//		eventsAfterIterations
//		if (eventsBetweenIterations != 0) {
//
//			eventsAfterIterations = aPostForkEventsSize % eventsBetweenIterations;
//		} else {
//			eventsAfterIterations = aPostForkEventsSize;
//		}
		int anActualIterationsSize = anEvents.length
				- (aPreForkEventsSize + aPostIterationEventsSize + aPostJoinEventsSize);

		TestCaseResult anIterationsCountResult = partialPass(totalIterationsCountCredit(),
				"Correct number of iterations");
		if (anActualIterationsSize < totalIterationEvents()) {
			anIterationsCountResult = fail(
					"Actual # iterations  =" + anActualIterationsSize + " < " + "expected #" + totalIterations());
		}
		int aForkStartIndex = aPreForkEventsSize;
		int aForkStopIndex = aForkStartIndex + anActualIterationsSize + aPostIterationEventsSize;
		int anIterationsStartIndex = aForkStartIndex;
		int anIterationsStopIndex = aForkStopIndex;
//		int anIterationsStartIndex = aPreForkEventsSize;
//		int anIterationsStopIndex = anIterationsStartIndex + anActualIterationsSize + aPostIterationEventsSize;
		int aPostIterationStartIndex = anIterationsStartIndex;

		int aPostIterationStopIndex = anIterationsStopIndex;
		int aPostJoinStartIndex = aPostIterationStopIndex;
		int aPostJoinStopIndex = aPostIterationStopIndex + aPostJoinEventsSize;

		TestCaseResult aPreForkResult = checkPreForkEvents(anEvents, aPreForkEventsSize);
		TestCaseResult aFinalResult = combineResults(aPreForkResult, anIterationsCountResult);

//		TestCaseResult anIterationsResult = checkIterationsEvents(anEvents, anIterationsStartIndex,
//				anIterationsStopIndex);
//		aFinalResult = combineResults(aFinalResult, anIterationsResult);
//		TestCaseResult aPostIterationsResult = checkPostIterationsEvents(anEvents, aPostIterationStartIndex,
//				aPostIterationStopIndex);
//		aFinalResult = combineResults(aFinalResult, aPostIterationsResult);
		TestCaseResult aForkResult = checkForkEvents(anEvents, aForkStartIndex, aForkStopIndex);
		aFinalResult = combineResults(aFinalResult, aForkResult);
		TestCaseResult aPostJoinResult = checkPostJoinEvents(anEvents, aPostJoinStartIndex, aPostJoinStopIndex);
		aFinalResult = combineResults(aFinalResult, aPostJoinResult);
		return aFinalResult;
//
//
//		propertyChanges = anEvents;
//		
//		threadToIterationEvents = ConcurrentEventUtility.getConcurrentPropertyChangesByThread(propertyChanges);
////		if (threadCheckerProperty() == null) {
//		threadToStrings = ConcurrentEventUtility.toNewValueStrings(threadToIterationEvents, threadCheckerProperty());
////		}
////		numOutputtingThreads = threadToPropertyChanges.size();
//		
//		TestCaseResult aRootThreadResult = null;
//		if (rootThreadChecker() == null) {
//			aRootThreadResult = pass();
//		} else {
//			aRootThreadResult = fail ("No root thread output");
//		}
//		List<TestCaseResult> aForkedThreadResults = new ArrayList();
//		for (Thread aThread : threadToStrings.keySet()) {
//			String[] aStrings = threadToStrings.get(aThread);
//			
//			if (aThread == rootThread) {
//				aRootThreadResult = checkRootThreadOutput(aStrings);
//			} else {
//				aForkedThreadResults.add(checkForkedThreadOutput(aThread, aStrings));
//			}
//		}
//		numOutputtingForkedThreads = aForkedThreadResults.size();
//		int anExpectedForkedThreads = numExpectedForkedThreads();
//		if (forkedThreadChecker() != null && numOutputtingForkedThreads != anExpectedForkedThreads) {
//			return partialPass(
//					aRootThreadResult.getPercentage(),
//					aRootThreadResult.getNotes() + "\n" +
//					"# expected forked threads = " + anExpectedForkedThreads + " but # outputting forked threads = " + numOutputtingForkedThreads);
//			
//		}
//		return combineResults(aRootThreadResult, aForkedThreadResults);
	}

//	protected double preForkEventCredit() {
//		return 0;
//	}

	protected Map<Thread, ConcurrentPropertyChange[]> preForkEvents;
	protected ConcurrentPropertyChange[] emptyEvents = {};

	protected TestCaseResult checkPreForkEvents(ConcurrentPropertyChange[] anEvents, int aStopIndex) {
		if (aStopIndex == 0) {
			return partialPass(preForkEventCredit(), "Pre fork events correct");

		}
		preForkEvents = ConcurrentEventUtility.getConcurrentPropertyChangesByThread(anEvents, 0, aStopIndex);
		if (preForkEvents.size() > 1) {
			return fail("#pre fork notifying threads == " + preForkEvents.size() + " instead of 1");
		}
		for (Thread aThread : preForkEvents.keySet()) { // loop should go omly once
			ConcurrentPropertyChange[] aThreadEvents = preForkEvents.get(aThread);
			return checkPreForkEvents(aThread, aThreadEvents);
		}
		return fail("#pre fork notifying threads == " + preForkEvents.size() + " instead of 1");

	}
	
	public  Map<String, Object> toNameValueMap (ConcurrentPropertyChange[] anEvents) {
		Map<String, Object> anOriginal = ConcurrentEventUtility.toNameValueMap(anEvents);
		toNameTypedValueMap(anOriginal);
		return anOriginal;
	}
	
	protected void toNameTypedValueMap (Map<String, Object> anOriginal) {
		for (String aName: anOriginal.keySet()) {
			try {
			Object[] aNameAndType = nameToType.get(aName);
			if (aNameAndType == null) {
				continue;
			}
			Class aValueClass = (Class) aNameAndType[1];
			
			String aValueString = (String) anOriginal.get(aName);
			Object aNewValue = ObservablePrintStreamUtility.toTypedObject(aName, aValueString, aValueClass);
			if (aNewValue != aValueString) {
				anOriginal.put(aName, aNewValue);
			}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
		}
	}

	protected TestCaseResult checkPreForkEvents(Thread aThread, ConcurrentPropertyChange[] anEvents) {
		Map<String, Object> aNameValuePairs = toNameValueMap(anEvents);

		return checkPreForkEvents(aThread, aNameValuePairs);
	}

	protected TestCaseResult checkPreForkEvents(Thread aThread, Map<String, Object> aNameValuePairs) {
		String aMessage = preForkEventsMessage(aThread, aNameValuePairs);
		if (aMessage == null) {
			return partialPass(preForkEventCredit(), "Pre fork events correct");
		} else {
			return fail(aMessage);

		}
	}

	protected String preForkEventsMessage(Thread aThread, Map<String, Object> aNameValuePairs) {
		return null;
	}

	protected Map<Thread, ConcurrentPropertyChange[]> postJoinEvents;

	protected TestCaseResult checkPostJoinEvents(ConcurrentPropertyChange[] anEvents, int aStartIndex, int aStopIndex) {
		postJoinEvents = ConcurrentEventUtility.getConcurrentPropertyChangesByThread(anEvents, aStartIndex, aStopIndex);
		if (postJoinEvents.size() != 1) {
			return fail("#post join notifying threads == " + postJoinEvents.size() + " instead of 1");
		}
		for (Thread aThread : postJoinEvents.keySet()) { // loop should go omly once
			ConcurrentPropertyChange[] aThreadEvents = postJoinEvents.get(aThread);
			return checkPostJoinEvents(aThread, aThreadEvents);
		}
		return fail("#post join notifying threads == " + postJoinEvents.size() + " instead of 1");

	}

	protected TestCaseResult checkPostJoinEvents(Thread aThread, ConcurrentPropertyChange[] anEvents) {
		Map<String, Object> aNameValuePairs = toNameValueMap(anEvents);

		return checkPostJoinEvents(aThread, aNameValuePairs);
	}

	protected TestCaseResult checkPostJoinEvents(Thread aThread, Map<String, Object> aNameValuePairs) {
		String aMessage = postJoinEventsMessage(aThread, aNameValuePairs);
		if (aMessage == null) {
			return partialPass(postJoinEventCredit(), "Post join events correct");
		} else {
			return fail(aMessage);

		}
	}

	protected String postJoinEventsMessage(Thread aThread, Map<String, Object> aNameValuePairs) {
		return null;
	}

	// Post Iterations
	protected Map<Thread, List<ConcurrentPropertyChange>> postIterationEvents;

//	protected double postIterationsEventCredit() {
//		return 0;
//	}

	protected Map<Thread, List<ConcurrentPropertyChange>> postIterationsEvents;

	protected TestCaseResult checkPostIterationsEvents(ConcurrentPropertyChange[] anEvents, int aStartIndex,
			int aStopIndex) {
		TestCaseResult aResult = partialPass(postIterationsEventCredit(), "Post iterations correct");

		if (aStartIndex == aStopIndex) {
			return aResult;
		}
		postIterationEvents = ConcurrentEventUtility.getConcurrentPropertyChangeListByThread(anEvents, aStartIndex,
				aStopIndex);
//		if (postIterationEvents.size() != 1) {
//			return fail("#post iteration notifying threads == " + postIterationEvents.size() + " instead of 1" );
//		}
		for (Thread aThread : postIterationEvents.keySet()) { // loop should go omly once
			ConcurrentPropertyChange[] anOriginalEvents = threadToIterationEvents.get(aThread);
			ConcurrentPropertyChange[] aPostIterationEvents = ConcurrentEventUtility
					.filterByProperties(anOriginalEvents, postIterationPropertyNames);
			aResult = checkPostIterationEvents(aThread, aPostIterationEvents);

			if (aResult.getPercentage() < postIterationsEventCredit()) {
				return aResult;
			}
		}
		return aResult;

	}

	protected TestCaseResult checkPostIterationEvents(Thread aThread, ConcurrentPropertyChange[] anEvents) {
		Map<String, Object> aNameValuePairs = toNameValueMap(anEvents);

		return checkPostIterationEvents(aThread, aNameValuePairs);
	}

	protected TestCaseResult checkPostIterationEvents(Thread aThread, Map<String, Object> aNameValuePairs) {
		String aMessage = postIterationEventsMessage(aThread, aNameValuePairs);
		if (aMessage == null) {
			return partialPass(preForkEventCredit(), "Post iteration events correct");
		} else {
			return fail(aMessage);

		}
	}

	protected String postIterationEventsMessage(Thread aThread, Map<String, Object> aNameValuePairs) {
		return null;
	}

	// Iterations
//		protected Map<Thread, ConcurrentPropertyChange[]> iterationEvents;

//	protected double iterationsEventCredit() {
//		return 0;
//	}

//	protected Map<Thread, ConcurrentPropertyChange[]> iterationsEvents;
	
	protected void threadEventProcessingSwitched(Thread aPreviousThread, Thread aNewThread) {
		
	}

	protected TestCaseResult checkForkEvents(ConcurrentPropertyChange[] anEvents, int aStartIndex, int aStopIndex) {
		if (aStopIndex == aStartIndex) {
			return partialPass(forkEventCredit(), "Fork events correct");

		}
		
		TestCaseResult aResult = partialPass(forkEventCredit(), "Fork correct");
		TestCaseResult anInterleavingResult = null;

		boolean hasInterleaving = numExpectedForkedThreads() == 1 || ConcurrentEventUtility.someInterleaving(anEvents,
				aStartIndex,
				aStopIndex,
				null, null); 
		if (!hasInterleaving ) {
			anInterleavingResult = fail ("No interleaving during fork");
		} else {
			anInterleavingResult = partialPass (interleavingCredit(), "Interleaving correct");
		}
		threadToForkEvents = ConcurrentEventUtility.getConcurrentPropertyChangesByThread(anEvents, aStartIndex,
				aStopIndex);
//			if (iterationEvents.size() != 1) {
//				return fail("#iteration notifying threads == " + iterationEvents.size() + " instead of 1" );
//			}
//		TestCaseResult aResult = partialPass(forkEventCredit(), "Fork correct");
		TestCaseResult anIterationsResult = null;
		TestCaseResult aPostIterationsResult = null;
		Thread aPreviousThread = null;
		for (Thread aThread : threadToForkEvents.keySet()) {
			threadEventProcessingSwitched(aPreviousThread, aThread);
			aPreviousThread = aThread;
			ConcurrentPropertyChange[] anOriginalEvents = threadToForkEvents.get(aThread);
			if (iterationPropertyNames != null) {

				ConcurrentPropertyChange[] anIterationEvents = ConcurrentEventUtility
						.filterByProperties(anOriginalEvents, iterationPropertyNames);
				threadToIterationEvents.put(aThread, anIterationEvents);
				
				anIterationsResult = checkIterationEvents(aThread, anIterationEvents);
				if (anIterationsResult.getPercentage() < iterationsEventCredit()) {
					return combineResults (anInterleavingResult, anIterationsResult);
				}
			}
			if (postIterationPropertyNames != null) {

				ConcurrentPropertyChange[] aPostIterationEvents = ConcurrentEventUtility
						.filterByProperties(anOriginalEvents, postIterationPropertyNames);
				aPostIterationsResult = checkPostIterationEvents(aThread, aPostIterationEvents);
				
				if (aPostIterationsResult.getPercentage() < postIterationsEventCredit()) {
//					return combineResults(anIterationsResult, aPostIterationsResult);
					return combineResults (anInterleavingResult, 
							combineResults (anIterationsResult, aPostIterationsResult));
				}
			}
		}
		aResult = combineResults(anInterleavingResult, aResult);
//		TestCaseResult aCompareIterationEentsResult = compareIterationEvents(threadToIterationEvents);
		TestCaseResult aCompareIterationEentsResult = compareIterationTuples(threadToIterationTuples);

		aResult = combineResults(aCompareIterationEentsResult, aResult);
		return aResult;

//		return combineResults (anInterleavingResult, aResult);
//			return fail("#Post Iteration  notifying threads == " + postIterationEvents.size() + " instead of 1" );

	}
	
	protected TestCaseResult compareIterationEvents(Map<Thread, ConcurrentPropertyChange[]> aThreadToIterationEvents) {
		String aMessage = compareIterationEventsMessage(aThreadToIterationEvents);
		if (aMessage == null) {
			return partialPass(compareIterationEventsCredit(), "Iteration events compared correctly");
			
		} else {
			return fail(aMessage);
		}
	}
	protected TestCaseResult compareIterationTuples(Map<Thread, List<Map<String, Object>>> aThreadToIterationEvents) {
		String aMessage = compareIterationTuplesMessage(aThreadToIterationEvents);
		if (aMessage == null) {
			return partialPass(compareIterationEventsCredit(), "Iteration events compared correctly");
			
		} else {
			return fail(aMessage);
		}
	}
	protected String compareIterationEventsMessage(Map<Thread, ConcurrentPropertyChange[]> aThreadToIterationEvents) {
		int aMinIterations = Integer.MAX_VALUE;
		int aMaxIterations = 0;
		for (ConcurrentPropertyChange[] aChanges:aThreadToIterationEvents.values()) {
			int aNumIterations = aChanges.length;
			aMinIterations = Math.min(aMinIterations, aNumIterations);
			aMaxIterations = Math.max(aMaxIterations, aNumIterations);
		}
		return compareIterationEventsMessage(aMinIterations, aMaxIterations);
	}
	protected String compareIterationTuplesMessage(Map<Thread, List<Map<String, Object>>> aThreadToIterationTuples) {
		int aMinIterations = Integer.MAX_VALUE;
		int aMaxIterations = 0;
		for (List<Map<String, Object>> aChanges:aThreadToIterationTuples.values()) {
			int aNumIterations = aChanges.size();
			aMinIterations = Math.min(aMinIterations, aNumIterations);
			aMaxIterations = Math.max(aMaxIterations, aNumIterations);
		}
		return compareIterationEventsMessage(aMinIterations, aMaxIterations);
	}
protected String compareIterationEventsMessage(int aMinIterations, int aMaxIterations) {
	int aDifference = aMaxIterations - aMinIterations;
	if (aDifference >  1) {
		return "Max thread iterations:" + aMaxIterations + " - min thread iterations =" + aDifference; 
	} else {
		return null;
	}
}
	

	protected TestCaseResult checkIterationsEvents(ConcurrentPropertyChange[] anEvents, int aStartIndex,
			int aStopIndex) {
		if (aStopIndex == aStartIndex) {
			return partialPass(preForkEventCredit(), "Iteration events correct");

		}

		threadToIterationEvents = ConcurrentEventUtility.getConcurrentPropertyChangesByThread(anEvents, aStartIndex,
				aStopIndex);
//			if (iterationEvents.size() != 1) {
//				return fail("#iteration notifying threads == " + iterationEvents.size() + " instead of 1" );
//			}
		TestCaseResult aResult = partialPass(iterationsEventCredit(), "Iterations correct");
		for (Thread aThread : threadToIterationEvents.keySet()) {
			ConcurrentPropertyChange[] anOriginalEvents = threadToIterationEvents.get(aThread);
			ConcurrentPropertyChange[] anIterationEvents = ConcurrentEventUtility.filterByProperties(anOriginalEvents,
					iterationPropertyNames);
			aResult = checkIterationEvents(aThread, anIterationEvents);
			if (aResult.getPercentage() < iterationsEventCredit()) {
				return aResult;
			}
		}

		return aResult;
//			return fail("#Post Iteration  notifying threads == " + postIterationEvents.size() + " instead of 1" );

	}

	protected TestCaseResult checkIterationEvents(Thread aThread, ConcurrentPropertyChange[] anEvents) {
		TestCaseResult aResult = partialPass(iterationsEventCredit(), "Iteration events correct");
		ConcurrentPropertyChange[] aTuple = new ConcurrentPropertyChange[numPerIterationEvents()];
		List<Map<String, Object>> aTuples = new ArrayList();
		threadToIterationTuples.put(aThread, aTuples);

		for (int eventsIndex = 0; eventsIndex <= anEvents.length - aTuple.length;) {
			for (int aTupleIndex = 0; aTupleIndex < aTuple.length; aTupleIndex++) {
				aTuple[aTupleIndex] = anEvents[eventsIndex];
				eventsIndex++;
//				aTuple[aTupleIndex + 1] = anEvents[eventsIndex + 1];
//				aTuple[aTupleIndex + 2] = anEvents[eventsIndex + 2];
			}
			Map<String, Object> aNameValuePairs = toNameValueMap(aTuple);
			aTuples.add(aNameValuePairs);
			aResult = checkIterationEvents(aThread, aNameValuePairs);
			if (aResult.getPercentage() < iterationsEventCredit()) {
				return aResult;
			}
		}

		return aResult;
	}

	protected TestCaseResult checkIterationEvents(Thread aThread, Map<String, Object> aNameValuePairs) {
		String aMessage = iterationEventsMessage(aThread, aNameValuePairs);
		if (aMessage == null) {
			return partialPass(iterationsEventCredit(), "Iteration events correct");
		} else {
			return fail(aMessage);

		}
	}

	protected String iterationEventsMessage(Thread aThread, Map<String, Object> aNameValuePairs) {
		return null;
	}
	@Override
	protected TestCaseResult performanceCredit(long aLowThreadsTime, long aHighThreadsTime, int aNumProcessors) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected String[] lowThreadArgs() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected String[] highThreadArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
