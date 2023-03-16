package byteman.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import byteman.tools.exampleTestCases.BytemanClassRegistryProvided;
import byteman.tools.exampleTestCases.BytemanRegistry;
import byteman.tools.exampleTestCases.factorial.FactorialIsRecursive;
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
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodCalledTestCase;
import gradingTools.utils.RunningProjectUtils;
import unc.checks.ComprehensiveVisitCheck;
import unc.symbolTable.STMethod;
import util.trace.Tracer;

public abstract class AbstractBytemanIOTest extends PassFailJUnitTestCase{

	
//	protected abstract Class<?> getTarget();
	protected abstract Object[] getArgs();
	protected abstract String [] getRegexes();
	protected abstract String [] getInputs();
	
//	private BytemanRegistry registry;
//	
//	protected BytemanRegistry getRegistry() {
//		if(registry==null) {
//			registry = ((BytemanClassRegistryProvided)JUnitTestsEnvironment.getAndPossiblyRunGradableJUnitTest(BytemanClassRegistryProvided.class)).getRegistry();
//		}
//		return registry;
//	}
	
	
	
	
	public static String removeEscapes(String aPossiblyEscapedString) {
		return aPossiblyEscapedString.replace("\\", "");
	}
	
	public static String removeJavaLang(String withPossibleJavaLangs) {
		return withPossibleJavaLangs.replace("java.lang.", "");
	}


	protected  CheckstyleMethodCalledTestCase checkStyleMethodCalledTest() {
		return null;
	}
	
	protected int numExpectedCalls() {
		return -1;
	}
	
	protected Object[] expectedReturnValues() {
		return null;
	}
	
	protected String[] expectedThreadNames() {
		return null;
	}
	
	/*
	 DN: {byteman.examples.BytemanMerge=@MergeSort, byteman.examples.RecursiveFactorial=@Factorial}
GR*** Thread: main has called @Factorial.<init>() void from java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) with params: 
Running method: factorial in class: byteman.examples.RecursiveFactorial
GR*** Thread: main has called @Factorial.factorial(java.lang.Integer) java.lang.Integer from java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) with params: 5 
GR*** Thread: main has called @Factorial.factorial(java.lang.Integer) java.lang.Integer from byteman.examples.RecursiveFactorial.factorial(RecursiveFactorial.java:10) with params: 4 
GR*** Thread: main has called @Factorial.factorial(java.lang.Integer) java.lang.Integer from byteman.examples.RecursiveFactorial.factorial(RecursiveFactorial.java:10) with params: 3 
GR*** Thread: main has called @Factorial.factorial(java.lang.Integer) java.lang.Integer from byteman.examples.RecursiveFactorial.factorial(RecursiveFactorial.java:10) with params: 2 
GR*** Thread: main has called @Factorial.factorial(java.lang.Integer) java.lang.Integer from byteman.examples.RecursiveFactorial.factorial(RecursiveFactorial.java:10) with params: 1 
GR*** main has exited factorial(java.lang.Integer) java.lang.Integer in class @Factorial and returned: 1
GR*** main has exited factorial(java.lang.Integer) java.lang.Integer in class @Factorial and returned: 2
GR*** main has exited factorial(java.lang.Integer) java.lang.Integer in class @Factorial and returned: 6
GR*** main has exited factorial(java.lang.Integer) java.lang.Integer in class @Factorial and returned: 24
GR*** main has exited factorial(java.lang.Integer) java.lang.Integer in class @Factorial and returned: 120
	 */
	
	protected String[] toRegexes() {
		CheckstyleMethodCalledTestCase aCheckstyleMethodCalledTest = checkStyleMethodCalledTest();
		int aNumExpectedCalls = numExpectedCalls();
		Object[] anExpectedReturnValues = expectedReturnValues();
		if (
				aCheckstyleMethodCalledTest == null ||
				aNumExpectedCalls < 0 ||
				anExpectedReturnValues == null) {
			return null;
		}
		String aCallingMethodSignature = aCheckstyleMethodCalledTest.getCallingMethodSignature();
		
		String aCallingType = aCheckstyleMethodCalledTest.getTypeName();
		String aCalledType = aCallingType;
		String aCalledMethodSpecification =  aCheckstyleMethodCalledTest.getCalledMethodSignature();
		String[] aCalledMethodParts = aCalledMethodSpecification.split("!");
		String aCalledMethodSignature = aCalledMethodParts[0];
		boolean hasCalledType = aCalledMethodParts.length == 2;
		
		if (hasCalledType) {
			aCalledType= aCalledMethodParts[0];
			aCalledMethodSignature = aCalledMethodParts[1];
		}
		
		STMethod aCallingMethod = ComprehensiveVisitCheck.
				createMethodFromSignature(aCallingMethodSignature);
		STMethod aCalledMethod = ComprehensiveVisitCheck.
				createMethodFromSignature(aCalledMethodSignature);
		return null;

	}
	
	protected abstract Set<String> getTags();

	Class target;
	protected Class<?> getTarget() {
		if (target == null) {
		try {
			Set<String> aTags = getTags();
			String aClassName = InjectionTargeterFactory.getInjectionTargeter().getClassName(aTags);
			target = Class.forName(aClassName);
		} catch (Exception e) {
			e.printStackTrace();
//			return null;
		}
		}
		return target;
		
	}
	
	@Override
	public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
		try {
//			if(getRegistry()==null) 
//				return fail("Prereq tests did not pass");
			BytemanConfigurationWriterFactory.writeConfiguration(project);
//			InjectionTargeterFactory.getOrCreateInitializedInjectionTargeter(project);
			
			String anOut = getTraceOut();
			
			String[] aRegexes = toRegexes();

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
