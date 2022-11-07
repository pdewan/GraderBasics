package gradingTools.valgrindTestCases;

import java.util.Arrays;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.execution.AValgrindMakeCommandGenerator;
import grader.basics.execution.ExecutableFinderSelector;
import grader.basics.junit.BasicJUnitUtils;

@RunWith(Suite.class)
@Suite.SuiteClasses({

	LRUTest.class,
	
	
})
public class LRUTestSuite {
	public static void main (String[] args) {
		try {
//			setProcessTimeOut(25);
//			BasicStaticConfigurationUtils.setUseProjectConfiguration(true);
//			BasicStaticConfigurationUtils.setModule("Comp524");
//			BasicStaticConfigurationUtils.setProblem("Assignment1");
//			BasicStaticConfigurationUtils.setModuleProblemAndTest(Assignment1Suite.class);
			BasicJUnitUtils.interactiveTest(LRUTestSuite.class);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static {
//		sBasicProjectIntrospection.setCheckAllSpecifiedTags(true);
//		BasicStaticConfigurationUtils.setUseProjectConfiguration(true);
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setLanguage("C");
//		ExecutableFinderSelector.setMainClassFinder(new AnExecutableFinder());
		ExecutableFinderSelector.setMainClassFinder(new AValgrindMakeCommandGenerator());
//		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().
//		setValgrindConfigurationDirectory(newVal);
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
//		.setValgrindConfiguration("BoundedBufferConfig");
		.setValgrindConfiguration("MutexLruConfig");
		String[] aCommand = new String[]{"./lru-mutex-wrapped", "-c", "2"};
		List<String> aCommandList = Arrays.asList(aCommand);
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
//		.setValgrindConfiguration("BoundedBufferConfig");
		.setGraderBasicCommand(aCommandList);


	}
}
