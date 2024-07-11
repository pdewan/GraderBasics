package gradingTools.shared.testcases.valgrindTestCases;

import java.util.Arrays;
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
import valgrindpp.grader.ValgrindTrace;
@MaxValue(32)
@Explanation("This test checks if eactly three threads produce traces")
public abstract class ExpectedThreadCount extends PassFailJUnitTestCase {
//	List<String> outputs ;
	int EXPECTED_NUM_THREADS_TRACED = 3;
	int expectedNumThreadsTraced() {
		return EXPECTED_NUM_THREADS_TRACED;
	}
//	@Override
//	protected Class precedingTest() {
//		return ProducerConsumerOutput.class;
//	}
	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {		
		
		ValgrindOutputProduced anOutput = (ValgrindOutputProduced) getFirstPrecedingTestInstance();
		ConcurrentPropertyChangeSupport aConcurrentPropertyChangeSupport =
				anOutput.getConcurrentPropertyChangeSupport();
		Thread[] aNotifyingThreads = aConcurrentPropertyChangeSupport.getNotifyingThreads();
		int aNumThreadsTraced = aNotifyingThreads.length;
		int anExpectedNumThreadsTraced = expectedNumThreadsTraced();
		if (aNumThreadsTraced == aNumThreadsTraced ) {
			return pass();
		}
		return fail ("Expected number of threads traced:" + anExpectedNumThreadsTraced + " != " + "actual threads traced:" + Arrays.toString(aNotifyingThreads)); 
		
		
		
		
	}

}
