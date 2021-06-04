package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.List;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;
import util.trace.Tracer;


public class CheckstyleSpecificWarningTestCase extends CheckStyleTestCase {

	 protected String warningClassName;
	 @Override
	 protected boolean checkForPositives() {
		 return false;
	 }
	

	 public CheckstyleSpecificWarningTestCase (String aWarningClassName) {
	        super("", aWarningClassName);
	        warningClassName = aWarningClassName;
//	        super(aType, aType + "!" + aMethod);

	        
	        
	  }
	 @Override
	 protected String warningName() {
		 return warningClassName;
	 }
	
	
//	@Override
//	public String negativeRegexLineFilter() {
//	
//		return ".*" + "WARN" + ".*" +"\\[" + warningClassName +"\\]" + ".*";
//	}

		



	 public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {

		 TestCaseResult aResult = super.test(project, autoGrade);
		 return aResult;
//		 double aTotalFractionComplete = 0;
//		 for (PassFailJUnitTestCase aTestCase:precedingTestInstances) {
//			 aTotalFractionComplete += aTestCase.getLastResult().getPercentage();
//		 }
//		 double anAverageFractionComplete =  aTotalFractionComplete/ (double) precedingTestInstances.size();
//		 double anOriginalFractionComplete = aResult.getPercentage();
//		 Tracer.info(CheckstyleSpecificWarningTestCase.class, "Score of " + anOriginalFractionComplete + " scaled by average preceding test pass percentage:" + anAverageFractionComplete);
//	      aResult.setPercentage(anOriginalFractionComplete*anAverageFractionComplete); 
//		 return aResult;
	 }
	 
//	 public TestCaseResult scaleResult(TestCaseResult aResult) {
//		 double aTotalFractionComplete = 0;
//		 for (PassFailJUnitTestCase aTestCase:precedingTestInstances) {
//			 aTotalFractionComplete += aTestCase.getLastResult().getPercentage();
//		 }
//		 double anAverageFractionComplete =  aTotalFractionComplete/ (double) precedingTestInstances.size();
//		 double anOriginalFractionComplete = aResult.getPercentage();
//		 Tracer.info(CheckstyleSpecificWarningTestCase.class, "Score of " + anOriginalFractionComplete + " scaled by average preceding test pass percentage:" + anAverageFractionComplete);
//	      aResult.setPercentage(anOriginalFractionComplete*anAverageFractionComplete); 
//		 return aResult;
//	 }


	 protected TestCaseResult computeResult (Project aProject, String[] aCheckStyleLines, List<String> aFailedLines, List<String> aSucceededLines, boolean autoGrade) {
		 return singleMatchScore(aProject, aCheckStyleLines, aFailedLines, aSucceededLines, autoGrade);

	}

}

