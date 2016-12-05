package grader.driver;

import static grader.driver.GradingManagerType.A_HEADLESS_GRADING_MANAGER;

public class GradingManagerFactory {
	static GradingManagerType gradingManagerType = GradingManagerType.SAKAI_PROJECT_DATABASE;

	public static GradingManagerType getGradingManagerType() {
		return gradingManagerType;
	}

	public static void setGradingManagerType(GradingManagerType newVal) {
		gradingManagerType = newVal;
	}
	public static boolean isHeadless() {
		return GradingManagerFactory.getGradingManagerType().equals(A_HEADLESS_GRADING_MANAGER);
  }
}
