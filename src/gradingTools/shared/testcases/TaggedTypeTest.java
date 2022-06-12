package gradingTools.shared.testcases;

import java.util.Arrays;
import java.util.List;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;

public abstract class TaggedTypeTest extends TaggedClassTest {
	protected Class taggedInterface;
	public Class getTaggedInterface() {
		return taggedInterface;
	}
	
	protected boolean checkForInterface() {
		return true;
	}
//	abstract String tag();
	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {
		
		TestCaseResult aResult = super.test(project, autoGrade);
		if (!aResult.isPass() || taggedClass == null || !checkForInterface()) {
			return aResult;
		}
		Class[] anInterfaces = taggedClass.getInterfaces();
		if (anInterfaces.length == 0) {
			return fail ("Class " + taggedClass + " with tag " + mainClassIdentifier() + " does not have an interface");
		}
		if (anInterfaces.length > 1) {
			return fail ("Class " + taggedClass + " with tag " + mainClassIdentifier() + " has multiple interfaces " + Arrays.toString(anInterfaces));
		}
		taggedInterface = anInterfaces[0];
		String[] aTags = BasicProjectIntrospection.getTags(taggedInterface);
		List aTagsList = Arrays.asList(aTags);
		if (aTagsList.contains(mainClassIdentifier())) {
			return pass();
		}
		return fail ("Class " + taggedClass + " with tag " + mainClassIdentifier() + " has untagged interface " + taggedInterface);

		 
		 
	}
}
