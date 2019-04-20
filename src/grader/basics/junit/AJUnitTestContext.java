package grader.basics.junit;

import grader.basics.testcase.PassFailJUnitTestCase;

public class AJUnitTestContext implements JUnitTestContext {
	GradableJUnitTest gradableJUnitTest;
	PassFailJUnitTestCase junitTestCase;
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
		if (junitTestCase == null) {
			gradableJUnitTest.test();
		}
		return junitTestCase;
	}
	@Override
	public void setJUnitPassFailTest(PassFailJUnitTestCase junitTestCase) {
		this.junitTestCase = junitTestCase;
	}

}
