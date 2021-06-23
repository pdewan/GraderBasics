package grader.basics.testcase;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import grader.basics.execution.RunningProject;
//import grader.basics.execution.RunningProject;
import grader.basics.junit.BasicJUnitUtils;
import grader.basics.junit.JUnitTestsEnvironment;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.NotesAndScore;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.CurrentProjectHolder;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.shared.testcases.utils.ABufferingTestInputGenerator;
import junit.framework.TestCase;
import util.trace.Tracer;

/**
 * All test cases should extend this class. Subclasses will implement the
 * {@link TestCase#test(framework.project.Project, boolean)} method. This method
 * should call and return one of the following helper functions:
 *
 * 
 * An example:
 * 
 * <pre>
 * {@code
 * return partialPass(0.5, "Only got half of the points");
 * }
 * </pre>
 */
public abstract class PassFailJUnitTestCase implements JUnitTestCase {
	protected String name = "anonymous";
	protected TestCaseResult lastResult; // last run, for depndent tests
	protected double fractionComplete;

	protected ABufferingTestInputGenerator outputBasedInputGenerator;
	protected RunningProject interactiveInputProject;

	/**
	 * This is where we add an instance of the test case. This means only subclasses
	 * of PassFailJUnitTestCase can be made dependent.This makes sense since they
	 * have results etc.
	 * 
	 * This is also the superclass of a gradet testcase, and the clases of these
	 * have not been regustered.
	 */
	public PassFailJUnitTestCase() {
		addPassFailJUnitTestInstance();
//    	JUnitTestsEnvironment.addPassFailJUnitTestInstance(this);
	}

	protected void addPassFailJUnitTestInstance() {
		JUnitTestsEnvironment.addPassFailJUnitTestInstance(this);

	}

	protected TestCaseResult partialPass(double percentage, boolean autograded) {
		return new TestCaseResult(percentage, name, autograded);
	}

	protected TestCaseResult partialPass(double percentage, String notes, boolean autograded) {
		return new TestCaseResult(percentage, notes, name, autograded);
	}

	protected TestCaseResult partialPass(double percentage, String notes) {
		return partialPass(percentage, notes, true);
	}

	protected TestCaseResult pass() {
		return new TestCaseResult(true, name, true);
	}

	protected TestCaseResult pass(boolean autograded) {
		return new TestCaseResult(true, name, autograded);
	}

	protected TestCaseResult pass(String notes) {
		return new TestCaseResult(true, notes, name, true);
	}

	protected TestCaseResult pass(String notes, boolean autograded) {
		return new TestCaseResult(true, notes, name, autograded);
	}

	protected TestCaseResult fail(String notes) {
		return new TestCaseResult(false, notes, name, true);
	}

	protected TestCaseResult fail(String notes, boolean autograded) {
		return new TestCaseResult(false, notes, name, autograded);

	}

	public abstract TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException;

	@Test
	public void defaultTest() {
		passfailDefaultTest();
	}

	protected Class precedingTest() {
		return null;
	}

	static Class[] emptyClasses = {};
	static Class[] oneClassElement = { Object.class };

	protected Class[] precedingTests() {
		return emptyClasses;
	}

	protected List<PassFailJUnitTestCase> precedingTestInstances = new ArrayList<PassFailJUnitTestCase>();

	protected List<PassFailJUnitTestCase> getPrecedingTestInstances() {
		return precedingTestInstances;
	}

	protected boolean failedTestVetoes() {
		return true;
	}

