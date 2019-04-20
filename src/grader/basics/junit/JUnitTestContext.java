package grader.basics.junit;

import grader.basics.testcase.PassFailJUnitTestCase;

public interface JUnitTestContext {

	GradableJUnitTest getGradableJUnitTest();

	void setGradableJUnitTest(GradableJUnitTest gradableJUnitTest);

	PassFailJUnitTestCase getJUnitPassFailTest();

	void setJUnitPassFailTest(PassFailJUnitTestCase junitTestCase);

}