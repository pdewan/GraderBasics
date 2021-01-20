package grader.basics.junit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.AbstractMap;

import org.apache.commons.collections.keyvalue.AbstractKeyValue;
import org.apache.commons.collections.map.AbstractHashedMap;
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
	
	public String toString() {
		return "[" + toScoreString()+ "pts ]";
	}

	@Visible(false)
	public String getName() {
		return super.getName();
	}
	public void testAll() {

		try {
		testRunStarted(this);
		JUnitTestsEnvironment.clearCachedJUnitTestCases();

		super.testAll();
		testRunEnded(this);
		} catch (PropertyVetoException e) {
			System.err.println(e.getMessage());
		}

	}

	
	
//	@Override
//	@Visible(false)
//	public int numLeafNodeDescendents() {
//		int retVal = 0;
//		for (GradableJUnitTest aTest:children) {			
//			int aNumGrandChildren = aTest.numLeafNodeDescendents();
//			if (aNumGrandChildren == 0) // aleaf node
//				retVal++;
//			else
//				retVal += aNumGrandChildren;
//			
//		}
//		return retVal;
//	}
//	@Override
//	@Visible(false)
//	public int numInternalNodeDescendents() {
//		int retVal = 0;
//		for (GradableJUnitTest aTest:children) {			
//			int aNumGrandChildren = aTest.numLeafNodeDescendents();
//			if (aNumGrandChildren > 1) // do not count an artifical parent
//				retVal++;		
//			
//		}
//		return retVal;
//	}
	
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

	}
	protected void testRunStarted(GradableJUnitTest aTest) throws PropertyVetoException{
//		String aClassName = aTest.getJUnitClass().getSimpleName();
		String aClassName = aTest.getSimpleName();

		String aName = aTest.getExplanation();
//		String anId = aTest.getJUnitClass().getName();
		// aClassName and aName not really needed, as aTest has that info.
		AbstractMap.SimpleEntry<GradableJUnitSuite, GradableJUnitTest> anEntry = new AbstractMap.SimpleEntry(this, aTest);
//		Description aDescription = Description.createTestDescription(aClassName, aName, this);
		Description aDescription = Description.createTestDescription(aClassName, aName, anEntry);

		RunVetoerFactory.getOrCreateRunVetoer().vetoableChange(new PropertyChangeEvent(this, TEST_RUN_STARTED, this, null));

		refreshPreviousClasses();	
	
		RunNotifierFactory.getOrCreateRunNotifier().fireTestRunStarted(aDescription);
	}
	
	protected void testRunEnded(GradableJUnitTest aTest) {
		RunNotifierFactory.getOrCreateRunNotifier().fireTestRunFinished(null);
//		RunNotifierFactory.getOrCreateRunNotifier().fireTestRunFinished(aTest);




	}
	


    protected void maybeProcessTestRunFinished(PropertyChangeEvent evt) {
    	if (!TEST_RUN_ENDED.equals(evt.getPropertyName())) {
				return;
    	}
//		RunNotifierFactory.getRunNotifier().fireTestRunFinished(null);
    	testRunEnded((GradableJUnitTest) evt.getNewValue());

	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		maybeProcessTestRunStarted(evt);
		maybeProcessTestRunFinished(evt);
		super.propertyChange(evt);
	}
	

	
}
