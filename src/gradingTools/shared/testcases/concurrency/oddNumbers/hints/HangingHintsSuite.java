package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import gradingTools.shared.testcases.concurrency.oddNumbers.HangingScenario;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	HangingScenario.class,	
	HangingHint1OnOutput.class,
	HangingHint2OnOutput.class,
	HangingHint3OnOutput.class,
	HangingHint4OnOutput.class
})

public class HangingHintsSuite {

}
