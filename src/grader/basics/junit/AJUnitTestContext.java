package grader.basics.junit;

import grader.basics.testcase.PassFailJUnitTestCase;
/**
 * 
 * Associates a tree node with a gradertest case.
 * A tree node is what is opened or run for testing.
 * This was added to allow dependent test cased to be run.
 * We go from a Junit test case to a context object, 
 * and then from the context object to the tree node,
 * which can then be run.
 * wondwr if we do this in grader mode. We can probbaly run the test directly.
 * GradableJUnitTest does have a reference of the class of JunitTestCase, 
 * junitTestCase keeps a reference to the instance
 *
 */
public class AJUnitTestContext implements JUnitTestContext {
	GradableJUnitTest gradableJUnitTest;
	PassFailJUnitTestCase junitTestCase; // so this is initially null
	public AJUnitTestContext(GradableJUnitTest aGradableJUnitTest) {
		gradableJUnitTest = aGradableJUnitTest;
	}
	@Override
	public GradableJUnitTest getGradableJUnitTest() {
		return gradableJUnitTest;
	}
	@Override
	public void setGradableJUnitTest(GradableJUnitTest gradableJUnitTest) {
		this.gradableJUnitTest = gradableJUnitTest;
	}
	@Override
	public PassFailJUnitTestCase getJUnitPassFailTest() {
//		if (junitTestCase == null) {
//			gradableJUnitTest.test();
//		}
		return junitTestCase;
	}
	@Override
	/**
	 * Called when an instance of the test is created
	 */
	public void setJUnitPassFailTest(PassFailJUnitTestCase junitTestCase) {
		this.junitTestCase = junitTestCase;
	}
	@Override
	/**
	 * Called explictly by the application programmer through JUNitTestEnvirobment
	 */
	public PassFailJUnitTestCase getAndPossiblyRunJUnitPassFailTest() {
		if (junitTestCase == null) { 
//			System.err.println("Unepected null test in context");
			gradableJUnitTest.test();
		}
		return junitTestCase;
	}

}
