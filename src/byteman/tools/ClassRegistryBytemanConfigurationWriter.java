package byteman.tools;

import java.io.IOException;

import byteman.tools.exampleTestCases.BytemanRegistry;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.shared.testcases.utils.ConfigurationWriter;

public class ClassRegistryBytemanConfigurationWriter implements BytemanConfigurationWriter {
	private boolean written = false;
	public static final Class<?> REGISTRY_INTERFACE = BytemanRegistry.class;
	public BytemanRegistry registry;
	public BytemanRegistry timeoutRegistry;
	
	public BytemanRegistry getRegistry() {
		return registry;
	}
	public BytemanRegistry getTimeoutRegistry() {
		return timeoutRegistry;
	}
	
	
	@Override
	public void writeConfigurationIfNotWritten(Project project) {
		if (written) {
			return;
		}
		written = true;
		registry = (BytemanRegistry) BasicProjectIntrospection.createInstanceOfPredefinedSupertype(REGISTRY_INTERFACE);
		if(registry==null) {
			throw new NotGradableException("No registry found");
		}
		timeoutRegistry = (BytemanRegistry) BasicProjectIntrospection.createTimingOutProxy(REGISTRY_INTERFACE, registry);
		ConfigurationWriter.writeConfiguration(project, registry);
		
		try {
//			InjectionTargeter targeter = new InjectionTargeter();
			InjectionTargeter targeter = InjectionTargeterFactory.getInjectionTargeter();
			targeter.addFromRegistry("ClassRegistry.csv", InjectedCode.class);
			targeter.write();
			
		} catch (IOException e) {
			throw new NotGradableException("An internal error has occurred:" + e.getMessage());

		}
		
	}

}
