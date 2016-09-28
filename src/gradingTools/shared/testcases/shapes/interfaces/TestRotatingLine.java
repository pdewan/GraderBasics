package gradingTools.shared.testcases.shapes.interfaces;

import util.annotations.Tags;

@Tags({"RotatingLine"})
public interface TestRotatingLine extends TestPolarLine, TestMovable, TestRotatable {
	public void setRadius(double r);
	
	public void setAngle(double theta);
//	@Tags({"rotate"})
//	public void rotate(int degrees);
	
}
