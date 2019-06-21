package grader.basics.junit;

import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.project.NotGradableException;
import grader.basics.testcase.JUnitTestCase;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import util.annotations.Explanation;
import util.annotations.Group;
import util.annotations.IsExtra;
import util.annotations.IsRestriction;
import util.annotations.MaxValue;
import util.annotations.Position;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.annotations.Visible;
import util.trace.Tracer;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.introspect.Attribute;
@StructurePattern(StructurePatternNames.BEAN_PATTERN)
/**
 * This is the object displayed in the LocalChecks UI as a  (leaf-level?) tree node.
 *
 */
public class AGradableJUnitTest implements GradableJUnitTest{
	public static final Color UNTESTED_COLOR = Color.BLACK;
	public static final Color ALL_FAIL_COLOR = Color.RED;
	public static final Color MOSTLY_FAIL_COLOR = Color.PINK;
	public static final Color MOSTLY_PASS_COLOR = Color.ORANGE;
	public static final Color ALL_PASS_COLOR = Color.GREEN;
	static int DEFAULT_SCORE = 0;	
	int defaultScore = DEFAULT_SCORE;
	Class jUnitClass;
	Color color = UNTESTED_COLOR;
	boolean isExtra;
//	boolean writeToConsole;
//	boolean writeToFile;	
//	boolean writeToServer;
	boolean isRestriction;
	Double maxScore;
	Double computedMaxScore;
	Double score = 0.0001;
	String explanation;
	String group = "";
	Set<Class> leafClasses;
	GradableJUnitSuite topLevelSuite;
	RunNotifier runNotifier = new RunNotifier();
//	RunNotifier runNotifier = RunNotifierFactory.getRunNotifier();

	AJUnitTestResult runListener = new AJUnitTestResult();
	JUnitTestCase jUnitTest;
	int numTests = 0;
	double fractionComplete = 0;
	String status = "Not Tested";
	String message = "";
	Failure failure;
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	TestCaseResult testCaseResult;
	
	
	public AGradableJUnitTest (Class aJUnitClass) {
		init();
		setJUnitClass(aJUnitClass);	
	}
	
//	public AJUnitTestToGraderTestCase () {
//		init();
//	}
	@Visible(false)
	public void init() {
		runNotifier.addListener(runListener);
	}
	@Visible(false)
	public Class getJUnitClass() {
		return jUnitClass;
	}
	@Visible(false)

