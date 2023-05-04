package byteman.tools;

import java.lang.reflect.Array;

public class BytemanMisc {
	
	public static void addString(StringBuilder sb, Object o) {
		if (o.getClass().isArray()) {
			sb.append(arrayPrinter(o));
		} else {
			sb.append(o.toString());
		}
	}
	public static String toString(Object o) {
		StringBuilder sb = new StringBuilder();
		 addString(sb, o);
		 return sb.toString();
		
	}
	public static void arrayPrinter(StringBuilder sb, Object o) {
//		StringBuilder sb = new StringBuilder();
		if (!o.getClass().isArray()) {
			sb.append(o.toString());
		} else {
			sb.append('[');
			Object[] arr = toObjectArray(o);
			for (int i = 0; i < 9 && i < arr.length; i++) {
				arrayPrinter(sb, arr[i]);
				sb.append(',');
			}
			if (arr.length > 0)
				sb.deleteCharAt(sb.length() - 1);
			if (arr.length == 10) {
				sb.append("," + arr[9]);
			} else if (arr.length == 11) {
				sb.append("," + arr[10]);
			} else if (arr.length > 10) {
				sb.append(", ... ");
				sb.append(arr[arr.length - 1]);
			}

			sb.append(']');
		}
//		return sb.toString();
	}
	public static String arrayPrinter(Object o) {
		StringBuilder sb = new StringBuilder();
		arrayPrinter(sb, o);
		return sb.toString();
		
	}
	public static Object[] toObjectArray(Object o) {
		if(!o.getClass().isArray()) return null;
		if(!o.getClass().getComponentType().isPrimitive()) return (Object [])o;
		int arrlen = Array.getLength(o);
		Object [] retval = new Object[arrlen];
		for(int i=0;i<arrlen;i++) {
			retval[i]=Array.get(o, i);
		}
		return retval;
	}
	
	
	
	public static void main(String[] args) {
		int[] barArray = { 5, 4, 3};
		String aString = byteman.tools.BytemanMisc.toString(barArray);
		System.out.println(aString);
	}

}
