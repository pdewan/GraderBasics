package gradingTools.shared.testcases;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Assert;

import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.GradingMode;
import grader.basics.execution.ResultWithOutput;
import grader.basics.execution.ResultingOutErr;
import grader.basics.junit.BasicJUnitUtils;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.NotesAndScore;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.CurrentProjectHolder;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.shared.testcases.utils.ALinesMatcher;
import gradingTools.shared.testcases.utils.LinesMatchKind;
import gradingTools.shared.testcases.utils.LinesMatcher;
import org.apache.commons.lang.NotImplementedException;
import util.trace.Tracer;



public abstract class MethodExecutionTest extends PassFailJUnitTestCase  {
	public static String MATCH_ANY = "(.*)";
	protected Object lastTargetObject;
//	protected double fractionComplete;
	protected Object returnValue; 
//		expectedReturnValue, 
//		studentExpectedReturnValue, graderExpectedReturnValue;
	protected ResultWithOutput resultWithOutput;
	protected String output = "";
	protected String[] outputLines = {};
	protected String error = "";
	protected OutputErrorStatus outputErrorStatus;
	protected ResultingOutErr resultingOutError;
	protected String incorrectOutputDetails = "";
	protected boolean testing = false;
	// should move to MethodExecution
//	protected Set<Thread> previousThreads = new HashSet();
//	protected Set<Thread> currentThreads = new HashSet();
//	protected List<Thread> newThreads = new ArrayList();
	public static Object[] emptyArgs = {};
	protected Method doTestMethod;
	protected SubstringSequenceChecker checker;	
	protected LinesMatcher linesMatcher;
	protected String processName;
	protected boolean checkTrue = true;
	protected StringBuffer programmingRunOutput;



	
	public MethodExecutionTest() {
		Tracer.setKeywordPrintStatus(this.getClass(), true);
		init(processName(), checker(), checkTrue());

//		if (!CurrentProjectHolder.isLocalProject()) {
//		try {
//		doTestMethod = getClass().getMethod("doTest");
////		doTestMethod.setAccessible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//	
//		}
////		}
	}

	
	public Object[] getStudentArgs() {
		return new Object[]{};
	}
	public Object[] getGraderArgs() {
		return getStudentArgs();
	}
	
	public Object[] getArgs() {
		if(GradingMode.getGraderRun()) {
			return getGraderArgs();
		} else {
			return getStudentArgs();
		}
	}
	public enum OutputErrorStatus {
		NO_OUTPUT, CORRECT_OUTPUT_NO_ERRORS, CORRECT_OUTPUT_ERRORS, INCORRECT_OUTPUT_NO_ERRORS, INCORRECT_OUTPUT_ERRORS
	}
	public Object getLastTargetObject() {
		return lastTargetObject;
	}
	protected boolean isInteractive() {
		return false;
	}
	public static Class[] toArgTypes(Object[] anArgs) {
		 Class[] anArgTypes = BasicProjectIntrospection.toClasses(anArgs);
		    BasicProjectIntrospection.toPrimitiveTypes(anArgTypes);
		    return anArgTypes;
	}
	protected Class[] getArgTypes() {
		Object[] anArgs = getArgs();
		return toArgTypes(anArgs);
//		  Class[] anArgTypes = BasicProjectIntrospection.toClasses(anArgs);
//		    BasicProjectIntrospection.toPrimitiveTypes(anArgTypes);
//		    return anArgTypes;
	}
    public  TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException,
   	NotGradableException {
    	
    	throw new NotImplementedException();
    }

