package byteman.tools.exampleTestCases;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import byteman.tools.exampleTestCases.factorial.FactorialSuite;
import byteman.tools.exampleTestCases.mergeSort.MergeSortSuite;
import byteman.tools.exampleTestCases.mvcSummer.MVCSummerSuite;
import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.junit.BasicJUnitUtils;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	BytemanProcessCheckstyleConfiguration.class,
	MergeSortSuite.class,
	FactorialSuite.class,
	MVCSummerSuite.class
})
public class BytemanTestCheckStyleConfiguration {

	public static void main (String[] args) {
		try {
			BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
			.setCheckStyleConfiguration("unc_checks_byteman_test.xml");
			BasicJUnitUtils.interactiveTest(BytemanTestCheckStyleConfiguration.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
