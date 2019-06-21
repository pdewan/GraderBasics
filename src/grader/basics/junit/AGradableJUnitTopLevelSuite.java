package grader.basics.junit;

import grader.basics.util.ClassComparator;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.runner.Description;

import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.annotations.Visible;
@StructurePattern(StructurePatternNames.LIST_PATTERN)
/**
 * Top-level node in tree created by LocalChecks
 * @author dewan
 *
 */
public class AGradableJUnitTopLevelSuite extends AGradableJUnitSuite {
	public AGradableJUnitTopLevelSuite(Class aJUnitClass) {
		super(aJUnitClass);
	}

	@Visible(false)
	public String getName() {
		return super.getName();
	}
	public void testAll() {
//		Description aDescription = Description.createSuiteDescription(getJUnitClass());
//		RunNotifierFactory.getRunNotifier().fireTestRunStarted(aDescription);
		try {
		testRunStarted(this);
		super.testAll();
		testRunFinished(this);
		} catch (PropertyVetoException e) {
			System.err.println(e.getMessage());
		}
//		RunNotifierFactory.getRunNotifier().fireTestRunFinished(null);

	}
//
//	@Visible(false)
//	public void open(GradableJUnitTest aTest) {
//		// System.out.println ("opened: " + aTest);
////		Description aDescription = Description.createSuiteDescription(aTest
////				.getJUnitClass());
////		RunNotifierFactory.getRunNotifier().fireTestRunStarted(aDescription);
//		
//		testRunStarted();
//		aTest.test();
//		testRunFinished();
////		RunNotifierFactory.getRunNotifier().fireTestRunFinished(null);
//	}
	
	
	@Override
	@Visible(false)
	public int numLeafNodeDescendents() {
		int retVal = 0;
		for (GradableJUnitTest aTest:children) {			
			int aNumGrandChildren = aTest.numLeafNodeDescendents();
			if (aNumGrandChildren == 0) // aleaf node
				retVal++;
			else
				retVal += aNumGrandChildren;
			
		}
		return retVal;
	}
	@Override
	@Visible(false)
	public int numInternalNodeDescendents() {
		int retVal = 0;
		for (GradableJUnitTest aTest:children) {			
			int aNumGrandChildren = aTest.numLeafNodeDescendents();
			if (aNumGrandChildren > 1) // do not count an artifical parent
				retVal++;		
			
		}
		return retVal;
	}
	
	protected void maybeProcessTestRunStarted(PropertyChangeEvent evt) {
		if (!TEST_RUN_STARTED.equals(evt.getPropertyName())) {
				return;
		}
		GradableJUnitTest aTest = (GradableJUnitTest) evt.getNewValue();
		try {
			testRunStarted(aTest);
		} catch (PropertyVetoException e) {
			// this should never happen
			e.printStackTrace();
		}
//		String aClassName = aTest.getJUnitClass().getSimpleName();
//		String aName = aTest.getExplanation();
//		String anId = aTest.getJUnitClass().getName();
//		// aClassName and aName not really needed, as aTest has that info.
//		// aTest is not really an id, but we will ovrload it
//		Description aDescription = Description.createTestDescription(aClassName, aName, aTest);
//		refreshPreviousClasses();	
//		RunNotifierFactory.getRunNotifier().fireTestRunStarted(aDescription);
	}
	protected void testRunStarted(GradableJUnitTest aTest) throws PropertyVetoException{
		String aClassName = aTest.getJUnitClass().getSimpleName();
		String aName = aTest.getExplanation();
//		String anId = aTest.getJUnitClass().getName();
		// aClassName and aName not really needed, as aTest has that info.
		Description aDescription = Description.createTestDescription(aClassName, aName, this);
		RunVetoerFactory.getOrCreateRunVetoer().vetoableChange(new PropertyChangeEvent(this, TEST_RUN_STARTED, this, null));

		refreshPreviousClasses();	
		RunNotifierFactory.getOrCreateRunNotifier().fireTestRunStarted(aDescription);
	}
	
	protected void testRunFinished(GradableJUnitTest aTest) {
		RunNotifierFactory.getOrCreateRunNotifier().fireTestRunFinished(null);
//		RunNotifierFactory.getOrCreateRunNotifier().fireTestRunFinished(aTest);


//		Description aDescription = Description.createTestDescription(GradableJUnitTest.TEST_RUN_STARTED, "a test", 0);
//		RunNotifierFactory.getRunNotifier().fireTestFinished(aDescription);
//		notifyTestRunStarted(aTest);

	}
	

//	// tests that were failed or partially successful that are now successful
//	protected Set<Class> getNewlySuccesfulTests() {
//		
//	// tests that were failed that are now partially successful
//    protected Set<Class> getPositivePartialSucessfulTests() {
//		return null;
//	}
//	// tests that were suucessful that are now failed
//	protected Set<Class>  getNegativePartialSuccessfulTests() {
//		return null;
//		
//	}
//	// tests that were not failed that are now failed
//	protected Set<Class> getNewlyFailedTests () {
//		return null;
//
//	}
    protected void maybeProcessTestRunFinished(PropertyChangeEvent evt) {
    	if (!TEST_RUN_FINISHED.equals(evt.getPropertyName())) {
				return;
    	}
//		RunNotifierFactory.getRunNotifier().fireTestRunFinished(null);
    	testRunFinished((GradableJUnitTest) evt.getNewValue());

	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		maybeProcessTestRunStarted(evt);
		maybeProcessTestRunFinished(evt);
		super.propertyChange(evt);
	}
	
//	public String getText() {
//		String retVal = getName() + "\n";
//		for (GradableJUnitTest aTest:children) {
//			retVal += aTest + "\n";
//		}
//		return retVal;
//	}
	
}
