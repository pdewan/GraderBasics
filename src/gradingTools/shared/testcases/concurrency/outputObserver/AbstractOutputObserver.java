package gradingTools.shared.testcases.concurrency.outputObserver;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grader.basics.concurrency.propertyChanges.BasicConcurrentPropertyChangeSupport;
import grader.basics.concurrency.propertyChanges.ConcurrentPropertyChange;
import grader.basics.concurrency.propertyChanges.ConcurrentPropertyChangeSupport;
import grader.basics.concurrency.propertyChanges.Selector;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.NotRunnableException;
import grader.basics.execution.ResultingOutErr;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.output.observer.BasicNegativeOutputSelector;
import grader.basics.output.observer.BasicPositiveOutputSelector;
import grader.basics.output.observer.ObservablePrintStream;
import grader.basics.output.observer.ObservablePrintStreamFactory;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.shared.testcases.TaggedOrNamedClassTest;
import gradingTools.shared.testcases.concurrency.timing.AbstractConcurrencyPerformanceChecker;
import junit.framework.TestResult;
import net.sf.saxon.pattern.CombinedNodeTest;
//public abstract class AbstractOutputObserver extends TaggedOrNamedClassTest {

public abstract class AbstractOutputObserver extends AbstractConcurrencyPerformanceChecker {
	public static final String THREAD_COUNT = "threadCount";
	public static final String COMBINED_OUTPUT = "combinedOutput";
	public static final String COMBINED_EVENTS = "combinedEvents";
//	protected Map<String, TestCaseResult> nameToResult = new HashMap();

//	private Thread rootThread;
//	public Thread getRootThread() {
//		return rootThread;
//	}
//	private ConcurrentPropertyChangeSupport concurrentPropertyChangeSupport;
//	private ResultingOutErr resultingOutErr;
//	public static final int CONCURRENT_PROGRAM_MAX_TIME = 1000;
	public AbstractOutputObserver() {
		
	}
//	protected Selector positiveSelector() {
//		return new BasicPositiveOutputSelector();
//	}
//	protected Selector negativeSelector() {
//		return new BasicNegativeOutputSelector();
//	}
//	protected Selector<ConcurrentPropertyChangeSupport> waitSelector() {
//		return null;
//	}
//	protected ResultingOutErr getResultingOutErr() {
//		return resultingOutErr;
//	}
//	protected ConcurrentPropertyChangeSupport getConcurrentPropertyChangeSupport() {
//		return concurrentPropertyChangeSupport;
//	}
//	protected  Class mainClass() throws ClassNotFoundException {
//		return Class.forName("Main");
//	}
//	static String[] emptyArray = {};
//	protected String[] args() {
//		return emptyArray;
//	}
//	protected String[] inputs() {
//		return emptyArray;
//	}
//
//	protected int timeOut() {
//		return BasicProjectExecution.getMethodTimeOut();
//	}
//	protected static PrintStream originalOut = System.out;
//    protected ObservablePrintStream redirectOutput() {    	
//    	ObservablePrintStream aRedirectedStream = ObservablePrintStreamFactory.getObservablePrintStream();
////		aRedirectedStream.addPropertyChangeListener(new BasicPrintStreamListener());
//		aRedirectedStream.addPositiveSelector(positiveSelector());
//		aRedirectedStream.addNegativeSelector(negativeSelector());
//		aRedirectedStream.setRedirectionFrozen(false);
//
//		System.setOut((PrintStream) aRedirectedStream);
//		return aRedirectedStream;
//    }
//	public Map<String, TestCaseResult> getNameToResult() {
//		return nameToResult;
//	}
//	public boolean isPassed(String aTestName) {
//		TestCaseResult aResult = nameToResult.get(aTestName);
//		return (aResult != null) && (aResult.isPass());
//	}
//	public TestCaseResult combineNormalizedResults(String[] aTestNames) {
//		return combineResults(toTestResults(aTestNames));
//	}
//	
//	public TestCaseResult[] toTestResults(String[] aTestNames) {
//		TestCaseResult[] aModifiedResults = new TestCaseResult[aTestNames.length];
//		for (int anIndex = 0; anIndex < aTestNames.length; anIndex++) {
//				String aTestName = aTestNames[anIndex];
//				TestCaseResult anOriginalResult = nameToResult.get(aTestName);
//				if (anOriginalResult == null) {
//					System.err.println("Missing result for:" + aTestName);
//					continue;
//				}
//				TestCaseResult aModifiedResult = anOriginalResult.clone();
//				double aNewPercentage = anOriginalResult.isFail()?0:1.0/aTestNames.length;
//				aModifiedResult.setPercentage(aNewPercentage);
//				aModifiedResults[anIndex] = aModifiedResult;
//		}
//		return aModifiedResults;
//	}
	
