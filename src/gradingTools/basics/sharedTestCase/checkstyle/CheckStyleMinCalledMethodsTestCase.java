package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.List;

import grader.basics.junit.TestCaseResult;
import grader.basics.project.Project;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;


public class CheckStyleMinCalledMethodsTestCase extends CheckStyleTestCase {
	int minimum;
	 public CheckStyleMinCalledMethodsTestCase(int aMinimum) {
	        super(null, "Min called method test case");
	        minimum = aMinimum;
	    }
    
	@Override
	public String negativeRegexLineFilter() {
		return "(.*)minCalledMethods(.*)";
	}



	@Override
	public String failMessageSpecifier(List<String> aFailedLines) {
		// TODO Auto-generated method stub
		return "Number of called methods less than" + minimum;
	}
  //String literal expressions should be on the left side
	 protected TestCaseResult computeResult (Project aProject, String[] aCheckStyleLines, List<String> aFailedLines, List<String> aSucceededLines, boolean autoGrade) {
	    	return singleMatchScore(aProject, aCheckStyleLines, aFailedLines, aSucceededLines, autoGrade);
	    	
	}

}

