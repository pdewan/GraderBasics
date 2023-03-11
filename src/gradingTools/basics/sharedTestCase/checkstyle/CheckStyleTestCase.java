package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import unc.checks.ISATypesCheck;
import util.trace.Tracer;


public abstract class CheckStyleTestCase extends PassFailJUnitTestCase {
	public static final String COMMENT_START = "//";

	protected boolean foundType;
	protected String typeTag;
//	 protected String typeName;


    public CheckStyleTestCase(String aTypeTag, String aName) {
//        super(aName);
        foundType = false;
//        if (aTypeTag == null) {
//        	System.out.println ("Null type tag");
//        }
        typeTag = aTypeTag;
        actualType = aTypeTag;
    }
    protected boolean failedTestVetoes() {
    	return false;
    }
    
	protected String typeTag() {
		return typeTag;
	}
	protected boolean foundType() {
		return foundType;
	}
	// interface defined should also use similar syntax
//	protected String typeRegex(String aTypeTag) {
//		return "(.*)" + "Class" + "(.*)" + "matching" + "(.*)" + aTypeTag + "(.*)" + "defined" + "(.*)" ;
//	}
	
	protected String typeRegex(String aTypeTag) {
//        return "(.*)" + "Class" + "(.*)" + "matching" + "(.*)[@ ]" + aTypeTag + "[ /](.*)" + "defined" + "(.*)" ;
        return "(.*)" + "Class" + "(.*)" + "matching" + "(.*)[@ ]" + aTypeTag + "(.*)" + "defined" + "(.*)" ;

    }
    
    protected boolean failOnMatch() {
    	return true;
    }
    protected   String warningName() {
    	return null;
    }
	 protected  String infoName() {
		 return warningName();
	 }
    
//	public String positiveRegexLineFilter() {
//		String anInfoName = infoName();
//		if (anInfoName == null) {
//			return null;
//		}
//		// TODO Auto-generated method stub
//		return ".*" + anInfoName + ".*";
//	}
	
//	public String negativeRegexLineFilter() {
//		String aWarningName = warningName();
//		if (aWarningName == null) {
//			return null;
//		}
//		// TODO Auto-generated method stub
//		return  ".*" + aWarningName + ".*";
//	}
	 
	 
	 protected boolean addBrackets() {
		 return true;
	 }
	 
	 public String positiveRegexLineFilter() {
			String aWarningClassName = infoName();
			if(addBrackets()) {
				return ".*" + "INFO" + ".*" +"\\[" + aWarningClassName +"\\]" + ".*";
			}
			return ".*" + "INFO" + ".*" + aWarningClassName + ".*";
	 }

	public String negativeRegexLineFilter() {
		String aWarningClassName = warningName();
		if(addBrackets()) {
			return ".*" + "WARN" + ".*" +"\\[" + aWarningClassName +"\\]" + ".*";
		}
		return ".*" + "WARN" + ".*" + aWarningClassName + ".*";
	}
    protected String toLinesString(List<String> aLines) {
    	StringBuilder aString = new StringBuilder();
    	for (String aLine:aLines) {
    		aString.append(aLine);
    	}
    	return aString.toString();
    }
  
	protected String beautify (String aCheckstyleString) {
//		return aCheckstyleString.substring(aCheckstyleString.indexOf(warningName())) + "\n";
		return aCheckstyleString;
	}
	protected String beautify (List<String> aList) {
		StringBuffer sb = new StringBuffer();
		for (String aString: aList) {
			String beautifiedString = beautify(aString);
			sb.append(beautifiedString);
			
		}
		return sb.toString();
	}
    
