package gradingTools.shared.testcases.shapes.rotate.detached;

import util.annotations.Explanation;

@Explanation("Move and then rotate line")
public class DetachedRotatingLineMinusFortyFiveDegreeTest extends DetachedRotatingLineFortyFiveDegreeTest{
	@Override
	protected Double inputStudentAngle() {
		return - Math.PI/4;
	}
	

}
