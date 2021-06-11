package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.List;

import grader.basics.execution.GradingMode;
import grader.basics.junit.JUnitTestsEnvironment;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;
import gradingTools.shared.testcases.utils.AbstractConfigurationProvided;
import gradingTools.shared.testcases.utils.TaggedClassesDefined;

public abstract class CheckStyleWarningsTestCase extends CheckStyleTestCase {
//	public static final double DEFAULT_PENALTY_PER_MISTAKE = 0.2;
//	protected double penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;

	public CheckStyleWarningsTestCase(String aTypeName, String aMessage) {
		super(aTypeName, aMessage);
	}
	
	public CheckStyleWarningsTestCase(String aTypeName, String aMessage, double aPenaltyPerMistake) {
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

	@Override
	public String failMessageSpecifier(List<String> aFailedLines) {
		return name + "failed in:\n" + beautify(aFailedLines);
	}
	
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
	@Override
	
	protected  TestCaseResult test (Project aProject, String[] aCheckStyleLines, List<String> aFailedMatchedLines, List<String> aSucceededMatchedLines, boolean autoGrade) {
		double aScore = computeScore(aProject, aCheckStyleLines, aFailedMatchedLines, aSucceededMatchedLines, autoGrade);
		if (aScore == 1) {
			return pass();
		}
		printTrace(aScore, aProject, aCheckStyleLines, aFailedMatchedLines, aSucceededMatchedLines, autoGrade);
		
		return partialPass(aScore, "See console trace about lines failing  this check");
		


    	
    }
	protected Class configurationClass() {
		return null;
	}
	
	public TestCaseResult test(Project aProject, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        TestCaseResult aSuperResult = super.test(aProject, autoGrade);
        return scaleResult(aSuperResult);
        //        return toConfigurationBasedResult(aProject, aSuperResult);


        
    }
	public TestCaseResult toConfigurationBasedResult(Project aProject, TestCaseResult aSuperResult) throws NotAutomatableException, NotGradableException {
//        TestCaseResult aSuperResult = super.test(aProject, autoGrade);
        Class aConfigurationClass = configurationClass();
        
        if (aConfigurationClass == null) {
        
        	return toTaggedClassBasedResult (aProject, aSuperResult);
        }
        AbstractConfigurationProvided aConfigurationProvided = (AbstractConfigurationProvided) JUnitTestsEnvironment.getAndPossiblyRunGradableJUnitTest(aConfigurationClass);
		return aConfigurationProvided.computeResultBasedOnTaggedClasses(aSuperResult);
        
//        return retVal;

        
    }
	protected Class taggedClassesDefined() {
		return null;
	}
	public TestCaseResult toTaggedClassBasedResult(Project aProject, TestCaseResult aSuperResult) throws NotAutomatableException, NotGradableException {
//      TestCaseResult aSuperResult = super.test(aProject, autoGrade);
      Class aTaggedClassesDefinedClass = taggedClassesDefined();
      if (aTaggedClassesDefinedClass == null) {
    	  return aSuperResult;
      }
      TaggedClassesDefined aTaggedClassesDefined = (TaggedClassesDefined) JUnitTestsEnvironment.getAndPossiblyRunGradableJUnitTest(aTaggedClassesDefinedClass);
		return aTaggedClassesDefined.computeResultBasedOnTaggedClasses(aSuperResult);
      
//      return retVal;

      
  }
	
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
	protected static boolean printSuccessLines = false;
	protected static int maxTraceLines = 25;
	protected  void printTrace (double aScore, Project aProject, String[] aCheckStyleLines, List<String> aFailedMatchedLines, List<String> aSucceededMatchedLines, boolean autoGrade) {
		

        if (aScore != 1) {
        	if (aFailedMatchedLines != null ) {
        		util.trace.Tracer.info(CheckStyleWarningsRatioTestCase.class, aFailedMatchedLines.size()  + " lines failing check");
        	int aMaxLines = Math.min (aFailedMatchedLines.size(), maxTraceLines);
        		
        	for (int i = 0; i < aMaxLines; i++) {
        		util.trace.Tracer.info(CheckStyleWarningsRatioTestCase.class, aFailedMatchedLines.get(i));
        	}
//        	System.out.println(Arra(aFailedMatchedLines));
        	}
        	if (aSucceededMatchedLines != null && printSuccessLines) {
        		util.trace.Tracer.info(CheckStyleWarningsRatioTestCase.class, aSucceededMatchedLines.size()  + " lines passing check");
//            	System.out.println(beautify(aSucceededMatchedLines));
            	int aMaxLines = Math.min (aSucceededMatchedLines.size(), maxTraceLines);

        		for (int i = 0; i < aMaxLines; i++) {
            		util.trace.Tracer.info(CheckStyleWarningsRatioTestCase.class, aSucceededMatchedLines.get(i));
            	}
//        		for (String aLine:aSucceededMatchedLines) {
//        			util.trace.Tracer.info(CheckStyleWarningsRatioTestCase.class, aLine);
//            	}
        	}
        }
//        return partialPass(aScore, "");

    	
    }
	protected abstract  double computeScore (Project aProject, String[] aCheckStyleLines, List<String> aFailedMatchedLines, List<String> aSucceededMatchedLines, boolean autoGrade) ;
	 

}
