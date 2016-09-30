package gradingTools.shared.testcases.shapes;

import gradingTools.shared.testcases.shapes.interfaces.TestLocatable;
import gradingTools.shared.testcases.shapes.interfaces.TestRotatingLine;

public abstract class RotatingLineTest extends LocatableTest {

	@Override
	protected Class proxyClass() {
		return TestRotatingLine.class;
	}

	protected TestRotatingLine getRotatingLine() {
		return (TestRotatingLine) rootProxy;
	}

	@Override
	protected void executeOperations(Object aLocatable) {
		leafProxy().setX(inputX());
		leafProxy().setY(inputY());

	}

	@Override
	protected void setActual(Object aLocatable) {
		actualX = leafProxy().getX();
		actualY =  leafProxy().getY();
		actualHeight = getRotatingLine().getHeight();
		actualWidth = getRotatingLine().getWidth();
	}

	protected boolean checkOutput(Object aLocatable) {
		assertWrongX();
		assertWrongY();
		return true;
	}
	@Override
	protected void setLeafProxy() {
		leafProxy = (TestLocatable) rootProxy();
		
	}
}