	protected void receivePropertyChanges() {
    	super.receivePropertyChanges();
    	observablePrintStream.addPropertyChangeListener(getConcurrentPropertyChangeSupport());
		Selector<ConcurrentPropertyChangeSupport> aWaitSelector = waitSelector();
		if (aWaitSelector != null) {
	    	getConcurrentPropertyChangeSupport().addtWaitSelector(aWaitSelector);
		}    	
    }
//    protected void receivePropertyChanges() {
//    	concurrentPropertyChangeSupport = new BasicConcurrentPropertyChangeSupport();
//    	observablePrintStream.addPropertyChangeListener(concurrentPropertyChangeSupport);
//		Selector<ConcurrentPropertyChangeSupport> aWaitSelector = waitSelector();
//		if (aWaitSelector != null) {
//	    	concurrentPropertyChangeSupport.addtWaitSelector(aWaitSelector);
//		}    	
//    }
    
//    protected void restoreOutput() {
//    	System.setOut(originalOut);
////    	observablePrintStream.removePropertyChangeListener(concurrentPropertyChangeSupport);
////    	observablePrintStream.setRedirectionFrozen(true);
//    }
//    protected void doNotReceiveEvents() {
//    	observablePrintStream.removePropertyChangeListener(concurrentPropertyChangeSupport);
//    	observablePrintStream.setRedirectionFrozen(true);
//    }
    protected  TestCaseResult checkOutput(ResultingOutErr anOutput) {
    	return pass();
    }
    protected  TestCaseResult checkEvents(ConcurrentPropertyChange[] anEvents) {
    	return pass();
    }
//    protected void waitForTermination() {
//    	Selector<ConcurrentPropertyChangeSupport> aWaitSelector = waitSelector();
//    	long aTimeOut = timeOut();
//		if (aWaitSelector != null) {
//	    	concurrentPropertyChangeSupport.selectorBasedWait(aTimeOut);
//		} else {
//			concurrentPropertyChangeSupport.timeOutBasedWait(aTimeOut);
//		}
//    }
//    private ObservablePrintStream observablePrintStream;
//	public ObservablePrintStream getObservablePrintStream() {
//		return observablePrintStream;
//	}
//	protected void invokeMainMethod(Class aMainClass, String[] anArgs, String[] anInputs) throws Throwable {
//		int aPreviousTimeout = BasicProjectExecution.getMethodTimeOut();
//		BasicProjectExecution.setMethodTimeOut(mainTimeOut());
//		resultingOutErr = BasicProjectExecution.invokeMain(aMainClass, anArgs, anInputs);
//		BasicProjectExecution.setMethodTimeOut(aPreviousTimeout);
//
//	}
//	protected int mainTimeOut() {
//		return BasicProjectExecution.getMethodTimeOut();
//	}
    abstract protected int numExpectedForkedThreads();
    
    protected abstract double threadCountCredit () ;
	protected int numOutputtingForkedThreads;

	protected TestCaseResult checkNumThreads (int aNumThreadsCreated) {
		if  (aNumThreadsCreated == numExpectedForkedThreads()) {
			return partialPass(threadCountCredit(), "Number of forked threads correct");
		}
		return fail("Num threads created " + aNumThreadsCreated + " != num expected threads " + numExpectedForkedThreads());
	}
	abstract protected boolean sufficientOutputCredit(TestCaseResult aResult) ;
	
	
//	protected boolean printPreAnnouncement() {
//		return true;
//	}
	

//	List<String> preAnnouncements = new ArrayList();
//	List<String> postAnnouncemets = new ArrayList();

