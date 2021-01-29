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
//import main.ClassRegistry;
import util.annotations.MaxValue;
import util.trace.Tracer;
@MaxValue(2)
public class GreetingClassRegistryProvided extends AbstractConfigurationProvided {

//public class GreetingClassRegistryProvided extends PassFailJUnitTestCase {
	public static final Class REGISTRY_INTERFACE = GreetingClassRegistry.class;
//	public GreetingClassRegistry classRegistry;
//	public GreetingClassRegistry timingOutClassRegistryProxy;
//
//	public GreetingClassRegistry getClassRegistry() {
//		return classRegistry;
//	}
//	public GreetingClassRegistry getTimingOutClassRegistryProxy() {
//		return timingOutClassRegistryProxy;
//	}

//
//	@Override
//	public TestCaseResult test(Project project, boolean autoGrade)
//			throws NotAutomatableException, NotGradableException {		
//			classRegistry = (GreetingClassRegistry) BasicProjectIntrospection.createInstanceOfPredefinedSupertype(REGISTRY_INTERFACE);
//			if (classRegistry == null)	{
//				Tracer.info(this, "Classes in project:"  + project.getClassesManager().get().getClassDescriptions());
//
//				return fail("No registry class in class path:" + System.getProperty("java.class.path"));
//			}
//
//			timingOutClassRegistryProxy = (GreetingClassRegistry) BasicProjectIntrospection.createTimingOutProxy(REGISTRY_INTERFACE, classRegistry);
//			
//			 ConfigurationWriter.writeConfiguration(project, classRegistry);
//			return pass();		
//	}
	@Override
	public Class referenceClass() {
		return null;
	}
	@Override
	public Class referenceInterface() {
		// TODO Auto-generated method stub
		return REGISTRY_INTERFACE;
	}
	public GreetingClassRegistry getTimingOutClassRegistryProxy() {
	return (GreetingClassRegistry) super.getTestConfiguration();
}

}