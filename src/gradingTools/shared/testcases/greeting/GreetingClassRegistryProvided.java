package gradingTools.shared.testcases.greeting;

import java.io.File;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.shared.testcases.utils.AbstractConfigurationProvided;
import gradingTools.shared.testcases.utils.ConfigurationWriter;
import util.annotations.Explanation;
//import main.ClassRegistry;
import util.annotations.MaxValue;
import util.trace.Tracer;
@MaxValue(2)
@Explanation("Is an implementation of gradingTools.shared.testcases.greeting.GreetingClassRegistry provided?")
public class GreetingClassRegistryProvided extends AbstractConfigurationProvided {

	public static final Class REGISTRY_INTERFACE = GreetingClassRegistry.class;

	@Override
	public Class referenceClass() {
		return null;
	}
	@Override
	public Class referenceInterface() {
		return REGISTRY_INTERFACE;
	}
	public GreetingClassRegistry getTimingOutClassRegistryProxy() {
	return (GreetingClassRegistry) super.getTestConfiguration();
}

}