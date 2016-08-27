package gradingTools.shared.testcases;

import java.lang.reflect.Method;

public interface MethodMatcher {

	public boolean matches(Method method);

	public boolean matchesIgnoreCase(Method method);

	public String getLabel();

}
