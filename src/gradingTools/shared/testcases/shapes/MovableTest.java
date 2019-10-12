package gradingTools.shared.testcases.shapes;

import gradingTools.shared.testcases.shapes.interfaces.TestMovable;

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
    protected void move() throws Exception {
    	setOriginalLocation();
		doMove();
    }
    protected void doMove() throws Exception {
    	movable().move(inputXDelta(), inputYDelta());
    }
	@Override
	protected void executeOperations(Object aLocatable) throws Exception {
		move();
	}
	
	

	@Override
	protected void setActual(Object aLocatable) {
		setActualLocation();
		
	}
	protected void setExpectedMove() {
		expectedX = originalX + inputXDelta();
		expectedY = originalY + inputYDelta();
	}
	protected void setExpected(Object aLocatable) {
		setExpectedMove();
	}
	
	protected boolean checkMove() {
		assertChangedLeafProxy();
		assertWrongX();
		assertWrongY();
		return true;
	}

	@Override
	protected boolean checkOutput(Object aLocatable) {
		return checkMove();
	}

	

}
