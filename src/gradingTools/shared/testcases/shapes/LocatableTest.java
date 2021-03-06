package gradingTools.shared.testcases.shapes;

import org.junit.Assert;

import grader.basics.execution.GradingMode;
import grader.basics.junit.NotesAndScore;
import gradingTools.shared.testcases.FactoryMethodTest;
import gradingTools.shared.testcases.shapes.interfaces.TestLine;
import gradingTools.shared.testcases.shapes.interfaces.TestLocatable;
import gradingTools.shared.testcases.shapes.interfaces.TestPolarLine;
import util.trace.Tracer;

public abstract class LocatableTest extends FactoryMethodTest{
//	protected Object rootProxy;
//	protected TestLocatable leafProxy;
	public static final double FRACTION_TOLERANCE = 0.1;
	public static final double INT_TOLERANCE = 2;
	
//	protected abstract Class proxyClass();
	protected int originalX, originalY, originalWidth, originalHeight;
	protected int expectedX, expectedY, expectedWidth, expectedHeight;

	protected int actualX, actualY, actualWidth, actualHeight;
	protected double actualRadius, actualAngle;
	
//	protected  void maybeCreateInstanceFailed (Object anObject  ) {
//		if (anObject == null) {
//			assertMissingClass(BasicProjectIntrospection.getTags(proxyClass()));
//		}
//	}
//	protected Object createRootProxy(){
//		rootProxy = BasicProjectIntrospection.createInstance(proxyClass(), getArgs());
//		
//		setLeafLocatable();
//		return rootProxy;
//	}
//	
//	protected Object createOrGetLastRootProxy(){
//		rootProxy = BasicProjectIntrospection.createOrGetLastInstance(proxyClass(), getArgs());
//		setLeafLocatable();
//		return rootProxy;
//	}
	
	protected void assertMoved() {
		assertTrue("Original x " + originalX + " == " + "actual x " + actualX +
				" and " + 
				"original y " + originalY + " == " + "actual Y " + actualY,								
				originalX != actualX || originalY != actualY);
//		assertTrue("Original y " + originalY + " == " + "actual Y " + actualY, originalY != actualY);
	}
	protected void assertBoundsChanged() {
//		Tracer.info(this,"Checking if bounds have changed in:" + this);
//		if (originalHeight == actualHeight) {
//			Tracer.info(this,"Actual height:" + actualHeight + " original Heught" + originalHeight);
//			System.out.println("Assertion fail on height");
//		}
		assertTrue("Original width " + originalWidth + " == " + "actual width " + actualWidth +
				" and " +
				"original height " + originalHeight + " == " + "actual Height " + actualHeight, 
				originalHeight != actualHeight || originalWidth != actualWidth);
				
				
//		assertTrue("Original height " + originalHeight + " == " + "actual Height " + actualHeight, originalHeight != actualHeight);
//		assertTrue("Original width " + originalWidth + " == " + "actual width " + actualWidth, originalWidth != actualWidth);
//		assertTrue("Original height " + originalHeight + " == " + "actual Height " + actualHeight, originalHeight != actualHeight);
//		Tracer.info(this,"Bounds have changed in:" + this);

	}

	protected void assertAngle(double aComputed, double aCorrect) {
		Assert.assertTrue("In: " + leafProxy  + " computedAngle " + aComputed + " != correctAngle " + aCorrect + NotesAndScore.PERCENTAGE_MARKER + fractionComplete, Math.abs(aComputed - aCorrect) <= FRACTION_TOLERANCE);

	}
	protected void assertRadius(double aComputed, double aCorrect) {
		Assert.assertTrue("In: " + leafProxy  + " computedRadius " + aComputed + " != correctRadius " + aCorrect + NotesAndScore.PERCENTAGE_MARKER + fractionComplete, Math.abs(aComputed - aCorrect) <= FRACTION_TOLERANCE);

	}
	protected void assertHeight(int aComputed, int aCorrect) {
		Assert.assertTrue("In: " + leafProxy  + " computedHeight " + aComputed + " != correctHeight " + aCorrect + NotesAndScore.PERCENTAGE_MARKER + fractionComplete, Math.abs(aComputed - aCorrect) < INT_TOLERANCE);

	}
	protected void assertWidth(int aComputed, int aCorrect) {
		Assert.assertTrue("In: " + leafProxy  + " computedWidth " + aComputed + " != correctWidth " + aCorrect + NotesAndScore.PERCENTAGE_MARKER + fractionComplete, Math.abs(aComputed - aCorrect) < INT_TOLERANCE);

	}
	
