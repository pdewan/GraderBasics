package byteman.tools;

import java.lang.reflect.Array;

public interface EnterExitInjectionSite {

	/**
	 * This method is called prior to the constructor
	 * @param clazz - the class being initialized
	 * @param params - the arguments provided
	 */
	public void atInitialization(String clazz, Object[] params);
	
	/**
	 * This method is call prior to the targeted method being called.
	 * @param clazz - the name of the class the method belongs to
	 * @param method - the name of the method 
	 * @param params - the arguments provided
	 */
	public void atEnter(String clazz, String method, Object[] params);
	

	/**
	 * This method is called when the targeted method finished execution
	 * @param clazz - the name of the class the method belongs to
	 * @param method - the name of the method
	 * @param returned - value returned by the method. If the return type is void returned is the string "void"
	 */
	public void atExit(String clazz, String method, Object returned);
	
	public void atExit(String clazz, String method, Object[] params);
	
	
	public default void atExit(String clazz, String method){atExit(clazz,method,"void");}
	public default Object[] toObjectArray(Object o) {
		if(!o.getClass().isArray()) return null;
		if(!o.getClass().getComponentType().isPrimitive()) return (Object [])o;
		int arrlen = Array.getLength(o);
		Object [] retval = new Object[arrlen];
		for(int i=0;i<arrlen;i++) {
			retval[i]=Array.get(o, i);
		}
		return retval;
	}
	
	public default String arrayPrinter(Object o) {
		StringBuilder sb = new StringBuilder();
		if(!o.getClass().isArray()) {
			sb.append(o.toString());
		}else {
			sb.append('[');
			Object [] arr = toObjectArray(o);
			for(int i=0;i<9&&i<arr.length;i++) {
				sb.append(arrayPrinter(arr[i]));
				sb.append(',');
			}
			if(arr.length>0)
				sb.deleteCharAt(sb.length()-1);
			if(arr.length == 11) {
				sb.append(","+arr[10]);
			}else if(arr.length>10) {
				sb.append(", ... ");
				sb.append(arr[arr.length-1]);
			}
			
			sb.append(']');
		}
		return sb.toString();
	}	
	
	public default void atExit(String clazz, String method, int returned) {
		Integer prim = returned;
		atExit(clazz,method,(Object)prim);
	}
	public default void atExit(String clazz, String method, byte returned) {
		Byte prim = returned;
		atExit(clazz,method,(Object)prim);
	}
	public default void atExit(String clazz, String method, short returned) {
		Short prim = returned;
		atExit(clazz,method,(Object)prim);
	}
	public default void atExit(String clazz, String method, long returned) {
		Long prim = returned;
		atExit(clazz,method,(Object)prim);
	}
	public default void atExit(String clazz, String method, float returned) {
		Float prim = returned;
		atExit(clazz,method,(Object)prim);
	}
	public default void atExit(String clazz, String method, double returned) {
		Double prim = returned;
		atExit(clazz,method,(Object)prim);
	}
	public default void atExit(String clazz, String method, boolean returned) {
		Boolean prim = returned;
		atExit(clazz,method,(Object)prim);
	}
	public default void atExit(String clazz, String method, char returned) {
		Character prim = returned;
		atExit(clazz,method,(Object)prim);
	}
}
