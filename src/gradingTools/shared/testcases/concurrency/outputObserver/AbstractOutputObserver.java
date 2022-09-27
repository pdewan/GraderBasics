package gradingTools.shared.testcases.concurrency.outputObserver;

import java.io.PrintStream;

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
import gradingTools.shared.testcases.TaggedOrNamedClassTest;
public abstract class AbstractOutputObserver extends TaggedOrNamedClassTest {
	private ConcurrentPropertyChangeSupport concurrentPropertyChangeSupport;
	private ResultingOutErr resultingOutErr;
	public static final int CONCURRENT_PROGRAM_MAX_TIME = 1000;
	public AbstractOutputObserver() {
		
	}
	protected Selector positiveSelector() {
		return new BasicPositiveOutputSelector();
	}
	protected Selector negativeSelector() {
		return new BasicNegativeOutputSelector();
	}
	protected Selector<ConcurrentPropertyChangeSupport> waitSelector() {
		return null;
	}
	protected ResultingOutErr getResultingOutErr() {
		return resultingOutErr;
	}
	protected ConcurrentPropertyChangeSupport getConcurrentPropertyChangeSupport() {
		return concurrentPropertyChangeSupport;
	}
//	protected  Class mainClass() throws ClassNotFoundException {
//		return Class.forName("Main");
//	}
	static String[] emptyArray = {};
	protected String[] args() {
		return emptyArray;
	}
	protected String[] inputs() {
		return emptyArray;
	}

	protected int timeOut() {
		return BasicProjectExecution.getMethodTimeOut();
	}
	protected static PrintStream originalOut = System.out;
    protected ObservablePrintStream redirectOutput() {    	
    	ObservablePrintStream aRedirectedStream = ObservablePrintStreamFactory.getObservablePrintStream();
//		aRedirectedStream.addPropertyChangeListener(new BasicPrintStreamListener());
		aRedirectedStream.addPositiveSelector(positiveSelector());
		aRedirectedStream.addNegativeSelector(negativeSelector());
		aRedirectedStream.setRedirectionFrozen(false);

		System.setOut((PrintStream) aRedirectedStream);
		return aRedirectedStream;
    }
    protected void receivePropertyChanges() {
    	concurrentPropertyChangeSupport = new BasicConcurrentPropertyChangeSupport();
    	observablePrintStream.addPropertyChangeListener(concurrentPropertyChangeSupport);
		Selector<ConcurrentPropertyChangeSupport> aWaitSelector = waitSelector();
		if (aWaitSelector != null) {
	    	concurrentPropertyChangeSupport.addtWaitSelector(aWaitSelector);
		}    	
    }
    
    protected void restoreOutput() {
    	System.setOut(originalOut);
//    	observablePrintStream.removePropertyChangeListener(concurrentPropertyChangeSupport);
//    	observablePrintStream.setRedirectionFrozen(true);
    }
    protected void doNotReceiveEvents() {
    	observablePrintStream.removePropertyChangeListener(concurrentPropertyChangeSupport);
    	observablePrintStream.setRedirectionFrozen(true);
    }
    protected  TestCaseResult checkOutput(ResultingOutErr anOutput) {
    	return pass();
    }
    protected  TestCaseResult checkEvents(ConcurrentPropertyChange[] anEvents) {
    	return pass();
    }
    protected void waitForTermination() {
    	Selector<ConcurrentPropertyChangeSupport> aWaitSelector = waitSelector();
    	long aTimeOut = timeOut();
		if (aWaitSelector != null) {
	    	concurrentPropertyChangeSupport.selectorBasedWait(aTimeOut);
		} else {
			concurrentPropertyChangeSupport.timeOutBasedWait(aTimeOut);
		}
    }
    protected ObservablePrintStream observablePrintStream;
	public ObservablePrintStream getObservablePrintStream() {
		return observablePrintStream;
	}
	protected void invokeMainMethod(Class aMainClass, String[] anArgs, String[] anInputs) throws Throwable {
		int aPreviousTimeout = BasicProjectExecution.getMethodTimeOut();
		BasicProjectExecution.setMethodTimeOut(mainTimeOut());
		resultingOutErr = BasicProjectExecution.invokeMain(aMainClass, anArgs, anInputs);
		BasicProjectExecution.setMethodTimeOut(aPreviousTimeout);

	}
	protected int mainTimeOut() {
		return BasicProjectExecution.getMethodTimeOut();
	}
    abstract protected int numExpectedForkedThreads();
    
    protected abstract double threadCountCredit () ;
	protected int numOutputtingForkedThreads;

	protected TestCaseResult checkNumThreads (int aNumThreadsCreated) {
		if  (aNumThreadsCreated == numExpectedForkedThreads()) {
			return partialPass(threadCountCredit(), "Number of forked threads corrrect");
		}
		return fail("Num threads created " + aNumThreadsCreated + " != num expected threads " + numExpectedForkedThreads());
	}
	abstract protected boolean sufficientOutputCredit(TestCaseResult aResult) ;
	protected TestCaseResult runAndCheck(Class aMainClass, String[] anArgs, String[] anInputs) throws Throwable {		
		observablePrintStream = redirectOutput();
		receivePropertyChanges();
		BasicProjectExecution.setMethodTimeOut(timeOut());
		invokeMainMethod(aMainClass, anArgs, anInputs);
		waitForTermination();
		restoreOutput();
		doNotReceiveEvents();
		numOutputtingForkedThreads = getConcurrentPropertyChangeSupport().getNotifyingThreads().length - 1;

		TestCaseResult aRetValOut = checkOutput(getResultingOutErr());
		
		if (!sufficientOutputCredit(aRetValOut)) {
  			TestCaseResult badOutput = fail ("Event tests will not be run until output fixed");
			return combineResults(badOutput, aRetValOut);
		}
//		 getConcurrentPropertyChangeSupport().getConcurrentPropertyChanges();
//		numOutputtingForkedThreads = getConcurrentPropertyChangeSupport().getNotifyingThreads().length - 1;

		TestCaseResult aNumThreadsCheck = checkNumThreads(
				numOutputtingForkedThreads);
		TestCaseResult aRetValEvents = checkEvents(
				getConcurrentPropertyChangeSupport().getConcurrentPropertyChanges());
		return combineResults(aRetValOut, aNumThreadsCheck, aRetValEvents);

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
	
	@Override
	public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException,
			NotGradableException {
		try {
			Class aMainClass = mainClass(project);
			TestCaseResult retVal = 
			runAndCheck(aMainClass, args(), inputs());
			return retVal;			

		} catch (NotRunnableException e) {
			e.printStackTrace();
			throw new NotGradableException();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new NotGradableException();
		}
	}
	protected Class mainClass(Project aProject) {
		return findClassByName(aProject, mainClassIdentifier());
	}
}