	public void setDefaultScore(int aDefaultScore) {
		defaultScore = aDefaultScore;
	}
	@Visible(false)
	public int getDefaultScore() {
		return defaultScore;
	}
	@Visible(false)
	public void setMaxScore (Class aJUnitClass) {
		if (aJUnitClass.isAnnotationPresent(MaxValue.class)) {
			MaxValue aMaxValue =  (MaxValue) aJUnitClass.getAnnotation(MaxValue.class);
			maxScore = (double) aMaxValue.value();
		} else {
			maxScore = null;
			maybeSetDefaultMaxScore();
		}
	}
	protected void maybeSetDefaultMaxScore() {
		maxScore = (double) DEFAULT_SCORE;
	}
	@Visible(false)
	public void setIsRestriction (Class aJUnitClass) {
		if (aJUnitClass.isAnnotationPresent(IsRestriction.class)) {
			IsRestriction anIsRestriction =  (IsRestriction) aJUnitClass.getAnnotation(IsRestriction.class);
			isRestriction = anIsRestriction.value();
		} else {
			isRestriction = false;
		}
	}
	@Visible(false)
	public void setIsExtra (Class aJUnitClass) {
		if (aJUnitClass.isAnnotationPresent(IsExtra.class)) {
			IsExtra anIsExtra =  (IsExtra) aJUnitClass.getAnnotation(IsExtra.class);
			isExtra = anIsExtra.value();
		} else {
			isExtra = false;
		}
	}
	@Visible(false)
	public void setExplanation (Class aJUnitClass) {
		if (aJUnitClass.isAnnotationPresent(Explanation.class)) {
			Explanation anExplanation =  (Explanation) aJUnitClass.getAnnotation(Explanation.class);
			explanation = aJUnitClass.getSimpleName() + ":" + anExplanation.value();
		} else {
			explanation = aJUnitClass.getSimpleName();
		}
//		setName(explanation);
	}	
	@Visible(false)
	public void setGroup (Class aJUnitClass) {
		if (aJUnitClass.isAnnotationPresent(Group.class)) {
			Group aGroup =  (Group) aJUnitClass.getAnnotation(Group.class);
			group = aGroup.value();
		} else {
			group = explanation;
		}
	}
	@Override
	@Visible(false)
	public String getGroup() {
		return group;
	}
	@Visible(false)
	@Override
	public JUnitTestCase getJUnitTestCase() {
		return JUnitTestsEnvironment.getPassFailJUnitTest(jUnitClass);
	}
	@Visible(false)
	public void setJUnitClass(Class aJUnitClass) {
		jUnitClass = aJUnitClass;
		setExplanation(aJUnitClass);
		setMaxScore(aJUnitClass);
		setIsRestriction(aJUnitClass);
		setIsExtra(aJUnitClass);
		setGroup(aJUnitClass);
		JUnitTestsEnvironment.addPassFailJUnitTestClass(aJUnitClass, this);

//		this.jUnitClass = aJUnitClass;
//		if (aJUnitClass.isAnnotationPresent(MaxValue.class)) {
//			MaxValue aMaxValue =  (MaxValue) aJUnitClass.getAnnotation(MaxValue.class);
//			maxScore = aMaxValue.value();
//		} else {
//			maxScore = DEFAULT_SCORE;
//		}
		
	}
	@Visible(false)
	public boolean isRestriction() {
		return isRestriction;
	}
	@Visible(false)
	public boolean isExtra() {
		return isExtra;
	}
	@Visible(false)
	public Double getMaxScore() {
		return maxScore;
	}
	@Visible(false)
	public String getExplanation() {
		return explanation;
	}
	protected void showColor() {
		Color oldColor = color;
		color = computeColor();
		propertyChangeSupport.firePropertyChange("this", oldColor,
				new Attribute(AttributeNames.COMPONENT_FOREGROUND, color));
	}
	protected void showScore() {
		Double oldScore = score;
//		Double oldScore = getDisplayedScore();

//		score = getUnroundedScore();
		score = getDisplayedScore();

//		propertyChangeSupport.firePropertyChange("UnroundedScore", oldScore,
//				score);
		propertyChangeSupport.firePropertyChange("DisplayedScore", oldScore,
				score);
	}
	protected void showResult (TestCaseResult aTestCaseResult) {
		String oldStatus = status;
		String oldMessage = message;
		double oldScore = score;
		status = aTestCaseResult.getPercentage()*100 + "% complete";
		message = aTestCaseResult.getNotes();
//		score = getUnroundedScore();	
		score = getDisplayedScore();		

		propertyChangeSupport.firePropertyChange("DisplayedScore", oldScore, getDisplayedScore());
//		propertyChangeSupport.firePropertyChange("Status", oldStatus, status);

		propertyChangeSupport.firePropertyChange("Message", oldMessage, message);
		showColor();
//		showScore();
//		Color oldColor = color;
//		Color color = computeColor();
//		propertyChangeSupport.firePropertyChange("this", oldColor,
//				new Attribute(AttributeNames.COMPONENT_FOREGROUND, color));
		
	}
	@Override
	@Visible(false)
	public List<Double> getPercentages() {
		List<Double> retVal = new ArrayList();
		retVal.add(fractionComplete);
		return retVal;
	}
	@Override
	@Visible(false)
	public List<String> getMessages() {
		List<String> retVal = new ArrayList();
		retVal.add(message);
		return retVal;
	}
	@Override
	@Visible(false)
	public List<TestCaseResult> getTestCaseResults() {
		List<TestCaseResult> retVal = new ArrayList();
		retVal.add(testCaseResult);
		return retVal;
	}
	@Visible(false)
	public TestCaseResult test()
			throws NotAutomatableException, NotGradableException {
		try {
			Tracer.resetNumTraces();// previous test output should not affect this one
			numTests++;
			Class aJUnitClass = getJUnitClass();
			BasicStaticConfigurationUtils.setTest(aJUnitClass);
//			JUnitTestsEnvironment.addGradableJUnitTest(aJUnitClass, this);

			runListener.setJUnitName(aJUnitClass.getName());
			Runner aRunner = new BlockJUnit4ClassRunner(aJUnitClass);
			aRunner.run(runNotifier);
			testCaseResult = runListener.getTestCaseResult();
			jUnitTest = getJUnitTestCase();
			if (jUnitTest != null) {
				jUnitTest.setLastResult(testCaseResult);
			}
			failure = runListener.getFailure();
			fractionComplete = testCaseResult.getPercentage();
			showResult(testCaseResult);
//			status = aTestCaseResult.getPercentage()*100 + " % complete";
//			message = aTestCaseResult.getNotes();			
			return testCaseResult;

			
		} catch (InitializationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String anExceptionMessage = e.getMessage();
			String aMessage = e.getClass().getSimpleName() + " Could not initialize an instance of test class ";
			TestCaseResult aTestCaseResult = new TestCaseResult(false, aMessage, getExplanation(), true);
			showResult(aTestCaseResult);
			return aTestCaseResult;
//			return fail(e.getMessage());
		} catch (IllegalArgumentException e ) {
			e.printStackTrace();
			String anExceptionMessage = e.getMessage();
			TestCaseResult aTestCaseResult = new TestCaseResult(false,  e.getMessage(), getExplanation(), true);
			showResult(aTestCaseResult);
			return aTestCaseResult;
		} catch (Exception e) {
			e.printStackTrace();
			String anExceptionMessage = e.getMessage();
			TestCaseResult aTestCaseResult = new TestCaseResult(false, e.getMessage(), getExplanation(), true);
			showResult(aTestCaseResult);
			return aTestCaseResult;
		}
			
		
		// InitializationError
//		Runner aRunner = new BlockJUnit4ClassRunner(ACartesianPointParametrizedJUnitTester.class);
//		Runner aRunner = new BlockJUnit4ClassRunner(ASinglePointBeforeClassJUnitMultiTester.class);
		// IniitializationError
//		Runner aRunner = new BlockJUnit4ClassRunner(ACartesianPointParametrizedJUnitMultiTester.class);
//		return null;
	}
	protected boolean isPass() {
		return getFractionComplete() == 1.0;
	}
	protected boolean isPartialPass() {
		return !isPass() && getFractionComplete() >  0.0;
	}
	protected boolean isFail() {
		return getFractionComplete() == 0.0 & numTests != 0;
	}
	protected boolean isUntested() {
		return  numTests == 0;
	}
	