	protected void possiblyRunAndCheckPrecedingTests() {
		Class[] aPrecedingTests;
		precedingTestInstances.clear();
		Class aPrecedingTest = precedingTest();
		if (aPrecedingTest != null) {
			aPrecedingTests = oneClassElement;
			aPrecedingTests[0] = aPrecedingTest;
		} else {
//    	if (aPrecedingTest == null) {
			aPrecedingTests = precedingTests();
			if (aPrecedingTests == null || aPrecedingTests.length == 0) {
				return;
			}
		}

//		for (Class aPrecedingTestElement:aPrecedingTests) {
//			PassFailJUnitTestCase aPrecedingTestInstance = JUnitTestsEnvironment.getAndPossiblyRunGradableJUnitTest(aPrecedingTestElement);
//			TestCaseResult aLastResult = aPrecedingTestInstance.getLastResult();
//			if (!aLastResult.isFail()) {
//				if (aLastResult.isPartialPass()) {
//					
//					Tracer.info(PassFailJUnitTestCase.class, "Preceding test " + aPrecedingTestElement.getSimpleName() + " partially passed:" + aLastResult.getPercentage());
//					Tracer.info(PassFailJUnitTestCase.class, "Scores of this test will be scaled");
//
//				}
//				precedingTestInstances.add(aPrecedingTestInstance);
//				continue;
//			} else {
//				Assert.assertTrue("Please find in the appropiate suite and correct test  " + aPrecedingTestElement.getSimpleName() + " before running this test", false);
//
//			}
//		}

		for (Class aPrecedingTestElement : aPrecedingTests) {
			try {
			PassFailJUnitTestCase aPrecedingTestInstance = JUnitTestsEnvironment
					.getAndPossiblyRunGradableJUnitTest(aPrecedingTestElement);
			if (aPrecedingTestInstance == null ) {
				if (failedTestVetoes()) {
					assertTrue("Preceding test " + aPrecedingTestElement.getSimpleName() + " failed due to an excecption:", false);

				} else {
					precedingTestInstances.add(aPrecedingTestInstance);
					continue;
				}
			}
			TestCaseResult aLastResult = aPrecedingTestInstance.getLastResult();
			if (aLastResult.isFail() && failedTestVetoes()) {
				assertTrue("Preceding test " + aPrecedingTestElement.getSimpleName() + " failed:", false);
			}
			// if (!aLastResult.isFail()) {
			if (!aLastResult.isPass()) {

				Tracer.info(PassFailJUnitTestCase.class, "Preceding test " + aPrecedingTestElement.getSimpleName()
						+ " partially passed:" + aLastResult.getPercentage());
				Tracer.info(PassFailJUnitTestCase.class, "Scores of this test will be scaled");

			}
			precedingTestInstances.add(aPrecedingTestInstance);
			continue;
			} catch (Exception e) {
					assertTrue("Preceding test " + aPrecedingTestElement.getSimpleName() + " failed:", false);
				
			}
		}
//		else {
//				Assert.assertTrue("Please find in the appropiate suite and correct test  " + aPrecedingTestElement.getSimpleName() + " before running this test", false);
//
//			}
//		}

	}

	public void passfailDefaultTest() {
		possiblyRunAndCheckPrecedingTests();
//		TestCaseResult result = null;
		TestCaseResult lastResult = null;

		try {
			lastResult = test(CurrentProjectHolder.getOrCreateCurrentProject(), true);
			if (lastResult == null) {
				BasicJUnitUtils.assertTrue("Test returned null result", 0, false);

			} else {
				BasicJUnitUtils.assertTrue(lastResult.getNotes(), lastResult.getPercentage(), lastResult.isPass());
			}
		} catch (Throwable e) {
			if (BasicJUnitUtils.isGiveAssertionErrorStackTrace()) {
				e.printStackTrace();
			}
			if (lastResult != null) {
				BasicJUnitUtils.assertTrue(e, lastResult.getPercentage());
			} else {
				BasicJUnitUtils.assertTrue(e, 0);
			}
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String aName) {
		name = aName;
	}

	@Override
	public void setLastResult(TestCaseResult lastResult) {
		this.lastResult = lastResult;
	}

	@Override
	public TestCaseResult getLastResult() {
		return lastResult;
	}

	@Override
	public ABufferingTestInputGenerator getOutputBasedInputGenerator() {
		return outputBasedInputGenerator;
	}

	@Override
	public void setOutputBasedInputGenerator(ABufferingTestInputGenerator newVal) {
		outputBasedInputGenerator = newVal;
	}

	@Override
	public RunningProject getInteractiveInputProject() {
		return interactiveInputProject;
	}

	@Override
	public void setInteractiveInputProject(RunningProject aProject) {
		interactiveInputProject = aProject;
	}

	protected void assertTrue(String aMessage, boolean aCheck) {
//		testing = false;
		Assert.assertTrue(aMessage + NotesAndScore.PERCENTAGE_MARKER + fractionComplete, aCheck);
//		testing = true;
//		Assert.assertTrue(aMessage + NotesAndScore.PERCENTAGE_MARKER + fractionComplete, aCheck);
	}
}