	protected  String getClassName() {
		return null;
	}
	protected  String[] getClassNames() {
		return new String[] {getClassName()};
	}
	protected Object getTargetObject() {
		return null;
	}
	protected Object[] getTargetObjects() {
		return new Object[] {getTargetObject()};
	}
	/*
	 * This should not be called
	 */
	public Class getTargetClass() {
		Class[] aTargetClasses = getTargetClasses();
		if (aTargetClasses.length != 1) { 
			Tracer.warning("Did not find unique class. See console output above.");
			return null;
		}
//		return BasicProjectIntrospection.findClass(CurrentProjectHolder.getOrCreateCurrentProject(), getClassName());
		return getTargetClasses() [0];

	}
	protected Class[] getTargetClasses() {
		String[] aClassNames = getClassNames();
		Tracer.info(this, "Searching for classes matching:" + Arrays.toString(aClassNames));
		List<Class> aClassList = new ArrayList();
//		Class[] aTargetClasses = new Class[aClassNames.length];
		for (int i = 0; i < aClassNames.length; i++) {
			Class aFoundClass = BasicProjectIntrospection.findClass(CurrentProjectHolder.getOrCreateCurrentProject(), aClassNames[i]);
			if (aClassList.contains(aFoundClass) || aFoundClass == null)
				continue;
			aClassList.add(aFoundClass);
//			aTargetClasses[i] = BasicProjectIntrospection.findClass(CurrentProjectHolder.getOrCreateCurrentProject(), aClassNames[i]);
		}
		Class[] aTargetClasses = new Class[aClassList.size()];
		aTargetClasses = aClassList.toArray(aTargetClasses);		

//		return BasicProjectIntrospection.findClass(CurrentProjectHolder.getOrCreateCurrentProject(), getClassName());
		return aTargetClasses;
	}
	protected Class getMethodClass (Object anObject) {
		Class aClass;
		if (anObject instanceof Class)
			aClass = (Class) anObject;
		else
			aClass = anObject.getClass();
		return aClass;
	}
	protected Method getMethod() {			
		return 	BasicProjectIntrospection.findMethod(
				getMethodClass(getTargetObject()), getMethodName(), getArgTypes());
	}
	protected String getMethodName() {
		return null;
	}
	protected Object getExpectedReturnValue() {
		if(GradingMode.getGraderRun()) {
			return getGraderExpectedReturnValue();
		} else {
			return getStudentExpectedReturnValue();
		}
	}
	protected Object getStudentExpectedReturnValue() {
		return null;
	}
	protected Object getGraderExpectedReturnValue() {
		return getStudentExpectedReturnValue();
	}
	protected OutputErrorStatus getOutputErrorStatus() {
		return outputErrorStatus;
	}
	protected ResultWithOutput getResultWithOutput() {
		return resultWithOutput;
	}
	protected String getOutput() {
//		return resultingOutError.out;
		return output;
	}
	protected String getError() {
//		return resultingOutError.err;
		return error;

	}
	protected ResultingOutErr getResultingOutErr() {
		return resultingOutError;
	}
	protected Object getReturnValue() {
		return returnValue;
	}
	protected void setReturnValue(Object newVal) {
		returnValue = newVal;
	}
	protected double getPercentage() {
		return 0;
	}
	protected String getMessage() {
		return "";
	}
	protected String getInput() {
		return "";
	}
	String[] emptyStringLines = {};
	protected String[] getInputLines() {
		return emptyStringLines;
	}
	protected long getTimeOut() {
		return BasicStaticConfigurationUtils.DEFAULT_METHOD_TIME_OUT;
	}
	protected boolean isValidOutput() {
		return true;
	}
	
	protected void setOutputErrorStatus() {
		outputErrorStatus = computeOutputErrorStatus();
	}
	protected boolean hasError(String anError) {
		return !anError.isEmpty();
	}

