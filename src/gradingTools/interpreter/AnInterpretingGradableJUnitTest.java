package gradingTools.interpreter;

import grader.basics.junit.AGradableJUnitTest;
import grader.basics.junit.TestCaseResult;
import grader.basics.requirements.interpreter.specification.CSVRequirementsSpecification;
import grader.basics.testcase.PassFailJUnitTestCase;
import util.annotations.Visible;

public class AnInterpretingGradableJUnitTest extends AGradableJUnitTest {
	PassFailJUnitTestCase passFailJUnitTestCase;
	CSVRequirementsSpecification csvRequirementsSpecification;
	int requirementNumber = 0;
	String simpleName;
//	double maxScore;
	
	public AnInterpretingGradableJUnitTest(Class aJUnitClass, PassFailJUnitTestCase aPassFailJUnitTestCase, String aSimpleName, CSVRequirementsSpecification aCSVRequirementsSpecification, int aRequirementNumber) {
		super(null);
		passFailJUnitTestCase = aPassFailJUnitTestCase;
		simpleName = aSimpleName;
		csvRequirementsSpecification = aCSVRequirementsSpecification;
		requirementNumber = aRequirementNumber;
		setMaxScore(csvRequirementsSpecification.getMaxScore(requirementNumber));
		
	}
	@Override
	protected TestCaseResult testNullJUnitClass() {
		passFailJUnitTestCase.defaultTest();
		return passFailJUnitTestCase.getLastResult();
	}
	@Override
	/**
	 * do nothing as JUnit class is null
	 */
	public void setJUnitClass(Class aJUnitClass) {
	}
	@Override
	@Visible(false)
	public String getSimpleName() {
		return simpleName;
	}
	@Visible(false)
	public boolean isExtra() {
		return csvRequirementsSpecification.isExtraCredit(requirementNumber);
	}
//	@Visible(false)
//	public Double getMaxScore() {
//		return csvRequirementsSpecification.getMaxScore(requirementNumber);
//	}
	@Visible(false)
	public String getExplanation() {
		return getSimpleName();
	}
//	@Visible(false)
//	@Override
//	public double getComputedMaxScore() {
//		return getMaxScore();
//	}

}
