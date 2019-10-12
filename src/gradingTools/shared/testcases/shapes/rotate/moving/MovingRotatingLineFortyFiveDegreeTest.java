package gradingTools.shared.testcases.shapes.rotate.moving;

import gradingTools.shared.testcases.shapes.rotate.detached.DetachedRotatingLineFortyFiveDegreeTest;
import util.annotations.Explanation;

@Explanation("Rotate and then move line")
public class MovingRotatingLineFortyFiveDegreeTest extends DetachedRotatingLineFortyFiveDegreeTest{
	protected void executeOperations(Object aLocatable) {
		rotate();
		setLocation();
	}
	

}
