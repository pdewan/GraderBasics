package byteman.tools.exampleTestCases;

import java.io.File;
import java.io.IOException;

import byteman.tools.InjectedCode;
import byteman.tools.InjectionTargeter;
import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.shared.testcases.utils.ConfigurationWriter;
// NEED TO CHANGE
public class BytemanProcessCheckstyleConfiguration extends PassFailJUnitTestCase{
	
	@Override
	public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
		File aCheckStyleConfigurationFile = project.getCheckstyleConfigurationFile();
		if (aCheckStyleConfigurationFile == null) {
			throw new NotGradableException("Could not find checkstyle configuration file");
		}
		
		
		
		try {
			InjectionTargeter targeter = new InjectionTargeter();
			targeter.addFromCheckstyleConfiguration(aCheckStyleConfigurationFile, InjectedCode.class);
			targeter.write();
			
		} catch (IOException e) {
			return fail("An internal error has occurred:" + e.getMessage());
		}
		
		return pass();
	}
	
	
	
}
