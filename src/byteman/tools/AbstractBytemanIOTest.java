package byteman.tools;

import java.lang.reflect.Array;
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
GR*** Thread: main has called @MergeSort.divide(int[],int,int) int from byteman.examples.BytemanMerge.sort(BytemanMerge.java:16) with params: [5,14] 0 1 
GR*** Thread: main has called @MergeSort.divide(int[],int,int) int from byteman.examples.BytemanMerge.sort(BytemanMerge.java:16) with params: [7,12] 0 1 
GR*** main has exited divide(int[],int,int) int in class @MergeSort and returned: [7]	
	 */
	static final String INFO_PREFIX = "GR*** ";
	static final String THREAD_PREFIX = "Thread: ";
	static final String CALLED_INDICATOR = "has called ";
	static final String EXITED_INDICATOR = "has exited ";
	
	protected static String getCalledMethodSignature(STMethod aCalledMethod) {
		StringBuilder sb = new StringBuilder();
		sb.append(aCalledMethod.getName());
		sb.append("(");
		String[] aParamTypes = aCalledMethod.getParameterTypes();
		for(int i=1;i<aParamTypes.length;i++) {
			if (i != 0) {
				sb.append (",");
			}
			sb.append(aParamTypes[i]);			
		}
		sb.append(")");
		sb.append(" ");
		String aReturnType = aCalledMethod.getReturnType();
		sb.append(aReturnType);
		return sb.toString();
	}

	
	protected String getThreadName() {
		return ".*";
	}
	
	protected String getCallingMethodSpecification(String aCallingTypeFileName, String aCallingTypeName, STMethod aCallingMethod) {
		String[] aCallingTypeFileNameParts = aCallingTypeFileName.split("[/\\]");
		String aShortFileName = aCallingTypeFileNameParts[aCallingTypeFileNameParts.length - 1];
		return null;
	}
	public static Object[] toObjectArray(Object o) {
		if(!o.getClass().isArray()) return null;
		if(!o.getClass().getComponentType().isPrimitive()) return (Object [])o;
		int arrlen = Array.getLength(o);
		Object [] retval = new Object[arrlen];
		for(int i=0;i<arrlen;i++) {
			retval[i]=Array.get(o, i);
		}
		return retval;
	}
	public static String arrayPrinter(Object o) {
		StringBuilder sb = new StringBuilder();
		if(!o.getClass().isArray()) {
			sb.append(o.toString());
		}else {
			sb.append('[');
			Object [] arr = toObjectArray(o);
			for(int i=0;i<9&&i<arr.length;i++) {
				sb.append(arrayPrinter(arr[i]));
				sb.append(',');
			}
			if(arr.length>0)
				sb.deleteCharAt(sb.length()-1);
			if(arr.length == 11) {
				sb.append(","+arr[10]);
			}else if(arr.length>10) {
				sb.append(", ... ");
				sb.append(arr[arr.length-1]);
			}
			
			sb.append(']');
		}
		return sb.toString();
	}	
	
	protected String getActualParametersSpecification() {
		Object[] params = getArgs();
		StringBuilder sb = new StringBuilder();
		for(int i=1;i<params.length;i++) {
			if(params[i].getClass().isArray()) {
				sb.append(arrayPrinter(params[i]));
			}else {
				sb.append(params[i].toString());
			}
			sb.append(' ');
		}
		return sb.toString();
//		Class[] aActualArgTypes
		
	}
	
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
		
		String aCallingTypeFileName = aCheckstyleMethodCalledTest.getCallingTypeFileName();
		String aCallingType = aCheckstyleMethodCalledTest.getTypeName();
		String aCalledType = aCallingType;
		String anActualCalledType = aCheckstyleMethodCalledTest.getCallingTypeName();
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
//		String jarLocation = "./Logs/LocalChecks/agent.jar";
		Tracer.showInfo(true);
		CurrentProjectHolder.setProject(".java");
		Project aProject = CurrentProjectHolder.getCurrentProject();
		List<String> aCommand = BasicStaticConfigurationUtils.getBasicCommand();
		List<String> aModifiedCommand = new ArrayList<>(aCommand);
		aModifiedCommand.add(1,"-javaagent:./Logs/LocalChecks/agent.jar");
//		aModifiedCommand.add(1,"-javaagent:./Logs/LocalChecks/agent.jar=script:thread.btm,boot:./Logs/LocalChecks/agent.jar -Dorg.jboss.byteman.transform.all");

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
