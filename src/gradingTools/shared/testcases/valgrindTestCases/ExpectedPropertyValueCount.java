package gradingTools.shared.testcases.valgrindTestCases;

import java.util.List;
import java.util.Map;

import grader.basics.concurrency.propertyChanges.ConcurrentEventUtility;
import grader.basics.concurrency.propertyChanges.ConcurrentPropertyChange;
import grader.basics.concurrency.propertyChanges.ConcurrentPropertyChangeSupport;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import util.annotations.Explanation;
import util.annotations.MaxValue;
@MaxValue(32)
@Explanation("This test checks if eactly three threads produce traces")
public abstract class ExpectedPropertyValueCount extends PassFailJUnitTestCase {
	Object[][] propertyValueCounts = { 
			{true, 20, 25},
			{false, 20, 25}				
			};
	protected Object[][] propertyValueCounts() {
		return propertyValueCounts;
	}
	

	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {		
		
		ValgrindOutputProduced anOutput = (ValgrindOutputProduced) getFirstPrecedingTestInstance();
		ConcurrentPropertyChangeSupport aConcurrentPropertyChangeSupport =
				anOutput.getConcurrentPropertyChangeSupport();
		Map<Object, List<ConcurrentPropertyChange>> aPropertyValueView = 
				ConcurrentEventUtility.getConcurrentEventListByNewValue(aConcurrentPropertyChangeSupport.getConcurrentPropertyChanges());	
		Object[][] aPropertyValueCounts = propertyValueCounts();
		for (int aPropertyValueIndex = 0; aPropertyValueIndex < aPropertyValueCounts.length; aPropertyValueIndex++ ) {
			Object[] aPropertyValueTuple = aPropertyValueCounts[aPropertyValueIndex];
			Object aPropertyValue =  aPropertyValueTuple[0];
			List<ConcurrentPropertyChange> aConcurrentPropertyChanges = aPropertyValueView.get(aPropertyValue);
			if (aConcurrentPropertyChanges == null) {
				return fail ("Expected property value " + aPropertyValue + " missing in traces ");
			}
			int aMinSize = (int) aPropertyValueTuple[1];
			int aMaxSize =  (int) aPropertyValueTuple[2];
			int aSize = aConcurrentPropertyChanges.size();
			if (aSize < aMinSize || aSize > aMaxSize) {
				return fail("Number of " + aPropertyValue + " traces:" + aSize + " not between " + aMinSize + " and " + aMaxSize);
			}			
			
		}
		return pass("");
		
//		return fail ("Expected number of sources traced:" + anExpectedNumSourcesTraced + " != " + "actual sources traced in:" + aNotifyingSources); 
		
				
	}
	


}
