package byteman.tools.exampleTestCases;

import java.io.IOException;

import byteman.tools.InjectedCode;
import byteman.tools.InjectionTargeter;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.shared.testcases.utils.ConfigurationWriter;

public class BytemanClassRegistryProvided extends PassFailJUnitTestCase{
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
	public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
		registry = (BytemanRegistry) BasicProjectIntrospection.createInstanceOfPredefinedSupertype(REGISTRY_INTERFACE);
		if(registry==null) {
			return fail("No registry found");
		}
		timeoutRegistry = (BytemanRegistry) BasicProjectIntrospection.createTimingOutProxy(REGISTRY_INTERFACE, registry);
		ConfigurationWriter.writeConfiguration(project, registry);
		
		try {
			InjectionTargeter targeter = new InjectionTargeter();
			targeter.addFromRegistry("ClassRegistry.csv", InjectedCode.class);
			targeter.write();
			
		} catch (IOException e) {
			return fail("An error has occurred");
		}
		
		return pass();
	}
	
	
	
}
