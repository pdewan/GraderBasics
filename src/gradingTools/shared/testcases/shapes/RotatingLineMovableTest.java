package gradingTools.shared.testcases.shapes;

import gradingTools.shared.testcases.shapes.interfaces.TestLocatable;
import gradingTools.shared.testcases.shapes.interfaces.TestRotatingLine;

public class RotatingLineMovableTest extends MovableTest{

	@Override
	protected Class locatableClass() {
		return TestRotatingLine.class;
	}

	@Override
	protected void setLeafLocatable() {
		leafLocatable = (TestLocatable) rootLocatable();
		
	}

	

}
