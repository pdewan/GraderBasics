package valgrindpp.codegen;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function {
	String soName, fnName, returnType;
	String[] arguments;
	
	public class InvalidFunctionException extends Exception {
		private static final long serialVersionUID = 1L;

		public InvalidFunctionException(String message) {
			super(message);
		}
	}
	
	public Function(String encoded) throws InvalidFunctionException {
		String[] data = encoded.split(":");
		String[] name = data[0].split(",");
		String[] signature = data[1].split("->");
		
		if(signature[0].trim().equals("void")) {
			this.arguments = new String[0];
		} else {
			this.arguments = signature[0].split(",");
			for(int i=0; i<arguments.length; i++) {
				arguments[i] = arguments[i].trim();
			}
		}
		
		this.returnType = signature[1];
		returnType = returnType.trim();

		if(name.length == 1) {
			this.soName = "NONE";
			this.fnName = name[0];
		} else if(name.length == 2) {
			this.soName = name[0];
			this.fnName = name[1];
		} else {
			throw new InvalidFunctionException("Invalid Name");
		}
		
		soName = soName.trim();
		fnName = fnName.trim();
	}
	
	public String toCString() throws InvalidFunctionException {
		StringBuilder sb = new StringBuilder();
		
		sb.append(returnType);
		sb.append(" ");
		sb.append("I_WRAP_SONAME_FNNAME_ZZ(");
		sb.append(zEncodeName(soName));
		sb.append(", ");
		sb.append(zEncodeName(fnName));
		sb.append(")\n");
		
		sb.append("(");
		for(int i=0; i<arguments.length; i++) {
			sb.append(arguments[i]);
			if(i<arguments.length-1) {
				sb.append(", ");
			}
		}
		sb.append(")\n");
		
		sb.append("{");
		
		sb.append("\n\t");
		
		sb.append("OrigFn fn;");
		sb.append("\n\t");

		sb.append("VALGRIND_GET_ORIG_FN(fn);");
		sb.append("\n\t");

		
		if(!isVoid()) {
			sb.append(returnType);
			sb.append(" result;");
			sb.append("\n\t");

		}
		
		sb.append("CALL_FN_");
		
		if(isVoid()) {
			sb.append("v_");
		} else {
			sb.append("W_");
		}
		
		switch(arguments.length) {
		case 0: 
			sb.append("v"); break;
		case 1: case 2: case 3: case 4: 
			for(int i=0; i<arguments.length; i++) sb.append("W");
			break;
		case 5: case 6: case 7: case 8: case 9: case 10: case 11: case 12:
			sb.append(arguments.length);
			sb.append("W");
			break;
		default: 
			throw new InvalidFunctionException("Function contains more than 12 arguments");
		}
		
		sb.append("(");
		
		if(!isVoid()) {
			sb.append("result, ");
		}
		
		sb.append("fn");
		
		for(int i=0; i<arguments.length; i++) {
			if(i == 0) {
				sb.append(", ");
			}
			
			sb.append(parseArgName(arguments[i]));
			
			if(i < arguments.length-1) {
				sb.append(", ");
			}
		}
		
		sb.append(");");
		sb.append("\n\t");

		
		sb.append("trace(\""+fnName+": ");
		
		if(arguments.length == 0) sb.append("void");
		
		for(int i=0; i<arguments.length; i++) {			
			sb.append("%");
			sb.append(argStringFormatter(arguments[i]));
			
			if(i < arguments.length-1) {
				sb.append(", ");
			}
		}
		

		sb.append(" -> ");
		
		if(!isVoid()) {
			sb.append("%");
			sb.append(argStringFormatter(returnType));
		} else {
			sb.append("void");
		}
		
		sb.append("\"");		
		for(int i=0; i<arguments.length; i++) {
			if(i == 0) {
				sb.append(",");
			}
			
			if(argType(arguments[i]) == StringType.OTHER) {
				sb.append("&");
			}
			
			sb.append(parseArgName(arguments[i]));
			
			if(i < arguments.length-1) {
				sb.append(", ");
			}
		}
		
		if(!isVoid()) {
			sb.append(", ");
			
			if(argType(returnType) == StringType.OTHER) {
				sb.append("&");
			}
			
			sb.append("result");
		}
		sb.append(");");
		sb.append("\n");

		
		if(!isVoid()) {
			sb.append("\t");
			sb.append("return result;");
			sb.append("\n");
		}
		
		sb.append("}");
		
		return sb.toString();
	}
	
	private enum StringType {
		INT, UNSIGNED_INT, SIZE_T,
		LONG, UNSIGNED_LONG, LONG_LONG, UNSIGNED_LONG_LONG,
		SHORT,UNSIGNED_SHORT,
		FLOAT, DOUBLE, LONG_DOUBLE,
		CHAR, POINTER, ARRAY, OTHER
	}
	
	private StringType argType(String arg) {
		if(arg.contains("*")) return StringType.POINTER;
		if(arg.contains("[]")) return StringType.ARRAY;
		
		if(arg.contains("long double")) return StringType.LONG_DOUBLE;
		if(arg.contains("double")) return StringType.DOUBLE;
		if(arg.contains("float")) return StringType.FLOAT;
		
		if(arg.contains("unsigned long long")) return StringType.UNSIGNED_LONG_LONG;
		if(arg.contains("unsigned long")) return StringType.UNSIGNED_LONG;
		if(arg.contains("long long")) return StringType.LONG_LONG;
		if(arg.contains("long")) return StringType.LONG;
		
		if(arg.contains("unsigned short")) return StringType.UNSIGNED_SHORT;
		if(arg.contains("short")) return StringType.SHORT;
		
		if(arg.contains("unsigned int")) return StringType.UNSIGNED_INT;
		if(arg.contains("int")) return StringType.INT;
		
		if(arg.contains("char")) return StringType.CHAR;
		
		if(arg.contains("size_t")) return StringType.SIZE_T;
			
		return StringType.OTHER;
	}
	
	private String argStringFormatter(String arg) {
		switch(argType(arg)) {
		case POINTER: return "p";
		case ARRAY: return "p";
		
		case LONG_DOUBLE: return "Lg";
		case DOUBLE: case FLOAT: return "g";
		
		case UNSIGNED_LONG_LONG: return "llu";
		case UNSIGNED_LONG: return "lu";
		case LONG_LONG: return "lli";
		case LONG: return "li";
		
		case UNSIGNED_SHORT: return "hu";
		case SHORT: return "hi";
		
		case UNSIGNED_INT: return "u";
		case INT: return "i";
		
		case CHAR: return "c";
		
		case SIZE_T: return "zu";	
		
		case OTHER: default: return "p";
		}
	}
	
	private String parseArgName(String arg) throws InvalidFunctionException {
		// funarg
		if(arg.charAt(arg.length()-1) == ')') {
			Pattern pattern = Pattern.compile(".+\\(\\*(.+)\\)\\(.+\\)");
			Matcher m = pattern.matcher(arg);
			if(m.matches()) {
				return m.group(1);
			} else {
				throw new InvalidFunctionException("Invalid argument syntax: "+arg);
			}
		} else {
			String[] comps = arg.split(" |\\*");
			return comps[comps.length-1];
		}
	}
	
	private String zEncodeName(String name) throws InvalidFunctionException {
		/*
			 Za   encodes    *
			 Zp              +
			 Zc              :
			 Zd              .
			 Zu              _
			 Zh              -
			 Zs              (space)
			 ZA              @
			 ZZ              Z
			 ZL              (       # only in valgrind 3.3.0 and later
			 ZR              )       # only in valgrind 3.3.0 and later
		 */
		
		StringBuilder sb = new StringBuilder();
		for(char c: name.toCharArray()) {
			switch(c) {
			case('*'): sb.append("Za"); break;
			case('+'): sb.append("Zp"); break;
			case(':'): sb.append("Zc"); break;
			case('.'): sb.append("Zd"); break;
			case('_'): sb.append("Zu"); break;
			case('-'): sb.append("Zh"); break;
			case(' '): sb.append("Zs"); break;
			case('@'): sb.append("ZA"); break;
			case('Z'): sb.append("ZZ"); break;
			case('('): sb.append("ZL"); break;
			case(')'): sb.append("ZR"); break;
			default: 
				if(Character.isLetterOrDigit(c)) {
					sb.append(c);
				} else {
					throw new InvalidFunctionException("Invalid character in function name");
				}
			}
		}
		
		return sb.toString();
	}
	
	private boolean isVoid() {
		return returnType.equals("void");
	}
}
