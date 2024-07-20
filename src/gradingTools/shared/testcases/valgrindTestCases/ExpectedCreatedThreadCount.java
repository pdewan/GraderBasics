package gradingTools.shared.testcases.valgrindTestCases;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import grader.basics.concurrency.propertyChanges.ConcurrentEventUtility;
import grader.basics.concurrency.propertyChanges.ConcurrentPropertyChange;
import grader.basics.concurrency.propertyChanges.ConcurrentPropertyChangeSupport;
import grader.basics.concurrency.propertyChanges.ValgrindConcurrentPropertyChangeSupport;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import util.annotations.Explanation;
import util.annotations.MaxValue;
import util.trace.Tracer;
import valgrindpp.grader.ValgrindTrace;
@MaxValue(32)
@Explanation("This test checks if eactly three threads produce traces")
public abstract class ExpectedCreatedThreadCount extends PassFailJUnitTestCase {
//	List<String> outputs ;
	int EXPECTED_NUM_THREADS_CREATED= 2;
	int expectedNumThreadsCreated() {
		return EXPECTED_NUM_THREADS_CREATED;
	}
//	@Override
//	protected Class precedingTest() {
//		return ProducerConsumerOutput.class;
//	}
	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {		
		
		ValgrindOutputProduced anOutput = (ValgrindOutputProduced) getFirstPrecedingTestInstance();
		ValgrindConcurrentPropertyChangeSupport aConcurrentPropertyChangeSupport =
				anOutput.getConcurrentPropertyChangeSupport();
		List<Thread> aCreatedThreads = aConcurrentPropertyChangeSupport.getReturnedThreads();
		Tracer.info(this, "Created threads:" + aCreatedThreads);
		int aNumThreadsCreated = aCreatedThreads.size();
		int anExpectedNumThreadsCreated = expectedNumThreadsCreated();
		if (aNumThreadsCreated == anExpectedNumThreadsCreated ) {
			return pass();
		}
		return fail ("Expected number of threads created:" + 
				anExpectedNumThreadsCreated + " != " + "actual threads created in:" +
				aCreatedThreads); 
		
		
		
		
	}

}
