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
@Explanation("This test checks if the lock and unlock calls of the producer and consumer threads involve exactlttwo mutexes")
public class MutexAlwaysLockedOrUnlockedByAChildThread extends PassFailJUnitTestCase {
	
	static int MIN_PER_CHILD_PER_SOURCE_EVENTS = 5;
	@Override
	protected Class[] precedingTests() {
		return new Class[] {
				ProducerConsumerOutput.class, 
				ExpectedThreadCount.class,
				InterleavedProducerConsumerThreads.class,
				TwoMutexesPerChildThread.class
				};
	}
	int minPerChildPerSourceEvents() {
		return MIN_PER_CHILD_PER_SOURCE_EVENTS ;
	}
	ConcurrentPropertyChange[] dummyConcurrentPropertyChange = {};
	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {		
		ProducerConsumerOutput anOutput = (ProducerConsumerOutput) getFirstPrecedingTestInstance();
		TwoMutexesPerChildThread aTwoMutexesPerChild =
				(TwoMutexesPerChildThread) getLastPrecedingTestInstance();
		Map<Thread, Map<Object, List<ConcurrentPropertyChange>>> aThreadSourceMap =
		aTwoMutexesPerChild.getThreadSourceMap();
		int aNumPreviousThreadEvents = -1; 
		int aNumCurrentThreadEvents = 0;
		Thread aPreviousThread = null;

		for (Thread aThread:aThreadSourceMap.keySet()) {
			Map<Object, List<ConcurrentPropertyChange>> aSourceMap = aThreadSourceMap.get(aThread);
			int aNumPreviousSourceEvents = -1;
			int aNumCurrentSoureEvents = 0;
			for (Object aSource:aSourceMap.keySet()) {
				List<ConcurrentPropertyChange> aConcurrentPropertyChanges = aSourceMap.get(aSource);
				int aNumCurrentSourceEvents = aConcurrentPropertyChanges.size();
				Object aPreviousSource = null;
				Tracer.info(this, "Thread:" + aThread + " Mutex: " + aSource + " Properties:" +  aConcurrentPropertyChanges);
				if (aNumPreviousSourceEvents != -1 && aNumCurrentSourceEvents != aNumPreviousSourceEvents ) {
					return fail("Thread " + aThread + " Mutex " + aSource + " does not have  the same # events as mutex " + aPreviousSource);
				}
				aNumPreviousSourceEvents = aNumCurrentSourceEvents;
				aPreviousSource = aSource;
				aNumCurrentThreadEvents += aNumCurrentSourceEvents;				
			}
			if (aNumPreviousThreadEvents != -1 && aNumCurrentThreadEvents != aNumPreviousThreadEvents ) {
				return fail("Thread " + aThread +" does not have  the same # events as thread " + aPreviousThread);
			}
			aNumPreviousThreadEvents = aNumCurrentThreadEvents;
			aPreviousThread = aThread;
			
		}		
		return pass();		
	}
}
