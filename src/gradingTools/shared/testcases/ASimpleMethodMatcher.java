package gradingTools.shared.testcases;

import java.lang.reflect.Method;

public class ASimpleMethodMatcher implements MethodMatcher {

	String methodName;

	public ASimpleMethodMatcher(String methodName) {
		this.methodName = methodName;
	}

	@Override
	public boolean matches(Method method) {
		return method.getName().equals(methodName);
	}

	@Override
	public boolean matchesIgnoreCase(Method method) {
		return method.getName().equalsIgnoreCase(methodName);
	}

	@Override
	public String getLabel() {
		return methodName;
	}

}
