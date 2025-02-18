package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.junit.BasicJUnitUtils;
import gradingTools.shared.testcases.concurrency.oddNumbers.SmallNumberOfRandoms;
import gradingTools.shared.testcases.concurrency.oddNumbers.hanging.HangingScenario;

@RunWith(Suite.class)
@Suite.SuiteClasses({
//	SmallNumberOfRandoms.class,
//	HangingScenario.class,	
	TestsForHangingHints.class,
	HangingHints.class
//	HangingHint1OnOutput.class,
//	HangingHint2OnOutput.class,
//	HangingHint3OnOutput.class,
//	HangingHint4OnOutput.class
})

public class HangingHintsSuite {
	public static void main (String[] args) {
		try {
			BasicJUnitUtils.interactiveTest(HangingHintsSuite.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//	static {
//		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().
//		setCheckStyleConfiguration("unc_checks_533_A0_1.xml");
//	}
}
