package grader.basics.testcase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.execution.RunningProject;
import grader.basics.junit.AGradableJUnitTest;
//import grader.basics.execution.RunningProject;
import grader.basics.junit.BasicJUnitUtils;
import grader.basics.junit.GradableJUnitTest;
import grader.basics.junit.JUnitTestsEnvironment;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.NotesAndScore;
import grader.basics.junit.TestCaseResult;
import grader.basics.observers.IOTraceRepository;
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
//	private List<String> preAnnouncements = new ArrayList<String>();
//	private List<String> postAnnouncements = new ArrayList<String>();
	protected String name = "anonymous";
	protected TestCaseResult lastResult; // last run, for depndent tests
	protected double fractionComplete;
	public final static  TestCaseResult NO_OP_RESULT =  new TestCaseResult(0, null, null, true);

	protected boolean precedingTestMustBeCorrected = false;
//	private GradableJUnitTest gradableJUnitTest;

	

	protected ABufferingTestInputGenerator outputBasedInputGenerator;
	protected RunningProject interactiveInputProject;
	protected Map<String, TestCaseResult> nameToResult = new HashMap();
	
	protected Exception timeoutException = null;


	
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
	Date date = new Date();
	@Test
	public void defaultTest() {
//		date.setTime(System.currentTimeMillis());
//		String anExplanation = AGradableJUnitTest.computeExplanation(this.getClass());
//
//		String anOutput =
//				">>Running at " + date + " test " +  anExplanation+ "\n<<";
//
//		System.out.println(anOutput);
//////		System.out.println(">>Running at " + date + " test " +  getExplanation()+ "\n<<");
//		addPreAnnouncement(anOutput);
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
	protected PassFailJUnitTestCase getFirstPrecedingTestInstance() {
		return precedingTestInstances.get(0);
	}
	protected PassFailJUnitTestCase getLastPrecedingTestInstance() {
		return precedingTestInstances.get(precedingTestInstances.size()-1);
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
//					aPrecedingTestInstance.setPreTest(true);
					precedingTestInstances.add(aPrecedingTestInstance);
					continue;
				}
			}
			TestCaseResult aLastResult = aPrecedingTestInstance.getLastResult();
			if (aLastResult.isFail() && failedTestVetoes(aPrecedingTestElement)) {
				precedingTestMustBeCorrected = true;

				assertTrue("\nPreceding test " + aPrecedingTestElement.getSimpleName() + " failed.\nPlease correct the problems identified by preceding test:" + aPrecedingTestElement.getSimpleName() +  " before running this test", false);
			} else if (aLastResult.isPartialPass() && partialPassTestVetoes(aPrecedingTestElement)) {
				precedingTestMustBeCorrected = true;

				assertTrue("\nPreceding test " + aPrecedingTestElement.getSimpleName() + " did not pass.\nPlease correct the problems identified by preceding test:" + aPrecedingTestElement.getSimpleName() +  " before running this test", false);

			}