    public static  List<String> matchedLines (String[] aLines, String aRegex) {
    	List<String> result = new ArrayList();    
//    	int aCount = 0;
    	for (String aLine:aLines) {
//    		if (aLine.contains("expected")) {
//    			System.out.println ("Found line");
//    		}
//    		if (aLine.contains("JavaDoc")) {
//    			int i = 0;
//    		}
    		if (aLine.matches(aRegex))
    			result.add(aLine);
    	}
    	return result;
    }
    public static  int numMatches (String[] aLines, String aRegex) {
    	return matchedLines(aLines, aRegex).size();
    }
//    public abstract String negativeRegexLineFilter();
//    public  String positiveRegexLineFilter() {
//    	return null;
//    }
    public  String failMessageSpecifier(List<String> aMatchedLines) {
    	return beautify(aMatchedLines);
    }
    protected String actualType = null;
    public String getActualType() {
    	return actualType;
    }
    public static String maybeStripComment(String aString) {	 
	 	int aCommentStart = aString.indexOf(COMMENT_START);
	 	if (aCommentStart < 0)
	 		return aString.trim();
	 	return aString.substring(0, aCommentStart).trim();
	 }
    protected String getType(String aLine) {
    	final String prefix = "Class ";
    	int beginIndex = aLine.indexOf(prefix);
    	String aSuffix = aLine.substring(beginIndex+prefix.length());
    	int endIndex = aSuffix.indexOf(" ");
    	
    	return aSuffix.substring(0, endIndex);
    }
    protected TestCaseResult test(Project aProject, String[] aCheckStyleLines, boolean autoGrade) {
  
//    	String aTypeTag = typeTag();
//    	if (aTypeTag != null) {
//    	List<String> aTypeDefinedLines = matchedLines(aCheckStyleLines, typeRegex(aTypeTag));
//    	  this.foundType = aTypeDefinedLines.size() > 0;
//    	  if (!foundType) {
//    		  return fail (aTypeTag + " not found by checkstyle");
//    	  }
//    	  actualType = getType(aTypeDefinedLines.get(0));
//    	}
    	List<String> aFailedLines = null;
    	String aNegativeFilter = negativeRegexLineFilter();
    	Tracer.info(CheckStyleTestCase.class, "Failure regex:" + aNegativeFilter);
    	if (aNegativeFilter != null) {
    		aFailedLines =	matchedLines(aCheckStyleLines, aNegativeFilter);
    	}
    	List<String> aSucceededLines = null;
    	String aPositiveFilter = positiveRegexLineFilter();
    	Tracer.info(CheckStyleTestCase.class, "Success regex:" + aPositiveFilter);

    	if (aPositiveFilter != null && checkForPositives()) {
    		aSucceededLines =	matchedLines(aCheckStyleLines, aPositiveFilter);
    	}
    	
    	return test(aProject, aCheckStyleLines, aFailedLines, aSucceededLines, autoGrade);    	
    }
    
    protected TestCaseResult classFractionResult (Project aProject, String[] aCheckStyleLines, List<String> aMatchedLines, boolean autoGrade) {
    	int aNumMatchedInstances = aMatchedLines.size();    	
//        int aTotalClassCount = aProject.getClassesManager().getClassDescriptions().size();
        int aTotalClassCount = aProject.getClassesManager().get().getClassDescriptions().size();

        String aNotes = failMessageSpecifier(aMatchedLines) + " in " + aNumMatchedInstances + " out of " + aTotalClassCount + " classes ";
        return partialPass((aTotalClassCount - aNumMatchedInstances)/aTotalClassCount, aNotes, autoGrade);    
    	
    }
    protected TestCaseResult numMatchesResult (Project aProject, String[] aCheckStyleLines, List<String> aFailedLines, List<String> aSucceededLines, boolean autoGrade) {
    	int aNumFailedInstances = aFailedLines.size();   
    	double aScore = scoreForMatches(aNumFailedInstances);
        String aNotes = failMessageSpecifier(aFailedLines) + " " + aNumFailedInstances + " number of times";
        return partialPass((1 - aScore), aNotes, autoGrade);    
    	
    }
    protected boolean checkForPositives() {
		 return true;
	 }
    
