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
	protected Object getTargetObject() {
		return getTargetClass();
	}
	protected Class getTargetClass() {
		return BasicProjectIntrospection.findClass(CurrentProjectHolder.getOrCreateCurrentProject(), getClassName());

	}
	protected Method getMethod() {
		return 	BasicProjectIntrospection.findMethod(
				getTargetClass(), getMethodName(), getArgTypes());
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

	protected void invokeMethod() throws Throwable  {
		if (isInteractive()) {
			resultWithOutput = BasicProjectExecution.
					proxyAwareGeneralizedInteractiveTimedInvoke(
							getTargetObject(),
							getMethod(),
							getArgs(),
							getInput(),
							getTimeOut());
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
		}
	}
	protected void compareActualAndExcpectedReturnValue() {
		
	}
	@Test
    public void test() {
        try {
        	invokeMethod();
            
            
        } catch (Throwable e) {
        	BasicJUnitUtils.assertTrue(e, 0);
        }
    }
	
}