//			if (!aLastResult.isPass()) {
////				precedingTestMustBeCorrected = true;
//			}
			// if (!aLastResult.isFail()) {
			if (shouldScaleResult() &&
					!aLastResult.isPass()) {

				Tracer.info(PassFailJUnitTestCase.class, "Preceding test " + aPrecedingTestElement.getSimpleName()
						+ " partially passed or failed:" + aLastResult.getPercentage());
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
	  
	  public static long composeTime(int aYear, int aMonth, int aDay, int anHour, int aMinute) {
		  Calendar calendar = Calendar.getInstance();
	        
	        // Set the desired fields. Note: months are zero-indexed (January is 0, April is Calendar.APRIL which equals 3)
	        calendar.set(Calendar.YEAR, aYear);
	        calendar.set(Calendar.MONTH, aMonth - 1);
	        calendar.set(Calendar.DAY_OF_MONTH, aDay);
	        calendar.set(Calendar.HOUR_OF_DAY, anHour); // 24-hour clock
	        calendar.set(Calendar.MINUTE, aMinute);
	        calendar.set(Calendar.SECOND, 0);
	        calendar.set(Calendar.MILLISECOND, 0);
	        return calendar.getTimeInMillis();  
	        
		  
	  }
	  public static void setTimeToStart(int aYear, int aMonth, int aDay, int anHour, int aMinute) {
		  setTimeToStart(composeTime(aYear, aMonth, aDay, anHour, aMinute));
	  }
	  public static void setTimeToEnd(int aYear, int aMonth, int aDay, int anHour, int aMinute) {
		  setTimeToEnd(composeTime(aYear, aMonth, aDay, anHour, aMinute));
	  }


	public void passfailDefaultTest() {
		String aMessage = canRunTestMessage();
		if (aMessage != null) {
			BasicJUnitUtils.assertTrue(aMessage, 0, false);
			return;
		}
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
			if (aTestCaseResult == NO_OP_RESULT || aTestCaseResult == null) {
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
	static long timeToStart = System.currentTimeMillis();
	static long startTime = System.currentTimeMillis();;
	public static long getTimeToStart() {
		return timeToStart;
	}
	public static void setTimeToStart (long newVal) {
		 timeToStart = newVal;
	}
	static long timeToEnd =  System.currentTimeMillis();
	
	public static long getTimeToEnd() {
		return timeToEnd;
	}
	public static void setTimeToEnd (long newVal) {
		 timeToEnd = newVal;
	}
	
	private static final long THRESHOLD = 5000;
	private static String runMagic = "";
	
	private static String testerMagic = runMagic;

	public static String getTesterMagic() {
		return testerMagic;
	}

	public static void setTesterMagic(String testerMagic) {
		PassFailJUnitTestCase.testerMagic = testerMagic;
	}

	public static String getRunMagic() {
		return runMagic;
	}

	public static void setRunMagic(String runMagic) {
		PassFailJUnitTestCase.runMagic = runMagic;
	}

	public static void setTesterRunMagic(String bewVal) {
		PassFailJUnitTestCase.testerMagic = bewVal;
	}
	protected boolean canRunTestBasedOnTime() {
		long aCurrentTime = startTime;
		long aTimeToStart = getTimeToStart();
		long aTimeToEnd = getTimeToEnd(); 
//		Date aCurrentDate = new Date(aCurrentTime);
//		Date aTimeToStartDate = new Date (aTimeToStart);
//		Date aTimeEndDate = new Date (aTimeToEnd);
		return aCurrentTime >=aTimeToStart - THRESHOLD &&
				aCurrentTime <= aTimeToEnd + THRESHOLD;
		
	}
	protected boolean cabRunTestBasedOnMagic() {
		return getTesterMagic().equals(getRunMagic());
	}
	protected String  canRunTestMessage() {
		if (canRunTestBasedOnTime()) {
			return null;
		}
		if (cabRunTestBasedOnMagic()) {
			return null;
		}
		return "Cannot run test based on time of execution.\nGet magic password from instructor to run it earlier";
	}
	protected boolean showResult = true;
	public boolean isShowResult() {
		return showResult;
	}
	public void setShowResult(boolean newVal) {
		showResult = newVal;
	}
	protected boolean showMessage = true;
	public boolean isShowMessage() {
		return showMessage;
	}
	public void setShowMessage(boolean newVal) {
		showMessage = newVal;
	}
	public Map<String, TestCaseResult> getNameToResult() {
		return nameToResult;
	}
	public boolean isPassed(String aTestName) {
		TestCaseResult aResult = nameToResult.get(aTestName);
		return (aResult != null) && (aResult.isPass());
	}
	public TestCaseResult combineNormalizedResults(String[] aTestNames) {
		return combineResults(toTestResults(aTestNames));
	}
	
	public TestCaseResult[] toTestResults(String[] aTestNames) {
		TestCaseResult[] aModifiedResults = new TestCaseResult[aTestNames.length];
		for (int anIndex = 0; anIndex < aTestNames.length; anIndex++) {
				String aTestName = aTestNames[anIndex];
				TestCaseResult anOriginalResult = nameToResult.get(aTestName);
				if (anOriginalResult == null) {
					System.err.println("Missing result for:" + aTestName);
					continue;
				}
				TestCaseResult aModifiedResult = anOriginalResult.clone();
				double aNewPercentage = anOriginalResult.isFail()?0:1.0/aTestNames.length;
				aModifiedResult.setPercentage(aNewPercentage);
				aModifiedResults[anIndex] = aModifiedResult;
		}
		return aModifiedResults;
	}
	protected boolean precedingTestsCorrect() {
		List<PassFailJUnitTestCase> aPrecedingTestInstances = 
				getPrecedingTestInstances();
		for (PassFailJUnitTestCase aTestCase:aPrecedingTestInstances) {
			if (!aTestCase.getLastResult().isPass()) {
				return false;
			}
		}
		return true;
	}
	protected boolean precedingTestCorrect() {
		List<PassFailJUnitTestCase> aPrecedingTestInstances = 
				getPrecedingTestInstances();
		PassFailJUnitTestCase aLastTest = aPrecedingTestInstances.get(aPrecedingTestInstances.size() -1);
		for (PassFailJUnitTestCase aTestCase:aPrecedingTestInstances) {
			if (!aTestCase.getLastResult().isPass()) {
				return false;
			}
		}
		return true;
	}
	protected boolean precedingTestsMustBeCorrected() {
		List<PassFailJUnitTestCase> aPrecedingTestInstances = 
				getPrecedingTestInstances();
		for (PassFailJUnitTestCase aTestCase:aPrecedingTestInstances) {
			if (aTestCase.isPrecedingTestMustBeCorrected()) {
				return true;
			}
		}
		return false;
	}
//	static boolean printPreTestAnnoucement = true; // for hints, we will disable it
//	
//	public static boolean printPreTestAnnouncement() {
//		return printPreTestAnnoucement;
//	}
//	public static void setPrintPreTestAnnouncement(boolean newVal) {
//		printPreTestAnnoucement = newVal;
//	}
	public boolean isPrecedingTestMustBeCorrected() {
		return precedingTestMustBeCorrected;
	}
//    private boolean isPreTest = false;
//	
//    @Override
//	public boolean isPreTest() {
//		return isPreTest;
//	}
//	
//	protected void setPreTest(boolean newVal) {
//		isPreTest = newVal;
//	}
	public void addPreAnnouncement(String aNewValue) {
		IOTraceRepository.addPreAnnouncement(aNewValue);
//		preAnnouncements.add(aNewValue);
	}
	public void addPostAnnouncement(String aNewValue) {
		IOTraceRepository.addPostAnnouncement(aNewValue);

//		postAnnouncements.add(aNewValue);
	}
	
	public Exception getTimeoutException() {
		return timeoutException;
	}

	public void setTimeoutException(Exception timeoutException) {
		this.timeoutException = timeoutException;
	}

//	public void clearAnnouncements() {
//		preAnnouncements.clear();
//	}
//	public List getPreAnnouncements() {
//		return preAnnouncements;
//	}
//	public List getPostAnnouncements() {
//		return postAnnouncements;
//	}
//	public GradableJUnitTest getGradableJUnitTest() {
//		return gradableJUnitTest;
//	}
//	public void setGradableJUnitTest(GradableJUnitTest newVal) {
//		gradableJUnitTest = newVal;
//	}
	
}
