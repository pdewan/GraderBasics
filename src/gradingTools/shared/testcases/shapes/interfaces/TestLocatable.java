package gradingTools.shared.testcases.shapes.interfaces;

import util.models.PropertyListenerRegisterer;

public interface TestLocatable extends PropertyListenerRegisterer{
	public int getHeight();
	public int getWidth();	
	public int getX();
	public void setX(int x);
	public int getY();
	public void setY(int y);	

}
