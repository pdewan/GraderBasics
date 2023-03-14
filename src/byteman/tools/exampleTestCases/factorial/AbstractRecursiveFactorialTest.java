package byteman.tools.exampleTestCases.factorial;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import byteman.tools.AbstractBytemanUnitTest;
import byteman.tools.InjectionTargeterFactory;

public abstract class AbstractRecursiveFactorialTest extends AbstractBytemanUnitTest{

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
	String[] factorialTagsArray = {
			"Factorial"
	};
	
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
