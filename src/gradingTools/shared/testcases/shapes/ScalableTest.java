package gradingTools.shared.testcases.shapes;

import gradingTools.shared.testcases.shapes.interfaces.TestLocatable;
import gradingTools.shared.testcases.shapes.interfaces.TestMovable;
import gradingTools.shared.testcases.shapes.interfaces.TestScalable;
import gradingTools.shared.testcases.shapes.interfaces.TestRotatingLine;

public abstract class ScalableTest extends LocatableTest{
protected TestScalable scalable;
	
	protected TestScalable scalable() {
		if (scalable == null)
			scalable = initializeScalable();
		return scalable;
	}
	protected TestScalable initializeScalable() {
		return (TestScalable) rootProxy;
	}
	@Override
	protected void executeOperations(Object aLocatable) {
		setOriginalBounds();
		scalable().scale(inputWidthMultiplier());
	}
	@Override
	protected void setActual(Object aLocatable) {
		setActualBounds();		
	}
	protected void setExpected(Object aLocatable) {
		expectedWidth = (int) Math.round(
				(double) originalWidth * inputWidthMultiplier());
		expectedHeight = (int) Math.round(
				(double) originalHeight * inputHeightMultiplier());
	}
	@Override
	protected boolean checkOutput(Object aLocatable) {
		assertWrongWidth();
		assertWrongHeight();
		return true;
	}

	

}
