package gradingTools.valgrindTestCases;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import grader.basics.execution.RunningProject;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.utils.RunningProjectUtils;
import valgrindpp.grader.ValgrindTrace;

public class MutexTest extends PassFailJUnitTestCase {

	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {
		RunningProject noInputRunningProject = RunningProjectUtils.runProject(project, 1);
		String mutexOut = noInputRunningProject.await();
		Map<String, List<String>> aLinesMap = noInputRunningProject.getProcessOutputLines();
		List<String> aMainLines = aLinesMap.get("main");
		List<ValgrindTrace> aMainTraces = new ArrayList();
		List<String> aMainOutputs = new ArrayList();
		for (String aLine: aMainLines) {
			try {
				aMainTraces.add(new ValgrindTrace(aLine));
			} catch (Exception e) {
				aMainOutputs.add(aLine);
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
		
		
		
		String anOut = noInputRunningProject.getOutput();
		
		System.out.println("mutex_out\n " + anOut);
		return pass();
	}

}
