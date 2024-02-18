package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import grader.basics.junit.BasicJUnitUtils;
import gradingTools.shared.testcases.ConcurrencySuiteSkeleton;
import util.annotations.Explanation;

@RunWith(Suite.class)
@Suite.SuiteClasses({
//	SmallNumberOfRandoms.class,
//	ForkJoinSmallProblem.class,
	TestsForForkJoinHints.class,
	ForkJoinHints.class
//	ForkJoinHint2OnOutput.class,
//	ForkJoinHint3OnOutput.class,
//	ForkJoinHint1OnOutput.class,
//	ForkJoinHint4OnCode.class,
//	ForkJoinHint5OnCode.class,
//	ForkJoinHint6OnCode.class

		
})
public class ForkJoinHintsSuite extends ConcurrencySuiteSkeleton{
	public static void main (String[] args) {
		try {
//			BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setLanguage(BasicLanguageDependencyManager.JAVA_LANGUAGE);
//			BasicExecutionSpecificationSelector.getBasicExecutionSpecification().
//			setCheckStyleConfiguration("unc_checks_533_A0_1.xml");
			
			BasicJUnitUtils.interactiveTest(ForkJoinHintsSuite.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	

}