	protected void assertX(int aComputed, int aCorrect) {
		Assert.assertTrue("In: " + leafProxy  + " computedX " + aComputed + " != correctX " + aCorrect + NotesAndScore.PERCENTAGE_MARKER + fractionComplete, Math.abs(aComputed - aCorrect) < INT_TOLERANCE);

	}
	protected void assertY(int aComputed, int aCorrect) {
		Assert.assertTrue("In: " + leafProxy  + " computedY " + aComputed + " != correctY " + aCorrect + NotesAndScore.PERCENTAGE_MARKER + fractionComplete, Math.abs(aComputed - aCorrect) <= INT_TOLERANCE);

	}
	
	protected void testAngleRadius(TestPolarLine aPolarLine, Double aCorrectRadius, Double aCorrectAngle) {
		double aComputedRadius =  aPolarLine.getRadius();
		double aComputedAngle = aPolarLine.getAngle();
		if (aCorrectRadius != null) {
			assertRadius(aComputedRadius, aCorrectRadius);
		}
		if (aCorrectAngle != null) {
			assertAngle(aComputedAngle, aCorrectAngle);
		}
	}
	protected void testWidthHeight(TestLine aLine, Integer aCorrectWidth, Integer aCorrectHeight) {
		int aComputedWidth=  aLine.getWidth();
		int aComputedHeight = aLine.getHeight();
		if (aCorrectWidth != null) {
			assertWidth(aComputedWidth, aCorrectWidth);
		}
		if (aCorrectHeight != null) {
			assertHeight(aComputedHeight, aCorrectHeight);
		}
	}
	protected void testLocation(TestLine aLine, Integer aCorrectX, Integer aCorrectY) {
		int aComputedX=  aLine.getX();
		int aComputedY = aLine.getY();
		if (aCorrectX != null) {
			assertX(aComputedX, aCorrectX);
		}
		if (aCorrectY != null) {
			assertHeight(aComputedY, aCorrectY);
		}
	}    
	
	
	
//	protected void traceProcessReturnValue() {
//		System.out.println(
//				"Comparing actial return value: " + 
//						Arrays.toString((Object[]) getReturnValue()) +
//				" with expected return value: " + 
//						Arrays.toString((Object[]) getExpectedReturnValue()));
//	}
//	@Override
//	protected Object getExpectedReturnValue() {
//		return null;
////		return tokenLines()[getLineIndex()];
//	}
//	protected abstract void executeOperations(Object aLocatable) ;
//	protected abstract void setActual(Object aLocatable) ;

	protected void setExpected(Object aLocatable) {
		
	}

	protected void doExtraStep() {
		
	}
//	protected Object create() {
//		return createOrGetLastRootProxy();
//	}
//	protected abstract boolean checkOutput(Object aLocatable);
	protected void setDependentObjects() {
		
	}
//	protected boolean doTest() throws Throwable {
//		create();
//		setDependentObjects();
//		executeOperations(rootProxy);
//		setExpected(rootProxy);
//		setActual(rootProxy);
//		return checkOutput(rootProxy);
//		
//	}
	// Student test data
	protected Double inputStudentAngle() {
		return null;
	}
	protected Double inputStudentRadius() {
		return null;
	}
	protected Double expectedStudentAngle() {
		return null;
	}
	protected Double expectedStudentRadius() {
		return null;
	}
	
	
	
