package gradingTools.shared.testcases.concurrency.timing;


import java.io.PrintStream;
import java.util.Arrays;

import grader.basics.concurrency.propertyChanges.BasicConcurrentPropertyChangeSupport;
import grader.basics.concurrency.propertyChanges.ConcurrentPropertyChangeSupport;
import grader.basics.concurrency.propertyChanges.Selector;
import grader.basics.config.BasicExecutionSpecificationSelector;
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
import util.annotations.MaxValue;

@MaxValue(2)
public abstract class AbstractConcurrencyPerformanceChecker extends TaggedOrNamedClassTest {
	private ResultingOutErr resultingOutErr;
	protected static final int CONCURRENT_PROGRAM_MAX_TIME = 1000;
	private ConcurrentPropertyChangeSupport concurrentPropertyChangeSupport;
	
//	protected int minThreads;
//	protected int maxThreads;
	private int numRounds;
	protected String[] minThreadArgs;
	protected String[] maxThreadArgs;
	protected String minThreadArgsString;
	protected String maxThreadArgsString;
	protected long totalMinThreadTime = 0;
	protected long totalMaxThreadTime = 0;
	

	public AbstractConcurrencyPerformanceChecker() {
		numRounds = numRounds();
//		minThreads = minThreads();
//		maxThreads = maxThreads();
		minThreadArgs = lowThreadArgs();
		maxThreadArgs = highThreadArgs();
		minThreadArgsString = Arrays.toString(minThreadArgs);
		maxThreadArgsString = Arrays.toString(maxThreadArgs);
//		minThreadTimes = new long[numRounds];
//		maxThreadTimes = new long[numRounds];
	}
	private Thread rootThread;
	public Thread getRootThread() {
		return rootThread;
	}
	protected boolean isRootThread(Thread aThread) {
		return aThread == rootThread;
	}
	
