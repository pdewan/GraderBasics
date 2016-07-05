package grader.basics.junit;

import grader.basics.project.NotGradableException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.runner.Description;

import util.annotations.Position;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.annotations.Visible;

// why is the implicit pattern stack pattern?
@StructurePattern(StructurePatternNames.LIST_PATTERN)
public class AGradableJUnitSuite extends AGradableJUnitTest implements
		GradableJUnitSuite, PropertyChangeListener {
	Class[] previousPassClasses, previousFailClasses, previousPartialPassClasses;
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

	@Visible(false)
	public void add(GradableJUnitTest anElement) {
		anElement.setTopLevelSuite(getTopLevelSuite()); // asuume children are added top to bottom
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
	protected void testRunStarted() {
		Description aDescription = Description.createTestDescription("a test", "a test", 0);
		RunNotifierFactory.getRunNotifier().fireTestRunStarted(aDescription);
	}
	
	protected void testRunFinished() {
		Description aDescription = Description.createTestDescription("a test", "a test", 0);
		RunNotifierFactory.getRunNotifier().fireTestFinished(aDescription);
		RunNotifierFactory.getRunNotifier().fireTestRunFinished(null);

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
		Description aDescription = Description.createSuiteDescription(aTest
				.getJUnitClass());
		RunNotifierFactory.getRunNotifier().fireTestRunStarted(aDescription);
		aTest.test();
		RunNotifierFactory.getRunNotifier().fireTestRunFinished(null);
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
	public int numLeafNodeDescendents() {
		return children.size();
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
			TestCaseResult aChildResult = aTest.test();
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

	@Visible(true)
	@Position(0)
	@Override
	public double getScore() {
		double retVal = 0;
		for (GradableJUnitTest aTest : children) {
			retVal += aTest.getScore();
		}
		return retVal;
	}

	@Visible(false)
	public Double getMaxScore() {
		// if (maxScore == null) {
		// double retVal = 0;
		// for (GradableJUnitTest aTest:children) {
		// retVal += aTest.getMaxScore();
		// }
		// maxScore = retVal;
		// }
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
	protected void maybeSetDefaultMaxScore() {

	}

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
		String retVal = getName() + "," + getScore() + "\n";
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
		if (!"Score".equals(evt.getPropertyName()))
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
	public Class[] getLeafClasses() {
		if (leafClasses == null) {
			List<Class> aLeafClasses = new ArrayList();
			for (GradableJUnitTest aTest : children) {
				aLeafClasses.addAll(Arrays.asList(aTest.getLeafClasses()));
			}
			leafClasses = aLeafClasses.toArray(emptyClassArray);
		}
		return leafClasses;
	}

	@Visible(false)
	@Override
	public Class[] getPassClasses() {

		List<Class> aChildrenClasses = new ArrayList();
		for (GradableJUnitTest aTest : children) {
			aChildrenClasses.addAll(Arrays.asList(aTest.getPassClasses()));
		}
		return aChildrenClasses.toArray(emptyClassArray);
	}
	@Visible(false)
	@Override
	public Class[] getFailedClasses() {

		List<Class> aChildrenClasses = new ArrayList();
		for (GradableJUnitTest aTest : children) {
			aChildrenClasses.addAll(Arrays.asList(aTest.getFailedClasses()));
		}
		return aChildrenClasses.toArray(emptyClassArray);
	}
	protected void refreshPreviousClasses() {
		previousPassClasses = getPassClasses();
		previousFailClasses = getFailedClasses();
		previousPartialPassClasses = getPartialPassClasses();
	}

}
