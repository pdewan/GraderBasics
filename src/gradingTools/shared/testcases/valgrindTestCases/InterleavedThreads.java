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
@Explanation("This test checks if the traces include calls to pthread_cond_init and pthread_cond_wait with the same argument")
public abstract class InterleavedThreads extends PassFailJUnitTestCase {
	protected Thread[] allThreads;
	protected Thread[] interleavedThreads;
	protected Thread[] interleavedThreads() {
		return allThreads;
	}
	protected Thread[] allThreads() {
		return allThreads;
	}
	
	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {	

		
		ValgrindOutputProduced anOutput = (ValgrindOutputProduced) getFirstPrecedingTestInstance();
		ConcurrentPropertyChangeSupport aConcurrentPropertyChangeSupport =
				anOutput.getConcurrentPropertyChangeSupport();
		ConcurrentPropertyChange[] anOrginalEvents = aConcurrentPropertyChangeSupport.getConcurrentPropertyChanges();

		allThreads = aConcurrentPropertyChangeSupport.getNotifyingThreads();
		Thread[] anInterleavedThreads = interleavedThreads();
		boolean anInterleavingOccurred = ConcurrentEventUtility.someInterleaving(anOrginalEvents, anInterleavedThreads, null);
		if (anInterleavingOccurred) {
			return pass();
		}
		return fail ("No interleaving of threads:" + Arrays.toString(anInterleavedThreads));	
		
	}

}