	protected ResultingOutErr getResultingOutErr() {
		return resultingOutErr;
	}
	static String[] emptyArray = {};
	protected String[] args() {
		return emptyArray;
	}
	protected String[] inputs() {
		return emptyArray;
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
	
	protected void hideOutput() {
	 	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setHideRedirectedOuput(true);
	}
	
	protected void showOutput() {
	 	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setHideRedirectedOuput(false);
	}

	protected int timeOut() {
		return BasicProjectExecution.getMethodTimeOut();
	}
	private static PrintStream originalOut = System.out;
	 protected void restoreOutput() {
	    	System.setOut(originalOut);
//	    	observablePrintStream.removePropertyChangeListener(concurrentPropertyChangeSupport);
//	    	observablePrintStream.setRedirectionFrozen(true);
	    }
    protected ObservablePrintStream redirectOutput() {    	
    	ObservablePrintStream aRedirectedStream = ObservablePrintStreamFactory.getObservablePrintStream();
//		aRedirectedStream.addPropertyChangeListener(new BasicPrintStreamListener());
		aRedirectedStream.addPositiveSelector(positiveSelector());
		aRedirectedStream.addNegativeSelector(negativeSelector());
		aRedirectedStream.setRedirectionFrozen(false);

		System.setOut((PrintStream) aRedirectedStream);
		return aRedirectedStream;
    }
	protected ConcurrentPropertyChangeSupport getConcurrentPropertyChangeSupport() {
		return concurrentPropertyChangeSupport;
	}
	protected void invokeMainMethod(Class aMainClass, String[] anArgs, String[] anInputs) throws Throwable {
		int aPreviousTimeout = BasicProjectExecution.getMethodTimeOut();
		BasicProjectExecution.setMethodTimeOut(mainTimeOut());
		resultingOutErr = BasicProjectExecution.invokeMain(aMainClass, anArgs, anInputs);
		BasicProjectExecution.setMethodTimeOut(aPreviousTimeout);
		rootThread = BasicProjectExecution.getLastMainMethodThread();


	}
	protected int mainTimeOut() {
		return BasicProjectExecution.getMethodTimeOut();
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
	 protected Class mainClass(Project aProject) {
			return findClassByName(aProject, mainClassIdentifier());
		}
	 protected void waitingInvoke(Class aMainClass, String[] anArgs, String[] anInputs) {
		 try {
			hideOutput();
			invokeMainMethod(aMainClass, anArgs, anInputs);
			waitForTermination();
			showOutput();		
		

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	 }
	 protected TestCaseResult runAndCheck(Class aMainClass, String[] anArgs, String[] anInputs) throws Throwable {		
			int aNumProcessors = Runtime.getRuntime().availableProcessors();
			System.out.println("Number of available processors:" + aNumProcessors);
			if (aNumProcessors == 1) {
				System.out.println("A single processor available - performance should degrade with more threads");
			}
//		 	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setHideRedirectedOuput(true);

		 	observablePrintStream = redirectOutput();
			receivePropertyChanges();
			BasicProjectExecution.setMethodTimeOut(timeOut());
			for (int aRound = 0; aRound < numRounds; aRound++) {
				System.out.println("Timing Round:" + aRound);
				System.out.println("Running with args:" + minThreadArgsString);

				long aStartTime = System.currentTimeMillis();
				waitingInvoke(aMainClass, minThreadArgs, anInputs);

				long aMinThreadTime = System.currentTimeMillis() - aStartTime;
				System.out.println("Time taken:" + aMinThreadTime);
				System.out.println("Running with args:" + maxThreadArgsString);
				totalMinThreadTime += aMinThreadTime;
				aStartTime = System.currentTimeMillis();
				waitingInvoke(aMainClass, maxThreadArgs, anInputs);
				long aMaxThreadTime = System.currentTimeMillis() - aStartTime;
				System.out.println("Time taken:" + aMaxThreadTime);

				totalMaxThreadTime += aMaxThreadTime;
				
			}
			
			restoreOutput();
		 	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setHideRedirectedOuput(false);

//			doNotReceiveEvents();
			
//			 getConcurrentPropertyChangeSupport().getConcurrentPropertyChanges();
//			numOutputtingForkedThreads = getConcurrentPropertyChangeSupport().getNotifyingThreads().length - 1;

			
			return performanceCredit(totalMinThreadTime, totalMaxThreadTime, aNumProcessors);



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
	 protected ObservablePrintStream observablePrintStream;
	 protected void receivePropertyChanges() {
	    	concurrentPropertyChangeSupport = new BasicConcurrentPropertyChangeSupport();
//	    	observablePrintStream.addPropertyChangeListener(concurrentPropertyChangeSupport);
//			Selector<ConcurrentPropertyChangeSupport> aWaitSelector = waitSelector();
//			if (aWaitSelector != null) {
//		    	concurrentPropertyChangeSupport.addtWaitSelector(aWaitSelector);
//			}    	
	    }
		public ObservablePrintStream getObservablePrintStream() {
			return observablePrintStream;
		}
		protected void doNotReceiveEvents() {
	    	observablePrintStream.removePropertyChangeListener(concurrentPropertyChangeSupport);
	    	observablePrintStream.setRedirectionFrozen(true);
	    }
		
		protected int minThreads() {
			return 1;
		}
		protected int maxThreads() {
			return 4;
		}
		
		protected int numRounds() {
			return 10;
		}
		protected abstract TestCaseResult performanceCredit(long aLowThreadsTime, long aHighThreadsTime, int aNumProcessors);
//		protected double minSingleProcessorRatio( ) {
//			return 0.9;
//		}
//		protected double minMultipleProcessorRatio( ) {
//			return 1.3;
//		}
//		@Override
//		protected String[] args() {
//			return maxThreadArgs();
//		}

		protected abstract String[] lowThreadArgs() ;
		protected abstract String[] highThreadArgs() ;

}