	protected boolean hasError() {
		return !getError().isEmpty();
	}
	protected boolean noOutput = false;
	protected boolean noOutput() {
		return noOutput;
	}
	protected OutputErrorStatus computeOutputErrorStatus() {
		if (noOutput()) {
			return OutputErrorStatus.CORRECT_OUTPUT_NO_ERRORS;
		}
		ResultingOutErr aResult = getResultingOutErr();
		if (aResult == null) {
			return OutputErrorStatus.NO_OUTPUT;
		}
		boolean validOutput = isValidOutput();
		boolean hasError = hasError(aResult.err);
		if (validOutput && !hasError) {
			return OutputErrorStatus.CORRECT_OUTPUT_NO_ERRORS;
		}
		if (validOutput && hasError) {
//			if (aResult.getOut().equals(aResult.getErr())) {
//				return OutputErrorStatus.CORRECT_OUTPUT_NO_ERRORS;
//			}
			return OutputErrorStatus.CORRECT_OUTPUT_ERRORS;
		}
		if (!validOutput && !hasError) {
			return OutputErrorStatus.INCORRECT_OUTPUT_NO_ERRORS;
		}
		return OutputErrorStatus.INCORRECT_OUTPUT_ERRORS;
	}
//	protected void assertTrue(String aMessage, boolean aCheck) {
////		testing = false;
//		Assert.assertTrue(aMessage + NotesAndScore.PERCENTAGE_MARKER + fractionComplete, aCheck);
////		testing = true;
////		Assert.assertTrue(aMessage + NotesAndScore.PERCENTAGE_MARKER + fractionComplete, aCheck);
//	}
	protected void assertNotExpected(Object anActual, Object anExpected) {
		assertTrue ("Actual value: " + anActual + " not expected value: " + anExpected, anExpected.equals(anActual));

//		Assert.assertTrue(aMessage + NotesAndScore.PERCENTAGE_MARKER + fractionComplete, aCheck);
	}
	protected void assertNoClass() {
		Assert.assertTrue(noClassMessage()
				+ NotesAndScore.PERCENTAGE_MARKER
				+ noClassMessage(), false);
	}
	protected void assertNoMethod() {
		Assert.assertTrue(noMethodMessage()
				+ NotesAndScore.PERCENTAGE_MARKER
				+ noMethodCredit(), false);
	}
	protected void maybeAssertInfinite() {
		if (CurrentProjectHolder.getOrCreateCurrentProject().isInfinite()) {
			Assert.assertTrue(infiniteRunMessage()
					+ NotesAndScore.PERCENTAGE_MARKER
					+ infiniteRunCredit(), false);
		}

		
	}
	public boolean invokeRunMethod(Runnable aRunnable) throws Throwable {
//		Tracer.info(this, "Calling run method on:" + aRunnable);
			Method aRunMethod = Runnable.class.getMethod("run");
			return invokeMethod(
					aRunnable, 
					aRunMethod,
					emptyArgs);		
			
		
	}
	
	
	public boolean invokeMethod(Object aTargetObject, Method aMethod, Object[] anArgs) throws Throwable {
		resetIO();
		lastTargetObject = aTargetObject;
		if (isInteractive()) {
//			resultWithOutput = BasicProjectExecution.
//					proxyAwareGeneralizedInteractiveTimedInvoke(
//							getTargetObject(),
//							getMethod(),
//							getArgs(),
//							getInput(),
//							getTimeOut());
			Tracer.info(this, "Calling  interactive method:" + aMethod.getName() + " with args "
					+ Arrays.toString(anArgs) + " and input " + getInput());
			resultWithOutput = BasicProjectExecution.proxyAwareGeneralizedInteractiveTimedInvoke(aTargetObject, aMethod,
					anArgs, getInput(), getTimeOut());
			if (resultWithOutput == null) {
				returnValue = null;
				resultingOutError = null;
				return false;
			}
			returnValue = resultWithOutput.getResult();
//			resultingOutError = new ResultingOutErr(
//					resultWithOutput.getOutput(), 
//					resultWithOutput.getError());
			error += resultWithOutput.getError();
			output += resultWithOutput.getOutput();
			// should define seters in this class
			resultingOutError = new ResultingOutErr(output, error);
			setOutputErrorStatus();
			processOutputErrorStatus();
		} else {
//			Tracer.info (this, "Calling non interactive method:" + 
//					aMethod.getName() + 
//					" with args " +
//					Arrays.toString(anArgs));
			Tracer.info(this, "Calling on object " + aTargetObject + "  method:" + aMethod.getName() + " with args "
					+ Arrays.toString(anArgs));
			returnValue = BasicProjectExecution.proxyAwareTimedInvoke(aTargetObject, aMethod, anArgs, getTimeOut());
			if (returnValue == null) {
				return false;
			}
//			String aReturnString = (returnValue != null && returnValue.getClass().isArray())
//					? Arrays.toString((Object[]) returnValue)
//					: returnValue.toString();
			
			String aReturnString = objectToStringOrNull(returnValue);

//			Tracer.info (this, "Return value =" + returnValue);
			Tracer.info(this, "Return value: " + aReturnString);

		}
//		if (returnValueIsExpected()) {
//			return true;
//		}
//		if (returnValueIsOffByOne()) {
//			Assert.assertTrue(offByOneMessage()
//					+ NotesAndScore.PERCENTAGE_MARKER
//					+ offByOneCredit(), false);
//		}
//		return true;
		return true;
	}
	
