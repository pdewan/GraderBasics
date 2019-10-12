package gradingTools.shared.testcases.shapes.rotate.moving;

import util.annotations.Explanation;

@Explanation("Rotate and then move line")
public class MovingRotatingLineMinusFortyFiveDegreeTest extends MovingRotatingLineFortyFiveDegreeTest{
	
	@Override
	protected Double inputStudentAngle() {
		return - Math.PI/4;
	}
}