	protected TestCaseResult runAndCheck(Class aMainClass, String[] anArgs, String[] anInputs) throws Throwable {		
		String aPreAnnouncement = ">>Test about to invoke " + aMainClass.getName() + " with arguments " + Arrays.toString(anArgs) + "<<";
		System.out.println(aPreAnnouncement);
		addPreAnnouncement(aPreAnnouncement);

//		System.out.println(">>Test about to invoke " + aMainClass.getName() + " with arguments " + Arrays.toString(anArgs) + "<<");

		observablePrintStream = redirectOutput();
		receivePropertyChanges();
		BasicProjectExecution.setMethodTimeOut(timeOut());
		invokeMainMethod(aMainClass, anArgs, anInputs);
		waitForTermination();
		restoreOutput();
		doNotReceiveEvents();
//		System.out.println("TEST RESULTS");

		Thread[] aThreads = getConcurrentPropertyChangeSupport().getNotifyingThreads();
		List<Thread> aThreadList = Arrays.asList(aThreads);
		Thread aRootThread = getRootThread();
		if (aThreadList.contains(aRootThread)) {
			numOutputtingForkedThreads = aThreadList.size() - 1;
		} else {
			numOutputtingForkedThreads = aThreadList.size(); 
		}
		// moved code from below
		TestCaseResult aNumThreadsCheck = checkNumThreads(
				numOutputtingForkedThreads);
		nameToResult.put(THREAD_COUNT, aNumThreadsCheck);
//		if (aNumThreadsCheck.isFail() && numOutputtingForkedThreads == 0) {
//			return aNumThreadsCheck;
//		}
		
		
//		if (aThreadList.contains())
		
//		numOutputtingForkedThreads = getConcurrentPropertyChangeSupport().getNotifyingThreads().length - 1;
//		System.out.println("Checking the form of output");
		ResultingOutErr aResultingOutErr = getResultingOutErr();
		if (aResultingOutErr == null) {
			return fail ("Failed to get output");
		}

		TestCaseResult aRetValOut = checkOutput(getResultingOutErr());
		
		TestCaseResult aFinalRetValOut;
		
//		if (!sufficientOutputCredit(aRetValOut) && aRetValOut != PassFailJUnitTestCase.NO_OP_RESULT) {
//  			TestCaseResult badOutput = fail ("Test semantics will not be checked until output form fixed");
//			return combineResults(badOutput, aRetValOut);
//		}
		if (!sufficientOutputCredit(aRetValOut) && aRetValOut != PassFailJUnitTestCase.NO_OP_RESULT) {
  			TestCaseResult badOutput = fail ("Test semantics will not be reliable until output form fixed");
  			aFinalRetValOut = combineResults(aRetValOut, badOutput);
//  			return combineResults(badOutput, aRetValOut);
		} else {
			aFinalRetValOut = aRetValOut;
		}
		nameToResult.put(COMBINED_OUTPUT, aFinalRetValOut);
//		System.out.println("Checking output event semantics");

//		 getConcurrentPropertyChangeSupport().getConcurrentPropertyChanges();
//		numOutputtingForkedThreads = getConcurrentPropertyChangeSupport().getNotifyingThreads().length - 1;

//		TestCaseResult aNumThreadsCheck = checkNumThreads(
//				numOutputtingForkedThreads);
		TestCaseResult aRetValEvents = checkEvents(
				getConcurrentPropertyChangeSupport().getConcurrentPropertyChanges());
		nameToResult.put(COMBINED_EVENTS, aRetValEvents);
		return combineResults(aFinalRetValOut, aNumThreadsCheck, aRetValEvents);

//		if (aRetValOut.isPass() && aRetValEvents.isPass()) {
//			return pass();
//		}
//		return partialPass(
//				aRetValOut.getPercentage() + aRetValEvents.getPercentage(),
//				aRetValOut.getNotes() + "\n" + aRetValEvents.getNotes());
//		if (aRetValOut.isFail() && aRetValEvents.isFail()) {
//			return fail(aRetValOut.getNotes() + " " + aRetValEvents.getNotes());
//		}
//		if (!aRetValEvents.isPass()) {
//			return partialPass(0.2, aRetValEvents.getNotes());
//		}
//		return partialPass(0.8, aRetValOut.getNotes());
//		return checkEvents(aNumThreads);

	}
	
//	@Override
//	public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException,
//			NotGradableException {
//		try {
//			Class aMainClass = mainClass(project);
//			TestCaseResult retVal = 
//			runAndCheck(aMainClass, args(), inputs());
//			return retVal;			
//
//		} catch (NotRunnableException e) {
//			e.printStackTrace();
//			throw new NotGradableException();
//		} catch (Throwable e) {
//			e.printStackTrace();
//			throw new NotGradableException();
//		}
//	}
//	protected Class mainClass(Project aProject) {
//		return findClassByName(aProject, mainClassIdentifier());
//	}
}
