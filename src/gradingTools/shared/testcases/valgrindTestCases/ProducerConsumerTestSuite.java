package gradingTools.shared.testcases.valgrindTestCases;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import grader.basics.concurrency.propertyChanges.ConcurrentEventUtility;
import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.execution.AValgrindCommandGenerator;
import grader.basics.execution.AValgrindDirectoryCommandGenerator;
import grader.basics.execution.AnExecutableFinder;
import grader.basics.execution.ExecutableFinderSelector;
import grader.basics.junit.BasicJUnitUtils;
import grader.basics.project.BasicProjectIntrospection;

@RunWith(Suite.class)
@Suite.SuiteClasses({

	ProducerConsumerOutput.class,
	ExpectedThreadCount.class,
	InterleavedProducerConsumerThreads.class,
	TwoMutexesPerChildThread.class	
	
	
	
})
public class ProducerConsumerTestSuite {
	public static void main (String[] args) {
		try {
//			setProcessTimeOut(25);
//			BasicStaticConfigurationUtils.setUseProjectConfiguration(true);
//			BasicStaticConfigurationUtils.setModule("Comp524");
//			BasicStaticConfigurationUtils.setProblem("Assignment1");
//			BasicStaticConfigurationUtils.setModuleProblemAndTest(Assignment1Suite.class);
			BasicJUnitUtils.interactiveTest(ProducerConsumerTestSuite.class);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static {
//		sBasicProjectIntrospection.setCheckAllSpecifiedTags(true);
//		BasicStaticConfigurationUtils.setUseProjectConfiguration(true);
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setLanguage("C");
//		ExecutableFinderSelector.setMainClassFinder(new AnExecutableFinder());
		ExecutableFinderSelector.setMainClassFinder(new AValgrindDirectoryCommandGenerator());
//		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().
//		setValgrindConfigurationDirectory(newVal);
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
//		.setValgrindConfiguration("BoundedBufferConfig");
		.setValgrindConfiguration("MutexLruConfig");
		ConcurrentEventUtility.setThreadsInDifferentProcess(true);





	}
}
