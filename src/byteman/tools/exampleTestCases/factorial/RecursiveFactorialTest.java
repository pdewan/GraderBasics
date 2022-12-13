package byteman.tools.exampleTestCases.factorial;

import byteman.tools.AbstractBytemanUnitTest;

public class RecursiveFactorialTest extends AbstractBytemanUnitTest{

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
