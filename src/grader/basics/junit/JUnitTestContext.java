package grader.basics.junit;

import grader.basics.testcase.PassFailJUnitTestCase;

public interface JUnitTestContext {

	GradableJUnitTest getGradableJUnitTest();
	void clearJUnitTest();

	void setGradableJUnitTest(GradableJUnitTest gradableJUnitTest);

	PassFailJUnitTestCase getJUnitPassFailTestCase();
	PassFailJUnitTestCase getAndPossiblyRunJUnitPassFailTestCase();


	void setJUnitPassFailTestCase(PassFailJUnitTestCase junitTestCase);
	long getGradableJUnitTestTime();

}