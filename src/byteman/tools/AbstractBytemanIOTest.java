package byteman.tools;

import java.util.ArrayList;
import java.util.List;

import byteman.tools.exampleTestCases.BytemanClassRegistryProvided;
import byteman.tools.exampleTestCases.BytemanRegistry;
import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.execution.RunningProject;
import grader.basics.junit.JUnitTestsEnvironment;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.CurrentProjectHolder;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.utils.RunningProjectUtils;
import util.trace.Tracer;

public abstract class AbstractBytemanIOTest extends PassFailJUnitTestCase{

	
	protected abstract Class<?> getTarget();
	protected abstract Object[] getArgs();
	protected abstract String [] getRegexes();
	protected abstract String [] getInputs();
	
	private BytemanRegistry registry;
	
	protected BytemanRegistry getRegistry() {
		if(registry==null) {
			registry = ((BytemanClassRegistryProvided)JUnitTestsEnvironment.getAndPossiblyRunGradableJUnitTest(BytemanClassRegistryProvided.class)).getRegistry();
		}
		return registry;
	}
	
	@Override
	public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
		try {
			if(getRegistry()==null) 
				return fail("Prereq tests did not pass");
			
			String anOut = getTraceOut();

			if(checker(cleanResult(anOut),true,getRegexes())>0.95) 
				return pass();
			else
				return fail("see console");
		}catch(Exception e) {
			e.printStackTrace();
			return fail(e.getMessage());
		}
	}
	
	protected String getTraceOut() {
		return runMain(getTarget(),(String[])getArgs());
	}
	
	protected String runMain(Class<?> target, String [] args) {
		if(args == null) {
			args = new String[0];
		}
		
		Tracer.showInfo(true);
		CurrentProjectHolder.setProject(".java");
		Project aProject = CurrentProjectHolder.getCurrentProject();
		List<String> aCommand = BasicStaticConfigurationUtils.getBasicCommand();
		List<String> aModifiedCommand = new ArrayList<>(aCommand);
		aModifiedCommand.add(1,"-javaagent:./Logs/LocalChecks/agent.jar");
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setGraderBasicCommand(aModifiedCommand);
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setForkInProjectFolder(false);
		
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setEntryPoint(target.getName());
		RunningProject aRunningProject = RunningProjectUtils.runProject(aProject, 100, args, getInputs());
		
		aRunningProject.await();
		String anOut = aRunningProject.getOutput();
		
		BasicStaticConfigurationUtils.setBasicCommandToDefaultEntryTagCommand();
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setEntryPoint(null);
		
		return anOut;
	}
	
	protected List<String> cleanResult(String out){
		List<String> retval = new ArrayList<>();
		String [] lines = out.split("\n");
		boolean traceStartFound = false;
		for(String line:lines)
			if(traceStartFound || line.matches("GR.*")) {
				retval.add(line);
				traceStartFound = true;
			}
		return retval;
	}

	protected float checker(List<String> output, boolean printOnFail, String ... regexs) {
		int passed = 0;
		List<String> matches = new ArrayList<>();
		
		for(String outline:output) {
			if(passed>=regexs.length) break;
			if(outline.matches(regexs[passed])) {
				matches.add("Regex:" + regexs[passed]+" matched line: " + outline);
				passed++;
			}
		}
		
		if(passed<regexs.length) {
			System.out.println("Collected Output:");
			for(int i=0;i<output.size();i++)
				System.out.println(i+" "+output.get(i));
			System.out.println("Regexes matched:");
			for(int i=0;i<matches.size();i++)
				System.out.println(matches.get(i));
			System.out.println("Regexes not matched:");
			for(int i=matches.size();i<regexs.length;i++)
				System.out.println(regexs[i]);
		}
		
		if(passed == regexs.length)
			return 1;
		return passed/(float)regexs.length;
	}
	
}
