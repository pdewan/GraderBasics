package grader.basics.junit;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import util.trace.Tracer;

public class AJUnitTestResult extends RunListener implements JUnitTestResult {
	TestCaseResult checkResult;
	Failure failure;
	String name = "";
	
	public void setJUnitName(String aName) {
		name = aName;
	}
	
	public String getJUnitName() {
		return name;
	}
	@Override
	public void testRunStarted(Description description) throws Exception {
			super.testRunStarted(description);
			RunNotifierFactory.getOrCreateRunNotifier().fireTestRunStarted(description);
			
    }
	@Override
	public void testStarted(Description description) throws Exception {
		super.testStarted(description);
			checkResult = new TestCaseResult(true, 
					"", name, true);
			failure = null;
			RunNotifierFactory.getOrCreateRunNotifier().fireTestStarted(description);
			Tracer.clearTracedMessages();
			
    }
	@Override
	public void testAssumptionFailure(Failure aFailure) {
		super.testAssumptionFailure(aFailure);
		RunNotifierFactory.getOrCreateRunNotifier().fireTestAssumptionFailed(aFailure);

	}

	

	@Override
	public void testFailure(Failure aFailure) {
	       try {
			super.testFailure(aFailure);
			failure = aFailure;
			Throwable aThrowable = aFailure.getException();
			String aMessage = aThrowable.getMessage();
			System.err.println ("Test Failed:" + aMessage);
			System.out.println("Steps traced since last test:");
			System.out.println(Tracer.getBufferedTracedMessages());
			if (aMessage == null) { // some exception
				checkResult = new TestCaseResult(false, 
						aThrowable.toString(), name, true);
				return;
			}
			NotesAndScore aNotesAndScore = NotesAndScore.create(aMessage);
			checkResult = new TestCaseResult(aNotesAndScore.getPercentage(), aNotesAndScore.getNotes(), name, true); 
//			String[] aNotesAndScore = aMessage.split(":");
//			String aNotes = aNotesAndScore[0];
//			if (aNotesAndScore.length == 1) { // assume zero percentage
//				checkResult = new TestCaseResult(false, 
//						aNotes, name, true);
//			} else {
//				String aPercentageString = aNotesAndScore[aNotesAndScore.length - 1].trim();
//				try {
//				double aPercentage = Double.parseDouble(aPercentageString);
//				checkResult =new TestCaseResult(aPercentage, aNotes, name, true);
//
//				} catch (Exception e) {
//					checkResult = new TestCaseResult(false, 
//							aMessage, name, true);
//					
//				}
//				
//			}
			RunNotifierFactory.getOrCreateRunNotifier().fireTestFailure(aFailure);
//			System.out.println ("Failure:" + failure);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void testFinished(Description description) throws Exception {
	
			super.testFinished(description);
//			System.out.println ("Test finished:"+ description);
		
		RunNotifierFactory.getOrCreateRunNotifier().fireTestFinished(description);

	}
	public void testIgnored(Description description) throws Exception {
		super.testIgnored(description);
		RunNotifierFactory.getOrCreateRunNotifier().fireTestIgnored(description);

	}

	public TestCaseResult getTestCaseResult() {
		return checkResult;
	}

	@Override
	public Failure getFailure() {
		// TODO Auto-generated method stub
		return failure;
	}
	
	static {
		Tracer.setBufferTracedMessages(true);
	}

}
