package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.List;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.shared.testcases.MethodExecutionTest;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;


public class CheckstyleInterfaceDefinedTestCase extends CheckStyleTestCase {
	public static final String WARNING_NAME = "expectedInterface";

	protected String descriptor;
	 String expectedInterface;
	 public CheckstyleInterfaceDefinedTestCase(String aTypeName, String anInterface) {
	        super(aTypeName, aTypeName + " defined");
	        descriptor = aTypeName;
	        expectedInterface = anInterface;
	  }
	 @Override
	protected String warningName() {
		return WARNING_NAME;
	}
	// [INFO] D:\dewan_backup\Java\Assignment12Daniel\src\grail\shapes\Locatable.java:10: Expected interface util.models.PropertyListenerRegisterer of class @Comp301Tags.LOCATABLE. Good! [ExpectedInterfaces]

    
//	@Override
//	public String negativeRegexLineFilter() {
//		return MethodExecutionTest.toRegex(
//				"In type(.*)" + getActualType() +"(.*)missing interface:(.*)" + expectedInterface);
//				
//	}
	@Override
	public String negativeRegexLineFilter() {
		return ".*" + "WARN" + ".*" + expectedInterface + ".*" + descriptor + ".*";
				
	}
	@Override
	public String positiveRegexLineFilter() {
		return ".*" + "INFO" + ".*" + expectedInterface + ".*" + descriptor + ".*";
				
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

	@Override
//	public String failMessageSpecifier(List<String> aFailedLines) {
//		// TODO Auto-generated method stub
//		return "Type:" + getActualType() +"missing interface:" + expectedInterface;
//		
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

