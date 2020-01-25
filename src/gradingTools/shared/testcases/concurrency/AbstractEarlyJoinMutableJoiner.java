package gradingTools.shared.testcases.concurrency;

import grader.basics.junit.TestCaseResult;

public abstract class AbstractEarlyJoinMutableJoiner extends AbstractEarlyJoinBasicJoiner {
	protected TestMutableJoiner mutableTimingOutJoiner;
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
		Class[] aConstructorArgTypes = { };

		
		Object[] anArgs = {  };
		
		mutableTimingOutJoiner = (TestMutableJoiner) getProxyJoinerObject(aConstructorArgTypes, anArgs, TestMutableJoiner.class);
		timingOutJoiner = mutableTimingOutJoiner;
	}
	@Override
	protected TestCaseResult doJoinTest() {

		TestCaseResult aFirstResult = parallelInc();
		if (!aFirstResult.isPass()) {
			return aFirstResult;
		}
		taskCount = 0;
		return parallelInc();
		
	}
	@Override
	protected TestCaseResult parallelInc() {
		mutableTimingOutJoiner.setNumThreads(joinerCount);
		return super.parallelInc();
	}
	
//	@Override
//	protected void createJoiner() {
//		MutableJoinerClassProvided aCheckClass = (MutableJoinerClassProvided) JUnitTestsEnvironment.getAndPossiblyRunGradableJUnitTest(MutableJoinerClassProvided.class);
//		if (aCheckClass == null) {
//			assertTrue("No check class found", false);
//		}
//		Class aJoinerClass = aCheckClass.getMutableJoinerClass();
//		if (aJoinerClass == null) {
//			assertTrue("No Joiner class found", false);
//		}
//		Class[] aConstructorArgTypes = {};
//		try {
//			Constructor aJoinerConstructor = aJoinerClass.getConstructor(aConstructorArgTypes);
//		     Object[] anArgs = {};
//			MutableJoiner mutableJoiner = (MutableJoiner) aJoinerConstructor.newInstance(anArgs);
//			mutableTimingOutJoiner = 	(MutableJoiner) BasicProjectIntrospection.createTimingOutProxy(MutableJoiner.class, mutableJoiner);
//			joiner = mutableJoiner;
//			timingOutJoiner = mutableJoiner;
//
//		
//		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//			assertTrue("No argumentless constructor in joiner class:" + aJoinerClass, false);
//		}
//	}
}
