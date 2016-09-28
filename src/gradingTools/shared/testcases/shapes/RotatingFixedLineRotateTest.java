package gradingTools.shared.testcases.shapes;

import org.junit.Assert;

import util.assertions.Asserter;
import grader.basics.junit.NotesAndScore;
import gradingTools.shared.testcases.shapes.interfaces.TestLocatable;
import gradingTools.shared.testcases.shapes.interfaces.TestRotatingLine;

public class RotatingFixedLineRotateTest extends RotatingLineTest{
	protected static Integer inputStudentX = 0;
	protected Integer inputStudentY = 0;
	protected int delta = 6;
	protected double intermediateAngle;
	protected double inputStudentAngle = Math.PI/4;
	protected double initialWidth;
	protected double initialHeight;
	boolean clockwise;
	
	@Override
	protected void executeOperations(Object aLocatable) {
		super.executeOperations(aLocatable);
		initialWidth = getRotatingLine().getWidth();
		initialHeight = getRotatingLine().getHeight();
		getRotatingLine().rotate(delta);
		intermediateAngle = getRotatingLine().getAngle();
		clockwise = intermediateAngle < inputAngle();
		getRotatingLine().rotate(delta);
		
	}	
	@Override
	protected void setActual(Object aLocatable) {
		actualAngle =  getRotatingLine().getAngle();
		
		
	}
	
	@Override
	protected Double expectedAngle() {
		Double anOutputDelta = Math.abs(intermediateAngle - inputAngle());
		if (clockwise) {
			return inputAngle() - 2*anOutputDelta;
		}
		return inputAngle() + 2*anOutputDelta;
		
	}
	
	protected boolean checkOutput(Object aLocatable) {
		super.checkOutput(aLocatable);
		Assert.assertTrue("initial angle" + inputAngle() + " same as final angle " +
					NotesAndScore.PERCENTAGE_MARKER + fractionComplete, 
					Math.abs (inputAngle() - actualAngle) < FRACTION_TOLERANCE);
		assertWrongAngle();
		// proceed from here
		Assert.assertTrue("initial height" + inputAngle() + " same as final angle " +
				NotesAndScore.PERCENTAGE_MARKER + fractionComplete, 
				Math.abs (inputAngle() - actualAngle) < FRACTION_TOLERANCE);
		
		
		return true;
	}
	@Override
	protected Integer expectedX() {
		return inputX();
	}
	@Override
	protected Integer expectedY() {
		return inputY();
	}
	
	@Override
	protected Integer inputStudentX() {
		return inputStudentX;
	}
	@Override
	protected Integer inputStudentY() {
		return inputStudentY;
	}
	@Override
	protected Double inputStudentAngle() {
		return inputStudentAngle;
	}
	
	@Override
	protected Integer expectedHeight() {
		return (int) Math.round(inputRadius()*Math.sin(inputAngle()));
	}
	protected Integer expectedWidth() {
		return (int) Math.round(inputRadius()*Math.cos(inputAngle()));
	}
	@Override
	protected void setLeafLocatable() {
		leafLocatable = (TestLocatable) rootLocatable();
		
	}

	
	

}
