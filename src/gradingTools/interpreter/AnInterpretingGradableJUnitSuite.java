package gradingTools.interpreter;

import grader.basics.junit.AGradableJUnitSuite;
import util.annotations.Visible;

public class AnInterpretingGradableJUnitSuite extends AGradableJUnitSuite {
//	PassFailJUnitTestCase passFailJUnitTestCase;
//	CSVRequirementsSpecification csvRequirementsSpecification;
//	int requirementNumber = 0;
	String simpleName;
//	double maxScore;
	
	public AnInterpretingGradableJUnitSuite(String aSimpleName) {
		super(null);
		
		simpleName = aSimpleName;
		
		
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