	@Visible(false)
	@Override
	public void setMaxScore(double aMaxScore) {
		maxScore = aMaxScore;
//		maybeSetChildrenMaxScores();
	}
//	protected void maybeSetChildrenMaxScores() {
//		
//	}
	@Visible(false)
	@Override
	public void setGroup(String newVal) {
		group = newVal;
		
	}
	@Visible(false)
	@Override
	public void setRestriction(boolean newVal) {
		isRestriction = newVal;		
	}
	@Visible(false)
	@Override
	public void setExtra(boolean newVal) {
		isExtra = newVal;		
	}
	@Visible(false)
	@Override
	public void setExplanation(String newVal) {
		explanation = newVal;		
	}
	
	protected String description = null;
	public String getName() {
		if (description == null) {
		String aScore = "[" + GradableJUnitTest.round(getComputedMaxScore()) + " pts" + "]";
		String anExtra = isExtra?
				"(extra credit)"
				:"";
//		description = explanation + aScore + anExtra;
		description = getJUnitClass().getSimpleName() + aScore + anExtra;

		}
		return description;
	}
	@Visible(false)
	@Position(0)
	@Override
	public String getStatus() {
		return status;
	}
	@Position(1)
	@Override
	public String getMessage() {
		return message;
	}
	@Visible(false)
	public void open(String aField) {
//		System.out.println ("opened: " + aTest);
		test();
	}
	@Override
	@Visible(false)
	public int numExecutions() {
		return numTests;
	}
	protected Color computeColor(int aNumTests,double aFractionComplete) {
		if (aNumTests == 0)
			return UNTESTED_COLOR;
		if (aFractionComplete == 1)
			return ALL_PASS_COLOR;
		if (aFractionComplete == 0)
			return ALL_FAIL_COLOR;		
		if (aFractionComplete >= 0.5)
			return MOSTLY_PASS_COLOR;
		return MOSTLY_FAIL_COLOR;
	}
	protected Color computeColor() {
		return computeColor(numTests, fractionComplete);
//		if (numTests == 0)
//			return UNTESTED_COLOR;
//		if (fractionComplete == 1)
//			return ALL_PASS_COLOR;
//		if (fractionComplete == 0)
//			return ALL_FAIL_COLOR;		
//		if (fractionComplete >= 0.5)
//			return MOSTLY_PASS_COLOR;
//		return MOSTLY_FAIL_COLOR;
	}
//	double aFractionCorrect = ((double) numTestsSuceeded())/children.size());
//	if (aFractionCorrect == 1)
//		return ALL_PASS_COLOR;
//	else if (aFractionCorrect == 0) {
//		
//	}
//}
	@Override
	@Visible(false)
	public double getFractionComplete() {
		return fractionComplete;
	}
	@Override
	@Visible(false)
	public void addPropertyChangeListenerRecursive(PropertyChangeListener arg0) {
		addPropertyChangeListener(arg0);		
	}
	@Override
	public void addPropertyChangeListener(PropertyChangeListener arg0) {
		propertyChangeSupport.addPropertyChangeListener(arg0);
		showColor();
		
	}
	@Visible(false)
	@Override
	public double getUnroundedScore() {
		return maxScore*fractionComplete;
	}
	@Position(0)
	@Override
	public double getDisplayedScore() {
		return GradableJUnitTest.round(getUnroundedScore());
	}
	@Override
	@Visible(false)
	public String getText() {
		return getName() + "," + getUnroundedScore() +  "," + getMessage();
	}
//	public String toString() {
//		return getName();
//	}
	@Visible(false)
	@Override
	public double getComputedMaxScore() {
		return maxScore;
	}
	@Override
	public int numLeafNodeDescendents() {
		return 0;
	}
//	public static void main (String[] args) {
//		ObjectEditor.edit(new bus.uigen.test.ACompositeColorer());
//		AGradableJUnitTest foo = new AGradableJUnitTest(ACartesianPointJUnitTester.class);
////		foo.setJUnitClass(ACartesianPointJUnitTester.class);
//		System.out.println (foo);
//	}

