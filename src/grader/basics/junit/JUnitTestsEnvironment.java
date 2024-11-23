package grader.basics.junit;

import java.util.HashMap;
import java.util.Map;

import grader.basics.testcase.PassFailJUnitTestCase;

public class JUnitTestsEnvironment {
//	static GradableJUnitSuite
	// The class should be PassFailTestCase class
	static Map<Class<? extends PassFailJUnitTestCase>, JUnitTestContext> history = new HashMap();
	/**
	 * This adds the class of the JUnit class
	 *
	 */
	public static void clearCachedJUnitTestCases() {
		for (Class aClass:history.keySet()) {
			JUnitTestContext aContext = history.get(aClass);
			if (aContext != null) {
				aContext.clearJUnitTest();
			}
		}
	}
	/*
	 * Called by the tree node created for a test case
	 */
	public static void addPassFailJUnitTestCaseClass(Class<? extends PassFailJUnitTestCase> aClass, GradableJUnitTest aTest) {
		JUnitTestContext aContext = history.get(aClass);
		if (aContext == null) {
//			System.out.println("Creating context for " + aClass);
			aContext = new AJUnitTestContext(aTest); // aTest is nevernull
		} else {
//			System.out.println("Context already created for " + aClass);
		}
		history.put(aClass, aContext);		
	}
	/**
	 *  adds an instance of the class that was added earlier
	 */
	public static void addPassFailJUnitTestInstance(PassFailJUnitTestCase aTestCase) {
		JUnitTestContext aContext = history.get(aTestCase.getClass());
		if (aContext == null) { // The class was not added, this is an an instance of a grader class, possibly automatically created from a junit class
//			System.err.println("No context found for " + aClass);
			return;
		}
		aContext.setJUnitPassFailTestCase(aTestCase);
	}
	public static GradableJUnitTest getGradableJUnitTest(Class<? extends PassFailJUnitTestCase> aClass) {
		JUnitTestContext aContext = history.get(aClass);
		if (aContext == null) {
			return null;
		}
		return aContext.getGradableJUnitTest();
	}
	/**
	 * 
	 * A dependent test case calls this
	 */
	public static PassFailJUnitTestCase getAndPossiblyRunGradableJUnitTest(Class aClass) {
		JUnitTestContext aContext = history.get(aClass);
		if (aContext == null) {
			System.err.println("Internal error when trying to run missing independent test:" + aClass + ".\nPlease report to instructor");
			return null;
		}
		return aContext.getAndPossiblyRunJUnitPassFailTestCase();
	}
	public static PassFailJUnitTestCase getPassFailJUnitTest(Class aClass) {
		JUnitTestContext aContext = history.get(aClass);
		return aContext.getJUnitPassFailTestCase();
	}

}