	private String objectToStringOrNull(Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof boolean[]) {
			return Arrays.toString((boolean[])obj);
		} else if (obj instanceof byte[]) {
			return Arrays.toString((byte[])obj);
		} else if (obj instanceof short[]) {
			return Arrays.toString((short[])obj);
		} else if (obj instanceof char[]) {
			return Arrays.toString((char[])obj);
		} else if (obj instanceof int[]) {
			return Arrays.toString((int[])obj);
		} else if (obj instanceof long[]) {
			return Arrays.toString((long[])obj);
		} else if (obj instanceof float[]) {
			return Arrays.toString((float[])obj);
		} else if (obj instanceof double[]) {
			return Arrays.toString((double[])obj);
		} else if (obj instanceof Object[]) {
			return Arrays.toString((Object[])obj);
		} else {
			return obj.toString();
		}
	}
	
	protected void resetIO() {
		error = "";
		output = "";
	}
	protected void maybeNoMethod(Method aMethod) {
		if (aMethod == null) {
			Assert.assertTrue(noMethodMessage()
					+ NotesAndScore.PERCENTAGE_MARKER
					+ noMethodCredit(), false);
		}
	}
	protected Method findMethod (Object aTargetObject) {
		Class aClass = getMethodClass(aTargetObject);
		Method aMethod = BasicProjectIntrospection.findMethod(
				aClass, getMethodName(), getArgTypes());
		return aMethod;
		
	}
	protected void maybeExceptionMethod(Boolean aResult) {
		if (!aResult) {
			Assert.assertTrue(exceptionMethodMessage()
					+ NotesAndScore.PERCENTAGE_MARKER
					+ exceptionMethodCredit(), false);
		}
	}
	public boolean invokeMethod(Object aTargetObject) throws Throwable  {
		resetIO();
		
		Method aMethod = findMethod(aTargetObject);

		
			
		maybeNoMethod(aMethod);
//		if (aMethod == null) {
//			Assert.assertTrue(noMethodMessage()
//					+ NotesAndScore.PERCENTAGE_MARKER
//					+ noMethodCredit(), false);
//		}
	
		boolean aResult = invokeMethod(aTargetObject, aMethod, getArgs());	
		maybeExceptionMethod(aResult);
//		if (!aResult) {
//			Assert.assertTrue(exceptionMethodMessage()
//					+ NotesAndScore.PERCENTAGE_MARKER
//					+ exceptionMethodCredit(), false);
//		}

	return processReturnValue();
	}
	
	public boolean invokeMethod() throws Throwable  {
		resetIO();
		Object aTargetObject = null ;
		Method aMethod = null;
		for (Object anObject:getTargetObjects()) {
			aTargetObject = anObject;
			if (anObject == null) {
				
				continue;
			}
			aMethod = findMethod(aTargetObject);
//			Class aClass = getMethodClass(aTargetObject);
//			aMethod = BasicProjectIntrospection.findMethod(
//					aClass, getMethodName(), getArgTypes());
			if (aMethod != null ) {
				break;
			}
		}
		if (aTargetObject == null) {
//			Assert.assertTrue(noClassMessage()
//					+ NotesAndScore.PERCENTAGE_MARKER
//					+ noClassMessage(), false);
			assertNoClass();
		}
		if (aMethod == null) {
			Assert.assertTrue(noMethodMessage()
					+ NotesAndScore.PERCENTAGE_MARKER
					+ noMethodCredit(), false);
		}
	
		boolean aResult = invokeMethod(aTargetObject, aMethod, getArgs());	
		maybeExceptionMethod(aResult);
//		if (!aResult) {
//			Assert.assertTrue(exceptionMethodMessage()
//					+ NotesAndScore.PERCENTAGE_MARKER
//					+ exceptionMethodCredit(), false);
////			return false;
//		}
//		if (isInteractive()) {
//
//			Tracer.info (this, "Calling  interactive method:" + 
//					aMethod.getName() + 
//					" with args " +
//					getArgs() +
//					" and input " +
//					getInput()); 
//			resultWithOutput = BasicProjectExecution.
//					proxyAwareGeneralizedInteractiveTimedInvoke(
//							aTargetObject,
//							aMethod,
//							getArgs(),
//							getInput(),
//							getTimeOut());
//			if (resultWithOutput == null) {
//				returnValue = null;
//				resultingOutError = null;
//				return false;
//			}
//			returnValue = resultWithOutput.getResult();
//			resultingOutError = new ResultingOutErr(
//					resultWithOutput.getOutput(), 
//					resultWithOutput.getError());
//			setOutputErrorStatus();
//		} else {
//			Tracer.info (this, "Calling non interactive method:" + 
//					aMethod.getName() + 
//					" with args " +
//					getArgs()); 
//			returnValue = BasicProjectExecution.proxyAwareTimedInvoke(
//					aTargetObject,
//					aMethod,
//					getArgs(),
//					getTimeOut());
//			if (returnValue == null) {
//				return false;
//			}
//		}
	return processReturnValue();
	}
	protected void assertOffByOne() {
		Assert.assertTrue(offByOneMessage()
				+ NotesAndScore.PERCENTAGE_MARKER
				+ offByOneCredit(), false);
	}
