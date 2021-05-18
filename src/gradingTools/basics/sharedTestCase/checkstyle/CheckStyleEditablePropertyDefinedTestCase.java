package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.List;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.shared.testcases.MethodExecutionTest;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;


public class CheckStyleEditablePropertyDefinedTestCase extends CheckStyleTestCase {
	 protected String descriptor;
	 protected String property;
	 protected String  propertyType;
		public static final String WARNING_NAME = "expectedSetter";

	 public CheckStyleEditablePropertyDefinedTestCase(String aTypeName, String aProperty, String aPropertyType) {
	        super(aTypeName, aTypeName + " defines setter for property "  + aProperty);
	        descriptor = aTypeName;
	        property = aProperty;
	        propertyType = aPropertyType;
	  }
	 @Override
		protected String warningName() {
			return WARNING_NAME;
		}
    
	@Override
	public String negativeRegexLineFilter() {
		return MethodExecutionTest.toRegex(getActualType() + ", missing setter for property "+ property + " of type " + propertyType);
				
	}
	 public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
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
//		return "Setter for property " + descriptor + " not defined";
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

