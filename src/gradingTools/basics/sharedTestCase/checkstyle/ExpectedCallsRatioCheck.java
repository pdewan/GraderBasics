package gradingTools.basics.sharedTestCase.checkstyle;

import grader.basics.junit.JUnitTestsEnvironment;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleWarningsCountingTestCase;
import gradingTools.shared.testcases.utils.AbstractConfigurationProvided;

public class ExpectedCallsRatioCheck extends CheckStyleWarningsRatioTestCase {
	//expectedMethodCall = method {1} in class matching {2}  made expected call {0}. Good!
	//missingMethodCall = method {1}  in class matching {2} has not made expected call {0}.
	public static final String WARNING_NAME = "No method.*made expected call";
	public static final String INFO_NAME = "has made expected call.*Good!";

	public static final String MESSAGE = "Expected Calls";
	public static final double DEFAULT_PENALTY_PER_MISTAKE = 0.1;

	public ExpectedCallsRatioCheck(String aMessage) {
		super(null, aMessage);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public ExpectedCallsRatioCheck() {
		super(null, MESSAGE);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public ExpectedCallsRatioCheck(double aPenaltyPerMistake) {
		super(null, MESSAGE, aPenaltyPerMistake);

	}
	
//	protected Class configurationClass() {
//		return null;
//	}

	@Override
	protected String warningName() {
		return WARNING_NAME;
	}

	@Override
	protected String infoName() {
		return INFO_NAME;
	}
	
	@Override 
	protected boolean addBrackets() {
		return false;
	}
//	public TestCaseResult test(Project aProject, boolean autoGrade) throws NotAutomatableException, NotGradableException {
//        TestCaseResult aSuperResult = super.test(aProject, autoGrade);
//        Class aConfigurationClass = configurationClass();
//        if (aConfigurationClass == null) {
//        	return aSuperResult;
//        }
//        AbstractConfigurationProvided aConfigurationProvided = (AbstractConfigurationProvided) JUnitTestsEnvironment.getAndPossiblyRunGradableJUnitTest(aConfigurationClass);
//		return aConfigurationProvided.computeResultBasedOnTaggedClasses(aSuperResult);
//        
////        return retVal;
//
//        
//    }

}
