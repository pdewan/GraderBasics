package gradingTools.shared.testcases;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.CurrentProjectHolder;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;

public abstract class NamedClassTest extends PassFailJUnitTestCase {
	protected Class taggedClass;
	protected abstract String mainClassIdentifier();
	public Class getTaggedClass() {
		return taggedClass;
	}
	protected Class findClassByName(String aTag) {
		try {
			return Class.forName(aTag);
		} catch (ClassNotFoundException e) {
			return null;
		}
//		return BasicProjectIntrospection.findClassByNameMatch(CurrentProjectHolder.getOrCreateCurrentProject(), aTag);
	}
	protected Class findClass(String aTag) {
		return findClassByName(aTag);
//		try {
//			return Class.forName(aTag);
//		} catch (ClassNotFoundException e) {
//			return null;
//		}
//		return BasicProjectIntrospection.findClassByNameMatch(CurrentProjectHolder.getOrCreateCurrentProject(), aTag);
	}
	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {	
		 
		 try {
			 String aTag = AssignmentSuiteSkeleton.getMainClass();
			 if (aTag == null) {
				 aTag = mainClassIdentifier();
			 }
					 
					 
			 taggedClass =  findClass(aTag);
			
		    if (taggedClass == null) {	
		    	return fail ("No class in project matching name/tag:" + aTag);
		    }	
		    
			return pass();		
		 } 		 catch (Exception e) {
			 return fail (e.getMessage());
		 } catch (Throwable e) {
			 	return fail(e.getMessage());
		}
	}
}
