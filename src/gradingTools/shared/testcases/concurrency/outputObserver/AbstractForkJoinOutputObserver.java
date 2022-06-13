package gradingTools.shared.testcases.concurrency.outputObserver;

import java.beans.PropertyChangeEvent;
import java.io.PrintStream;
import java.util.ArrayList;
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
import gradingTools.shared.testcases.concurrency.propertyChanges.AbstractConcurrentEventSupport;
import gradingTools.shared.testcases.concurrency.propertyChanges.BasicConcurrentPropertyChangeSupport;
import gradingTools.shared.testcases.concurrency.propertyChanges.ConcurrentEvent;
import gradingTools.shared.testcases.concurrency.propertyChanges.ConcurrentEventUtility;
import gradingTools.shared.testcases.concurrency.propertyChanges.ConcurrentPropertyChange;
import gradingTools.shared.testcases.concurrency.propertyChanges.ConcurrentPropertyChangeSupport;
import gradingTools.shared.testcases.greeting.AGreetingChecker;
import gradingTools.shared.testcases.greeting.GreetingMainProvided;
import gradingTools.shared.testcases.utils.ALinesMatcher;
import gradingTools.shared.testcases.utils.LinesMatchKind;
import gradingTools.shared.testcases.utils.LinesMatcher;
import gradingTools.utils.RunningProjectUtils;
import util.annotations.MaxValue;
import util.models.PropertyListenerRegisterer;

@MaxValue(2)
public abstract class AbstractForkJoinOutputObserver extends AbstractOutputObserver {
//	public static final int TIME_OUT_SECS = 1; // secs

	private int forkLineNumber = 0;
	private int joinLineNumber = 0;
	private ConcurrentPropertyChange[] propertyChanges;
	private Map<Thread, ConcurrentPropertyChange[]> threadToPropertyChanges;
	private Map<Thread, String[]> threadToStrings;
	private Thread rootThread;
	private int numOutputtingThreads;

//	private LinesMatcher linesMatcher;

	public Thread getRootThread() {
		return rootThread;
	}

	public AbstractForkJoinOutputObserver() {
//		rootThread = Thread.currentThread();
	}
    abstract protected int numExpectedForkedThreads();
	abstract protected SubstringSequenceChecker preForkChecker();

	protected abstract SubstringSequenceChecker postForkChecker();

	protected abstract SubstringSequenceChecker postJoinChecker();

	protected abstract SubstringSequenceChecker forkedThreadChecker();

