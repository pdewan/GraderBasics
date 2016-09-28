package gradingTools.shared.testcases.shapes;

import gradingTools.shared.testcases.shapes.interfaces.TestLocatable;
import gradingTools.shared.testcases.shapes.interfaces.TestRotatingLine;

public abstract class RotatingLineTest extends LocatableTest {

	@Override
	protected Class locatableClass() {
		return TestRotatingLine.class;
	}

	protected TestRotatingLine getRotatingLine() {
		return (TestRotatingLine) rootLocatable;
	}

	@Override
	protected void executeOperations(Object aLocatable) {
		leafLocatable().setX(inputX());
		leafLocatable().setY(inputY());

	}

	@Override
	protected void setActual(Object aLocatable) {
		actualX = leafLocatable().getX();
		actualY = leafLocatable().getY();
		actualHeight = getRotatingLine().getHeight();
		actualWidth = getRotatingLine().getWidth();
	}

	protected boolean checkOutput(Object aLocatable) {
		assertWrongX();
		assertWrongY();
		return true;
	}
	@Override
	protected void setLeafLocatable() {
		leafLocatable = (TestLocatable) rootLocatable();
		
	}
}
