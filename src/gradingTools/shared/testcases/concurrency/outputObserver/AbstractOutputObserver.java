package gradingTools.shared.testcases.concurrency.outputObserver;

import java.beans.PropertyChangeEvent;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.NotRunnableException;
import grader.basics.execution.ResultingOutErr;
import grader.basics.execution.RunningProject;
import grader.basics.junit.JUnitTestsEnvironment;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.output.observer.BasicNegativeOutputSelector;
import grader.basics.output.observer.BasicPositiveOutputSelector;
import grader.basics.output.observer.BasicPrintStreamListener;
import grader.basics.output.observer.ObservablePrintStream;
import grader.basics.output.observer.ObservablePrintStreamFactory;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.shared.testcases.SubstringSequenceChecker;
import gradingTools.shared.testcases.TaggedOrNamedClassTest;
import gradingTools.shared.testcases.concurrency.propertyChanges.AbstractConcurrentEventSupport;
import gradingTools.shared.testcases.concurrency.propertyChanges.BasicConcurrentPropertyChangeSupport;
import gradingTools.shared.testcases.concurrency.propertyChanges.ConcurrentEvent;
import gradingTools.shared.testcases.concurrency.propertyChanges.ConcurrentEventUtility;
import gradingTools.shared.testcases.concurrency.propertyChanges.ConcurrentPropertyChange;
import gradingTools.shared.testcases.concurrency.propertyChanges.ConcurrentPropertyChangeSupport;
import gradingTools.shared.testcases.concurrency.propertyChanges.Selector;
import gradingTools.shared.testcases.greeting.AGreetingChecker;
import gradingTools.shared.testcases.greeting.GreetingMainProvided;
import gradingTools.shared.testcases.utils.ALinesMatcher;
import gradingTools.shared.testcases.utils.LinesMatchKind;
import gradingTools.shared.testcases.utils.LinesMatcher;
import gradingTools.utils.RunningProjectUtils;
import util.annotations.MaxValue;
import util.models.PropertyListenerRegisterer;
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
	protected  Class mainClass() throws ClassNotFoundException {
		return Class.forName("Main");
	}
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
    protected  TestCaseResult checkOutput() {
    	return pass();
    }
    protected  TestCaseResult checkEvents() {
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
		resultingOutErr = BasicProjectExecution.invokeMain(aMainClass, anArgs, anInputs);
		
	}
	protected TestCaseResult runAndCheck(Class aMainClass, String[] anArgs, String[] anInputs) throws Throwable {		
		observablePrintStream = redirectOutput();
		receivePropertyChanges();
		BasicProjectExecution.setMethodTimeOut(timeOut());
		invokeMainMethod(aMainClass, anArgs, anInputs);
		waitForTermination();
		restoreOutput();
		doNotReceiveEvents();
		TestCaseResult aRetValOut = checkOutput();
		TestCaseResult aRetValEvents = checkEvents();
		if (aRetValOut.isPass() && aRetValEvents.isPass()) {
			return pass();
		}
		return partialPass(
				aRetValOut.getPercentage() + aRetValEvents.getPercentage(),
				aRetValOut.getNotes() + "\n" + aRetValEvents.getNotes());
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
			Class aMainClass = mainClass();
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

}
