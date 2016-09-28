package gradingTools.shared.testcases.shapes;


public class RotatingLineFortyFiveDegreeTest extends RotatingFixedLineSetTest{
	protected static final Double inputStudentRadius = 5.0;

	@Override
	protected Double inputStudentRadius() {
		return inputStudentRadius;
	}
	
	@Override
	protected Double inputStudentAngle() {
		return Math.PI/4;
	}
	
	

}
