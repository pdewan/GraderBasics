package gradingTools.shared.testcases.shapes.rotate.moving;

import gradingTools.shared.testcases.shapes.interfaces.TestMovable;
import gradingTools.shared.testcases.shapes.rotate.detached.DetachedRotatingLineFortyFiveDegreeTest;
import gradingTools.shared.testcases.shapes.rotate.fixed.RotatingFixedLineSetTest;
import gradingTools.shared.testcases.shapes.rotate.fixed.RotatingLineFortyFiveDegreeTest;


public class MovingRotatingLineFortyFiveDegreeTest extends DetachedRotatingLineFortyFiveDegreeTest{
	protected void executeOperations(Object aLocatable) {
		rotate();
		setLocation();
	}
	protected TestMovable movable() {
		return getRotatingLine();
	}

}
