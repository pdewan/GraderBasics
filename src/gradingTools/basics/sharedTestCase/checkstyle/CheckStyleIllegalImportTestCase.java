package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.List;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.shared.testcases.MethodExecutionTest;
import util.annotations.Explanation;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;

@Explanation("Checks that the property has the expected name and its interface type has expected name, tag, or pattern")

public class CheckStyleIllegalImportTestCase extends CheckStyleTestCase {
	 protected String descriptor;
	 protected String property;
	 protected String  propertyType;
		public static final String WARNING_NAME = "IllegalTypeImported";
		String type;
	 public CheckStyleIllegalImportTestCase(String anImport) {
	        super(anImport, anImport);
	        type = anImport;
	  }
	 @Override
		protected String warningName() {
			return WARNING_NAME;
		}
		
	
	 protected boolean checkForPositives() {
		 return false;
	 }
	@Override
	public String negativeRegexLineFilter() {
		return 	".*" + "WARN" + ".*" + type + ".*" + "\\[IllegalTypeImported\\]" + ".*" ;

//		return MethodExecutionTest.toRegex(getActualType() + ", missing getter for property "+ property + " of type " + propertyType);
				
	}
	//[INFO] D:\dewan_backup\Java\grail13\src\shapes\AFork.java:1: Expected getter for property LeftLine of type @Comp301Tags.ROTATING_LINE in parent type @Comp301Tags.ANGLE. Good! [ExpectedGetters]

	@Override
	public String positiveRegexLineFilter() {
		return 	".*" + "INFO" + ".*" + type + ".*" + "\\[IllegalTypeImported\\]" + ".*" ;
	
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
//		return "Property matching " + descriptor + " not defined";
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

