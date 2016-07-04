package grader.basics.junit;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class AJUnitRunToTestCaseResult extends RunListener {
	TestCaseResult checkResult;
	String name = "";
	
	public void setJUnitName(String aName) {
		name = aName;
	}
	
	public String getJUnitName() {
		return name;
	}
	public void testStarted(Description description) throws Exception {
			checkResult = new TestCaseResult(true, 
					"", name, true);
    }

	@Override
	public void testFailure(Failure failure) {
	       try {
			super.testFailure(failure);
			Throwable aThrowable = failure.getException();
			String aMessage = aThrowable.getMessage();
			if (aMessage == null) { // some exception
				checkResult = new TestCaseResult(false, 
						aThrowable.toString(), name, true);
				return;
			}
			String[] aNotesAndScore = aMessage.split(":");
			String aNotes = aNotesAndScore[0];
			if (aNotesAndScore.length == 1) { // assume zero percentage
				checkResult = new TestCaseResult(false, 
						aNotes, name, true);
			} else {
				String aPercentageString = aNotesAndScore[aNotesAndScore.length - 1].trim();
				try {
				double aPercentage = Double.parseDouble(aPercentageString);
				checkResult =new TestCaseResult(aPercentage, aNotes, name, true);

				} catch (Exception e) {
					checkResult = new TestCaseResult(false, 
							aMessage, name, true);
					
				}
				
			}
//			System.out.println ("Failure:" + failure);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void testFinished(Description description) {
		try {
			super.testFinished(description);
//			System.out.println ("Test finished:"+ description);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
	}
	public TestCaseResult getTestCaseResult() {
		return checkResult;
	}

}
