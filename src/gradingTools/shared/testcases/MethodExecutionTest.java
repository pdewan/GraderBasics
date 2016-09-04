package gradingTools.shared.testcases;

import java.lang.reflect.Method;

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

import util.awt.AnOutputQueue;


public abstract class MethodExecutionTest  {
	
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
		return true;
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
	protected boolean invokeMethod() throws Throwable  {
		Object aTargetObject = null ;
		Method aMethod = null;
		for (Object anObject:getTargetObjects()) {
			aTargetObject = anObject;
			if (anObject == null) {
				
				continue;
			}
			Class aClass = getMethodClass(anObject);
			aMethod = BasicProjectIntrospection.findMethod(
					aClass, getMethodName(), getArgTypes());
			if (aMethod != null ) {
				break;
			}
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
		} else {
			returnValue = BasicProjectExecution.proxyAwareTimedInvoke(
					getTargetObject(),
					getMethod(),
					getArgs(),
					getTimeOut());
			if (returnValue == null) {
				return false;
			}
		}
		return false;
	}
	protected boolean processMethodExecutionResults() {
		return true;
	}
	
	protected boolean doTest() throws Throwable {
		invokeMethod();
    	return processMethodExecutionResults();
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
