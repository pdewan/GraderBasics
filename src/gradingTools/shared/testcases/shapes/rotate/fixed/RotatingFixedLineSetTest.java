package gradingTools.shared.testcases.shapes.rotate.fixed;

public class RotatingFixedLineSetTest extends RotatingLineTest{
	protected Integer inputStudentX = 0;
	protected Integer inputStudentY = 0;
	protected void rotate() {
		try {
			getRotatingLine().setAngle(inputAngle());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getRotatingLine().setRadius(inputRadius());
	
	}
	
	@Override
	protected void executeOperations(Object aLocatable) {
//		super.executeOperations(aLocatable);
		setLocation();
		rotate();
	}
//	@Override
//	protected void setActual(TestLocatable aLocatable) {
//		actualHeight = getRotatingLine().getHeight();
//		actualWidth = getRotatingLine().getWidth();
//		
//	}
	protected boolean checkRotate() {
		assertWrongHeight();
		assertWrongWidth();
		return true;
	}
	protected boolean checkOutput(Object aLocatable) {
		return checkRotate();
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
		super.setActual(aLocatable);
//		actualHeight = getRotatingLine().getHeight();
//		actualWidth = getRotatingLine().getWidth();
	}
	protected void setExpectedRotate() {
		expectedHeight = expectedHeight();
		expectedWidth = expectedWidth();
	}

	@Override
	protected void setExpected(Object aLocatable) {
		setExpectedRotate();
	}

	@Override
	protected Integer expectedHeight() {
		return (int) Math.round(inputRadius()*Math.sin(inputAngle()));
	}
	protected Integer expectedWidth() {
		return (int) Math.round(inputRadius()*Math.cos(inputAngle()));
	}
	
	

	
	

}
