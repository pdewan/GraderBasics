package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.junit.BasicJUnitUtils;
import gradingTools.shared.testcases.ConcurrencySuiteSkeleton;
import util.annotations.Explanation;

@RunWith(Suite.class)
@Suite.SuiteClasses({
//	SmallNumberOfRandoms.class,
//	FairAllocationSmallProblem.class,
	FairAllocationHint1OnOutput.class,
	FairAllocationHint2OnOutput.class,
	FairAllocationHint3OnError.class,
	FairAllocationHint4OnOutput.class,
	FairAllocationHint5OnCode.class,
//	FairAllocationHint6OnCode.class		
})
@Explanation("If the code in fairThreadRemainderSize(), the output of the program, and the error message produced by the FairAllocationSmallProblem and FairAllocationLargeProblem tests are not sufficient to identify its bug, then execute the tests in this suite in sequence for increasingly detailed hints on how to find and fix the bug")
public class FairAllocationHints extends ConcurrencySuiteSkeleton {
	public static void main (String[] args) {
		try {
			BasicJUnitUtils.interactiveTest(FairAllocationHints.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static {
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().
		setCheckStyleConfiguration("unc_checks_533_A0_1.xml");
	}
}

