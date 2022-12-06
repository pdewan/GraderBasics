package byteman.tools.exampleTestCases.mvcSummer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	ParallelAddTest.class,
	MVCSequentialAdderTest.class,
	MVCParallelAdderTest.class,
	ParallelIOCorrectnessTest.class,
	SequentialIOCorrectnessTest.class,
	MVCSummerStructure.class
})

public class MVCSummerSuite {

}
