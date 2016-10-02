package gradingTools.shared.testcases.shapes.rotate.fixed;

import gradingTools.shared.testcases.shapes.MovableTest;
import gradingTools.shared.testcases.shapes.interfaces.TestLocatable;
import gradingTools.shared.testcases.shapes.interfaces.TestRotatingLine;

public class RotatingLineMovableTest extends MovableTest{

	@Override
	protected Class proxyClass() {
		return TestRotatingLine.class;
	}

	@Override
	protected void setLeafProxy() {
		leafProxy = (TestLocatable) rootProxy();
		
	}

	

}
