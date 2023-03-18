package byteman.tools.exampleTestCases;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import byteman.tools.exampleTestCases.factorial.FactorialSuite;
import byteman.tools.exampleTestCases.mergeSort.MergeSortSuite;
import byteman.tools.exampleTestCases.misc.MiscTest;
import byteman.tools.exampleTestCases.mvcSummer.MVCSummerSuite;
import grader.basics.junit.BasicJUnitUtils;


@RunWith(Suite.class)
@Suite.SuiteClasses({
//	BytemanClassRegistryProvided.class,
	MergeSortSuite.class,
	FactorialSuite.class,
	MVCSummerSuite.class,
	MiscTest.class
})
public class BytemanTest {

	public static void main (String[] args) {
		try {
			BasicJUnitUtils.interactiveTest(BytemanTest.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
