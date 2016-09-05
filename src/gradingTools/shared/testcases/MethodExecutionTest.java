package gradingTools.shared.testcases;

import java.lang.reflect.Method;
import java.util.Arrays;

import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.NotRunnableException;
import grader.basics.execution.ResultWithOutput;
import grader.basics.execution.ResultingOutErr;
import grader.basics.junit.BasicJUnitUtils;
import grader.basics.junit.NotesAndScore;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.CurrentProjectHolder;
import grader.basics.project.NotGradableException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import util.awt.AnOutputQueue;


public abstract class MethodExecutionTest  {
	public static String MATCH_ANY = "(.*)";
	protected Object returnValue;
	protected ResultWithOutput resultWithOutput;
	protected String output;
	protected OutputErrorStatus outputErrorStatus;
	protected ResultingOutErr resultingOutError;
	
	public Object[] getArgs() {
		return new Object[] {} ;
	}
	public enum OutputErrorStatus {
		NO_OUTPUT, CORRECT_OUTPUT_NO_ERRORS, CORRECT_OUTPUT_ERRORS, INCORRECT_OUTPUT_NO_ERRORS, INCORRECT_OUTPUT_ERRORS
	}
	
	protected boolean isInteractive() {
		return false;
	}
	
	protected Class[] getArgTypes() {
		Object[] anArgs = getArgs();
		  Class[] anArgTypes = BasicProjectIntrospection.toClasses(anArgs);
		    BasicProjectIntrospection.toPrimitiveTypes(anArgTypes);
		    return anArgTypes;
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
	protected Class getTargetClass() {
//		return BasicProjectIntrospection.findClass(CurrentProjectHolder.getOrCreateCurrentProject(), getClassName());
		return getTargetClasses() [0];

	}
	protected Class[] getTargetClasses() {
		String[] aClassNames = getClassNames();
		Class[] aTargetClasses = new Class[aClassNames.length];
		for (int i = 0; i < aClassNames.length; i++) {
			aTargetClasses[i] = BasicProjectIntrospection.findClass(CurrentProjectHolder.getOrCreateCurrentProject(), aClassNames[i]);
		}
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
		return null;
	}
	protected OutputErrorStatus getOutputErrorStatus() {
		return outputErrorStatus;
	}
	protected ResultWithOutput getResultWithOutput() {
		return resultWithOutput;
	}
	protected String getOutput() {
		return resultingOutError.out;
	}
	protected String getError() {
		return resultingOutError.err;
	}
	protected ResultingOutErr getResultingOutErr() {
		return resultingOutError;
	}
	protected Object getReturnValue() {
		return returnValue;
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
	protected long getTimeOut() {
		return BasicProjectExecution.DEFAULT_METHOD_TIME_OUT;
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
	protected OutputErrorStatus computeOutputErrorStatus() {
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
			return OutputErrorStatus.CORRECT_OUTPUT_ERRORS;
		}
		if (!validOutput && !hasError) {
			return OutputErrorStatus.INCORRECT_OUTPUT_NO_ERRORS;
		}
		return OutputErrorStatus.INCORRECT_OUTPUT_ERRORS;
	}
	protected boolean invokeMethod() throws Throwable  {
		Object aTargetObject = null ;
		Method aMethod = null;
		for (Object anObject:getTargetObjects()) {
			aTargetObject = anObject;
			if (anObject == null) {
				
				continue;
			}
			Class aClass = getMethodClass(aTargetObject);
			aMethod = BasicProjectIntrospection.findMethod(
					aClass, getMethodName(), getArgTypes());
			if (aMethod != null ) {
				break;
			}
		}
		if (aTargetObject == null) {
			Assert.assertTrue(noClassMessage()
					+ NotesAndScore.PERCENTAGE_MARKER
					+ noClassMessage(), false);
		}
		if (aMethod == null) {
			Assert.assertTrue(noMethodMessage()
					+ NotesAndScore.PERCENTAGE_MARKER
					+ noMethodCredit(), false);
		}
		if (isInteractive()) {
//			resultWithOutput = BasicProjectExecution.
//					proxyAwareGeneralizedInteractiveTimedInvoke(
//							getTargetObject(),
//							getMethod(),
//							getArgs(),
//							getInput(),
//							getTimeOut());
			resultWithOutput = BasicProjectExecution.
					proxyAwareGeneralizedInteractiveTimedInvoke(
							aTargetObject,
							aMethod,
							getArgs(),
							getInput(),
							getTimeOut());
			if (resultWithOutput == null) {
				returnValue = null;
				resultingOutError = null;
				return false;
			}
			returnValue = resultWithOutput.getResult();
			resultingOutError = new ResultingOutErr(
					resultWithOutput.getOutput(), 
					resultWithOutput.getError());
			setOutputErrorStatus();
		} else {
			returnValue = BasicProjectExecution.proxyAwareTimedInvoke(
					aTargetObject,
					aMethod,
					getArgs(),
					getTimeOut());
			if (returnValue == null) {
				return false;
			}
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
		return processReturnValue();
	}
	protected boolean processReturnValue() {
		if (returnValueIsExpected()) {
			return true;
		}
		if (returnValueIsOffByOne()) {
			Assert.assertTrue(offByOneMessage()
					+ NotesAndScore.PERCENTAGE_MARKER
					+ offByOneCredit(), false);
		}
		return true;
	}
	protected boolean returnValueIsExpected() {
		return getExpectedReturnValue().equals(getReturnValue());
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
	protected boolean processMethodExecutionResults() {
		return true;
	}
	protected String noMethodMessage() {
		return "No method:" + getMethodName();
	}
	
	protected String offByOneMessage() {
		return "Expected value" + getExpectedReturnValue() + 
				" is off by one from return value " + getReturnValue();
	}
	protected double offByOneCredit() {
		return 0.8;
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
	protected double noClassCredit() {
		return 0.0;
	}
	
	protected boolean doTest() throws Throwable {
		invokeMethod();
    	return processMethodExecutionResults();
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
		String aReasons = possibleReasonsForIncorrectOutput();
		if (aReasons.isEmpty())
			return aPrefix;
		return aPrefix + ". Possible reasons:" + aReasons;
	}
	protected String possibleReasonsForIncorrectOutput() {
		return "";
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

		Assert.assertTrue(incorrectOutputMessage()
				+ NotesAndScore.PERCENTAGE_MARKER + incorrectOutputCredit(),

		false);
		return true;
	}
	@Test
    public void test() {
        try {
        	doTest();          
            
        } catch (Throwable e) {
        	BasicJUnitUtils.assertTrue(e, 0);
        }
    }
	
}
