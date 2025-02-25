package gradingTools.shared.testcases.concurrency.oddNumbers;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.management.RuntimeErrorException;

import gradingTools.shared.testcases.concurrency.outputObserver.AbstractForkJoinChecker;
import util.annotations.MaxValue;

@MaxValue(2)
public abstract class AbstractOddNumbersExecution extends AbstractForkJoinChecker {
	private static final String ROOT_CLASS = "ConcurrentOddNumbers";
	private static final String DISPATCHER_CLASS = "OddNumbersDispatcher";
	private static final String UTIL_CLASS = "OddNumbersUtil";
	private static final String IS_TRACE_FORK_JOIN_METHOD = "isTraceForkJoin";
	private static final String IS_TRACE_FAIR_ALLOCATION_METHOD = "isTraceFairAllocation";
	private static final String IS_TRACE_SYNCHRONIZATION_METHOD = "isTraceSynchronization";
	private static final String IS_TRACE_HANGING_METHOD = "isTraceHanging";
	private static final String SET_TRACE_FORK_JOIN_METHOD = "setTraceForkJoin";
	private static final String SET_TRACE_FAIR_ALLOCATION_METHOD = "setTraceFairAllocation";
	private static final String SET_TRACE_SYNCHRONIZATION_METHOD = "setTraceSynchronization";
	private static final String SET_TRACE_HANGING_METHOD = "setTraceHanging";	
	private static final String IS_DO_TESTS_METHOD = "isDoTests";




//	public static final String WORKER_CLASS = "OddNumbersWorke";
	public static final String WORKER_CLASS = "OddNumbersWorkerCode";
	// The testing code needs to know what main class to call,
	// what arguments to pass to it, and how many forked threads are expected
	
	public static String composNotEnabledMessage(String aTraceName) {
	  return aTraceName + " traces not enabled. Please run the appropriate test program given to you to enable them.";

	}
	
	public static Class getClass(String aClassName) {
		try {
			return Class.forName(aClassName);
		} catch (Exception e){
			return null;
		}
	}
	
	public static Method getBooleanGetter(Class aClass, String aTraceMethodNmae) {
		if (aClass == null) {
			return null;
		}
		try {
			return aClass.getMethod(aTraceMethodNmae);
		} catch (NoSuchMethodException | SecurityException e) {
			return null;
		}
	}
	private static Class[] BOOLEAN_ARGUMENT = {Boolean.TYPE};
	public static Method getBooleanSetter(Class aClass, String aMethodName) {
		if (aClass == null) {
			return null;
		}
		try {
			return aClass.getMethod(aMethodName, BOOLEAN_ARGUMENT);
		} catch (NoSuchMethodException | SecurityException e) {
			return null;
		}
	}
	
	public static Boolean callBooleanGetter(Class aClass, Method aTraceMethod ) {		
		try {
			aTraceMethod.setAccessible(true);
			return (Boolean) aTraceMethod.invoke(aClass);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return null;
		}		
	}
	public static void callBooleanSetter(Class aClass, Method aMethod, boolean newVal ) {		
		try {
			aMethod.setAccessible(true);
		 aMethod.invoke(aClass, newVal);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException("Could not invoke method:" + e.getMessage());
		}		
	}
	
	public static Boolean getBoolean(String aClassName, String aTraceMethodName) {
		Class aUtilClass = getClass(aClassName);
		if (aUtilClass == null) {
			return null;
		}
		Method aTraceMethod = getBooleanGetter(aUtilClass, aTraceMethodName);
		if (aTraceMethod == null) {
			return null;
		}
		Boolean retVal = callBooleanGetter(aUtilClass, aTraceMethod);
		return retVal;		
	}
	public static void setBoolean(String aClassName, String aTraceMethodName, boolean newVal) {
		
		Class aUtilClass = getClass(aClassName);
		if (aUtilClass == null) {
			throw new RuntimeException("Did not find class:" + aClassName);
			
		}
		Method aTraceMethod = getBooleanSetter(aUtilClass, aTraceMethodName);
		if (aTraceMethod == null) {
			throw new RuntimeException("Did not find method:" + aTraceMethodName + " in " + aUtilClass.getSimpleName());

		}
		callBooleanSetter(aUtilClass, aTraceMethod, newVal);
	}

	
	public static Boolean isTraceForkJoin() {
		return getBoolean(UTIL_CLASS, IS_TRACE_FORK_JOIN_METHOD);
	}
	public static Boolean isTraceFairAllocation() {
		return getBoolean(UTIL_CLASS, IS_TRACE_FAIR_ALLOCATION_METHOD);
	}
	public static Boolean isTraceSynchronization() {
		return getBoolean(UTIL_CLASS, IS_TRACE_SYNCHRONIZATION_METHOD);
	}
	public static Boolean isTraceHanging() {
		return getBoolean(UTIL_CLASS, IS_TRACE_HANGING_METHOD);
	}
	public static Boolean isDoTests() {
		return getBoolean(UTIL_CLASS, IS_DO_TESTS_METHOD);
	}
	
