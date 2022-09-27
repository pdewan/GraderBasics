package gradingTools.shared.testcases;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.CurrentProjectHolder;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;

public abstract class TaggedClassTest extends NamedClassTest
//PassFailJUnitTestCase 
{
	protected Class findClassByTag(Project aProject, String aTag) {
		return BasicProjectIntrospection.findClassByTags( aTag);
	}
	
	@Override
	protected Class findClass(Project aProject, String aTag) {
		return findClassByTag(null, aTag);
	}

}