	protected Integer expectedStudentWidth() {
		return expectedWidth;
	}
	protected Integer expectedStudentHeight() {
		return expectedHeight;
	}
	
	protected Integer inputStudentX() {
//		if (GradingMode.getGraderRun())
//			return inputGraderX();
//		return inputStudentX();
		return null;
	}
	protected Integer inputStudentY() {
		return null;
	}
	protected Integer inputStudentHeight() {
//		if (GradingMode.getGraderRun())
//			return inputGraderHeight();
//		return inputStudentHeight();
		return null;
	}
	protected Integer inputStudentWidth() {
//		if (GradingMode.getGraderRun())
//			return inputGraderWidth();
//		return inputStudentWidth();
		return null;
	}
	protected Integer expectedStudentX() {
		return expectedX;
	}
	protected Integer expectedStudentY() {
		return expectedY;
	}
	// Grader test data
	protected Double inputGraderAngle() {
		return inputStudentAngle();
	}
	protected Double inputGraderRadius() {
		return inputStudentRadius();
	}
	protected Double expectedGraderAngle() {
		return inputGraderAngle();
	}
	protected Double expectedGraderRadius() {
		return inputStudentRadius();
	}
	protected Integer inputGraderWidth() {
		return inputStudentWidth();
	}
	protected Integer inputGraderHeight() {
		return inputStudentHeight();
	}
	protected Integer expectedGraderWidth() {
		return expectedStudentWidth();
	}
	protected Integer expectedGraderHeight() {
		return expectedStudentHeight();
	}
	protected Integer inputGraderX() {
		return inputStudentX();
	}
	protected Integer inputGraderY() {
		return inputStudentY();
	}
	protected Integer expectedGraderX() {
		return expectedStudentX();
	}
	protected Integer expectedGraderY() {
		return expectedStudentY();
	}
	
	// final test data
	
	protected Double inputAngle() {
		if (GradingMode.getGraderRun())
			return inputGraderAngle();
		return inputStudentAngle();
	}
	protected Double inputRadius() {
		if (GradingMode.getGraderRun())
			return inputGraderRadius();
		return inputStudentRadius();	
	}
	protected Double expectedAngle() {
		if (GradingMode.getGraderRun())
			return expectedGraderAngle();
		return expectedStudentAngle();
	}
	protected Double expectedRadius() {
		if (GradingMode.getGraderRun())
			return expectedGraderRadius();
		return expectedStudentRadius();
	}
	protected Integer inputWidth() {
		if (GradingMode.getGraderRun())
			return inputGraderWidth();
		return inputStudentWidth();
	}
	protected Integer inputHeight() {
		if (GradingMode.getGraderRun())
			return inputGraderHeight();
		return inputStudentHeight();
	}
	protected Integer expectedWidth() {
		if (GradingMode.getGraderRun())
			return expectedGraderWidth();
		return expectedStudentWidth();
	}
	protected Integer expectedHeight() {
		if (GradingMode.getGraderRun())
			return expectedGraderHeight();
		return expectedStudentHeight();
	}
	protected Integer inputX() {
		if (GradingMode.getGraderRun())
			return inputGraderX();
		return inputStudentX();
	}
	protected Integer inputY() {
		if (GradingMode.getGraderRun())
			return inputGraderY();
		return inputStudentY();
	}
	protected Integer expectedX() {
		if (GradingMode.getGraderRun())
			return expectedGraderX();
		return expectedStudentX();
	}
	protected Integer expectedY() {
		if (GradingMode.getGraderRun())
			return expectedGraderY();
		return expectedStudentY();
	}
	
