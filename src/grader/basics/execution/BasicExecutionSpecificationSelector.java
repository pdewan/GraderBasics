package grader.basics.execution;

import grader.basics.config.ABasicExecutionSpecification;

public class BasicExecutionSpecificationSelector {
	public static BasicExecutionSpecification executionSpecification;

	public static BasicExecutionSpecification getBasicExecutionSpecification() {
		if (executionSpecification == null) {
			executionSpecification = new ABasicExecutionSpecification();
		}
		return executionSpecification;
	}

	public static void setBasicExecutionSpecification(
			BasicExecutionSpecification executionSpecification) {
		BasicExecutionSpecificationSelector.executionSpecification = executionSpecification;
	}

	

}
