package byteman.tools.exampleTestCases.factorial;

import byteman.tools.AbstractBytemanUnitTest;

public class FactorialSolutionTest extends AbstractBytemanUnitTest{

	int value = 5;
	
	String [] regexs = {
	};

	@Override
	protected Class<?> getTarget() {
		return getRegistry().getFactorial();
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
