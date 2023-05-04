package byteman.tools;

import java.util.HashMap;
import java.util.Map;

public class NameDefiner {
	private static Map<String, String> definedNames = new HashMap<>();
	 public static Map<String,String> getDisplayNameMapping(){
	    	return definedNames;
	 }
	 
	 public static void put (String aClassName, String aDisplayName) {
		 definedNames.put(aClassName, aDisplayName);
		 System.out.println("Class name " + aClassName + " display name " + aDisplayName);
	 }
	 
	 public static String get(String aClassName) {
		 return definedNames.get(aClassName);
	 }
	 

}
