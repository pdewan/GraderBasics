package byteman.tools;

import java.io.File;
import java.io.IOException;

import byteman.tools.exampleTestCases.BytemanRegistry;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.shared.testcases.utils.ConfigurationWriter;

public abstract class AbstractBytemanConfigurationWriter implements BytemanConfigurationWriter {

	private boolean written = false;
	
	protected abstract void addToConfiguration(Project project, InjectionTargeter targeter);
	@Override
	public void writeConfigurationIfNotWritten(Project project) {
		if (written) {
			return;
		}
		written = true;
//		File aCheckStyleConfigurationFile = project.getCheckstyleConfigurationFile();
//		if (aCheckStyleConfigurationFile == null) {
//			throw new NotGradableException("Could not find checkstyle configuration file");
//		}
		
		
		
		try {
//			InjectionTargeter targeter = new InjectionTargeter();
			InjectionTargeter targeter = InjectionTargeterFactory.getInjectionTargeter();
			addToConfiguration(project, targeter);
//			targeter.addFromCheckstyleConfiguration(aCheckStyleConfigurationFile, InjectedCode.class);
			targeter.write();
			
		} catch (IOException e) {
			throw new NotGradableException("An internal error has occurred:" + e.getMessage());
		}
		
	}

}
