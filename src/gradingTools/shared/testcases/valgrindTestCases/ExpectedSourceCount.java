package gradingTools.shared.testcases.valgrindTestCases;

import java.util.List;

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
public abstract class ExpectedSourceCount extends PassFailJUnitTestCase {
	int EXPECTED_NUM_SOURCES_TRACED = 2;
	int expectedNumSourcesTraced() {
		return EXPECTED_NUM_SOURCES_TRACED;
	}

	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {		
		
		ValgrindOutputProduced anOutput = (ValgrindOutputProduced) getFirstPrecedingTestInstance();
		ConcurrentPropertyChangeSupport aConcurrentPropertyChangeSupport =
				anOutput.getConcurrentPropertyChangeSupport();
		List aNotifyingSources = aConcurrentPropertyChangeSupport.getNotifyingSources();
		int aNumSourcesTraced = aNotifyingSources.size();
		int anExpectedNumSourcesTraced = expectedNumSourcesTraced();
		if (aNumSourcesTraced == anExpectedNumSourcesTraced ) {
			return pass();
		}
		return fail ("Expected number of sources traced:" + anExpectedNumSourcesTraced + " != " + "actual sources traced in:" + aNotifyingSources); 
		
				
	}
	


}