//	protected void assertWrongReturnValue() {
//		Assert.assertTrue(offByOneMessage()
//				+ NotesAndScore.PERCENTAGE_MARKER
//				+ offByOneCredit(), false);
//	}
	protected void assertWrongReturnValue() {
		Assert.assertTrue(wrongReturnValueMessage()
				+ NotesAndScore.PERCENTAGE_MARKER
				+ wrongReturnValueCredit(), false);
	}
	protected void traceProcessReturnValue() {
		Tracer.info (this, 
				"Comparing actial return value: " + getReturnValue() +
				" with expected return value: " + getExpectedReturnValue());
	}
	protected boolean processReturnValue() {
		traceProcessReturnValue();
		if (returnValueIsExpected()) {
			return true;
		}
		if (returnValueIsOffByOne()) {
			assertOffByOne();
//			Assert.assertTrue(offByOneMessage()
//					+ NotesAndScore.PERCENTAGE_MARKER
//					+ offByOneCredit(), false);
		} else {
			assertWrongReturnValue();
		}
		return true;
	}
	protected void processCorrectOutputNoErrors() {
		return;
	}
	protected void processCorrectOutputErrors() {
		Assert.assertTrue(correctOutputButErrorsMessage()
				+ NotesAndScore.PERCENTAGE_MARKER
				+ correctOutputButErrorsCredit(), false);
	}
	protected void processSingleTryOutputErrorStatus() {
		OutputErrorStatus retVal = getOutputErrorStatus();
		if (retVal == OutputErrorStatus.CORRECT_OUTPUT_NO_ERRORS) {
			processCorrectOutputNoErrors();
			return;
		}
		if (retVal == OutputErrorStatus.CORRECT_OUTPUT_ERRORS) {
			// String aMessage = correctOutputButErrorsMessage() +
			// NotesAndScore.PERECTAGE_CHARACTER +
			// + correctOutputButErrorsCredit();
			// Assert.assertTrue(
			// aMessage, false);
			// This results in character becoming an int if marker is a
			// character!
//			Assert.assertTrue(correctOutputButErrorsMessage()
//					+ NotesAndScore.PERCENTAGE_MARKER
//					+ correctOutputButErrorsCredit(), false);
			processCorrectOutputErrors();
			return;
		}
		Assert.assertTrue(incorrectOutputMessage() + 
				+ NotesAndScore.PERCENTAGE_MARKER
				+ incorrectOutputCredit(), false);
	}
	protected void processOutputErrorStatus() {
		processSingleTryOutputErrorStatus();
	}
	protected boolean returnValueIsExpected() {
		return getExpectedReturnValue() == (getReturnValue()) || getExpectedReturnValue().equals(getReturnValue());
	}
	protected boolean returnValueIsOffByOne() {
		try {
		return Math.abs(
				(Integer) getExpectedReturnValue() - 
				(Integer) getReturnValue()) == 1;
		} catch (Exception e) {
			return false;
		}
	}
	protected boolean processInteractiveMethodExecutionResults() {
		return true;
	}
	protected String noMethodMessage() {
		return "No method:" + getMethodName();
	}
	protected String infiniteRunMessage() {
		return "Call did not terminate withing expected time";
	}
	
	protected String exceptionMethodMessage() {
		return "Method throws tieout or other exception:" + getMethodName();
	}
	protected double exceptionMethodCredit() {
		return 0.2;
	}
	
	protected String offByOneMessage() {
		return "Expected value" + getExpectedReturnValue() + 
				" is off by one from return value " + getReturnValue();
	}
	protected String wrongReturnValueMessage() {
		Object anExpectedValue = getExpectedReturnValue();
		Object anActualValue = getReturnValue();
		String anExpectedString = objectToStringOrNull(anExpectedValue);
		String anActualString = objectToStringOrNull(anActualValue);
//		String anExpectedString = anExpectedValue.toString();
//		String anActualString = anActualValue.toString();
//		if (anExpectedValue.getClass().isArray() && anActualValue.getClass().isArray()) {
//			anExpectedString = Arrays.toString((Object[]) anExpectedValue);
//			anActualString = Arrays.toString((Object[]) anActualValue);
//
//		}
//		return "Expected value" + getExpectedReturnValue() + 
//				" is not same as return value " + getReturnValue();
		return "Expected value" + anExpectedString + 
				" is not same as return value " + anActualString;
	}
	protected double offByOneCredit() {
		return 0.8;
	}
	protected double wrongReturnValueCredit() {
		return 0.0;
	}
	protected String noClassMessage() {
		return "Did not find a class in:" + Arrays.toString(getClassNames());
	}

	protected double incorrectOutputCredit() {
		return 0.0;
	}

	protected double noMethodCredit() {
		return 0.0;
	}
	protected double infiniteRunCredit() {
		return 0.1;
	}
	
	protected double noClassCredit() {
		return 0.0;
	}
	
	protected boolean doTest() throws Throwable {
		invokeMethod();
    	return processInteractiveMethodExecutionResults();
	}
	protected boolean processSuccessfulOutputErrrorStatus() {
		OutputErrorStatus retVal = getOutputErrorStatus();

		if (retVal == OutputErrorStatus.CORRECT_OUTPUT_NO_ERRORS)
			return true;
		if (retVal == OutputErrorStatus.CORRECT_OUTPUT_ERRORS) {
			// String aMessage = correctOutputButErrorsMessage() +
			// NotesAndScore.PERECTAGE_CHARACTER +
			// + correctOutputButErrorsCredit();
			// Assert.assertTrue(
			// aMessage, false);
			// This results in character becoming an int if marker is a
			// character!
			Assert.assertTrue(correctOutputButErrorsMessage()
					+ NotesAndScore.PERCENTAGE_MARKER
					+ correctOutputButErrorsCredit(), false);
			return true; // will never be executed
		}
		return false;
	}
	protected double correctOutputButErrorsCredit() {
		return 0.5;
	}
	protected String correctOutputButErrorsMessage() {
		String aPrefix = "Correct output but errors";
		String aReasons = possibleReasonsForErrors();
		if (aReasons.isEmpty())
			return aPrefix;
		return aPrefix + ". Possible reasons:" + aReasons;
	}
	protected String incorrectOutputMessage() {
		String aPrefix = "Incorrect output";
		if (incorrectOutputDetails != null && !incorrectOutputDetails.isEmpty()){
			aPrefix += " (" + incorrectOutputDetails + ") ";
		}
		String aReasons = possibleReasonsForIncorrectOutput();
		if (aReasons.isEmpty())
			return aPrefix;
		return aPrefix + ". Possible reasons:" + aReasons;
	}
	protected String possibleReasonsForIncorrectOutput() {
		return "";
	}
	protected double wrongInterfaceCredit() {
		return 0.1;
	}
	protected void assertWrongInterface(Class anActualClass, Class anInterface) {
		Assert.assertTrue("Class " + anActualClass.getSimpleName() + 
				" does not implement" + anInterface.getSimpleName()
				+ NotesAndScore.PERCENTAGE_MARKER
				+ wrongInterfaceCredit(), false);
	}
	protected String possibleReasonsForErrors() {
		return "";
	}
	protected boolean processUnsuccessfulOutputErrrorStatus() {
		OutputErrorStatus retVal = getOutputErrorStatus();

		if (retVal == OutputErrorStatus.NO_OUTPUT) {
			Assert.assertTrue(noMethodMessage() + NotesAndScore.PERCENTAGE_MARKER
					+ noMethodCredit(),

			false);
		}

		Assert.assertTrue(incorrectOutputMessage() + " See console/transcript."
				+ NotesAndScore.PERCENTAGE_MARKER + incorrectOutputCredit(),

		false);
		return true;
	}
	// our parent class tags defaultTest
	@Override
	public void defaultTest() {
//		TestCaseResult aPrecedingResult = precedingTestResult();
		possiblyRunAndCheckPrecedingTests();
		
    	test();
    }
