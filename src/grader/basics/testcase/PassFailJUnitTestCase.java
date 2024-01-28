package grader.basics.testcase;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import grader.basics.config.BasicExecutionSpecificationSelector;
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
import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleSpecificWarningTestCase;
import gradingTools.shared.testcases.NamedClassTest;
import gradingTools.shared.testcases.utils.ABufferingTestInputGenerator;
import gradingTools.utils.RunningProjectUtils;
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
	public final static  TestCaseResult NO_OP_RESULT =  new TestCaseResult(0, null, null, true);


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

	protected boolean failedTestVetoes(Class aClass) {
		return true;
	}
	
	protected boolean partialPassTestVetoes(Class aClass) {
		return false;
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
				if (failedTestVetoes(aPrecedingTestElement)) {
					assertTrue("Preceding test " + aPrecedingTestElement.getSimpleName() + " failed due to an exception:", false);

				} else {
					precedingTestInstances.add(aPrecedingTestInstance);
					continue;
				}
			}
			TestCaseResult aLastResult = aPrecedingTestInstance.getLastResult();
			if (aLastResult.isFail() && failedTestVetoes(aPrecedingTestElement)) {
				assertTrue("Preceding test " + aPrecedingTestElement.getSimpleName() + " failed:", false);
			} else if (aLastResult.isPartialPass() && partialPassTestVetoes(aPrecedingTestElement)) {
				assertTrue("Preceding test " + aPrecedingTestElement.getSimpleName() + " did not pass:", false);

			}
			// if (!aLastResult.isFail()) {
			if (shouldScaleResult() &&
					!aLastResult.isPass()) {

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
	
	protected boolean shouldScaleResult() {
		return true;
	}
	
	  public TestCaseResult scaleResult(TestCaseResult aResult) {
		  	 
	    	 if (!shouldScaleResult() ||
	    			 precedingTestInstances.size() == 0 || 
	    			 aResult.getPercentage() == 0) {
	    		 return aResult;
	    	 }
			 double aTotalFractionComplete = 0;
			 for (PassFailJUnitTestCase aTestCase:precedingTestInstances) {
				 if (aTestCase != null) {
				 aTotalFractionComplete += aTestCase.getLastResult().getPercentage();
				 }
			 }
			 double anAverageFractionComplete =  aTotalFractionComplete/ (double) precedingTestInstances.size();
			 double anOriginalFractionComplete = aResult.getPercentage();
			 Tracer.info(CheckstyleSpecificWarningTestCase.class, "Score of " + anOriginalFractionComplete + " scaled by average preceding test pass percentage:" + anAverageFractionComplete);
		      aResult.setPercentage(anOriginalFractionComplete*anAverageFractionComplete); 
			 return aResult;
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
			if (!(e instanceof AssertionError) || BasicJUnitUtils.isGiveAssertionErrorStackTrace()) {
//			if (BasicJUnitUtils.isGiveAssertionErrorStackTrace() ) {
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
	
	protected void setEntryPoint (Project aProject, Class aClassProvided) {
//		NamedClassTest aNamedClassTest = (NamedClassTest) JUnitTestsEnvironment.getAndPossiblyRunGradableJUnitTest(aClassProvided);
//		Class aTaggedClass = aNamedClassTest.getTaggedClass();
//		NamedClassTest aNamedClassTest = (NamedClassTest) JUnitTestsEnvironment.getAndPossiblyRunGradableJUnitTest(aClassProvided);
//		Class aTaggedClass = aNamedClassTest.getTaggedClass();
		Class aTaggedClass = getTaggedClass(aProject, aClassProvided);
		if (aTaggedClass == null) {
//			System.err.println("Cannot run test, no main class");
			throw new NotGradableException("Cannot run test, no main class");
			
		}
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setEntryPoint(aTaggedClass.getName());
		
	}
	protected Class getTaggedClass (Project aProject, Class aClassProvided) {
		NamedClassTest aNamedClassTest = (NamedClassTest) JUnitTestsEnvironment.getAndPossiblyRunGradableJUnitTest(aClassProvided);
		Class aTaggedClass = aNamedClassTest.getTaggedClass();
		return aTaggedClass;
		
	}
	public TestCaseResult combineResults(TestCaseResult... aResultArray) {
		
		boolean anAllPassed = true;
		double aPercentage = 0;
		StringBuffer aMessage = new StringBuffer();
		boolean allNoOps = true;
		
		for (TestCaseResult aTestCaseResult : aResultArray) {
			if (aTestCaseResult == NO_OP_RESULT ) {
				continue;
			}
			allNoOps = false;
			if (!aTestCaseResult.isPass() && anAllPassed) {
				anAllPassed = false;
			}
			aPercentage += aTestCaseResult.getPercentage();
			if (!(aMessage.length() == 0)) { 
					//&& !aMessage.toString().endsWith("\n")) {
				aMessage.append(" \n");
			}
//			if (aPercentage == 0) {
			String aNotes = aTestCaseResult.getNotes();
			if (aNotes.length() > 0) {
			  aMessage.append(aNotes); // only failure messages
//			  System.out.println(aNotes);
			}
///			}
		}
		if (allNoOps) {
			return NO_OP_RESULT;
		}
		if (anAllPassed) {
			return pass();
		}
		return partialPass(aPercentage, aMessage.toString());
	
	}
	protected boolean showResult = true;
	public boolean isShowResult() {
		return showResult;
	}
	public void setShowResult(boolean newVal) {
		showResult = newVal;
	}
}
