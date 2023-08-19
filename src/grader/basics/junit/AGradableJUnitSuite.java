package grader.basics.junit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import analyzer.extension.timerTasks.LogNameManager;
//import drivers.WorkTimeDriverPD;
import grader.basics.config.BasicConfigurationManagerSelector;
import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.observers.ASourceAndTestLogWriter;
import grader.basics.observers.AnAbstractTestLogFileWriter;
import grader.basics.project.NotGradableException;
import grader.basics.testcase.JUnitTestCase;
import gradingTools.logs.models.MetricsUtils;
import gradingTools.logs.models.TestMetrics;
import util.annotations.Label;

//import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;

import util.annotations.Position;
import util.annotations.PreferredWidgetClass;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.annotations.Visible;
import util.trace.Tracer;

// why is the implicit pattern stack pattern?
@StructurePattern(StructurePatternNames.LIST_PATTERN)
/**
 * This is an internal node in the tree node created by the LocalChecks UI.
 * It starts a test run when it is double clicked.
 */
public class AGradableJUnitSuite extends AGradableJUnitTest implements
		GradableJUnitSuite, PropertyChangeListener {
	Set<Class> previousPassClasses, previousFailClasses, previousPartialPassClasses;
	double previousScore;
	List<GradableJUnitTest> children = new ArrayList();
	boolean implicit;
	
	public AGradableJUnitSuite(Class aJUnitClass) {
		this(aJUnitClass, false);
	}

	public AGradableJUnitSuite(Class aJUnitClass, boolean implicit) {
		super(aJUnitClass);
		topLevelSuite = this; // will be overridden by parent if they exist
		this.implicit = implicit;
	}
	
//	public List<String> totalAttempts() {
//		return new ArrayList();
//	}
	

	public GradableJUnitTest get(int anIndex) {
		return children.get(anIndex);
	}

	public int size() {
		return children.size();
	}
	@Override
	@Visible(false)
	public List<GradableJUnitTest> children() {
		return children;
	}

	@Visible(false)
	public void add(GradableJUnitTest anElement) {
//		anElement.setTopLevelSuite(getTopLevelSuite()); // asuume children are added top to bottom
		children.add(anElement);
		anElement.addPropertyChangeListener(this);
		// maybeSetChildrenMaxScores();
	}

	// protected void maybeSetChildrenMaxScores() {
	// if (maxScore == null)
	// return;
	// for (GradableJUnitTest aTest:children) {
	// double aChildScore = maxScore/children.size();
	// aTest.setMaxScore(aChildScore);
	// }
	// }
	@Visible(false)
	public void addAll(List<GradableJUnitTest> anElement) {
		children.addAll(anElement);
		for (GradableJUnitTest aTest : anElement) {
			aTest.addPropertyChangeListenerRecursive(this);
		}
		// maybeSetChildrenMaxScores();
	}
	protected void notifyTestRunStarted(GradableJUnitTest aTest) {
//		propertyChangeSupport.firePropertyChange(TEST_RUN_STARTED, aTest, null);
		propertyChangeSupport.firePropertyChange(TEST_RUN_STARTED, null, aTest);

//		
//		String aClassName = aTest.getJUnitClass().getSimpleName();
//		String aName = aTest.getExplanation();
		
	}
	protected void notifyTestStarted(GradableJUnitTest aTest) {
//		propertyChangeSupport.firePropertyChange(TEST_RUN_STARTED, aTest, null);
		propertyChangeSupport.firePropertyChange(TEST_STARTED, null, aTest);

//		
//		String aClassName = aTest.getJUnitClass().getSimpleName();
//		String aName = aTest.getExplanation();
		
	}
	protected void notifyTestRunEnded(GradableJUnitTest aTest) {
		propertyChangeSupport.firePropertyChange(TEST_RUN_ENDED, aTest, null);
//		
//		String aClassName = aTest.getJUnitClass().getSimpleName();
//		String aName = aTest.getExplanation();
		
	}
	protected void notifyTestEnded(GradableJUnitTest aTest) {
		propertyChangeSupport.firePropertyChange(TEST_ENDED, aTest, null);
//		
//		String aClassName = aTest.getJUnitClass().getSimpleName();
//		String aName = aTest.getExplanation();
		
	}
	protected void testRunStarted(GradableJUnitTest aTest) throws PropertyVetoException {

		RunVetoerFactory.getOrCreateRunVetoer().vetoableChange(new PropertyChangeEvent(this, TEST_RUN_STARTED, this, null));

		notifyTestRunStarted(aTest);
	}
	protected void testStarted(GradableJUnitTest aTest) throws PropertyVetoException {

		RunVetoerFactory.getOrCreateRunVetoer().vetoableChange(new PropertyChangeEvent(this, TEST_STARTED, this, null));

		notifyTestStarted(aTest);
	}
	
	protected void testRunEnded(GradableJUnitTest aTest) {
//		Description aDescription = Description.createTestDescription(GradableJUnitTest.TEST_RUN_STARTED, "a test", 0);
//		RunNotifierFactory.getRunNotifier().fireTestFinished(aDescription);
//		notifyTestRunStarted(aTest);
		notifyTestRunEnded(aTest);

	}
	
	protected void testEnded(GradableJUnitTest aTest) {
//		Description aDescription = Description.createTestDescription(GradableJUnitTest.TEST_RUN_STARTED, "a test", 0);
//		RunNotifierFactory.getRunNotifier().fireTestFinished(aDescription);
		notifyTestEnded(aTest);

	}
	
	

	// @Override
	// public List<GradableJUnitTest> getJUnitTests() {
	// return children;
	// }
	// public String getName() {
	// return getExplanation();
	// }
	@Visible(false)
	public void open(GradableJUnitTest aTest) {
		JUnitTestCase aJUnitTestCase = aTest.getJUnitTestCase();
		if (aJUnitTestCase != null && !BasicExecutionSpecificationSelector.getBasicExecutionSpecification().isReRunTests()) {
//			System.err.println("Not automatically rerunning:" + jUnitClass);
			
				System.err.println ("Not rerunning test " + aJUnitTestCase  + " again. + For rerun, please exit from local checks and start them again.");
				return;
		
		} 
		if (aJUnitTestCase != null) {
			System.out.println ("Re-running test " + aJUnitTestCase + " . Results may change.");
		}
//		if (aTest.getJUnitTestCase() != null) {
//			System.err.println ("Not rerunning test " + aTest.getJUnitTestCase() + " For rerunning it, please exit fron local checks and start them again.");
//			return;
//		}
		// System.out.println ("opened: " + aTest);
//		Description aDescription = Description.createSuiteDescription(aTest
//				.getJUnitClass());
//		RunNotifierFactory.getRunNotifier().fireTestRunStarted(aDescription);
		try {
		testRunStarted(aTest);
		
//		testStarted(aTest);
		aTest.test();
//		testEnded(aTest);
		testRunEnded(aTest);
		} catch (PropertyVetoException e) {
			System.err.println(e.getMessage());
		} 
		
//		RunNotifierFactory.getRunNotifier().fireTestRunFinished(null);
	}

	@Visible(false)
	public String getStatus() {
		return "";
	}

	@Position(1)
	@Visible(false)
	public String getMessage() {
		return "";
	}

	protected int numTestsSuceeded() {
		int retVal = 0;
		for (GradableJUnitTest aTest : children) {
			if (aTest.getFractionComplete() == 1.0)
				retVal++;
		}
		return retVal;
	}

	@Override
	@Visible(false)
	public int numLeafNodeDescendents() {
		int retVal = 0;
		for (GradableJUnitTest aChild:children) {
			retVal += aChild.numLeafNodeDescendents();
		}
		return retVal;
	}
	@Visible(false)
	public void fillLeafNodeDescendents(List<GradableJUnitTest> retVal) {
		for (GradableJUnitTest aChild:children) {
			aChild.fillLeafNodeDescendents(retVal);
		}
	}
	@Visible(false)
	public List<GradableJUnitTest> getLeafNodeDescendents() {
		List<GradableJUnitTest> retVal = new ArrayList();
		fillLeafNodeDescendents(retVal);
		return retVal;
	}
	@Override
	@Visible(false)
	public MaxScoreAssignmentResult assignMaxScores() {
		MaxScoreAssignmentResult aMaxScoreAssignmentResult = new MaxScoreAssignmentResult();
//		double anAssignedMaxScore = 0;
		for (GradableJUnitTest aChild:children) {
			MaxScoreAssignmentResult aChildResult = aChild.assignMaxScores();
			aMaxScoreAssignmentResult.assignedScores += aChildResult.assignedScores;
			aMaxScoreAssignmentResult.unassignedLeafNodes.addAll(aChildResult.unassignedLeafNodes);	
			
		}
		
		if (!isDefinesMaxScore()) { // no maxScore annotation
//		if (maxScore == DEFAULT_SCORE) { // no maxScore annotation
			return aMaxScoreAssignmentResult;
		}
		double anUnassignedMaxScore = getMaxScore() - aMaxScoreAssignmentResult.assignedScores;
		if (anUnassignedMaxScore < 0) {
			Tracer.info(this, "Ignoring MaxScore:" + getMaxScore() + " of " + jUnitClass.getSimpleName() + " as it is less than sum total of Max Scorse of descendents:" + aMaxScoreAssignmentResult.assignedScores );
			return aMaxScoreAssignmentResult;
		}
		double aDistributedScore = anUnassignedMaxScore/aMaxScoreAssignmentResult.unassignedLeafNodes.size();
		for (GradableJUnitTest anUnassignedLeafNode:aMaxScoreAssignmentResult.unassignedLeafNodes) {
			anUnassignedLeafNode.setMaxScore(aDistributedScore);
		}
		aMaxScoreAssignmentResult.unassignedLeafNodes.clear();
		aMaxScoreAssignmentResult.assignedScores = getMaxScore();
		return aMaxScoreAssignmentResult;
		
		
	}
	
	
	@Override
	@Visible(false)
	public int numInternalNodeDescendents() {
		int retVal = 0;
		for (GradableJUnitTest aChild:children) {
			retVal += aChild.numInternalNodeDescendents();
		}
		return retVal;
	}

	@Visible(false)
	public int numExecutions() {
		int retVal = 0;
		for (GradableJUnitTest aTest : children) {

			retVal += aTest.numExecutions() > 0 ? 1 : 0;
			;
		}
		return retVal;
	}

	// does not retiurn a result
	@Override
	@Visible(false)
	public void testAll() {
		test();
	}
//	public TestMetrics totalAttempts(GradableJUnitTest aTest) {
//		return null;
//		
//	
////		return allProgressMetrics();
//	}
	
//	public List<String> totalAttempts(GradableJUnitTest aTest) {
//		return allProgressMetrics();
//	}
//	@PreferredWidgetClass(javax.swing.JTextArea.class)
	public List<TestMetrics> testSpecificMetrics() {
		return MetricsUtils.progressMetrics();
	}
	public static void testLogAnalyzer() {
		System.err.println("Work time Driverr in Log Analyzer not included" );
//		WorkTimeDriverPD.main(null);
	}
	public List<String> workTimeMetrics() {
		return MetricsUtils.
				testingPeriodMetrics( this, AnAbstractTestLogFileWriter.toAssignmentNumber(this));
	}
	//TODO not sure what this is
	public String loggedName() {
		return "student";//LogNameManager.getLoggedName();
	}
	public void newLoggedName(String newVal) {
		// LogNameManager.setLoggedName(newVal);
	}
	public List<String[]> showSessions() {
		String aFileName = AnAbstractTestLogFileWriter.toFileName(this);
		
		List<String[]> aSessions = ASourceAndTestLogWriter.getInstance().readAllSessions(this);
		return aSessions;
	}


	@Visible(false)
	public TestCaseResult test() throws NotAutomatableException,
			NotGradableException {
//		JUnitTestsEnvironment.clearCachedJUnitTestCases();
		for (GradableJUnitTest aTest : children) {
			JUnitTestCase aJUnitTestCase = aTest.getJUnitTestCase();
			if (aJUnitTestCase != null) {
				continue;
			}
			TestCaseResult aChildResult = aTest.test();

//			if (aJUnitTestCase != null && !BasicExecutionSpecificationSelector.getBasicExecutionSpecification().isReRunTests()) {
////				System.err.println("Not automatically rerunning:" + jUnitClass);
//				
//					System.out.println ("Not rerunning test " + aTest.getJUnitTestCase()  + " again. + For rerun, please exit fron local checks and start them again.");
//			
//			} else {
//				if (aJUnitTestCase != null) {
//					System.out.println ("Re-running test " + aJUnitTestCase + " . Results may change.");
//				}
//			TestCaseResult aChildResult = aTest.test();
//			}
		}
		int aNumSuccesses = numTestsSuceeded();
		if (aNumSuccesses == children.size()) {
			return new TestCaseResult(true, getExplanation());
		} else {
			return new TestCaseResult(((double) aNumSuccesses)
					/ children.size(), getExplanation());
		}
	}

	@Override
	@Visible(false)
	public List<Double> getPercentages() {
		List retVal = new ArrayList();
		for (GradableJUnitTest aTest : children) {
			retVal.addAll(aTest.getPercentages());
		}
		return retVal;
	}

	@Visible(false)
	public List<String> getMessages() {
		List<String> retVal = new ArrayList();
		for (GradableJUnitTest aTest : children) {
			retVal.addAll(aTest.getMessages());
		}
		return retVal;
	}

	@Visible(false)
	@Position(0)
	@Override
	public double getUnroundedScore() {
		double retVal = 0;
		for (GradableJUnitTest aTest : children) {
			retVal += aTest.getUnroundedScore();
		}
		return retVal;
	}
//
//	@Visible(false)
//	public Double getMaxScore() {
//		
//		return maxScore;
//	}
	@Override
	protected String toScoreString() {
		double aMaxRegularScore = getComputedRegularMaxScore();
		double aMaxScoreWithExtraCredit = getComputedMaxScore();
		double aDifference = aMaxScoreWithExtraCredit - aMaxRegularScore;
		return aDifference == 0?
								super.toScoreString():
									aMaxRegularScore + "+" + aDifference + "=" + aMaxScoreWithExtraCredit;
	}
//	public String getName() {
//		if (description == null) {
//		double aMaxRegularScore = getComputedRegularMaxScore();
//		double aMaxScoreWithExtraCredit = getComputedMaxScore();
//		String aScore = "[" + toScoreString() +  " pts" + "]";
//
////		String aScore = "[" + GradableJUnitTest.round(getComputedRegularMaxScore()) + "," + GradableJUnitTest.round(getComputedMaxScore()) + " pts" + "]";
//		String anExtra = isExtra?
//				"(extra credit)"
//				:"";
////		description = explanation + aScore + anExtra;
////		description = getJUnitClass().getSimpleName() + aScore + anExtra;
//		description = getSimpleName() + aScore + anExtra;
//		}
//		return description;
//	}
	
	@Visible(false)
	public double getComputedRegularMaxScore() {
		if (computedMaxScoreNoExtra == null) {
			double retVal = 0;
			for (GradableJUnitTest aTest : children) {
				if (!aTest.isExtra())
				retVal += aTest.getComputedRegularMaxScore();
			}
			computedMaxScoreNoExtra = retVal;
		}
		return computedMaxScoreNoExtra;
	}

	@Visible(false)
	public double getComputedMaxScore() {
		if (computedMaxScore == null) {
			double retVal = 0;
			for (GradableJUnitTest aTest : children) {
				retVal += aTest.getComputedMaxScore();
			}
			computedMaxScore = retVal;
		}
		return computedMaxScore;
	}

	// do nothing as we need to know if the score was set or not
	// we have defines maxScore
//	protected void maybeSetDefaultMaxScore() {
//
//	}

	@Visible(false)
	public List<TestCaseResult> getTestCaseResults() {
		List<TestCaseResult> retVal = new ArrayList();
		for (GradableJUnitTest aTest : children) {
			retVal.addAll(aTest.getTestCaseResults());
		}
		return retVal;
	}

	@Visible(false)
	public String getText() {
		String retVal = getName() + "," + getUnroundedScore() + "\n";
		for (GradableJUnitTest aTest : children) {
			retVal += aTest.getText() + "\n";
		}
		return retVal;
	}

	protected void synthesizeAndDisplayPropertes() {
		numTests = numExecutions();
		fractionComplete = ((double) numTestsSuceeded()) / children.size();
		showColor();
		showScore();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// if (!"Status".equals(evt.getPropertyName()))
		// return; // do not want this computed multiple times for different
		// properties of test
		if (!"DisplayedScore".equals(evt.getPropertyName()))
			return; // do not want this computed multiple times for different
					// properties of test
		synthesizeAndDisplayPropertes();
		// numTests = numTests();
		// fractionComplete = ((double) numTestsSuceeded())/children.size();
		// showColor();
		// showScore();

	}

	@Override
	@Visible(false)
	public void addPropertyChangeListenerRecursive(PropertyChangeListener arg0) {
		for (GradableJUnitTest aTest : children) {
			aTest.addPropertyChangeListener(arg0);
		}
	}

	@Override
	@Visible(false)
	public void addPropertyChangeListener(PropertyChangeListener arg0) {
		synthesizeAndDisplayPropertes();
		// numTests = numTests();
		super.addPropertyChangeListener(arg0);

	}

	@Visible(false)
	@Override
	public Set<Class> getLeafClasses() {
		if (leafClasses == null) {
			Set<Class> aLeafClasses = new HashSet();
			for (GradableJUnitTest aTest : children) {
//				aLeafClasses.addAll(Arrays.asList(aTest.getLeafClasses()));
				aLeafClasses.addAll(aTest.getLeafClasses());

			}
			leafClasses = aLeafClasses;
		}
		return leafClasses;
	}

	@Visible(false)
	@Override
	public Set<Class> getPassClasses() {

		Set<Class> aChildrenClasses = new HashSet();
		for (GradableJUnitTest aTest : children) {
			aChildrenClasses.addAll(aTest.getPassClasses());
		}
		return aChildrenClasses;
	}
	@Override
	@Visible(false)
	public Set<Class> getPartialPassClasses() {

		Set<Class> aChildrenClasses = new HashSet();
		for (GradableJUnitTest aTest : children) {
			aChildrenClasses.addAll(aTest.getPartialPassClasses());
		}
		return aChildrenClasses;
	}
	@Visible(false)
	@Override
	public Set<Class> getFailClasses() {

		Set<Class> aChildrenClasses = new HashSet();
		for (GradableJUnitTest aTest : children) {
			aChildrenClasses.addAll(aTest.getFailClasses());
		}
		return aChildrenClasses;
	}
	@Visible(false)
	@Override
	public Set<Class> getUntestedClasses() {

		Set<Class> aChildrenClasses = new HashSet();
		for (GradableJUnitTest aTest : children) {
			aChildrenClasses.addAll(aTest.getUntestedClasses());
		}
		return aChildrenClasses;
	}
	@Visible(false)
	@Override
	public Set<Class> getPreviousFailClasses() {		
		return previousFailClasses;
	}
	@Visible(false)
	@Override
	public Set<Class> getPreviousPassClasses() {		
		return previousPassClasses;
	}
	@Visible(false)
	@Override
	public Set<Class> getPreviousPartialPassClasses() {		
		return previousPartialPassClasses;
	}
	@Visible(false)
	@Override
	public double getPreviousScore() {
		return previousScore;
	}
	protected void refreshPreviousClasses() {
		previousScore = getUnroundedScore();
		previousPassClasses = getPassClasses();
		previousFailClasses = getFailClasses();
		previousPartialPassClasses = getPartialPassClasses();
	}

	@Override
	public void test(Class aJUnitClass) {
		GradableJUnitTest aTest = findTest(aJUnitClass);
		if (aTest == null) {
			System.err.println("Could not find junit class:" + aJUnitClass);
		} else {
			aTest.test();
		}
		
	}

	@Override
	@Visible(false)
	public GradableJUnitTest findTest(Class aJUnitClass) {
		
		for (GradableJUnitTest aChild:children) {
			if (aChild.getJUnitClass().equals(aJUnitClass)) {
				return aChild;
			}
			if (aChild instanceof GradableJUnitSuite) {
				GradableJUnitTest aResult = ((GradableJUnitSuite) aChild).findTest(aJUnitClass);
				if (aResult!= null) {
					return aResult;
				}
			}
		}
		return null;
	}

	@Override
	@Visible(false)
	public boolean isImplicit() {
		return implicit;
	}

}