	@Override
	public int numInternalNodeDescendents() {
		// TODO Auto-generated method stub
		return 0;
	}
//	public void setWriteToServer(boolean writeToServer) {
//		this.writeToServer = writeToServer;
//	}
//	@Override
//	public void setWriteToConsole(boolean newVal) {
//		writeToConsole = newVal;
//	}
//	@Override
//	public boolean isWriteToConsole() {
//		return writeToConsole;
//	}
//	@Override
//	public boolean isWriteToFile() {
//		return writeToFile;
//	}
//	@Override
//	public void setWriteToFile(boolean writeToFile) {
//		this.writeToFile = writeToFile;
//	}
//	@Override
//	public boolean isWriteToServer() {
//		return writeToServer;
//	}
	static protected Class[] emptyClassArray = {};
	static protected Set<Class> emptySet = new HashSet();

	@Override
	@Visible(false)
	public Set<Class> getLeafClasses() {
		if (leafClasses == null) {
//			leafClasses = new Class[] {getJUnitClass()};
			leafClasses = new HashSet();
			leafClasses.add(getJUnitClass());

		}
		// TODO Auto-generated method stub
		return leafClasses;
	}

	@Override
	@Visible(false)
	public Set<Class> getPassClasses() {
		if (isPass())
			return getLeafClasses();
		return emptySet;
	}

	@Override
	@Visible(false)
	public Set<Class> getPartialPassClasses() {
		if (isPartialPass())
			return getLeafClasses();
		return emptySet;
	}

	@Override
	@Visible(false)
	public Set<Class> getFailClasses() {
		if (isFail())
			return getLeafClasses();
		return emptySet;
	}
	
	@Override
	@Visible(false)
	public Set<Class> getUntestedClasses() {
		if (isUntested())
			return getLeafClasses();
		return emptySet;
	}
	public String toString() {
		return getName() + "(" + super.toString() + ")";
	}
	
//	@Override
//	public void setTopLevelSuite(GradableJUnitSuite newVal) {
//		topLevelSuite = newVal;
//		
//	}
//
//	@Override
//	public GradableJUnitSuite getTopLevelSuite() {
//		// TODO Auto-generated method stub
//		return topLevelSuite;
//	}

//	@Override
//	public Class[] getUntestedClasses() {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
}
