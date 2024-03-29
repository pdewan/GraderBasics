package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.List;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;


public class CheckstyleMethodHasJavaDocTestCase extends CheckStyleTestCase {
	//:0: method interpolateLow in class matching [@BasicSocialDistanceUtility] has JavaDoc low interpolation to a sequence of values is either an element of the sequence or zero. Good! [ExpectedMethodJavaDoc]

	 protected String javaDoc;
	 public static final String WARNING_NAME = "has JavaDoc";
		public static final String INFO_NAME = "has JavaDoc";
//	 protected String typeTag;
//	 protected String typeName;
	 public CheckstyleMethodHasJavaDocTestCase(String aType, String aJavaDoc) {
	        super(aType, aJavaDoc);
//	        super(aType, aType + "!" + aMethod);

	        typeTag = aType;
	        javaDoc = aJavaDoc;
	        
	  }
//	 [MissingMethodCall]
	
	@Override
	public String negativeRegexLineFilter() {
//		return "(.*)" + getActualType() + "(.*)" + WARNING_NAME + "(.*)" + method + "(.*)";
		return ".*" + "WARN" + ".*" +typeTag + "(.*)" + javaDoc + "(.*)" + "\\[MissingMethodJavaDoc\\]" + ".*" ;


		
//		return "(.*)Signature(.*)" + method + "(.*)" + type + "(.*)";
//		return "(.*)" + getActualType() + "(.*)not made expected call(.*)\\Q" + method + "\\E(.*)";
//		return "(.*)" + getActualType() + " has not made expected call(.*)" + method + "(.*)";

	}
	@Override
	public String positiveRegexLineFilter() {
		return ".*" + "INFO" + ".*" +typeTag + "(.*)" + javaDoc + "(.*)" + "\\[ExpectedMethodJavaDoc\\]" + ".*";
//		return "(.*)" + getActualType() + "(.*)" + INFO_NAME + "(.*)" + method + "(.*)Good(.*)";
//		return "(.*)Signature(.*)" + method + "(.*)" + type + "(.*)";
//		return "(.*)" + getActualType() + "(.*)made expected call(.*)\\Q" + method + "\\E(.*)";
//		return "(.*)" + getActualType() + " has not made expected call(.*)" + method + "(.*)";

	}
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

