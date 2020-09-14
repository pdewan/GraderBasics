package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.List;

import grader.basics.junit.TestCaseResult;
import grader.basics.project.Project;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;

public class OldMagicNumberTestCase extends CheckStyleTestCase {
	public static final double DEFAULT_PENALTY_PER_MISTAKE = 0.2;
	protected double penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	public static final String WARNING_NAME = "magic";

	public OldMagicNumberTestCase(String aMessage) {
		super(null, aMessage);
	}
	public OldMagicNumberTestCase() {
		super(null, "Magic Number");
	}	

	@Override
	public String negativeRegexLineFilter() {
		// TODO Auto-generated method stub
		return ".*" + WARNING_NAME + ".*";
	}
	@Override
	 protected  String warningName(){
	    	return WARNING_NAME;
	    }

	@Override
	public String failMessageSpecifier(List<String> aFailedLines) {
		return "Magic number failed in:\n" + toLinesString(aFailedLines);
	}
//	protected String warningName() {
//		return WARNING_NAME;
//	}
//	protected String beautify (String aCheckstyleString) {
//		return aCheckstyleString.substring(aCheckstyleString.indexOf(warningName())) + "\n";
//	}
//	protected String beautify (List<String> aList) {
//		StringBuffer sb = new StringBuffer();
//		for (String aString: aList) {
//			String beautifiedString = beautify(aString);
//			sb.append(beautifiedString);
//			
//		}
//		return sb.toString();
//	}
	
	protected  TestCaseResult test (Project aProject, String[] aCheckStyleLines, List<String> aFailedMatchedLines, List<String> aSucceededMatchedLines, boolean autoGrade) {
		if (aFailedMatchedLines.size() == 0) {
			return pass();
		}
		double score = Math.min(aFailedMatchedLines.size(), 5) / 5.0;
        score = Math.max(0, 1 - score);
        return partialPass(score, beautify(aFailedMatchedLines));
    	
    }
	 

}
