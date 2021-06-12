package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.List;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.shared.testcases.MethodExecutionTest;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;


public class CheckstyleClassIsGenericTestCase extends CheckStyleTestCase {
//	public static final String WARNING_NAME = "expectedDeclaredSignature";
//	[INFO] D:\dewan_backup\Java\grail13\.\src\greeting\Cls.java:6: Expected signature main:String[]->void in type greeting.Cls:[@Comp301Tags.GREETING_MAIN]. Good! [ExpectedSignatures]
//	[WARN] D:\dewan_backup\Java\grail13\.\src\greeting\Cls.java:6: Missing signature main:String[]->void in type greeting.Cls:[@Comp301Tags.GREETING_MAIN]. [ExpectedSignatures]

//	 public static final String WARNING_NAME = "has not made expected call";
//	 public static final String INFO_NAME = "has made expected call";

//	 protected String typeTag;
//	 protected String typeName;
	 public CheckstyleClassIsGenericTestCase(String aType) {
	        super(aType, aType );
	        typeTag = aType;
	        
	  }
	 @Override
	 protected String typeTag() {
			return typeTag;
		}
//	 @Override
//		protected String warningName() {
//			return WARNING_NAME;
//		}
//		[INFO] D:\dewan_backup\Java\grail13\.\src\greeting\Cls.java:6: Expected signature main:String[]->void in type greeting.Cls:[@Comp301Tags.GREETING_MAIN]. Good! [ExpectedSignatures]
//		[WARN] D:\dewan_backup\Java\grail13\.\src\greeting\Cls.java:6: Missing signature main:String[]->void in type greeting.Cls:[@Comp301Tags.GREETING_MAIN]. [ExpectedSignatures]
	@Override
	public String negativeRegexLineFilter() {
		
		return ".*" + "WARN" + ".*" + typeTag + ".*" + "\\[ClassIsGeneric\\]" + ".*" ;
		
//		return "(.*)Signature(.*)" + method + "(.*)" + type + "(.*)";
//		return "(.*)Signature(.*)" + method + "(.*)" + getActualType() + "(.*)";
//		return  MethodExecutionTest.toRegex("WARN" + getActualType() +", missing declared signature: " + method);
//		return  MethodExecutionTest.toRegex("WARN" + getActualType() +", missing declared signature: " + method);


	}
	@Override
	public String positiveRegexLineFilter() {
		
		return ".*" + "INFO" + ".*" + typeTag + ".*" + "\\[ClassIsGeneric\\]" + ".*" ;
		
//		return "(.*)Signature(.*)" + method + "(.*)" + type + "(.*)";
//		return "(.*)Signature(.*)" + method + "(.*)" + getActualType() + "(.*)";
//		return  MethodExecutionTest.toRegex("WARN" + getActualType() +", missing declared signature: " + method);
//		return  MethodExecutionTest.toRegex("WARN" + getActualType() +", missing declared signature: " + method);


	}
	 public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
//	     Class aClass = IntrospectionUtil.getOrFindClass(project, this, typeTag); 
//	     if (aClass == null) {
//	    	 return fail("Type " + typeTag + " not defined, cannot check method:" + typeTag);
//	     }
//	     typeName = aClass.getSimpleName();
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
//		return "Method call matching " + method + " not defined by " + getActualType();
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

