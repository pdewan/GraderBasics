package gradingTools.shared.testcases;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class RunnableMethod {

	private final Class<?> javaClass;
	private final Method method;

	public RunnableMethod(Class<?> javaClass, Method method) {
		this.javaClass = javaClass;
		this.method = method;
	}

	public Method getMethod() {
		return method;
	}

	public Object run(Object[] args) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, InstantiationException {
		if (Modifier.isStatic(method.getModifiers())) {
			return method.invoke(null, args);
		} else {
			Object instance = javaClass.newInstance();
			return method.invoke(instance, args);
		}
	}
}
