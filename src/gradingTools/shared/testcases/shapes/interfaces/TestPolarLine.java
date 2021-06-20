package gradingTools.shared.testcases.shapes.interfaces;

public interface TestPolarLine extends TestLocatable {
	public void setRadius(double r);
	
	public void setAngle(double theta) throws Exception;
	
	public double getRadius();
	public double getAngle();
	
	
}
