package byteman.tools.exampleTestCases.factorial;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import byteman.tools.AbstractBytemanUnitTest;

public class FactorialSolutionTest extends AbstractBytemanUnitTest{

	int value = 5;
	
	String [] regexs = {
	};

//	@Override
//	protected Class<?> getTarget() {
//		return getRegistry().getFactorial();
//	}
	
	String[] factorialTagsArray = {
			"Factorial"
	};
	
	Set<String> factorialTagsSet = new HashSet(Arrays.asList(factorialTagsArray));
	@Override
	protected Set<String> getTags() {
		// TODO Auto-generated method stub
		return factorialTagsSet;
	}

	@Override
	protected String getMethodName() {
		return "factorial";
	}


	@Override
	protected Object[] getArgs() {
		return new Object[] {value};
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
