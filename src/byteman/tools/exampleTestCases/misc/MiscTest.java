package byteman.tools.exampleTestCases.misc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import byteman.tools.AbstractBytemanUnitTest;
import byteman.tools.InjectionTargeterFactory;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodCalledTestCase;


public class MiscTest 
//	extends AbstractRecursiveFactorialTest
	extends AbstractBytemanUnitTest
	{
	String [] regexs = {
			".*called.*size.*",
			".*exited size.*0.*",
			
		};
//	@Override
//	protected Class<?> getTarget() {
//		return getRegistry().;
//	}

	private String[] tagsArray = {
			"MiscClass"
	};
	Set<String> tagsSet = new HashSet(Arrays.asList(tagsArray));
	@Override
	protected Set<String> getTags() {
		// TODO Auto-generated method stub
		return tagsSet;
	}
	
	@Override
	protected String getMethodName() {
		return "size";
//		return "getName";
//		return "clear";
//		return "getMonth";
//		return "length";
	}


//	@Override
//	protected Object[] getArgs() {
//		return new Object[] {};
//	}
//	
//	@Override
//	protected Object[] getConstructorArgs() {
//		return new Object[] {System.currentTimeMillis()};
//	}
	
//	@Override
//	protected Class[] getConstructorArgTypes() {
//		return new Class[] {Long.TYPE};
//	}
//
//	@Override
//	protected Class[] getArgTypes() {
//		return new Class[] {Integer.TYPE};
//	}
	

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
		return "0";
	}


	
	@Override
	protected Object[] getArgs() {
		// TODO Auto-generated method stub
		return new Object[] {};
	}



}
