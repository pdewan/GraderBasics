package gradingTools.shared.testcases.greeting;

import java.io.File;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.shared.testcases.utils.ConfigurationWriter;
//import main.ClassRegistry;
import util.annotations.MaxValue;
@MaxValue(2)
public class GreetingClassRegistryProvided extends PassFailJUnitTestCase {
	public static final Class REGISTRY_INTERFACE = GreetingClassRegistry.class;
	public GreetingClassRegistry classRegistry;
	public GreetingClassRegistry timingOutClassRegistryProxy;

	public GreetingClassRegistry getClassRegistry() {
		return classRegistry;
	}
	public GreetingClassRegistry getTimingOutClassRegistryProxy() {
		return timingOutClassRegistryProxy;
	}

	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {		
			classRegistry = (GreetingClassRegistry) BasicProjectIntrospection.createInstanceOfPredefinedSupertype(REGISTRY_INTERFACE);
			if (classRegistry == null)	{
				
				return fail("No registry class in classa path:" + System.getProperty("java.class.path"));
			}

			timingOutClassRegistryProxy = (GreetingClassRegistry) BasicProjectIntrospection.createTimingOutProxy(REGISTRY_INTERFACE, classRegistry);
			
			 ConfigurationWriter.writeConfiguration(project, classRegistry);
			return pass();		
	}

}