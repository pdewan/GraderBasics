package byteman.tools;

import java.lang.reflect.Method;

public class MethodInjectionData {

	private final String methodName;
	private boolean returnsVoid;
		
	public MethodInjectionData(String toParse){
		String [] split = toParse.split("\\|");
		methodName = split[0];
		returnsVoid = split[1].equals("T");

	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public boolean returnsVoid() {
		return returnsVoid;
	}
	
	public MethodInjectionData(String method, boolean retVoid) {
		this.methodName=method;
		this.returnsVoid=retVoid;
	}
	
	public MethodInjectionData(Method method) {
		this(method.getName(),method.getReturnType().equals(Void.TYPE));
	}
	
	public String getPrintValue() {
		StringBuilder sb = new StringBuilder();
		sb.append(methodName);
		sb.append('|');
		sb.append(returnsVoid?'T':'F');
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if((o instanceof MethodInjectionData) && ((MethodInjectionData)o).methodName.equals(methodName)) {
			MethodInjectionData obj = (MethodInjectionData)o;
			obj.returnsVoid |= returnsVoid;
			returnsVoid |= obj.returnsVoid;
			return true;
		}
		return false;
	}
	
}
