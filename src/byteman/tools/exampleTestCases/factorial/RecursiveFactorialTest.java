package byteman.tools.exampleTestCases.factorial;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import byteman.tools.AbstractBytemanUnitTest;
import byteman.tools.InjectionTargeterFactory;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodCalledTestCase;


public class RecursiveFactorialTest 
//	extends AbstractRecursiveFactorialTest
	extends AbstractBytemanUnitTest
	{
	
int value = 5;
	
	String [] regexs = {
		".*called.*factorial.*from.*factorial.*",
		".*called.*factorial.*from.*factorial.*",
		".*called.*factorial.*from.*factorial.*",
		".*called.*factorial.*from.*factorial.*",
		".*exited factorial.*1.*",
		".*exited factorial.*2.*",
		".*exited factorial.*6.*",
		".*exited factorial.*24.*",
		".*exited factorial.*120.*",
	};
	Object[] returnValues = {
			1,
			2,
			6,
			24,
			120
	};
	
	private String[] factorialTagsArray = {
			"Factorial"
	};
	private Class[] PRECEDING_TESTS = {
			FactorialIsRecursive.class
	};
	
//	@Override
//	protected Class[] precedingTests() {
//		return PRECEDING_TESTS;
//	}
	
//	@Override
//	protected  CheckstyleMethodCalledTestCase checkStyleMethodCalledTest() {
////		List<PassFailJUnitTestCase> aPrecedingInstances =
//		return (CheckstyleMethodCalledTestCase) getPrecedingTestInstances().get(0);
//	}
	
	@Override
	protected int numExpectedCalls() {
//		return 5;
		return -1;
	}
	
	protected Object[] expectedReturnValues() {
		return returnValues;
	}

	
	Set<String> factorialTagsSet = new HashSet(Arrays.asList(factorialTagsArray));
	@Override
	protected Set<String> getTags() {
		// TODO Auto-generated method stub
		return factorialTagsSet;
	}
//	Class target;
//	@Override
//	protected Class<?> getTarget() {
//		if (target == null) {
//		try {
//			String aClassName = InjectionTargeterFactory.getInjectionTargeter().getClassName(factorialTagsSet);
//			target = Class.forName(aClassName);
//		} catch (Exception e) {
//			e.printStackTrace();
////			return null;
//		}
//		}
//		return target;
//		
//	}

//	@Override
//	protected Class<?> getTarget() {
//		return getRegistry().getFactorial();
//	}


	@Override
	protected String getMethodName() {
		return "factorial";
	}


	@Override
	protected Object[] getArgs() {
		return new Object[] {value};
	}
	
	@Override
	protected Object[] getConstructorArgs() {
		return new Object[] {System.currentTimeMillis()};
	}
	
	@Override
	protected Class[] getConstructorArgTypes() {
		return new Class[] {Long.TYPE};
	}

	@Override
	protected Class[] getArgTypes() {
		return new Class[] {Integer.TYPE};
	}
	

	@Override
	protected String[] getRegexes() {
		return regexs;
	}

	@Override
	protected boolean testMethodReturnMatches(Object returnValue) {
		if(returnValue instanceof Integer) {
			return ((Integer)returnValue).intValue() == 120;		
		}
		return false;
	}

	@Override
	protected String getExpectedResult() {
		return "120";
	}
}
