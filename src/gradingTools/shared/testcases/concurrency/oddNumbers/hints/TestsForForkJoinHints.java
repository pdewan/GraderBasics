package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.junit.BasicJUnitUtils;
import gradingTools.shared.testcases.ConcurrencySuiteSkeleton;
import gradingTools.shared.testcases.concurrency.oddNumbers.BasicsSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.ForkJoinSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.SmallNumberOfRandoms;
import util.annotations.Explanation;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	SmallNumberOfRandoms.class,
	BasicsSmallProblem.class,
	ForkJoinSmallProblem.class,
//	ForkJoinHints.class
//	ForkJoinHint2OnOutput.class,
//	ForkJoinHint3OnOutput.class,
//	ForkJoinHint1OnOutput.class,
//	ForkJoinHint4OnCode.class,
//	ForkJoinHint5OnCode.class,
//	ForkJoinHint6OnCode.class

		
})
@Explanation("These are secondary tests to generate the output on which the fork-join hints are based")
public class TestsForForkJoinHints extends ConcurrencySuiteSkeleton{
	public static void main (String[] args) {
		try {
//			BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setLanguage(BasicLanguageDependencyManager.JAVA_LANGUAGE);
//			BasicExecutionSpecificationSelector.getBasicExecutionSpecification().
//			setCheckStyleConfiguration("unc_checks_533_A0_1.xml");
			
			BasicJUnitUtils.interactiveTest(TestsForForkJoinHints.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	

}
