package gradingTools.shared.testcases.greeting;

import grader.basics.junit.JUnitTestsEnvironment;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;

import util.annotations.MaxValue;
@MaxValue(2)
public class GreetingMainProvided extends PassFailJUnitTestCase {
	public Class greetingMain;

	public Class getGreetingMain() {
		return greetingMain;
	}
	
	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {	
		 
		 try {
			 GreetingClassRegistryProvided aClassRegistryProvided = (GreetingClassRegistryProvided) JUnitTestsEnvironment.getAndPossiblyRunGradableJUnitTest(GreetingClassRegistryProvided.class);
				
				GreetingClassRegistry aClassRegistry = aClassRegistryProvided.getTimingOutClassRegistryProxy();  
			    if (aClassRegistry == null) {
			    	return fail ("No class registry");
			    }
//			sExpressionClass = (Class<? extends SExpression>) callGetterOnClassRegistry(aMethodName);
			greetingMain = aClassRegistry.getGreetingMain();   
		    if (greetingMain == null) {	
		    	return fail ("getGreetingMain returns null in class registry");
		    }	
		    
			return pass();		
		 } 		 catch (Exception e) {
			 return fail (e.getMessage());
		 } catch (Throwable e) {
			 	return fail(e.getMessage());
		}
	}

}
