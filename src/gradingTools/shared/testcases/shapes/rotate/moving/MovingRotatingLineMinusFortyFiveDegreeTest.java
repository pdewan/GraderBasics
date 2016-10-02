package gradingTools.shared.testcases.shapes.rotate.moving;

import util.annotations.Explanation;
import gradingTools.shared.testcases.shapes.interfaces.TestMovable;
import gradingTools.shared.testcases.shapes.rotate.detached.DetachedRotatingLineFortyFiveDegreeTest;
import gradingTools.shared.testcases.shapes.rotate.fixed.RotatingFixedLineSetTest;
import gradingTools.shared.testcases.shapes.rotate.fixed.RotatingLineFortyFiveDegreeTest;

@Explanation("Rotate and then move line")
public class MovingRotatingLineMinusFortyFiveDegreeTest extends MovingRotatingLineFortyFiveDegreeTest{
	
	@Override
	protected Double inputStudentAngle() {
		return - Math.PI/4;
	}
}
