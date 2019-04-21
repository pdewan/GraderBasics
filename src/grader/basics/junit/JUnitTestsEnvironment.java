package grader.basics.junit;

import java.util.HashMap;
import java.util.Map;

import grader.basics.testcase.PassFailJUnitTestCase;

public class JUnitTestsEnvironment {
//	static GradableJUnitSuite
	static Map<Class, JUnitTestContext> history = new HashMap();
	public static void addGradableJUnitTest(Class aClass, GradableJUnitTest aTest) {
		JUnitTestContext aContext = history.get(aClass);
		if (aContext == null) {
			aContext = new AJUnitTestContext(aTest);
		}
		history.put(aClass, aContext);		
	}
	public static void addPassFailJUnitTestCase(Class aClass, PassFailJUnitTestCase aTestCase) {
		JUnitTestContext aContext = history.get(aClass);
		aContext.setJUnitPassFailTest(aTestCase);
	}
	public static GradableJUnitTest getGradableJUnitTest(Class aClass) {
		JUnitTestContext aContext = history.get(aClass);
		return aContext.getGradableJUnitTest();
	}
	public static PassFailJUnitTestCase getAndPossiblyRunGradableJUnitTest(Class aClass) {
		JUnitTestContext aContext = history.get(aClass);
		return aContext.getAndPossiblyRunJUnitPassFailTest();
	}
	public static PassFailJUnitTestCase getPassFailJUnitTest(Class aClass) {
		JUnitTestContext aContext = history.get(aClass);
		return aContext.getJUnitPassFailTest();
	}

}
