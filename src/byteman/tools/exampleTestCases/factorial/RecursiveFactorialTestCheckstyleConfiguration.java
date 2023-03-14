package byteman.tools.exampleTestCases.factorial;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import byteman.tools.AbstractBytemanUnitTest;
import byteman.tools.InjectionTargeterFactory;


public class RecursiveFactorialTestCheckstyleConfiguration 
	extends AbstractRecursiveFactorialTest
//	extends AbstractBytemanUnitTest
	{

////	int value = 5;
////	
////	String [] regexs = {
////		".*called.*factorial.*from.*factorial.*",
////		".*called.*factorial.*from.*factorial.*",
////		".*called.*factorial.*from.*factorial.*",
////		".*called.*factorial.*from.*factorial.*",
////		".*exited factorial.*1.*",
////		".*exited factorial.*2.*",
////		".*exited factorial.*6.*",
////		".*exited factorial.*24.*",
////		".*exited factorial.*120.*",
////	};
//
////	@Override
////	protected Class<?> getTarget() {
////		return getRegistry().getFactorial();
////	}
//	
//	String[] factorialTagsArray = {
//			"Factorial"
//	};
//	
//	Set<String> factorialTagsSet = new HashSet(Arrays.asList(factorialTagsArray));
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
//	protected String getMethodName() {
//		return "factorial";
//	}
//
//
//	@Override
//	protected Object[] getArgs() {
//		return new Object[] {value};
//	}

//
//	@Override
//	protected String[] getRegexes() {
//		return regexs;
//	}
//
//	@Override
//	protected boolean testMethodReturnMatches(Object returnValue) {
//		if(returnValue instanceof Integer) {
//			return ((Integer)returnValue).intValue() == 120;		
//		}
//		return false;
//	}
//
//	@Override
//	protected String getExpectedResult() {
//		return "120";
//	}
}
