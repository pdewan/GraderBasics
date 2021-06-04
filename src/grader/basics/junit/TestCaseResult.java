package grader.basics.junit;

import util.trace.Tracer;

/**
 * A container which transports information about the result of running a test case.
 */
public class TestCaseResult {

    private String notes;
    private double percentage;
    private String name;
    private boolean autograded;
    private double maxScore;
   
	private double score;

   
	/**
     * @param percentage The percentage (0 to 1), meaning how much of the test case they passed
     * @param name       The name of the test case
     */
    public TestCaseResult(double percentage, String name) {
        this(percentage, name, false);
    }

    /**
     * @param percentage The percentage (0 to 1), meaning how much of the test case they passed
     * @param name       The name of the test case
     * @param autograded Whether the test was autonomously graded
     */
    public TestCaseResult(double percentage, String name, boolean autograded) {
        this(percentage, "", name, autograded);
    }

    /**
     * @param percentage The percentage (0 to 1), meaning how much of the test case they passed
     * @param notes      Notes about the result
     * @param name       The name of the test case
     */
    public TestCaseResult(double percentage, String notes, String name) {
        this(percentage, notes, name, false);
    }

    /**
     * @param percentage The percentage (0 to 1), meaning how much of the test case they passed
     * @param notes      Notes about the result
     * @param name       The name of the test case
     * @param autograded Whether the test was autonomously graded
     */
    public TestCaseResult(double percentage, String notes, String name, boolean autograded) {
        Tracer.info(this, "### " + name + ": " + percentage);
        this.percentage = Math.min(Math.max(percentage, 0), 1);
        this.notes = notes;
        this.name = name;
        this.autograded = autograded;
    }

    /**
     * @param passed Whether the user passed the test case
     * @param name   The name of the test case
     */
    public TestCaseResult(boolean passed, String name) {
        this(passed, name, false);
    }

    /**
     * @param passed Whether the user passed the test case
     * @param name   The name of the test case
     * @param autograded Whether the test was autonomously graded
     */
    public TestCaseResult(boolean passed, String name, boolean autograded) {
        this(passed, "", name, autograded);
    }

    /**
     * @param passed Whether the user passed the test case
     * @param notes  Notes about the result
     * @param name   The name of the test case
     */
    public TestCaseResult(boolean passed, String notes, String name) {
        this(passed, notes, name, false);
    }

    /**
     * @param passed Whether the user passed the test case
     * @param notes  Notes about the result
     * @param name   The name of the test case
     * @param autograded Whether the test was autonomously graded
     */
    public TestCaseResult(boolean passed, String notes, String name, boolean autograded) {
        this(passed ? 1 : 0, notes, name, autograded);
    }

    /**
     * @return The percentage of the test case that passed
     */
    public double getPercentage() {
        return percentage;
    }
    
    public void setPercentage(double newVal) {
       percentage = newVal;
    }
    
    
    public boolean isPass() {
    	return percentage == 1.0;
    }
    public boolean isPartialPass() {
    	return !isPass() && !isFail();
    }
    
    public boolean isFail() {
    	return percentage == 0.0;
    }
    public String toString() {
    	return name + ":" + percentage + ":" + notes;
    }
    /**
     * @return Any notes about the result
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @return The name of the test case
     */
    public String getName() {
        return name;
    }

    /**
     * @return Was the test case autograded
     */
    public boolean getAutoGraded() {
        return autograded;
    }
    public void setAutoGraded(boolean newVal) {
    	autograded = true;
    }
    public double getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(double maxScore) {
		this.maxScore = maxScore;
	}
	 public double getScore() {
			return score;
	}

	public void setScore(double score) {
			this.score = score;
	}
	

}

