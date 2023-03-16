package byteman.tools.exampleTestCases.factorial;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	FactorialIsRecursive.class,
	FactorialSolutionTest.class,
	RecursiveFactorialTest.class
	
})

public class FactorialSuite {

}
