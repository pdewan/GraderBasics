package gradingTools.shared.testcases;

import grader.basics.junit.NotesAndScore;
import grader.basics.project.BasicProjectIntrospection;

import org.junit.Assert;

import util.annotations.MaxValue;
@MaxValue(5)
public abstract class GetterExecutionTest extends BeanExecutionTest{
	protected Object actualReturnValue;
	protected abstract String propertyName();
	protected abstract Class expectedClass();
	@Override
	protected String[] getOutputPropertyNames() {
		return new String[]{propertyName()};
	}
	protected double noExceptionCredit() {
		return 0.3;
	}
	protected double correctClassCredit() {
		return 0.7;
	}
	protected String incorrectClassMessage() {
		return "Object " + actualReturnValue + " not instance of " + 
	  BasicProjectIntrospection.getTags(expectedClass());
	}
	
	protected void assertWrongObject() {
		Assert.assertTrue(incorrectClassMessage() + NotesAndScore.PERCENTAGE_MARKER + noExceptionCredit(), false);
	}
	@Override
	protected boolean checkOutput(Object actualReturnValue) {
		
		if (actualReturnValue == null) {
			assertWrongObject();
		}		
		if (!expectedClass().isInstance(actualReturnValue))
			assertWrongObject();
		
		return true;
	}
	protected boolean doTest() throws Throwable {
		executeBean();
		actualReturnValue = outputPropertyValues.get(propertyName());
		return checkOutput(actualReturnValue);
	}
	
}
