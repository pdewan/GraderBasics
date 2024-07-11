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
public abstract class ExpectedPropertyCount extends PassFailJUnitTestCase {
	Object[][] propertyCounts = { 
			{"lock", 40, 40},
			{"init", 1, 1}				
			};
	protected Object[][] propertyCounts() {
		return propertyCounts;
	}
	

	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {		
		
		ValgrindOutputProduced anOutput = (ValgrindOutputProduced) getFirstPrecedingTestInstance();
		ConcurrentPropertyChangeSupport aConcurrentPropertyChangeSupport =
				anOutput.getConcurrentPropertyChangeSupport();
		Map<String, List<ConcurrentPropertyChange>> aPropertyView = 
				ConcurrentEventUtility.getConcurrentEventListByProperty(aConcurrentPropertyChangeSupport.getConcurrentPropertyChanges());	
		Object[][] aPropertyCounts = propertyCounts();
		for (int aPropertyIndex = 0; aPropertyIndex < aPropertyCounts.length; aPropertyIndex++ ) {
			Object[] aPropertyTuple = aPropertyCounts[aPropertyIndex];
			String aPropertyName = (String) aPropertyTuple[0];
			List<ConcurrentPropertyChange> aConcurrentPropertyChanges = aPropertyView.get(aPropertyName);
			if (aConcurrentPropertyChanges == null) {
				return fail ("Expected property " + aPropertyName + " missing in traces ");
			}
			int aMinSize = (int) aPropertyTuple[1];
			int aMaxSize =  (int) aPropertyTuple[2];
			int aSize = aConcurrentPropertyChanges.size();
			if (aSize < aMinSize || aSize > aMaxSize) {
				return fail("Number of " + aPropertyName + " traces:" + aSize + " not bewteen " + aMinSize + " and " + aMaxSize);
			}			
			
		}
		return pass("");
		
//		return fail ("Expected number of sources traced:" + anExpectedNumSourcesTraced + " != " + "actual sources traced in:" + aNotifyingSources); 
		
				
	}
	


}