//	@Test
    public void test() {
		
        try {
        	testing = true;
        	doTest();
//        	if (doTestMethod != null ) {
//
////        	if (doTestMethod != null && !CurrentProjectHolder.isLocalProject()) {
//        	   BasicProjectExecution.timedInvoke(this, doTestMethod, emptyArgs);
//        	} else {
//        		doTest();  
//        	}
//        	testing = false;
            
        } catch (Throwable e) {
        	e.printStackTrace();
//        	testing = false;
        	BasicJUnitUtils.assertTrue(e, fractionComplete);
        } finally {
        	testing = false;
        }
    }
	public static String toRegex(String aString) {
		// This would be the ideal way this method would work, but currently it is called with
		// other regex's as arguments, which nullifies them.
//		return MATCH_ANY + "\\Q"+aString+"\\E" + MATCH_ANY;
		
		// Escape any '*' not preceded by '.'
		String result = "";
		for (int i = 0; i < aString.length(); ++i){
			if (aString.charAt(i) == '*' && 
					!(i>0 && aString.charAt(i-1) == '.')){	//if not preceded by dot
					result+="\\*";
					continue;
			
			} else {
				result+=aString.charAt(i);
			}
		}//end escaping *
		
		return MATCH_ANY + result + MATCH_ANY;
	}
	protected void assertMissingClass(String[] aTags) {
		Assert.assertTrue("No class matching: "
				+ Arrays.toString(aTags) + NotesAndScore.PERCENTAGE_MARKER + 0.0, false);
	}
	protected void printFractionComplete() {
		Tracer.info (this, "Fraction complete:" + fractionComplete);
	}
	protected Class instantiatedClass;
	protected Constructor constructor;
	protected String instantiatedTag;
	protected Class[] constructorArgs = {};
	protected String instantiatedTag() {
		return instantiatedTag;
	}	
	protected Class[] constructorArgs() {
		return constructorArgs;
	}
	protected void setConstructorArgs(Class[] anArgs) {
		constructorArgs = anArgs;
	}
	protected void setInstantiatedTag(String aTag) {
		instantiatedTag = aTag;
	}
	protected Class findInstantiatedClass() {
		return BasicProjectIntrospection.findClassByTags(instantiatedTag());
	}
	public  Constructor findConstructor(Class aClass) throws Exception {
		return aClass.getConstructor(
				 constructorArgs());
	}
	