	protected abstract SubstringSequenceChecker rootThreadChecker();

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
		return threadToPropertyChanges;
	}

	public Map<Thread, String[]> getThreadToStrings() {
		return threadToStrings;
	}

	protected TestCaseResult preForkFailTestResult(String anExpectedLines) {
		return fail("Prefork output did not match:" + anExpectedLines);
	}

	public int getNumOutputtingThreads() {
		return numOutputtingThreads;
	}

	protected abstract double preForkPartialCredit();

	protected abstract double postForkPartialCredit();

	protected abstract double postJoinPartialCredit();

	protected abstract double forkedThreadPartialCredit();

	protected abstract double rootThreadPartialCredit();

	protected TestCaseResult postForkFailTestResult(String anExpectedLines) {
		return partialPass(preForkPartialCredit(), "Post Fork output did not match:" + anExpectedLines);
	}

	protected TestCaseResult postJoinTestResult(String anExpectedLines) {
		return partialPass(postForkPartialCredit(), "Post Join output did not match:" + anExpectedLines);
	}

	protected LinesMatcher getLinesMatcher() {
		return getResultingOutErr().getLinesMatcher();
	}

	protected TestCaseResult checkPreForkOutput() {
		LinesMatcher aLinesMatcher = getLinesMatcher();
		boolean aPreForkRetVal = true;
		SubstringSequenceChecker aPreForkChecker = preForkChecker();
		if (aPreForkChecker != null) {
			aPreForkRetVal = aPreForkChecker.check(aLinesMatcher, LinesMatchKind.ONE_TIME_LINE, Pattern.DOTALL);

			if (!aPreForkRetVal) {
				String anExpectedLines = Arrays.toString(aPreForkChecker.getSubstrings());
				return fail("Pre fork output did not match:" + anExpectedLines);
			}
		}
		return partialPass(preForkPartialCredit(), "Pre fork output correct");
	}

	protected TestCaseResult checkPostForkOutput() {
		LinesMatcher aLinesMatcher = getLinesMatcher();
		forkLineNumber = aLinesMatcher.getMaxMatchedLineNumber();
		aLinesMatcher.setStartLineNumber(forkLineNumber);
		boolean aPostForkRetVal = true;
		SubstringSequenceChecker aPostForkChecker = postForkChecker();
		if (aPostForkChecker != null) {
			aPostForkRetVal = aPostForkChecker.check(aLinesMatcher, LinesMatchKind.ONE_TIME_UNORDERED, Pattern.DOTALL);

			if (!aPostForkRetVal) {
				String anExpectedLines = Arrays.toString(aPostForkChecker.getSubstrings());
				return fail("Post fork output did not match:" + anExpectedLines);
			}
		}
		return partialPass(postForkPartialCredit(), "Post fork output correct");
	}

	protected TestCaseResult checkPostJoinOutput() {
		LinesMatcher aLinesMatcher = getLinesMatcher();
		joinLineNumber = aLinesMatcher.getMaxMatchedLineNumber();
		aLinesMatcher.setStartLineNumber(joinLineNumber);

		boolean aPostJoinRetVal = true;
		SubstringSequenceChecker aPostJoinChecker = postJoinChecker();
		if (aPostJoinChecker != null) {
			aPostJoinRetVal = aPostJoinChecker.check(aLinesMatcher, LinesMatchKind.ONE_TIME_LINE, Pattern.DOTALL);

			if (!aPostJoinRetVal) {
				String anExpectedLines = Arrays.toString(aPostJoinChecker.getSubstrings());

				return fail("Post join output did not match:" + anExpectedLines);
			}
		}
		return partialPass(postJoinPartialCredit(), "Post join output correct");
	}

	protected TestCaseResult checkRootThreadOutput(String[] aRootThreadOutput) {
		SubstringSequenceChecker aRootThreadChecker = rootThreadChecker();
		if (aRootThreadChecker == null) {
			return pass();
		}
		if (aRootThreadOutput == null) {
			return fail("No root thread output");
		}
		LinesMatcher aLinesMatcher = new ALinesMatcher(aRootThreadOutput);
		boolean retVal = aRootThreadChecker.check(aLinesMatcher, LinesMatchKind.ONE_TIME_LINE, Pattern.DOTALL);

		if (!retVal) {
			String anExpectedLines = Arrays.toString(aRootThreadChecker.getSubstrings());

			return fail("Root thread output did not match:" + anExpectedLines);
		}

		return partialPass(rootThreadPartialCredit(), "Root thread output correct");
	}

	protected TestCaseResult checkForkedThreadOutput(Thread aThread, String[] aForkedThreadOutput) {
		SubstringSequenceChecker aForkedThreadChecker = forkedThreadChecker();
		if (aForkedThreadChecker == null) {
			return pass();
		}
		if (aForkedThreadChecker == null) {
			return fail("No root thread output");
		}
		LinesMatcher aLinesMatcher = new ALinesMatcher(aForkedThreadOutput);
		boolean retVal = aForkedThreadChecker.check(aLinesMatcher, LinesMatchKind.ONE_TIME_LINE, Pattern.DOTALL);

		if (!retVal) {
			String anExpectedLines = Arrays.toString(aForkedThreadChecker.getSubstrings());

			return fail("Forked thread " + aThread + " output did not match:" + anExpectedLines);
		}

		return partialPass(forkedThreadPartialCredit(), "Forked thread " + aThread + " output correct");
	}

	protected TestCaseResult checkOutput() {
		TestCaseResult aPreForkResult = checkPreForkOutput();
		TestCaseResult aPostForkResult = checkPostForkOutput();
		TestCaseResult aPostJoinResult = checkPostJoinOutput();
		double aTotalCredit = aPreForkResult.getPercentage() + aPostForkResult.getPercentage()
				+ aPostJoinResult.getPercentage();
		if (aTotalCredit == 1.0) {
			return pass();
		}

		String aTotalNotes = aPreForkResult.getNotes() + "\n" + aPostForkResult.getNotes() + "\n"
				+ aPostJoinResult.getNotes();
		return partialPass(aTotalCredit, aTotalNotes);

	}

	protected TestCaseResult combineResults(TestCaseResult aRootTestCaseResult, List<TestCaseResult> aForkedThreadResults) {
		int aNumForkedTHreads = aForkedThreadResults.size();
		
		double aPercentage = aRootTestCaseResult.getPercentage();
		StringBuffer aMessage = new StringBuffer(aRootTestCaseResult.getNotes());
		boolean anAllPassed = aRootTestCaseResult.isPass();
		for (TestCaseResult aTestCaseResult : aForkedThreadResults) {
			if (!aTestCaseResult.isPass() && anAllPassed) {
				anAllPassed = false;
			}
			aPercentage += aTestCaseResult.getPercentage()/numOutputtingForkedThreads;
			aMessage.append("\n" + aTestCaseResult.getNotes());
		}
		if (anAllPassed) {
			return pass();
		}
		return partialPass(aPercentage, aMessage.toString());
	}
	protected int numOutputtingForkedThreads;
	@Override
	 protected  TestCaseResult checkEvents() {
		propertyChanges = getConcurrentPropertyChangeSupport().getConcurrentPropertyChanges();
		threadToPropertyChanges = ConcurrentEventUtility.getConcurrentPropertyChangesByThread(propertyChanges);
		threadToStrings = ConcurrentEventUtility.toNewValueStrings(threadToPropertyChanges);
		numOutputtingThreads = threadToPropertyChanges.size();
		
		TestCaseResult aRootThreadResult = null;
		if (rootThreadChecker() == null) {
			aRootThreadResult = pass();
		} else {
			aRootThreadResult = fail ("No root thread output");
		}
		List<TestCaseResult> aForkedThreadResults = new ArrayList();
		for (Thread aThread : threadToStrings.keySet()) {
			String[] aStrings = threadToStrings.get(aThread);
			if (aThread == rootThread) {
				aRootThreadResult = checkRootThreadOutput(aStrings);
			} else {
				aForkedThreadResults.add(checkForkedThreadOutput(aThread, aStrings));
			}
		}
		numOutputtingForkedThreads = aForkedThreadResults.size();
		int anExpectedForkedThreads = numExpectedForkedThreads();
		if (forkedThreadChecker() != null && numOutputtingForkedThreads != anExpectedForkedThreads) {
			return partialPass(
					aRootThreadResult.getPercentage(),
					aRootThreadResult.getNotes() + "\n" +
					"# expected forked threads = " + anExpectedForkedThreads + " but # outputting forked threads = " + numOutputtingForkedThreads);
			
		}
		return combineResults(aRootThreadResult, aForkedThreadResults);
	}

}
