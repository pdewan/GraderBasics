package gradingTools.shared.testcases;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.CurrentProjectHolder;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;

public abstract class TaggedOrNamedClassTest extends TaggedClassTest
//PassFailJUnitTestCase 
{
//	protected Class findClassByTag(String aTag) {
//		return BasicProjectIntrospection.findClassByTags( aTag);
//	}
	
	@Override
	protected Class findClass(String aTag) {
		Class retVal = findClassByName (aTag);
		if (retVal != null) {
			return retVal;
		}
		return findClassByTag(aTag);

	}

}
