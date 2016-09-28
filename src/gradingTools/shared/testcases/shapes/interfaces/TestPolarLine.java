package gradingTools.shared.testcases.shapes.interfaces;

import util.annotations.Tags;

public interface TestPolarLine extends TestLocatable {
	public void setRadius(double r);
	
	public void setAngle(double theta);
	
	public double getRadius();
	public double getAngle();
	
	
}
