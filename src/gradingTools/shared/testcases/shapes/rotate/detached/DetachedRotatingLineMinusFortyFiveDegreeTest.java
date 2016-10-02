package gradingTools.shared.testcases.shapes.rotate.detached;

import util.annotations.Explanation;
import gradingTools.shared.testcases.shapes.rotate.fixed.RotatingFixedLineSetTest;
import gradingTools.shared.testcases.shapes.rotate.fixed.RotatingLineFortyFiveDegreeTest;

@Explanation("Move and then rotate line")
public class DetachedRotatingLineMinusFortyFiveDegreeTest extends DetachedRotatingLineFortyFiveDegreeTest{
	@Override
	protected Double inputStudentAngle() {
		return - Math.PI/4;
	}
	

}
