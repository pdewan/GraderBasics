package byteman.tools;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BytemanData implements Serializable{

	private static final long serialVersionUID = -7658146915406843504L;
	private final Class<?> clazz;
	private Class<?> [] constructorTypes = new Class<?>[0];
	private Object [] constructorArgs = new Object [0];
	private final Map<String,BytemanMethodData> methods = new HashMap<>();

	
	public BytemanData(Class<?> clazz, BytemanMethodData ... methods) {
		this.clazz=clazz;
		for(BytemanMethodData m:methods) {
			this.methods.put(m.getMethodName(), m);
		}
	}
	
	public BytemanData(Class<?> clazz, String ... method) {
		this.clazz = clazz;
		for(String m:method) {
			addMethod(m);
		}
	}

	public BytemanData(Method ... methods) {
		clazz = methods[0].getDeclaringClass();
		for(Method m:methods) {
			addMethod(m);
		}
	}
	
	public BytemanData(Class<?> clazz, String method, Class<?> ... params) {
		this.clazz=clazz;
		addMethod(method,params);
	}
	
	public Class<?> getTargetedClass(){
		return clazz;
	}
	
	public Set<BytemanMethodData> getMethods(){
		Set<BytemanMethodData> retval = new HashSet<>();
		retval.addAll(methods.values());
		return retval;
	}
	
	public void addMethod(Method method) {
		String className = clazz.getName();
		if(method.getDeclaringClass().getName().equals(className)) {
			BytemanMethodData data = new BytemanMethodData(method);
			this.methods.put(data.getMethodName(), data);
		}
			
	}
	
	public void addMethod(String method, Class<?> ... params) {
		methods.put(method,new BytemanMethodData(method,params));
	}
	
	public void addMethod(String methodName) {
		for(Method m:clazz.getMethods()) {
			if(methodName.equals(m.getName())) {
				methods.put(methodName,new BytemanMethodData(m));
			}
		}
	}
	
	public BytemanMethodData getMethod(String methodName) {
		return methods.get(methodName);
	}
	
	public void setConstructorArgs(Class<?> [] types, Object [] args) {
		constructorTypes = types;
		constructorArgs = args;
	}

	public void setConstructorArgs(Object [] args) {
		Class<?> [] types = new Class<?>[args.length];
		for(int i=0;i<args.length;i++) {
			types[i] = args[i].getClass();
		}
		setConstructorArgs(types,args);
	}
	
	public Class<?> [] getConTypes(){
		return constructorTypes;
	}
	
	public Object [] getConArgs() {
		return constructorArgs;
	}
	
	
}
