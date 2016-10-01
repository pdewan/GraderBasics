package gradingTools.shared.testcases.shapes;

import util.assertions.Asserter;
import gradingTools.shared.testcases.shapes.interfaces.TestLocatable;
import gradingTools.shared.testcases.shapes.interfaces.TestRotatingLine;

public class RotatingFixedLineSetTest extends RotatingLineTest{
	protected static Integer inputStudentX = 0;
	protected Integer inputStudentY = 0;
	
	@Override
	protected void executeOperations(Object aLocatable) {
		super.executeOperations(aLocatable);
		getRotatingLine().setAngle(inputAngle());
		getRotatingLine().setRadius(inputRadius());
	}
//	@Override
//	protected void setActual(TestLocatable aLocatable) {
//		actualHeight = getRotatingLine().getHeight();
//		actualWidth = getRotatingLine().getWidth();
//		
//	}
	protected boolean checkOutput(Object aLocatable) {
		super.checkOutput(aLocatable);
		assertWrongHeight();
		assertWrongWidth();
		return true;
	}
	@Override
	protected Integer expectedX() {
		return inputX();
	}
	@Override
	protected Integer expectedY() {
		return inputY();
	}
	
	@Override
	protected Integer inputStudentX() {
		return inputStudentX;
	}
	@Override
	protected Integer inputStudentY() {
		return inputStudentY;
	}
	@Override
	protected void setActual(Object aLocatable) {	
		actualHeight = getRotatingLine().getHeight();
		actualWidth = getRotatingLine().getWidth();
	}
	@Override
	protected void setExpected(Object aLocatable) {
		expectedHeight = expectedHeight();
		expectedWidth = expectedWidth();
	}

	@Override
	protected Integer expectedHeight() {
		return (int) Math.round(inputRadius()*Math.sin(inputAngle()));
	}
	protected Integer expectedWidth() {
		return (int) Math.round(inputRadius()*Math.cos(inputAngle()));
	}
	
	

	
	

}
