package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.List;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;


public class CheckstyleClassInstantiatedTestCase extends CheckStyleTestCase {
	 protected String instantiatedType;
	 protected String instantiatingMethod;
//	 protected String typeTag;
//	 protected String typeName;
	 
/*
 * [INFO] D:\dewan_backup\C\OpenMPTraining\JavaThreadsTraining\src\ConcurrentOddNumbers.java:1: 
 * Expected instantiation of .*Worker in type default.ConcurrentOddNumbers[Concurrent.*] by 
 * methods [static private  createRunnables:->void]. Good! [ExpectedClassInstantiations]

 */
	 public CheckstyleClassInstantiatedTestCase(String aType, String anInstantiatedType) {
		 this(aType, "", anInstantiatedType);
//	        super(aType, aType + "!" + anInstantiatedType);
//	        typeTag = aType;
//	        String[] aComponents = anInstantiatedType.split("\\.");	        
//	        instantiatedType = anInstantiatedType;
	        
	        
	        
	  }
	 public CheckstyleClassInstantiatedTestCase(String aType, 
			 String anInstantiatingMethod,
			 String anInstantiatedType 
			 ) {
	        super(aType, aType + "!" + anInstantiatedType);
	        typeTag = aType;
	        String[] aComponents = anInstantiatedType.split("\\.");	        
	        instantiatedType = anInstantiatedType;
	        instantiatingMethod = anInstantiatingMethod;
	        
	        
	        
	  }
	//[INFO] D:\dewan_backup\Java\grail13\src\shapes\APopulatedQuestGorge.java:1: Expected instantiation of @Comp301Tags.AVATAR in type @Comp301Tags.BRIDGE_SCENE by methods [public  createGuard:->void, public  createApproachingKnights:->void]. Good! [ExpectedClassInstantiations]

	@Override
	public String negativeRegexLineFilter() {
		return 	".*" + "WARN" + ".*" + instantiatedType + 
				".*" + typeTag + ".*" + 
				instantiatingMethod + ".*" +
				"\\[ExpectedClassInstantiations\\]" + ".*" ;

//			return MethodExecutionTest.toRegex(getActualType() + ", missing getter for property "+ property + " of type " + propertyType);
				
	}
	@Override
	public String positiveRegexLineFilter() {
		return 	".*" + "INFO" + ".*" + 
				instantiatedType + ".*" + 
				typeTag + 
				".*" + 
				instantiatingMethod + ".*" +
				"\\[ExpectedClassInstantiations\\]" + ".*" ;

//			return MethodExecutionTest.toRegex(getActualType() + ", missing getter for property "+ property + " of type " + propertyType);
				
	}
	
//	@Override
//	public String negativeRegexLineFilter() {
//		
////		return "(.*)Signature(.*)" + method + "(.*)" + type + "(.*)";
//		return "(.*)Type(.*)" + instantiatedType + "(.*)should be instantiated by(.*)" + getActualType() + "(.*)";
////		expectedClassInstantiations: (ScannerBean.java:1) Type @Call//EC should be instantiated by grail.ScannerBean 	ScannerBean.java	/Assignment3-Semion/src/grail	line 1	Checkstyle Problem
//
//	}
	 public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
		 TestCaseResult aResult = super.test(project, autoGrade);
	     return aResult;	        
	 }



	@Override
	public String failMessageSpecifier(List<String> aFailedLines) {
		// TODO Auto-generated method stub
		return "Class matching " + instantiatedType + " not instantiated in " + getActualType();
	}
  //String literal expressions should be on the left side
	 protected TestCaseResult computeResult (Project aProject, String[] aCheckStyleLines, List<String> aFailedLines, List<String> aSucceededLines, boolean autoGrade) {
		 return singleMatchScore(aProject, aCheckStyleLines, aFailedLines,aSucceededLines, autoGrade);
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

