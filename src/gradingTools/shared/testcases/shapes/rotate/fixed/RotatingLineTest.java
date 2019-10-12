package gradingTools.shared.testcases.shapes.rotate.fixed;

import gradingTools.shared.testcases.shapes.MovableTest;
import gradingTools.shared.testcases.shapes.interfaces.TestLocatable;
import gradingTools.shared.testcases.shapes.interfaces.TestRotatingLine;

public abstract class RotatingLineTest extends MovableTest {

	@Override
	protected Class proxyClass() {
		return TestRotatingLine.class;
	}

	protected TestRotatingLine getRotatingLine() {
		return (TestRotatingLine) rootProxy;
	}
	
	protected void setLocation() {
		leafProxy().setX(inputX());
		leafProxy().setY(inputY());
	}

	@Override
	protected void executeOperations(Object aLocatable) {
		setLocation();

	}

	@Override
	protected void setActual(Object aLocatable) {
		actualX = leafProxy().getX();
		actualY =  leafProxy().getY();
		actualHeight = getRotatingLine().getHeight();
		actualWidth = getRotatingLine().getWidth();
	}

//	protected boolean checkOutput(Object aLocatable) {
//		super.checkOutput(aLocatable);
//		assertWrongX();
//		assertWrongY();
//		return true;
//	}
	@Override
	protected void setLeafProxy() {
		leafProxy = (TestLocatable) rootProxy();
		
	}
}
