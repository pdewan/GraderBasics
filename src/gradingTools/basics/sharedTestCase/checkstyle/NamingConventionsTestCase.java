package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.List;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;


public class NamingConventionsTestCase extends CheckstyleSpecificWarningTestCase {

	 @Override
	 protected boolean checkForPositives() {
		 return false;
	 }
	

	 public NamingConventionsTestCase () {
	        super("");
//	        super(aType, aType + "!" + aMethod);

	        
	        
	  }
	
	
	@Override
	public String negativeRegexLineFilter() {
	
		return ".*WARN.*must match pattern.*\\[.*Name\\]";
	}

		



	 public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {

		 TestCaseResult aResult = super.test(project, autoGrade);
	        return aResult;
	 }


	 protected TestCaseResult computeResult (Project aProject, String[] aCheckStyleLines, List<String> aFailedLines, List<String> aSucceededLines, boolean autoGrade) {
		 return singleMatchScore(aProject, aCheckStyleLines, aFailedLines, aSucceededLines, autoGrade);

	}

}