    protected TestCaseResult singleMatchScore (Project aProject, String[] aCheckStyleLines, List<String> aFailedLines, List<String> aSucceededLines, boolean autoGrade) {
    	if (aSucceededLines != null && aSucceededLines.size() > 0) {
    		return pass();
    	}
		Tracer.info(CheckStyleTestCase.class, "No lines succeeded check");

    	if (aFailedLines != null && aFailedLines.size() > 0) {
    		Tracer.info(CheckStyleTestCase.class, "Relevant Checkstyle Warnings:");
    		for (String aFailedLine:aFailedLines) {
        		Tracer.info(CheckStyleTestCase.class, aFailedLine);

    		}
//    		Tracer.info(CheckStyleTestCase.class, aFailedLines.toString());
        String aNotes = failMessageSpecifier(aFailedLines); 
        return fail(aNotes, autoGrade); 
    	}
    	String aNegativeFilter = negativeRegexLineFilter();
    	
    	// when will this code be executed
    	if (aNegativeFilter != null && aFailedLines != null && aFailedLines.size() > 0 ) {
    	return fail ("Checkstyle output matches:" + aNegativeFilter, autoGrade);
    	}
    	if (!checkForPositives()) {
    		return pass();
    	}
    	String aPositiveFilter = positiveRegexLineFilter();
    	if (aPositiveFilter != null) {
        	return fail ("Checkstyle output does not match:" + aPositiveFilter + ". Is your class named or tagged properly and checkstyle file upto date?", autoGrade);

    	}
    	return fail ("Test failed, Ask instructor for better explanation:" + autoGrade);
    }
    
    protected TestCaseResult computeResult (Project aProject, String[] aCheckStyleLines, List<String> aFailedLines, List<String> aSucceededLines, boolean autoGrade) {
    	return numMatchesResult(aProject, aCheckStyleLines, aFailedLines, null, autoGrade);
    	
    }
    protected boolean isPassed(int aNumMatchedInstances, int aNumSucceededInstances) {
    	return aNumMatchedInstances == 0 && failOnMatch() || aNumMatchedInstances == 1 && !failOnMatch();
    }
    protected  TestCaseResult test (Project aProject, String[] aCheckStyleLines, List<String> aFailedMatchedLines, List<String> aSucceededMatchedLines, boolean autoGrade) {

//    	int aNumMatchedInstances = 0;
//    	if (aFailedMatchedLines != null) {
//    		aNumMatchedInstances = aFailedMatchedLines.size();
//    	}
//    	int aNumSucceededInstances = (aSucceededMatchedLines == null)?0:aSucceededMatchedLines.size();
////    	if (aNumMatchedInstances == 0 && failOnMatch() || aNumMatchedInstances == 1 && !failOnMatch())
//        if (isPassed(aNumMatchedInstances, 0))
//
//    		return pass();
    	TestCaseResult anOriginalResult = computeResult(aProject, aCheckStyleLines, aFailedMatchedLines, aSucceededMatchedLines, autoGrade);
    	TestCaseResult aScaledResult = scaleResult(anOriginalResult);
    	return aScaledResult;
    }
//    public TestCaseResult scaleResult(TestCaseResult aResult) {
//    	 if (precedingTestInstances.size() == 0 || aResult.getPercentage() == 0) {
//    		 return aResult;
//    	 }
//		 double aTotalFractionComplete = 0;
//		 for (PassFailJUnitTestCase aTestCase:precedingTestInstances) {
//			 if (aTestCase != null) {
//			 aTotalFractionComplete += aTestCase.getLastResult().getPercentage();
//			 }
//		 }
//		 double anAverageFractionComplete =  aTotalFractionComplete/ (double) precedingTestInstances.size();
//		 double anOriginalFractionComplete = aResult.getPercentage();
//		 Tracer.info(CheckstyleSpecificWarningTestCase.class, "Score of " + anOriginalFractionComplete + " scaled by average preceding test pass percentage:" + anAverageFractionComplete);
//	      aResult.setPercentage(anOriginalFractionComplete*anAverageFractionComplete); 
//		 return aResult;
//	 }
    protected double scoreForMatchNumber(int aMistakeNumber) {
    	return 1.0/(Math.pow(2, aMistakeNumber+1)); // starting at 0
    }
    protected double scoreForMatches(int aNumMistakes) {
    	double aScore = 0;
    	for (int aMistakeNumber = 0; aMistakeNumber < aNumMistakes; aMistakeNumber++) {
    		aScore += scoreForMatchNumber(aMistakeNumber);
    	}
    	return aScore;
    }
//    protected TestCaseResult test (SakaiProject aProject, String[] aCheckStyleLines, List<String> aFailedLines, boolean autoGrade) {
//    	int aNumFailedInstances = aFailedLines.size();
//    	double penaltyPerMistake = 0.2;
//        int aTotalClassCount = aProject.getClassesManager().getClassDescriptions().size();
//        String aNotes = failMessageSpecifier() + " in " + aNumFailedInstances + " out of " + aTotalClassCount + " classes ";
//        return partialPass((aTotalClassCount - aNumFailedInstances)/aTotalClassCount, aNotes, autoGrade);    
//    	
//    }
    
    

//    @Override
    public TestCaseResult test(Project aProject, boolean autoGrade) throws NotAutomatableException, NotGradableException {
//        if (aProject.getClassesManager().isEmpty())
//            throw new NotGradableException();
        String aTypeTag = typeTag();
        try {
        String aCheckStyleText = aProject.getCheckstyleText();
//        Tracer.info(CheckStyleTestCase.class, "Checkstyle output\n" + aCheckStyleText);
//        System.out.println("Checkstyle: " + aCheckStyleText);
        if (aCheckStyleText == null || aCheckStyleText.isEmpty()) {
//          System.err.println("No checkstyle output, check console error messages");
          return fail ("No checkstyle output, check console error messages");
        }
//        String aCheckStyleFileName = aProject.getCheckStyleFileName(); // can read lines from this, maybe more efficient
        String[] aCheckStyleLines = aCheckStyleText.split(System.getProperty("line.separator"));
        return test(aProject, aCheckStyleLines, autoGrade);
        } catch (Throwable e) {
        	e.printStackTrace();
        	return fail ("Internal error, please contact instructor");
        } 
        
    }
    public   String toClassName(String aCheckstyleMessage) {
		 int anIndex1 = aCheckstyleMessage.indexOf("(");
		 int anIndex2 = aCheckstyleMessage.indexOf (")");
		 if (anIndex1 < 0 || anIndex2 < 0 || anIndex2 <= anIndex1)
			 return "";
		 return aCheckstyleMessage.substring(anIndex1 + 1, anIndex2);
				
	 }
	static StringBuilder stringBuilder = new StringBuilder();

