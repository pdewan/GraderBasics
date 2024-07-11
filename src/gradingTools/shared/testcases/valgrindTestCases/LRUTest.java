package gradingTools.shared.testcases.valgrindTestCases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import grader.basics.execution.RunningProject;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.utils.RunningProjectUtils;
import valgrindpp.grader.ValgrindTrace;

public class LRUTest extends PassFailJUnitTestCase {
	List<String> outputs ;
	List<ValgrindTrace> traces;
	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {
		RunningProject noInputRunningProject = RunningProjectUtils.runProject(project, 1);
		String mutexOut = noInputRunningProject.await();
		Map<String, List<String>> aLinesMap = noInputRunningProject.getProcessOutputLines();
		List<String> aMainLines = aLinesMap.get("main");
		traces = new ArrayList();
		outputs = new ArrayList();
		for (String aLine: aMainLines) {
			try {
				traces.add(new ValgrindTrace(aLine));
			} catch (Exception e) {
				outputs.add(aLine);
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
		
		
		
//		String anOut = noInputRunningProject.getOutput();
//		
//		System.out.println("mutex_out\n " + anOut);
		boolean aBlockOnConditionVariables = blockOnConditionVariables();
		boolean aReleaseConditionedWaiters = releaseConditionedWaiters();
		boolean aBroadcastVsSignal = broadcastVsSignal();
		if (!aBlockOnConditionVariables) {
			return fail("block on condition variables failed");
		}
		if (!aReleaseConditionedWaiters) {
			return fail("release failed");
		}
		if (!aBroadcastVsSignal) {
			return fail("brodcast vs signal failed");
		}
		return pass();
		
	}
	private boolean blockOnConditionVariables() {
		
		Set<String> seen = new HashSet<String>(), inits = new HashSet<String>();
		boolean called = false;
		
		for(ValgrindTrace trace: traces) {
			if(trace.fnname.equals("pthread_cond_init")) {
				seen.add(trace.arguments[0]);
				inits.add(trace.arguments[0]);
				called = true;
			}
			
			if(trace.fnname.equals("pthread_cond_wait")) {
				if(!inits.contains(trace.arguments[0])) return false;
				return true;
			}
		}
		
		return seen.size() == 0 && called;
	}
private boolean releaseConditionedWaiters() {
		
		Set<String> waiters = new HashSet<String>(), seen = new HashSet<String>();
		for(ValgrindTrace trace: traces) {
			if(trace.fnname.equals("pthread_cond_wait")) {				
				if(!seen.contains(trace.arguments[0])) 
						waiters.add(trace.arguments[0]);
				seen.add(trace.arguments[0]);
			} 
			
			if(trace.fnname.equals("pthread_cond_broadcast") 
				|| trace.fnname.equals("pthread_cond_signal")){
				waiters.remove(trace.arguments[0]);
			}
		}
		
		return waiters.size() == 0;
	}
private boolean broadcastVsSignal() {
	
	Map<Long, Boolean> threadState = new HashMap<Long, Boolean>();
	
	for(ValgrindTrace trace: traces) {
		switch(trace.fnname) {
		case "clean": 
			Boolean calledSignal = threadState.get(trace.thread);
			if(calledSignal != null && calledSignal) {
				return false;
			}
			break;
		case "reference": case "shutdown_threads":
			threadState.put(trace.thread, false);
			break;
		case "pthread_cond_signal":
			threadState.put(trace.thread, true);
			break;
		}
	}
	
	return true;
}
}
