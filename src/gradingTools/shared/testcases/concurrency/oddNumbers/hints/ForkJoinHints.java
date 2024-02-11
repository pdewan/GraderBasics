package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.junit.BasicJUnitUtils;
import gradingTools.shared.testcases.ConcurrencySuiteSkeleton;
import gradingTools.shared.testcases.concurrency.oddNumbers.ForkJoinSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.SmallNumberOfRandoms;

@RunWith(Suite.class)
@Suite.SuiteClasses({
//	SmallNumberOfRandoms.class,
//	ForkJoinSmallProblem.class,
//	ForkJoinHint1OnOutput.class,
//	ForkJoinHint2OnOutput.class,
	ForkJoinHint1OnError.class,
	ForkJoinHint2OnCodeAndOutput.class,
	ForkJoinHint3OnOutput.class,
	ForkJoinHint4OnCode.class,
	ForkJoinHint5OnCode.class,
	ForkJoinHint6OnCode.class

		
})

public class ForkJoinHints extends ConcurrencySuiteSkeleton{
	public static void main (String[] args) {
		try {
//			BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setLanguage(BasicLanguageDependencyManager.JAVA_LANGUAGE);
//			BasicExecutionSpecificationSelector.getBasicExecutionSpecification().
//			setCheckStyleConfiguration("unc_checks_533_A0_1.xml");
			
			BasicJUnitUtils.interactiveTest(ForkJoinHints.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	

}
