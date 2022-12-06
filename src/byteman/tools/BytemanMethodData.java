package byteman.tools;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BytemanMethodData implements Serializable {
	private static final long serialVersionUID = -1791526849377475019L;
	private final String methodName;
	private final Class<?> [] argTypes;
	private List<Object> arguments = new ArrayList<>();
	private Object retVal;
	
	public BytemanMethodData(String name, String ... argTypes) throws ClassNotFoundException{
		methodName = name;
		this.argTypes = new Class<?>[argTypes.length];
		for(int i=0;i<argTypes.length;i++) {
			this.argTypes[i] = Class.forName(argTypes[i]);
		}
	}
	
	public BytemanMethodData(String name, Class<?> ... argTypes) {
		methodName = name;
		this.argTypes = argTypes;
	}
	
	public BytemanMethodData(Method method) {
		methodName = method.getName();
		argTypes = method.getParameterTypes();
	}

	public BytemanMethodData(String name, Object ... arguments) {
		methodName = name;
		argTypes = new Class<?>[arguments.length];
		for(int i=0;i<arguments.length;i++) {
			argTypes[i] = arguments[i].getClass();
			this.arguments.add(arguments[i]);
		}
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public Class<?>[] getParams(){
		return argTypes;
	}
	
	public void setArguments(Object ... args) {
			arguments.addAll(Arrays.asList(args));
	}
	
	public List<?> getArguments(){
		return arguments;
	}

	public Object getRetVal() {
		System.err.println(retVal + " has been called in "+ this);
		return retVal;
	}

	public void setRetVal(Object retVal) {
		System.err.println(retVal + " has been put in "+ this);
		this.retVal = retVal;
	}
	
}