//	public static long presleepTime() {
//		long aPresleepTime = System.currentTimeMillis();
//	    Tracer.info (this, "Pre sleep time:" + aPresleepTime);
//	    Tracer.info (this, "Sleeping for (ms):" + SLEEP_TIME);
//	    return aPresleepTime;
//		
//	}
//	public static boolean checkSleep(long aPresleepTime, long aSleepTime ) {
//		long aPostsleepTime = System.currentTimeMillis();
//		 Tracer.info (this, "Post sleep time:" + aPostsleepTime);
//		 return
//				 (aPostsleepTime - aPresleepTime) > SLEEP_TIME;
//	}
	
	protected Object instantiateClass(Object... anArgs) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
		return  constructor.newInstance(anArgs);
	}	
	
	protected void initConstructor() throws Exception {
		instantiatedClass = findInstantiatedClass();
		assertTrue("Could not find class  matching " + instantiatedTag(), instantiatedClass != null);
		constructor = findConstructor(instantiatedClass);
	}
	protected Set<Thread> previousThreads = new HashSet();
	protected Set<Thread> currentThreads = new HashSet();
	protected List<Thread> newThreads = new ArrayList();
	protected void recordPreviousThreads() {
		previousThreads = new HashSet(Thread.getAllStackTraces().keySet());
		Tracer.info (this, "Previous threads:" + previousThreads);		
	}
	protected void recordCurrentThreads() {
		currentThreads = new HashSet(Thread.getAllStackTraces().keySet());
		Tracer.info (this, "Current threads:" + currentThreads);

		newThreads = new ArrayList(currentThreads);
		newThreads.removeAll(previousThreads);
//		Tracer.info (this, "New threads:" + newThreads);		
	}
	protected void assertNewThreadCreated() {
		assertTrue("No thread created by previous operation:", newThreads.size() > 0);
		Tracer.info (this, "New threads:" + newThreads);
	}
	protected boolean checkTrue() {
		return true;
	}
	
	protected SubstringSequenceChecker checker() {
		return null;
	}
	public void init (String aProcessName,
			SubstringSequenceChecker aChecker,
			boolean aCheckTrue) {
		processName = aProcessName;
//		outputGeneratingTestCase = anOutputGeneratingTestcase;
		checker = aChecker;
		checkTrue = aCheckTrue;		
	}
	protected String processName() {
		return "main";
	}
	protected void independentSetLinesMatcher() {
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		linesMatcher = interactiveInputProject.getProcessLinesMatcher().get(processName);
		if (linesMatcher == null) {
			if (outputLines == null || outputLines.length == 0) {
			this.outputLines = output.split("\n");
			}
			linesMatcher = new ALinesMatcher(outputLines);
//			assertTrue("Internal error: Could not find line matcher for process:" + processName, false);
		}
	}
	protected boolean checkWithChecker() {
		if (checker == null) {
			return true;
		}
//		independentSetLinesMatcher();
//		ARegularCounterServerChecker aServerChecker = new ARegularCounterServerChecker(1.0);

//		boolean aCheckVal = checker.check(programmingRunOutput);
		boolean aCheckVal = checker.check(linesMatcher, LinesMatchKind.ONE_TIME_LINE, Pattern.DOTALL);

		boolean aRetVal = checkTrue && aCheckVal || !checkTrue && !aCheckVal;
		if (!aRetVal && checkTrue) {
//			return fail(processName + " Output Did not match:" + Arrays.toString(checker.getSubstrings()));
//			return fail(processName + " Output Did not match:" + checker.getRegex());
			assertTrue(processName + " Output Did not match:" + linesMatcher.getLastUnmatchedRegex(), false);

		}
		if (!aRetVal && !checkTrue) {
//			return fail("Did not match:" + Arrays.toString(checker.getSubstrings()));
			assertTrue(processName + " Output matched:" + checker.getRegex(), false);

		}
//		if (interactiveInputProject != null) {
//			interactiveInputProject.getProcessOutput().forEach((name, output) -> Tracer.info(this, "*** " + name + " ***\n" + output));
//		}
//		
//		if (!anOutputBasedInputGenerator.isNonsenseSetupComplete()) {
//		
//			return partialPass(0.80, "No nonsense");
//		}
//		
//		return pass();
		return true;
	}
	
}
