package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;


public class CheckstyleMethodNotCalledTestCase extends CheckstyleMethodCalledTestCase {
	 public CheckstyleMethodNotCalledTestCase(String aType, String aCalledMethod) {
		 super(aType, aCalledMethod) ;       
	  }
	 
	 
	 public CheckstyleMethodNotCalledTestCase(String aType, String aCallingMethod, String aCalledMethod) {
	        super(aType, aCallingMethod, aCalledMethod);
	  }
	 @Override
	 protected String maybeProcessFailedLine (String aSucceededLine) {
		 String aSuperProcessedLine = super.maybeProcessFailedLine(aSucceededLine);
//		 String retVal = aSuperProcessedLine.replace("INFO", "WARN");// will be removed by super class
		 String retVal = aSuperProcessedLine.replace(" Good! ", "");
		 retVal = retVal.replace("expected", "unexpected");
		 return retVal;
		}
	 @Override
	 protected String maybeProcessSucceededLine (String aSucceededLine) {
		 String aSuperProcessedLine = super.maybeProcessSucceededLine(aSucceededLine);
		 String retVal = aSuperProcessedLine.replace("WARN", "INFO");
		 return retVal;
		}
	 
	@Override
	public String negativeRegexLineFilter() {
		return super.positiveRegexLineFilter();
	}
	@Override
	public String positiveRegexLineFilter() {
		return super.negativeRegexLineFilter();
	}
}

