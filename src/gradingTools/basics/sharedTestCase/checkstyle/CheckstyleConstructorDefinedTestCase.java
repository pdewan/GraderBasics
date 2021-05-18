package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.List;

import grader.basics.junit.TestCaseResult;
import grader.basics.project.Project;
import gradingTools.shared.testcases.MethodExecutionTest;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;


public class CheckstyleConstructorDefinedTestCase extends CheckStyleTestCase {
	public static final String WARNING_NAME = "expectedConstructor";
	protected String constructor;
//	 protected String typeTag;
//	 protected String typeName;
	 public CheckstyleConstructorDefinedTestCase(String aType, String aConstructor) {
	        super(aType, aType + "!" + aConstructor);
	        typeTag = aType;
	        constructor = aConstructor;
	        
	  }
	 @Override
	protected String warningName() {
		return WARNING_NAME;
	}
	
    
	@Override
	public String negativeRegexLineFilter() {
		return  MethodExecutionTest.toRegex("(.*)In type " + getActualType() +", missing constructor: " + constructor);
//		return "(.*)Constructor(.*)" + constructor + "(.*)" + getActualType() + "(.*)";
	}
//	 public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
//	     Class aClass = IntrospectionUtil.getOrFindClass(project, this, typeTag); 
//	     if (aClass == null) {
//	    	 return fail("Type " + typeTag + "not defined, cannot check constructor");
//	     }
//	     typeName = aClass.getSimpleName();
//		 TestCaseResult aResult = super.test(project, autoGrade);
////		 TestCaseResult aResult = fail("foo");
//	        return aResult;
//
//	        
//	        
//	        
//	 }

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
//		return "Constructor matching " + constructor + " not defined in " + getActualType();
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

