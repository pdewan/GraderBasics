package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.List;

import grader.basics.junit.TestCaseResult;
import grader.basics.project.Project;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;

public abstract class WarningsRatioKnownMax extends CheckStyleWarningsTestCase {
//	public static final double DEFAULT_PENALTY_PER_MISTAKE = 0.2;
//	protected double penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	
	protected abstract int maxPositives();
	public WarningsRatioKnownMax() {
		super("", "");
	}

	public WarningsRatioKnownMax(String aTypeName, String aMessage) {
		super(aTypeName, aMessage);
	}
	
	public WarningsRatioKnownMax(String aTypeName, String aMessage, double aPenaltyPerMistake) {
		super(aTypeName, aMessage);
//		penaltyPerMistake = aPenaltyPerMistake;

	}
//	@Override
//	public String positiveRegexLineFilter() {
//		String anInfoName = infoName();
//		if (anInfoName == null) {
//			return null;
//		}
//		// TODO Auto-generated method stub
//		return ".*" + anInfoName + ".*";
//	}
//	
//	@Override
//	public String negativeRegexLineFilter() {
//		String aWarningName = warningName();
//		if (aWarningName == null) {
//			return null;
//		}
//		// TODO Auto-generated method stub
//		return ".*" + aWarningName + ".*";
//	}
//
//	@Override
//	public String failMessageSpecifier(List<String> aFailedLines) {
//		return name + "failed in:\n" + beautify(aFailedLines);
//	}
	
//	String beautify (String aCheckstyleString) {
//		return aCheckstyleString.substring(aCheckstyleString.indexOf(WARNING_NAME)) + "\n";
//	}
//	String beautify (List<String> aList) {
//		StringBuffer sb = new StringBuffer();
//		for (String aString: aList) {
//			String beautifiedString = beautify(aString);
//			sb.append(beautifiedString);
//			
//		}
//		return sb.toString();
//	}
//	 protected abstract  String warningName();
//	 protected  String infoName() {
//		 return null;
//	 }
	
//	protected  TestCaseResult test (Project aProject, String[] aCheckStyleLines, List<String> aFailedMatchedLines, List<String> aSucceededMatchedLines, boolean autoGrade) {
//		double aScore = computeScore(aProject, aCheckStyleLines, aFailedMatchedLines, aSucceededMatchedLines, autoGrade);
//		if (aScore == 1) {
//			return pass();
//		}
//		printTrace(aScore, aProject, aCheckStyleLines, aFailedMatchedLines, aSucceededMatchedLines, autoGrade);
//		
//		return partialPass(aScore, "See console trace");
//		
//
//
//    	
//    }
//	protected  TestCaseResult test (Project aProject, String[] aCheckStyleLines, List<String> aFailedMatchedLines, List<String> aSucceededMatchedLines, boolean autoGrade) {
//		if (aFailedMatchedLines != null && aFailedMatchedLines.size() == 0) {
//			return pass();
//		}
////		double score = Math.min(aMatchedLines.size(), 5) / 5.0;
////        score = Math.max(0, 1 - score);
////        return partialPass(score, beautify(aMatchedLines));
//		double aPenalty = aFailedMatchedLines.size() * penaltyPerMistake;
//        double aScore  = Math.max(0, 1 - aPenalty);
////        return partialPass(aScore, beautify(aFailedMatchedLines));
//        if (aScore != 1) {
//        	if (aFailedMatchedLines != null) {
//        	System.out.println(aFailedMatchedLines.size()  + " Lines failing check");
//        	for (String aLine:aFailedMatchedLines) {
//        		System.out.println(aLine);
//        	}
////        	System.out.println(Arra(aFailedMatchedLines));
//        	}
//        	if (aSucceededMatchedLines != null) {
//        		System.out.println(aSucceededMatchedLines.size()  + "Lines succeeding check");
////            	System.out.println(beautify(aSucceededMatchedLines));
//        		for (String aLine:aSucceededMatchedLines) {
//            		System.out.println(aLine);
//            	}
//        	}
//        }
//        return partialPass(aScore, "");
//
//    	
//    }
//	protected  void printTrace (double aScore, Project aProject, String[] aCheckStyleLines, List<String> aFailedMatchedLines, List<String> aSucceededMatchedLines, boolean autoGrade) {
//		
//
//        if (aScore != 1) {
//        	if (aFailedMatchedLines != null) {
//        	System.out.println(aFailedMatchedLines.size()  + " negatives.");
//        	for (String aLine:aFailedMatchedLines) {
//        		System.out.println(aLine);
//        	}
////        	System.out.println(Arra(aFailedMatchedLines));
//        	}
//        	if (aSucceededMatchedLines != null) {
//        		System.out.println(aSucceededMatchedLines.size()  + " positives");
////            	System.out.println(beautify(aSucceededMatchedLines));
//        		for (String aLine:aSucceededMatchedLines) {
//            		System.out.println(aLine);
//            	}
//        	}
//        }
////        return partialPass(aScore, "");
//
//    	
//    }
	
//	protected int numFailedMatches (List<String> aFailedMatchedLines) {
//		return aFailedMatchedLines == null?0:aFailedMatchedLines.size();
//
//	}
//	protected int numSucceededMatches (List<String> aSucceededMatchedLines) {
//		return aSucceededMatchedLines == null?0:aSucceededMatchedLines.size();
//
//
//	}
	
	
	protected  double computeScore (Project aProject, String[] aCheckStyleLines, List<String> aFailedMatchedLines, List<String> aSucceededMatchedLines, boolean autoGrade) {
//		if (aFailedMatchedLines != null && aFailedMatchedLines.size() == 0 && aSucceededMatchedLines != null && aSucceededMatchedLines) {
//			return 1.0;
//		}
//		double score = Math.min(aMatchedLines.size(), 5) / 5.0;
//        score = Math.max(0, 1 - score);
//        return partialPass(score, beautify(aMatchedLines));
//		double aNumFailedLines = aFailedMatchedLines == null?0:aFailedMatchedLines.size();
//		double aNumSucceededLines = aSucceededMatchedLines == null?0:aSucceededMatchedLines.size();
//		
		double aNumFailedLines = numFailedMatches(aFailedMatchedLines);
		double aNumSucceededLines = numSucceededMatches(aSucceededMatchedLines);
		double aMaxPositives = maxPositives();
		if (aSucceededMatchedLines != null) {
			return Math.min(aMaxPositives, aNumSucceededLines)/aMaxPositives;
		}
		if (aFailedMatchedLines != null) {
			return Math.max(0, aMaxPositives - aNumFailedLines) / aMaxPositives;
		}
		return 1;
			
    	
    }
}
