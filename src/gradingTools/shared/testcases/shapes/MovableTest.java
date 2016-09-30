package gradingTools.shared.testcases.shapes;

import gradingTools.shared.testcases.shapes.interfaces.TestLocatable;
import gradingTools.shared.testcases.shapes.interfaces.TestMovable;
import gradingTools.shared.testcases.shapes.interfaces.TestRotatingLine;

public abstract class MovableTest extends LocatableTest{
	protected TestMovable movable;
	
	protected TestMovable movable() {
		if (movable == null)
			movable = initializeMovable();
		return movable;
	}
	protected TestMovable initializeMovable() {
		
		return (TestMovable) rootProxy;
	}
    
	@Override
	protected void executeOperations(Object aLocatable) {
		setOriginalLocation();
		movable().move(inputXDelta(), inputYDelta());
	}
	
	

	@Override
	protected void setActual(Object aLocatable) {
		setActualLocation();
		
	}
	protected void setExpected(Object aLocatable) {
		expectedX = originalX + inputXDelta();
		expectedY = originalY + inputYDelta();
	}

	@Override
	protected boolean checkOutput(Object aLocatable) {
		assertWrongX();
		assertWrongY();
		return true;
	}

	

}