	public static void setTraceHanging(boolean newVal) {
		 setBoolean(UTIL_CLASS, SET_TRACE_HANGING_METHOD, newVal);
	}
	public static void setTraceForkJoin(boolean newVal) {
		 setBoolean(UTIL_CLASS, SET_TRACE_FORK_JOIN_METHOD, newVal);
	}
	public static void setTraceFairAllocation(boolean newVal) {
		 setBoolean(UTIL_CLASS, SET_TRACE_FAIR_ALLOCATION_METHOD, newVal);
	}
	public static void setTracSynchronization(boolean newVal) {
		 setBoolean(UTIL_CLASS, SET_TRACE_SYNCHRONIZATION_METHOD, newVal);
	}

	/**
	 * The full name of the main concurrency class
	 */
	@Override
	protected String mainClassIdentifier() {
//		return F23Assignment0_1Suite.ROOT_CLASS;
		
		return ROOT_CLASS;
	}

	protected String[] args = { "1" };

	/**
	 * specifies the arguments to be passed to the concurrency main class
	 */
	@Override
	protected String[] args() {
		args[0] = Integer.toString(totalIterations());
		return args;
	}

	public static final int NUM_FORKED_THREADS = 4;

	/**
	 * Specifies the number of threads to be forked by the root thread Can be
	 * computed based on an argument or user input
	 */
	@Override
	protected int numExpectedForkedThreads() {
		return NUM_FORKED_THREADS;
	};
	// The following are names and types of the properties output during different
	// phases of the fork-join problem.
	// These are part of the testing code specification.
	// Template code can be given to the students to ensure that these requirements
	// are adhered

	// Properties output by root thread before child threads are created
	public static final Object[][] PRE_FORK_PROPERTIES = { { "Random Numbers", Array.class } };
	// Properties output by each forked thread in each iteration
	public static final Object[][] ITERATION_PROPERTIES = { { "Index", Integer.class }, { "Number", Integer.class },
			{ "Is Odd", Boolean.class } };
	// Properties output by each forked thread after its iterations
	// have finished and before it terminates
	public static final Object[][] POST_ITERATION_PROPERTIES = { { "Num Odd Numbers", Integer.class }, };
	// Properties output by root thread after joining all
	// child threads, that is, after all child threads have
	// output their post iteration properties
	public static final Object[][] POST_JOIN_PROPERTIES = { { "Total Num Odd Numbers", Integer.class },
			{ "Odd Numbers", Set.class }, };

	@Override
	protected Object[][] preForkPropertyNamesAndType() {
		return PRE_FORK_PROPERTIES;
	}

	@Override
	protected Object[][] iterationPropertyNamesAndType() {
		return ITERATION_PROPERTIES;
	}

	@Override
	protected Object[][] postIterationPropertyNamesAndType() {
		return POST_ITERATION_PROPERTIES;
	}

	@Override
	protected Object[][] postJoinPropertyNamesAndType() {
		return POST_JOIN_PROPERTIES;
	}

	/**
	 * The above methods make properties output by each thread available to this
	 * testing code. Even though the property outputs are expected to be
	 * interleaved, the testing framework does not interleave the execution of these
	 * methods, allowing the testing code to finish processing all properties output
	 * by a thread, before it processes those output by another thread after a
	 * thread switch.
	 * 
	 * This method is called when a thread switch occurs. The first argument
	 * indicates the thread whose properties were previously made available, and the
	 * second one indicated those whose properties will now be made available.
	 * 
	 * It should be used to reset per thread state.
	 */
	protected void threadEventProcessingSwitched(Thread aPreviousThread, Thread aNewThread) {
//		System.out.println("Thread processing switched from " + aPreviousThread + " to " + aNewThread);
		numNumbersFoundByCurrentThread = 0;
	}