	protected void assertWrongX() {
		assertX(actualX, expectedX());
//		Assert.assertTrue("in object:" + locatable + " expected X:" + expectedX() + " != actual X: " + actualX +
//				+  NotesAndScore.PERCENTAGE_MARKER + fractionComplete,
//				false);
	}
	protected void assertWrongY() {
		assertY(actualY, expectedY());
//		Assert.assertTrue("in object:" + locatable + " expected Y:" + expectedY() + " != actual Y: " + actualY +
//				+  NotesAndScore.PERCENTAGE_MARKER + fractionComplete,
//				false);
	}
	protected void assertWrongHeight() {
		assertHeight(actualHeight, expectedHeight());
//		Assert.assertTrue("in object:" + locatable + " expected Height:" + expectedHeight() + " != actual Height: " + actualHeight +
//				+  NotesAndScore.PERCENTAGE_MARKER + fractionComplete,
//				false);
	}
	
	protected void assertWrongWidth() {
		assertWidth(actualWidth, expectedWidth());

//		Assert.assertTrue("in object:" + locatable + " expected Width:" + expectedWidth() + " != actual Width: " + actualWidth +
//				+  NotesAndScore.PERCENTAGE_MARKER + fractionComplete,
//				false);
	}

	protected void assertWrongAngle() {
		assertAngle(actualAngle, expectedAngle());

	}
	protected void assertWrongRadius() {
		assertAngle(actualRadius, expectedRadius());

	}
	
	protected Double inputStudentWidthMultiplier() {
		return null;
	}
	protected Double inputStudentHeightMultiplier() {
		return inputWidthMultiplier();
	}
	protected Double inputGraderWidthMultiplier() {
		return inputStudentWidthMultiplier();
	}
	protected Double inputGraderHeightMultiplier() {
		return inputStudentHeightMultiplier();
	}
	protected Double inputWidthMultiplier() {
		if (GradingMode.getGraderRun())
			return inputGraderWidthMultiplier();
		return inputStudentWidthMultiplier();
	}
	protected Double inputHeightMultiplier() {
		if (GradingMode.getGraderRun())
			return inputGraderHeightMultiplier();
		return inputStudentHeightMultiplier();
	}
	
	//Move methods
	protected Integer inputStudentXDelta() {
		return null;
	}
	protected Integer inputStudentYDelta() {
		return null;
	}
	protected Integer inputGraderXDelta() {
		return inputStudentXDelta();
	}
	protected Integer inputGraderYDelta() {
		return inputStudentYDelta();
	}
	protected Integer inputXDelta() {
		if (GradingMode.getGraderRun())
			return inputGraderXDelta();
		return inputStudentXDelta();
	}
	protected Integer inputYDelta() {
		if (GradingMode.getGraderRun())
			return inputGraderYDelta();
		return inputStudentYDelta();
	}
	
//	protected Object rootLocatable() {
//		return rootProxy;
//	}
//	
	protected TestLocatable leafProxy() {
		return (TestLocatable) leafProxy;
	}
//	protected abstract void setLeafLocatable();
	protected void setOriginalLocation() {
		originalX = leafProxy().getX();
		originalY = leafProxy().getY();
		Tracer.info(this,"Original location of:" + leafProxy() + 
				" is:(" + originalX + ", " + originalY + ")");

	}
	protected void setOriginalBounds() {
		originalWidth = leafProxy().getWidth();
		originalHeight = leafProxy().getHeight();
		Tracer.info(this,"Original bounds of:" + leafProxy() + 
				" are:(" + originalWidth + ", " + originalHeight + ")");
	}
	protected void setActualLocation() {
		actualX = leafProxy().getX();
		actualY = leafProxy().getY();
		Tracer.info(this,"Actual location of:" + leafProxy() + 
				" is:(" + actualX + ", " + actualY + ")");
	}
	
	protected void setActualBounds() {
		actualWidth = leafProxy().getWidth();
		actualHeight = leafProxy().getHeight();
		Tracer.info(this,"Actual bounds of:" + leafProxy() + 
				" are:(" + actualWidth + ", " + actualHeight + ")");
	}
	
//	protected boolean returnValueIsExpected() {
//		return Arrays.equals((Object[]) getExpectedReturnValue(), (Object[]) getReturnValue());
//	}

}