    protected static String makeMethodAndTagRegex(String aBeforeMethod, String aBeforeTag, String anAfterTag, String[][] aTagAndMethods ) {
		
    	
		stringBuilder.setLength(0);
		stringBuilder.append(aBeforeMethod);

		stringBuilder.append("(");
		for (int anIndex = 0; anIndex < aTagAndMethods.length; anIndex++) {
			String[] aTagAndMethod = aTagAndMethods[anIndex];
			if (aTagAndMethod.length != 2) {
				System.err.println(Arrays.toString(aTagAndMethod) + " should have exactly two elements ");
				return null;
			}
			stringBuilder.append(aTagAndMethod[1]);
			if (anIndex != aTagAndMethods.length - 1) {
				stringBuilder.append("|");
			}			
		}
		stringBuilder.append(")");
		stringBuilder.append(aBeforeTag);
		stringBuilder.append("(");
		for (int anIndex = 0; anIndex < aTagAndMethods.length; anIndex++) {
			String[] aTagAndMethod = aTagAndMethods[anIndex];
			
			stringBuilder.append(aTagAndMethod[0]);
			if (anIndex != aTagAndMethods.length - 1) {
				stringBuilder.append("|");
			}			
		}
		stringBuilder.append(")");
		stringBuilder.append(anAfterTag);
		return stringBuilder.toString();
		
	}
	protected int numFailedMatches (List<String> aFailedMatchedLines) {
		return aFailedMatchedLines == null?0:aFailedMatchedLines.size();

	}
	protected int numSucceededMatches (List<String> aSucceededMatchedLines) {
		return aSucceededMatchedLines == null?0:aSucceededMatchedLines.size();


	}
}

