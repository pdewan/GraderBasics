package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.List;
import java.util.Set;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.ClassDescription;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;


public class CheckStyleClassDefinedTestCase extends CheckStyleTestCase {
	 protected String descriptor;
	 public CheckStyleClassDefinedTestCase(String aDescriptor) {
	        super(null, aDescriptor + " defined");
	        descriptor = aDescriptor;
	  }
	 protected boolean failOnMatch() {
	    	return false;
	   }
    
	@Override
	public String negativeRegexLineFilter() {
		return "(.*)Class matching " + descriptor + " defined(.*)";
	}
	 public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
	        TestCaseResult aResult = super.test(project, autoGrade);
//		 TestCaseResult aResult = fail("foo");

		 String aTag = descriptor;
		 if (descriptor.startsWith("@"))
			 aTag = descriptor.substring(1);
		 else 
			 return aResult;
	        if (aResult.getPercentage() == 1.0) {
	        	return aResult;
	        }
	        
	        	Set<ClassDescription> aClasses = BasicProjectIntrospection.findClassesByTag(project, aTag);
	        	if (aClasses.size() == 1) {
	        		return pass();
	        	}
	        	if (aClasses.size() > 1) {
	        		return partialPass(0.5, "Multiple classes tagged:" + aTag + " " + aClasses);
	        	}
	        	return fail("No class tagged: " + aTag);
	        
	        
	        
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
	public String failMessageSpecifier(List<String> aFailedLines) {
		// TODO Auto-generated method stub
		return "Class matching " + descriptor + " not defined";
	}
  //String literal expressions should be on the left side
	 protected TestCaseResult computeResult (Project aProject, String[] aCheckStyleLines, List<String> aFailedLines, List<String> aSucceededLines, boolean autoGrade) {
		 return singleMatchScore(aProject, aCheckStyleLines, aFailedLines, autoGrade);
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

