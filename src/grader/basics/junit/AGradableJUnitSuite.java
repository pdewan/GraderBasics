package grader.basics.junit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import grader.basics.project.NotGradableException;

//import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;

import util.annotations.Position;
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
	

	public AGradableJUnitSuite(Class aJUnitClass) {
		super(aJUnitClass);
		topLevelSuite = this; // will be overridden by parent if they exist
	}
	

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
	protected void notifyTestRunEnded(GradableJUnitTest aTest) {
		propertyChangeSupport.firePropertyChange(TEST_RUN_FINISHED, aTest, null);
//		
//		String aClassName = aTest.getJUnitClass().getSimpleName();
//		String aName = aTest.getExplanation();
		
	}
	protected void testRunStarted(GradableJUnitTest aTest) throws PropertyVetoException {

		RunVetoerFactory.getOrCreateRunVetoer().vetoableChange(new PropertyChangeEvent(this, TEST_RUN_STARTED, this, null));

		notifyTestRunStarted(aTest);
	}
	
	protected void testRunFinished(GradableJUnitTest aTest) {
//		Description aDescription = Description.createTestDescription(GradableJUnitTest.TEST_RUN_STARTED, "a test", 0);
//		RunNotifierFactory.getRunNotifier().fireTestFinished(aDescription);
		notifyTestRunStarted(aTest);

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
		// System.out.println ("opened: " + aTest);
//		Description aDescription = Description.createSuiteDescription(aTest
//				.getJUnitClass());
//		RunNotifierFactory.getRunNotifier().fireTestRunStarted(aDescription);
		try {
		testRunStarted(aTest);
		aTest.test();
		testRunFinished(aTest);
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
//		if (!isDefinesMaxScore()) { // no maxScore annotation

		if (maxScore == DEFAULT_SCORE) { // no maxScore annotation
			return aMaxScoreAssignmentResult;
		}
		double anUnassignedMaxScore = maxScore - aMaxScoreAssignmentResult.assignedScores;
		if (anUnassignedMaxScore < 0) {
			Tracer.info(this, "Ignoring MaxScore:" + maxScore + " of " + jUnitClass.getSimpleName() + " as it is less than sum total of Max Scorse of descendents:" + aMaxScoreAssignmentResult.assignedScores );
			return aMaxScoreAssignmentResult;
		}
		double aDistributedScore = anUnassignedMaxScore/aMaxScoreAssignmentResult.unassignedLeafNodes.size();
		for (GradableJUnitTest anUnassignedLeafNode:aMaxScoreAssignmentResult.unassignedLeafNodes) {
			anUnassignedLeafNode.setMaxScore(aDistributedScore);
		}
		aMaxScoreAssignmentResult.unassignedLeafNodes.clear();
		aMaxScoreAssignmentResult.assignedScores = maxScore;
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
	// @Visible(false)
	public void testAll() {
		test();
	}

	@Visible(false)
	public TestCaseResult test() throws NotAutomatableException,
			NotGradableException {
		for (GradableJUnitTest aTest : children) {
			if (aTest.getJUnitTestCase() != null) {
				System.out.println("Not automatically rerunning:" + jUnitClass + ".  Please run it individually");
			} else {
			TestCaseResult aChildResult = aTest.test();
			}
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

	@Visible(false)
	public Double getMaxScore() {
		
		return maxScore;
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

}
