package gradingTools.shared.testcases.valgrindTestCases;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import grader.basics.concurrency.propertyChanges.ConcurrentEventUtility;
import grader.basics.concurrency.propertyChanges.ConcurrentPropertyChange;
import grader.basics.concurrency.propertyChanges.ConcurrentPropertyChangeSupport;
import grader.basics.concurrency.propertyChanges.ValgrindConcurrentPropertyChangeSupport;
import grader.basics.execution.AValgrindCommandGenerator;
import grader.basics.execution.RunningProject;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.utils.RunningProjectUtils;
import util.annotations.Explanation;
import util.annotations.MaxValue;
import util.misc.Common;
import util.trace.Tracer;
import valgrindpp.grader.ValgrindTrace;
@MaxValue(32)
@Explanation("This test checks if the traces include calls to pthread_cond_init and pthread_cond_wait with the same argument")
public class InterleavedProducerConsumerThreads extends PassFailJUnitTestCase {
	protected Thread[] producerConsumerThreads;
	public Thread[] getProducerConsumerThreads() {
		return producerConsumerThreads;
	}
	@Override
	protected Class[] precedingTests() {
		return new Class[] {ProducerConsumerOutput.class, ExpectedThreadCount.class};
	}
	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {	

		
//		ProducerConsumerOutput anOutput = (ProducerConsumerOutput) getFirstPrecedingTestInstance();
//		ConcurrentPropertyChangeSupport aConcurrentPropertyChangeSupport =
//				anOutput.getConcurrentPropertyChangeSupport();
//		Thread[] aNotifyingThreads = aConcurrentPropertyChangeSupport.getNotifyingThreads();
//		
		ValgrindOutputProduced anOutput = (ValgrindOutputProduced) getFirstPrecedingTestInstance();
		ValgrindConcurrentPropertyChangeSupport aConcurrentPropertyChangeSupport =
				anOutput.getConcurrentPropertyChangeSupport();
		
		producerConsumerThreads = aConcurrentPropertyChangeSupport.getReturnedThreads().toArray(producerConsumerThreads);
//				new Thread[] {aNotifyingThreads[1], aNotifyingThreads[2]};
		
		
		ConcurrentPropertyChange[] anOrginalEvents = aConcurrentPropertyChangeSupport.getConcurrentPropertyChanges();
		boolean anInterleavingOccurred = ConcurrentEventUtility.someInterleaving(anOrginalEvents, producerConsumerThreads, null);
		if (anInterleavingOccurred) {
			return pass();
		}
		return fail ("No interleaving of threads:" + Arrays.toString(producerConsumerThreads));	
		
	}

}
