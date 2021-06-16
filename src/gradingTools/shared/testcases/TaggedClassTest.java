package gradingTools.shared.testcases;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;

public abstract class TaggedClassTest extends PassFailJUnitTestCase {
	protected Class taggedClass;
	protected abstract String tag();
	public Class getTaggedClass() {
		return taggedClass;
	}
	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {	
		 
		 try {
			 taggedClass = BasicProjectIntrospection.findClassByTags(tag());
			
		    if (taggedClass == null) {	
		    	return fail ("No class in project matching tag:" + tag());
		    }	
		    
			return pass();		
		 } 		 catch (Exception e) {
			 return fail (e.getMessage());
		 } catch (Throwable e) {
			 	return fail(e.getMessage());
		}
	}
}
