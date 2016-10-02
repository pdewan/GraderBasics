package gradingTools.shared.testcases.shapes.rotate.detached;

import gradingTools.shared.testcases.shapes.rotate.fixed.RotatingFixedLineSetTest;
import gradingTools.shared.testcases.shapes.rotate.fixed.RotatingLineFortyFiveDegreeTest;


public class DetachedRotatingLineFortyFiveDegreeTest extends RotatingLineFortyFiveDegreeTest{
	protected Integer inputStudentX = 100;
	protected Integer inputStudentY = 100;
	@Override
	protected Integer inputStudentX() {
		return 20;
	}
	@Override
	protected Integer inputStudentY() {
		return 40;
	}
	protected Integer expectedStudentX() {
		return inputStudentX();
	}
	@Override
	protected Integer expectedStudentY() {
		return inputStudentY();
	}
	

}
