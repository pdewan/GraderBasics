package gradingTools.shared.testcases.shapes.rotate.fixed;


public class RotatingLineNinetyDegreeTest extends RotatingFixedLineSetTest{
	protected static final Double inputStudentRadius = 5.0;

	@Override
	protected Double inputStudentRadius() {
		return inputStudentRadius;
	}	
	@Override
	protected Double inputStudentAngle() {
		return Math.PI/2;
	}
}
