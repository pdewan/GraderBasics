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

public abstract class AbstractEarlyJoinBasicJoiner extends PassFailJUnitTestCase {

	protected Object joiner;
	protected int joinerCount = 4;
	protected int taskCount = 0;
	protected long slaveTimeout;
	protected long masterTimeout;
	protected TestJoiner timingOutJoiner;

	protected void setTimeouts() {
		slaveTimeout = 300;
		masterTimeout = 0;
	}

	protected TestCaseResult doJoinTest() {
//		for (int i =0; i < joinerCount; i++) {
//			new Thread (()-> {doSlaveTask();}).start();
//		}
//		return doMasterTask();
		return parallelInc();

	}

	protected TestCaseResult parallelInc() {
		for (int i = 0; i < joinerCount; i++) {
			new Thread(() -> {
				doSlaveTask();
			}).start();
		}
		return doMasterTask();

	}

	protected synchronized void doSlaveTask() {
		taskCount++;
		ThreadSupport.sleep(slaveTimeout);
		Tracer.info(this, Thread.currentThread() + "before finished");
		try {			
			timingOutJoiner.finished();
		} catch (Throwable e) {
			timingOutJoiner.finish();
		}
		Tracer.info(this, Thread.currentThread() + "after finished, taskCount=" + taskCount);

	}

	protected TestCaseResult doMasterTask() {
		try {
			ThreadSupport.sleep(masterTimeout);
			Tracer.info(this, Thread.currentThread() + "before join");
			timingOutJoiner.join();
			Tracer.info(this, Thread.currentThread() + "after join, taskCount =" + taskCount);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (taskCount == joinerCount) {
			return pass();
		}
		if (taskCount < joinerCount) {
			return fail("joiner finished early? taskCount:" + taskCount + " joinerCount:" + joinerCount);
		}
		return fail("taskCount:" + taskCount + " joinerCount:" + joinerCount);

	}

	protected abstract Class getJoinerClass();
	
	protected Object getJoinerObject (Class[] aConstructorArgTypes, Object[] aJoinerArgs, Class aProxyClass ) {
		Class aJoinerClass = getJoinerClass();
//		Class[] aConstructorArgTypes = { Integer.TYPE };
		
		try {
			Constructor aJoinerConstructor = aJoinerClass.getConstructor(aConstructorArgTypes);
			Object[] anArgs = aJoinerArgs;
			Object aJoiner =  aJoinerConstructor.newInstance(anArgs);
			return aJoiner;
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			Constructor[] aConstructors = aJoinerClass.getConstructors();
			String aConstructoraString = Arrays.toString(aConstructors);
			assertTrue("No public constructor with single int argument in joiner class:" + aJoinerClass
					+ "constructors found:" + aConstructoraString, false);
			return null;
		}
	}
	protected Object getProxyJoinerObject (Class[] aConstructorArgTypes, Object[] aJoinerArgs, Class aProxyClass ) {
		Object aJoiner =  getJoinerObject(aConstructorArgTypes, aJoinerArgs, aProxyClass);

		Object aTimingOutJoiner= BasicProjectIntrospection.createTimingOutProxy(aProxyClass, aJoiner);
		return aTimingOutJoiner;
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
	
	protected void createJoiner() {
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

		Object[] anArgs = { joinerCount };
		timingOutJoiner = (TestJoiner) getProxyJoinerObject(aConstructorArgTypes, anArgs, TestJoiner.class);
	}

	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {
		createJoiner();
		setTimeouts();
		TestCaseResult retVal = doJoinTest();
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
