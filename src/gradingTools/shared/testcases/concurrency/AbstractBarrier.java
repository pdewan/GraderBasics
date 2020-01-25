package gradingTools.shared.testcases.concurrency;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import grader.basics.junit.JUnitTestsEnvironment;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;

import util.annotations.MaxValue;
import util.misc.ThreadSupport;
import util.trace.Tracer;

public abstract class AbstractBarrier extends PassFailJUnitTestCase {

	protected Object barrier;
	protected int barrierCount = 4;
	protected int taskCount = 0;
	protected long threadTimeout; // multiplied by task count is the time spent before releasing monitor
	protected TestBarrier timingOutBarrier;

	protected void setTimeouts() {
		threadTimeout= 50;
	}

	protected TestCaseResult doBarrierTest() {
//		for (int i =0; i < joinerCount; i++) {
//			new Thread (()-> {doSlaveTask();}).start();
//		}
//		return doMasterTask();
		return parallelInc();

	}

	protected TestCaseResult parallelInc() {
		for (int i = 0; i < barrierCount -1; i++) {
			Tracer.info(this, "Creating new thread #:" + i);
			new Thread(() -> {
				doPeerTask();
			}).start();
		}
		return doMasterTask();

	}

	protected  void doPeerTask() {
		incrementTaskCount();
		ThreadSupport.sleep(taskCount*threadTimeout);
		Tracer.info(this, Thread.currentThread() + "before barrier");
		timingOutBarrier.barrier();
		Tracer.info(this, Thread.currentThread() + "after barrier, taskCount=" + taskCount);

	}
	protected synchronized void incrementTaskCount() {
		taskCount++;

	}

	protected TestCaseResult doMasterTask() {
		try {
			doPeerTask();
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (taskCount == barrierCount) {
			return pass();
		}
		if (taskCount < barrierCount) {
			return fail("master finished early? taskCount:" + taskCount + " barrierCount:" + barrierCount);
		}
		return fail("taskCount:" + taskCount + " barrierCount:" + barrierCount);

	}

	protected abstract Class getBarrierClass();
	
	protected Object getBarrierObject (Class[] aConstructorArgTypes, Object[] aJoinerArgs, Class aProxyClass ) {
		Class aBarrierClass = getBarrierClass();
//		Class[] aConstructorArgTypes = { Integer.TYPE };
		
		try {
			Constructor aJoinerConstructor = aBarrierClass.getConstructor(aConstructorArgTypes);
			Object[] anArgs = aJoinerArgs;
			Object aBarrier =  aJoinerConstructor.newInstance(anArgs);
			return aBarrier;
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			Constructor[] aConstructors = aBarrierClass.getConstructors();
			String aConstructoraString = Arrays.toString(aConstructors);
			assertTrue("No public constructor with single int argument in barrier class:" + aBarrierClass
					+ "constructors found:" + aConstructoraString, false);
			return null;
		}
	}
	protected Object getProxyBarrierObject (Class[] aConstructorArgTypes, Object[] aJoinerArgs, Class aProxyClass ) {
		Object aBarrier =  getBarrierObject(aConstructorArgTypes, aJoinerArgs, aProxyClass);

		Object aTimingOutBarrier= BasicProjectIntrospection.createTimingOutProxy(aProxyClass, aBarrier);
		return aTimingOutBarrier;
//		
//		Object aJoiner =  getJoinerObject(aConstructorArgTypes, aJoinerArgs, aProxyClass);
//		Class aJoinerClass = getJoinerClass();
////		Class[] aConstructorArgTypes = { Integer.TYPE };
//		
//		try {
//			Constructor aJoinerConstructor = aJoinerClass.getConstructor(aConstructorArgTypes);
//			Object[] anArgs = aJoinerArgs;
//			Object aJoiner =  aJoinerConstructor.newInstance(anArgs);
//			Object aTimingOutJoiner= BasicProjectIntrospection.createTimingOutProxy(aProxyClass, aJoiner);
//			return aTimingOutJoiner;
//		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
//				| IllegalArgumentException | InvocationTargetException e) {
//			Constructor[] aConstructors = aJoinerClass.getConstructors();
//			String aConstructoraString = Arrays.toString(aConstructors);
//			assertTrue("No public constructor with single int argument in joiner class:" + aJoinerClass
//					+ "constructors found:" + aConstructoraString, false);
//			return null;
//		}
	}
	
	protected void createBarrier() {
//		ImmmutableJoinerClassProvided aCheckClass = (ImmmutableJoinerClassProvided) JUnitTestsEnvironment
//				.getAndPossiblyRunGradableJUnitTest(ImmmutableJoinerClassProvided.class);
//		if (aCheckClass == null) {
//			assertTrue("No check class found", false);
//		}
//		Class aJoinerClass = aCheckClass.getImmutableJoinerClass();
//		if (aJoinerClass == null) {
//			assertTrue("No Joiner class found", false);
//		}
//		Class aJoinerClass = getJoinerClass();
//		Class[] aConstructorArgTypes = { Integer.TYPE };
//		try {
//			Constructor aJoinerConstructor = aJoinerClass.getConstructor(aConstructorArgTypes);
//			Object[] anArgs = { joinerCount };
//			joiner =  aJoinerConstructor.newInstance(anArgs);
//			timingOutJoiner = (TestJoiner) BasicProjectIntrospection.createTimingOutProxy(TestJoiner.class, joiner);
//
//		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
//				| IllegalArgumentException | InvocationTargetException e) {
//			Constructor[] aConstructors = aJoinerClass.getConstructors();
//			String aConstructoraString = Arrays.toString(aConstructors);
//			assertTrue("No public constructor with single int argument in joiner class:" + aJoinerClass
//					+ "constructors found:" + aConstructoraString, false);
//		}
		Class[] aConstructorArgTypes = { Integer.TYPE };

		Object[] anArgs = { barrierCount };
		timingOutBarrier = (TestBarrier) getProxyBarrierObject(aConstructorArgTypes, anArgs, TestBarrier.class);
	}

	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {
		createBarrier();
		setTimeouts();
		TestCaseResult retVal = doBarrierTest();
		return retVal;
//		ImmmutableJoinerClassProvided aCheckClass = (ImmmutableJoinerClassProvided) JUnitTestsEnvironment.getAndPossiblyRunGradableJUnitTest(ImmmutableJoinerClassProvided.class);
//		if (aCheckClass == null) {
//			return fail("No check class found");
//		}
//		Class aJoinerClass = aCheckClass.getImmutableJoinerClass();
//		if (aJoinerClass == null) {
//			return fail("No Joiner class found");
//
//		}
//		Class[] aConstructorArgTypes = {Integer.TYPE};
//		try {
//			Constructor aJoinerConstructor = aJoinerClass.getConstructor(aConstructorArgTypes);
//		     Object[] anArgs = {joinerCount};
//			joiner = (Joiner) aJoinerConstructor.newInstance(anArgs);
//			timingOutJoiner = 	(Joiner) BasicProjectIntrospection.createTimingOutProxy(Joiner.class, joiner);
//
//		
//		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//			return fail("No constructor with single int argument in joiner class:" + aJoinerClass);
//		}

	}

}
