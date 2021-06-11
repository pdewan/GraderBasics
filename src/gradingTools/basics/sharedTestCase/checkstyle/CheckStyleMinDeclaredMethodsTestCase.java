package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.List;

import grader.basics.junit.TestCaseResult;
import grader.basics.project.Project;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;


public class CheckStyleMinDeclaredMethodsTestCase extends CheckStyleTestCase {
	int minimum;
	 public CheckStyleMinDeclaredMethodsTestCase(int aMinimum) {
	        super(null, "Min declared method test case");
	        minimum = aMinimum;
	    }
    
	@Override
	public String negativeRegexLineFilter() {
		return "(.*)minDeclaredMethods(.*)";
	}



	@Override
	public String failMessageSpecifier(List<String> aFailedLines) {
		// TODO Auto-generated method stub
		return "Number of declared methods less than" + minimum;
	}
  //String literal expressions should be on the left side
	 protected TestCaseResult computeResult (Project aProject, String[] aCheckStyleLines, List<String> aFailedLines, List<String> aSucceededLines, boolean autoGrade) {
	    	return singleMatchScore(aProject, aCheckStyleLines, aFailedLines, aSucceededLines, autoGrade);
	    	
	}

}