	int numNumbersFoundByCurrentThread;

	// return null in the following message methods if there is no error, otherwise
	// return error message

	/**
	 * Invoked for each property output by the root thread before it forks new
	 * threads.
	 */
	@Override
	protected String preForkEventsMessage(Thread aThread, Map<String, Object> aNameValuePairs) {
//		System.out.println ("Thread:" + aThread.getId() + " prefork properties: " + aNameValuePairs);
		return null;
	}

	/**
	 * Invoked as each iteration of a thread is processed. The first argument
	 * indicates the thread that processed the iteration The second argument
	 * indicates the property names and values output on that iteration A failure
	 * message should be given if the values of the input and computed properties
	 * are not consistent Once a failure message is given no other iteration or post
	 * iteration methods will be called, though the post join method woud be called
	 */
	@Override
	protected String iterationEventsMessage(Thread aThread, Map<String, Object> aNameValuePairs) {
//		System.out.println ("Thread:" + aThread.getId() + " iteration properties: " + aNameValuePairs);
		boolean isOdd = (boolean) aNameValuePairs.get("Is Odd");
		int aNumber = (Integer) aNameValuePairs.get("Number");
		if (isOdd) {
			numNumbersFoundByCurrentThread++;
			expectedFinalSet.add(aNumber);
		}
		boolean isActualOdd = isOdd(aNumber);
		if (isOdd != isActualOdd) {
			return "Is Odd output as " + isOdd + " for number " + aNumber + " but should be " + isActualOdd;
		}

		return null;
	}

	protected boolean isOdd(int aNumber) {
		return aNumber % 2 == 1;
	}

	protected Set<Integer> expectedFinalSet = new HashSet();
	int numExpectedFinalNumbers;

	/**
	 * Invoked after all iterations of a thread have been processed. The first
	 * argument indicates the thread that finishes the iterations The second
	 * argument indicates the property names and values output before the thread
	 * terminates A failure message should be given if the values of the post
	 * iteration and iteration results are not consistent
	 */
	@Override
	protected String postIterationEventsMessage(Thread aThread, Map<String, Object> aNameValuePairs) {
//		System.out.println ("Thread:" + aThread.getId() + " post iteration properties: " + aNameValuePairs);
		int aNumNumbersComputed = (int) aNameValuePairs.get("Num Odd Numbers");
		if (aNumNumbersComputed != numNumbersFoundByCurrentThread) {
			return "Thread " + aThread.getId() + " found " + numNumbersFoundByCurrentThread + " but computed "
					+ aNumNumbersComputed;
		}
		numExpectedFinalNumbers += aNumNumbersComputed;
		return null;
	}

	/**
	 * Invoked after root thread has joined all forked threads, that is, after all
	 * forked threads have terminated. A failure message should be given if the
	 * values of root post join results and thread iteration results are not
	 * consistent
	 */
	@Override
	protected String postJoinEventsMessage(Thread aThread, Map<String, Object> aNameValuePairs) {
//		System.out.println ("Thread:" + aThread.getId() + " post join properties: " + aNameValuePairs);
		int aComputedFinalNumbers = (int) aNameValuePairs.get("Total Num Odd Numbers");
		if (aComputedFinalNumbers != numExpectedFinalNumbers) {
			return "\nComputed total number of odd numbers(" + aComputedFinalNumbers + ") != " + "expected total ("
					+ numExpectedFinalNumbers + ")";
		}
		if (aComputedFinalNumbers != expectedFinalSet.size()) {
			return "\nComputed set of odd numbers (" + aComputedFinalNumbers + ") != " + "does not have expected set size ("
					+ expectedFinalSet.size() + ")";
		}
		Set aComputedFinalSet = (Set) aNameValuePairs.get("Odd Numbers");
		if (!aComputedFinalSet.equals(expectedFinalSet)) {
			return "\nComputed set of odd numbers " + aComputedFinalSet + " != " + "expected set " + expectedFinalSet;
		}

		return null;
	}

}
