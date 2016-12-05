package grader.driver;

/**
 *
 * @author Andrew Vitkus
 */
public enum GradingManagerType {
    A_GUI_GRADING_MANAGER, A_HEADLESS_GRADING_MANAGER, SAKAI_PROJECT_DATABASE, NONE;
    public static final String HEADLESS_GRADING_MANAGER = "AHeadlessGradingManager";    
    public static GradingManagerType getFromConfigName(String name) {
        switch (name) {
            case "AGUIGradingManager":
                return A_GUI_GRADING_MANAGER;
//            case "AHeadlessGradingManager":
            case HEADLESS_GRADING_MANAGER:

                return A_HEADLESS_GRADING_MANAGER;
            case "SakaiProjectDatabase":
                return SAKAI_PROJECT_DATABASE;
            default:
                return NONE;
        }
    }
}
