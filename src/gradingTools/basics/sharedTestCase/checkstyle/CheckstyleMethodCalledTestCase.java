package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;


public class CheckstyleMethodCalledTestCase extends CheckStyleTestCase {

	 protected String calledMethod;
	 protected String callingMethod;
//	 public static final String WARNING_NAME = "has not made expected call";
//	 public static final String INFO_NAME = "has made expected call";
	 public static final String WARNING_NAME = "MissingMethodCall";
// is this the calling or called type? There is some confusion in the code
	 public CheckstyleMethodCalledTestCase(String aType, String aCalledMethod) {
		 this(aType, "", aCalledMethod);	        
	  }
	 
	 
	 public CheckstyleMethodCalledTestCase(String aType, String aCallingMethod, String aCalledMethod) {
	        super(aType, aCalledMethod);
//	        super(aType, aType + "!" + aMethod);

	        typeTag = aType;
	        calledMethod = aCalledMethod;
	        callingMethod = aCallingMethod;
	        
	  }
	 
	 protected   String warningName() {
	    	return WARNING_NAME;
	    }
	 
	 public String getCallingMethodSignature() {
		 return callingMethod;
	 }
	 
	 public String getCalledMethodSignature() {
		 return calledMethod;
	 }
//	 [MissingMethodCall]
	
	@Override
	public String negativeRegexLineFilter() {
//		return ".*" + "WARN" + ".*:"  + callingMethod + " .*" + typeTag + "(.*) " + calledMethod + "(.*)" + "\\[MissingMethodCall\\]" + ".*" ;
		return ".*" + "WARN" + ".*:"  + callingMethod + " .*" + typeTag + "(.*)." + calledMethod + "(.*)" + "\\[MissingMethodCall\\]" + ".*" ;


	}
	@Override
	public String positiveRegexLineFilter() {
//		return ".*" + "INFO" + ".*:" + callingMethod + " .*" +typeTag + "(.*) " + calledMethod + "(.*)" + "\\[MissingMethodCall\\]" + ".*";
		
		return ".*" + "INFO" + ".*:" + callingMethod + " .*" +typeTag + "(.*)." + calledMethod + "(.*)" + "\\[MissingMethodCall\\]" + ".*";

	}
	// [INFO] D:\dewan_backup\Java\PLTeaching\PL_Java\.\src\byteman\examples\BytemanMerge.java:1: Method:sort:int[]->int[] in class byteman.examples.BytemanMerge:[@MergeSort] has made expected call sort:int[]->int[]. Good! [MissingMethodCall]
//	String patternString = ".* in class (.*):.*";
	
//	Pattern pattern = Pattern.compile(patternString);
	String callingTypeName = null;
	String callingTypeFileName = null;
//	String callingTypeShortFileName = null;
	public String getCallingTypeName() {
		return callingTypeName;
	}
	
	public String getCallingTypeFileName() {
		return callingTypeFileName;
	}
	
	
	protected String maybeProcessSucceededLine (String aSucceededLine) {
		if (callingTypeName != null && callingTypeFileName != null) {
			return aSucceededLine;
		}
		// not sure what the point of the following code is
//		int aFileNameEnd = aSucceededLine.indexOf(": Method");
		int aFileNameEnd = aSucceededLine.indexOf(": ");

//		if (aFileNameEnd == -1) {
//			aFileNameEnd = aSucceededLine.indexOf(": Method");
//		}
		try {
		callingTypeFileName = aSucceededLine.substring("[INFO] ".length(), aFileNameEnd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		final String typeNamePrefix = " in class ";
		final String typeNameSuffix = ":";
		int aPrefixStart = aSucceededLine.indexOf(typeNamePrefix);
		int aSuffixStart = aSucceededLine.indexOf(typeNameSuffix, aPrefixStart);
		
		callingTypeName = aSucceededLine.substring(aPrefixStart + typeNamePrefix.length(),aSuffixStart );

//		Matcher matcher = pattern.matcher(aSucceededLine);
		
//        if(matcher.find()) {
////            System.out.println("found: " + matcher.group(1));
//            callingTypeName = matcher.group(1).split(":")[0];
//        }
		return aSucceededLine;
	}

//	protected void maybeProcessSucceededLines (List<String> aSucceededLines) {
//    	if (aSucceededLines != null ) {
//    		for (int i = 0; i < aSucceededLines.size(); i++) {
//    		String aSucceededLine = aSucceededLines.get(i);
//    		maybeProcessSucceededLine(aSucceededLine);
//    		}
//    	}
//    }
	
	public String getActualCallingTypeName() {
		return callingTypeName;
	}
    
//    protected void maybeProcessFailedLines (List<String> aFailedLines) {
//    	super.maybeProcessFailedLines(aFailedLines);
//    }
	
	
	 public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
//	     Class aClass = IntrospectionUtil.getOrFindClass(project, this, typeTag); 
//	     if (aClass == null) {
//	    	 return fail("Type " + typeTag + "not defined, cannot check method");
//	     }
//	     typeName = aClass.getSimpleName();
//		 int i = 0;
		 TestCaseResult aResult = super.test(project, autoGrade);
//		 TestCaseResult aResult = fail("foo");
	        return aResult;

	        
	        
	        
	 }

//	 protected  TestCaseResult test (SakaiProject aProject, String[] aCheckStyleLines, List<String> aMatchedLines, boolean autoGrade) {
////	    	int aNumFailedInstances = aFailedLines.size();
////	        int aTotalClassCount = aProject.getClassesManager().getClassDescriptions().size();
////	        String aNotes = failMessageSpecifier() + " in " + aNumFailedInstances + " out of " + aTotalClassCount + " classes ";
////	        return partialPass((aTotalClassCount - aNumFailedInstances)/aTotalClassCount, aNotes, autoGrade);  
//	    	int aNumMatchedInstances = aMatchedLines.size();
//	    	if (aNumMatchedInstances == 0 && failOnMatch() || aNumMatchedInstances == 1 && !failOnMatch())
//	    		return pass();
//	    	return computeResult(aProject, aCheckStyleLines, aMatchedLines, autoGrade);
//	    	
//	    }

//	@Override
//	public String failMessageSpecifier(List<String> aFailedLines) {
//		// TODO Auto-generated method stub
//		return "Method matching " + method + " not called in " + getActualType();
//	}
  //String literal expressions should be on the left side
	 protected TestCaseResult computeResult (Project aProject, String[] aCheckStyleLines, List<String> aFailedLines, List<String> aSucceededLines, boolean autoGrade) {
		 return singleMatchScore(aProject, aCheckStyleLines, aFailedLines, aSucceededLines, autoGrade);
//		 if (aResult.getPercentage() != 1.0) {
//			 if (aProject.getEntryPoints() == null || aProject.getEntryPoints().get(MainClassFinder.MAIN_ENTRY_POINT) == null)
//				 return aResult;
//			 String aMainClassUsed = aProject.getEntryPoints().get(MainClassFinder.MAIN_ENTRY_POINT);
//			 if (aMainClassUsed.contains("main.") || aMainClassUsed.contains("Main.") ) {
//				 return partialPass(0.5, aResult.getNotes() + " but main package defined or main package has wrong case");
//			 }
//		 }
//		 return aResult;
	    	
	}

}

