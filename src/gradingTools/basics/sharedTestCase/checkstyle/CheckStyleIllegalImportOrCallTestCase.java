package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.List;

import grader.basics.junit.TestCaseResult;
import grader.basics.project.Project;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;


public class CheckStyleIllegalImportOrCallTestCase extends CheckStyleTestCase {
	public static final String WARNING_NAME = "illegal";

	 public CheckStyleIllegalImportOrCallTestCase() {
	        super(null, "Illegal import or call test case");
	    }
    
	@Override
	public String negativeRegexLineFilter() {
		return "((.*)illegalTypeImported(.*))|((.*)illegalMethodCall(.*))";
	}

	@Override
	protected  String warningName(){
	   return WARNING_NAME;
	}

	@Override
	public String failMessageSpecifier(List<String> aFailedLines) {
		return "Illegal type or call in:\n" + beautify(aFailedLines);
	}
	 protected TestCaseResult computeResult (Project aProject, String[] aCheckStyleLines, List<String> aFailedLines, List<String> aSucceededLines, boolean autoGrade) {
	    	return singleMatchScore(aProject, aCheckStyleLines, aFailedLines, autoGrade);
	    	
	}
	 public static void main (String[] args) {		 
		 System.out.println(
				 "illegalMethodCall: (Assignment4.java:31) called disallowed method main--String.split	Assignment4.java" 	
				  .matches("((.*)illegalTypeImported(.*))|((.*)illegalMethodCall(.*))"));

	 }

}

