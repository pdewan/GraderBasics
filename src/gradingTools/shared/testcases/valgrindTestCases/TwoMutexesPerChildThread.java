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
public class TwoMutexesPerChildThread extends PassFailJUnitTestCase {
	int EXPECTED_PER_THREAD_SOURCES = 2;
	
	int expectedPerThreadSources() {
		return EXPECTED_PER_THREAD_SOURCES;
	}
	Map<Thread, Map<Object, List<ConcurrentPropertyChange>>> threadSourceMap = new HashMap();

	//	List<Lsst<ConcurrentPropertyChange>>  byThreadAndSourceEvents = new ArrayList();
//
//	public List<List<ConcurrentPropertyChange>> getByThreadAndSourceEvents() {
//		return byThreadAndSourceEvents;
//	}
	public Map<Thread, Map<Object, List<ConcurrentPropertyChange>>> getThreadSourceMap() {
		return threadSourceMap;
	}
	@Override
	protected Class[] precedingTests() {
		return new Class[] {
				ProducerConsumerOutput.class, 
				ExpectedThreadCount.class,
				InterleavedProducerConsumerThreads.class,
				};
	}
	ConcurrentPropertyChange[] dummyConcurrentPropertyChange = {};
	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {		
		
		ProducerConsumerOutput anOutput = (ProducerConsumerOutput) getFirstPrecedingTestInstance();
		InterleavedProducerConsumerThreads anInterleavedProducerConsumerThreads =
				(InterleavedProducerConsumerThreads) getLastPrecedingTestInstance();	
		
		
		ConcurrentPropertyChangeSupport aConcurrentPropertyChangeSupport =
				anOutput.getConcurrentPropertyChangeSupport();		
		ConcurrentPropertyChange[] anOriginalEvents = aConcurrentPropertyChangeSupport.getConcurrentPropertyChanges();
		Map<Thread, List<ConcurrentPropertyChange>> aByThreadView = ConcurrentEventUtility
		.getConcurrentPropertyChangeListByThread(anOriginalEvents);
		
		Thread[] aProducerConsumerThreads = anInterleavedProducerConsumerThreads.getProducerConsumerThreads();
		for (Thread aThread:aProducerConsumerThreads) {
			Map<Object, List<ConcurrentPropertyChange> > aSourceMap = new HashMap();

			ConcurrentPropertyChange[] aThreadEvents = aByThreadView.get(aThread).toArray(dummyConcurrentPropertyChange);
			Map<String, List<ConcurrentPropertyChange>> aByThreadAndPropertyView = ConcurrentEventUtility.getConcurrentEventListByProperty(aThreadEvents);
			ConcurrentPropertyChange[] aLockChanges = aByThreadAndPropertyView.get("lock").toArray(dummyConcurrentPropertyChange);
			if (aLockChanges.length == 0) {
				return fail("Thread " + aThread.getName() + " has no traces ");
			}
			Map<Object, List<ConcurrentPropertyChange>> aBySourceLockView =  ConcurrentEventUtility.getConcurrentEventListBySource(aLockChanges);	
			Set<Object> aSources = aBySourceLockView.keySet();
			if (aBySourceLockView.size() != expectedPerThreadSources()) {
				return fail("Thread " + aThread.getName() + " should have  " + expectedPerThreadSources() + "mutexes but has the following " + aSources);
			}
			for (Object aSource:aSources) {

				List<ConcurrentPropertyChange> aByThreadAndSourceEvents = aBySourceLockView.get(aSource);
				aSourceMap.put(aSource, aByThreadAndSourceEvents );
//				byThreadAndSourceEvents.add(aByThreadAndSourceEvents);
			}
			threadSourceMap.put(aThread, aSourceMap);
					
		}
		return pass();
	
		
	}

}
